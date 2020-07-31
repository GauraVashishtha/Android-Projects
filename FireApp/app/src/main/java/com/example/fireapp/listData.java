package com.example.fireapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.Map;

public class listData extends AppCompatActivity {

    private ListView mListView;
    private Firebase mRef;
    private ArrayList<String> mUserNames=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);

        mRef=new Firebase("https://fireapp-1fdad.firebaseio.com/users");

        mListView=(ListView)findViewById(R.id.listView);
        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mUserNames);
        mListView.setAdapter(arrayAdapter);

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

//                Map<String,String> mp=dataSnapshot.getChildren().getValue(Map.class);
//                for(Map.Entry m:mp.entrySet()){
//                    System.out.println(m.getKey()+" "+m.getValue());
//                }
                String values=dataSnapshot.getValue(String.class);
                System.out.println("datasnapshot : "+values);
                mUserNames.add(values);
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
}
