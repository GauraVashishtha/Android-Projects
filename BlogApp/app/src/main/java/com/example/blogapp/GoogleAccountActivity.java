package com.example.blogapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class GoogleAccountActivity extends AppCompatActivity {

    private Button logOutBtn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_account);
        logOutBtn=(Button)findViewById(R.id.google_log_out_btn_id);
        mAuth=FirebaseAuth.getInstance();

        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(GoogleAccountActivity.this,GoogleLoginActivity.class));
                }
            }
        };

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            mAuth.signOut(); // very important if you are using firebase.
                            Intent logout_intent = new Intent(getApplicationContext(),GoogleLoginActivity.class);
                            logout_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK); // clear previous task (optional)
                            startActivity(logout_intent);
                        }
                    }
                });


            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
}
