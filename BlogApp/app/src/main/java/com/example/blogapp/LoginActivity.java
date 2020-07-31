package com.example.blogapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity :";
    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginBtn;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailField=(EditText)findViewById(R.id.login_email_field_id);
        mNameField=(EditText)findViewById(R.id.login_name_field_id);
        mPasswordField=(EditText)findViewById(R.id.login_password_field_id);
        mLoginBtn=(Button)findViewById(R.id.login_login_btn_id);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    private void login() {
        final String name=mNameField.getText().toString().trim();
        String email=mEmailField.getText().toString().trim();
        String password=mPasswordField.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user=mAuth.getCurrentUser();
                                String uid=user.getUid();
                                DatabaseReference userDb=mDatabase.child("Users").child(uid);
                                userDb.child("name").setValue(name);

                                Toast.makeText(LoginActivity.this, "SignUp sucessfully...", Toast.LENGTH_SHORT).show();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }else{
            Toast.makeText(this, "Please provide all the inputs...!!!", Toast.LENGTH_SHORT).show();
        }
    }

}
