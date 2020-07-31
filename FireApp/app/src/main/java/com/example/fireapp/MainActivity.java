package com.example.fireapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.service.autofill.OnClickAction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button mBtn;
    private Firebase mRef;
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Firebase.setAndroidContext(this);
        // Initialize Firebase Auth
        // mAuth = FirebaseAuth.getInstance();

        //add value in database
//        mRef=new Firebase("https://fireapp-1fdad.firebaseio.com/");
//        mBtn=(Button)findViewById(R.id.addValuebtn);
//        mBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Firebase mRefChild=mRef.child("Name");
//                mRefChild.setValue("Gaurav Vashishtha");
//            }
//        });

        //retrieve value from database
//        mRef=new Firebase("https://fireapp-1fdad.firebaseio.com/");
//        mTextView=(TextView)findViewById(R.id.textView);
//
//        mRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                Map<String,String> mp=dataSnapshot.getValue(Map.class);
//
//                for(Map.Entry m:mp.entrySet()){
//                    System.out.println(m.getKey()+" "+m.getValue());
//                }
//
//                String value=dataSnapshot.child("Name").getValue(String.class);
//                Log.e("value in firebase",value);
//                mTextView.setText(value);
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });




    }

}
