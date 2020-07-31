package com.example.fireapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class upload_and_retrieve_image extends AppCompatActivity {

    private Button mSelectImageBtn;
    private StorageReference mStorage;
    private static final int CAMERA_REQUEST_CODE=2;
    private static final int GALLERY_INTENT=1;
    private ProgressDialog progressDialog;
    private ImageView mImageView;
    String image_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_and_retrieve_image);

        mStorage= FirebaseStorage.getInstance().getReference();
        mSelectImageBtn=(Button)findViewById(R.id.capture_image_btn);
        progressDialog=new ProgressDialog(this);
        mImageView=(ImageView)findViewById(R.id.capture_image_view_id);
        mSelectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent,CAMERA_REQUEST_CODE);
//                dispatchTakePictureIntent();
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

    }

    // tasksnapshot.getDownloadUrl() is giving nullpointer exception because of which the below methods are added to resolve that
    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK)
        {
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            final Uri uri=data.getData();
            StorageReference filePath=mStorage.child("Photos").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(upload_and_retrieve_image.this, "Uploaded Sucessfully !!!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();


                    StorageReference photoRef=FirebaseStorage.getInstance().getReference().child("Photos").child(uri.getLastPathSegment());
                    photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            Toast.makeText(getBaseContext(), "Upload success! URL - " + downloadUrl.toString() , Toast.LENGTH_SHORT).show();
                            //                    Use get() Instead of with() it will work
                            //                    Picasso.with(upload_and_retrieve_image.this).load(downloadUri).fit().centerCrop().into(mImageView);
                            Picasso.get().load(downloadUrl).fit().centerCrop().into(mImageView);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(upload_and_retrieve_image.this, "Upload Failed!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

        }
    }

}
