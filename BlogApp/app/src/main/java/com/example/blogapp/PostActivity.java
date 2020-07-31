package com.example.blogapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private EditText titleField;
    private EditText descField;
    private Button submitBtn;
    private static final int GALLERY_REQUEST=2;
    private Uri mImageUri=null;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mStorage= FirebaseStorage.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Blog");
        imageButton=(ImageButton)findViewById(R.id.post_image_id);
        titleField=(EditText)findViewById(R.id.post_title_id);
        descField=(EditText)findViewById(R.id.post_desc_id);
        submitBtn=(Button)findViewById(R.id.submit_post_btn_id);
        progressDialog=new ProgressDialog(this);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK)
        {
            mImageUri=data.getData();
            imageButton.setImageURI(mImageUri);

        }

    }

    private void startPosting(){

        progressDialog.setMessage("Uploading ....");
        progressDialog.show();

        final String title=titleField.getText().toString().trim();
        final String desc=descField.getText().toString().trim();

        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc)){
            final StorageReference filePath=mStorage.child("Blog Images").child(mImageUri.getLastPathSegment());
            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

//                    Uri downloadUrl=taskSnapshot.getDownloadUrl();

                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            DatabaseReference newPost=mDatabase.push();
                            newPost.child("title").setValue(title);
                            newPost.child("desc").setValue(desc);
                            newPost.child("image").setValue(uri.toString());
                            Toast.makeText(PostActivity.this, "Uploading Sucessfull!! "+uri.toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(PostActivity.this,MainActivity.class));
                        }
                    });




                }
            });
        }
    }
}
