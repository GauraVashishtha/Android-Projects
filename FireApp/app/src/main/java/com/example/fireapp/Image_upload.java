package com.example.fireapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Image_upload extends AppCompatActivity {

    private Button mSelectImageBtn;
    private StorageReference mStorage;
    private static final int GALLERY_INTENT=2;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        mStorage= FirebaseStorage.getInstance().getReference();
        mSelectImageBtn=(Button)findViewById(R.id.select_img_btn_id);
        progressDialog=new ProgressDialog(this);
        mSelectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK)
        {
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            Uri uri=data.getData();
            StorageReference filePath=mStorage.child("Photos").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Image_upload.this, "Uploaded Sucessfully !!!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }
}
