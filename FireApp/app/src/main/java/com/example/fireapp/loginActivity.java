package com.example.fireapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class loginActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private Button mLoginBtn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail=(EditText)findViewById(R.id.email_id);
        mPassword=(EditText)findViewById(R.id.password_id);
        mLoginBtn=(Button)findViewById(R.id.loginbtn);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null)
                {
//                    startActivity(new Intent(loginActivity.this,MainActivity.class));
                    Toast.makeText(loginActivity.this, "Hello i am here!!!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


    }

    private void login()
    {

        String email=mEmail.getText().toString();
        String password=mPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
//                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    updateUI(user);
                            startActivity(new Intent(loginActivity.this,listData.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("TAG", "signInWithEmail: failure");
                            Toast.makeText(loginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                                    updateUI(null);
                        }
                    }
                });


    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
        mAuth.addAuthStateListener(mAuthListener);
    }
}
