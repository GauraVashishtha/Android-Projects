package com.example.fireapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class firebaseListViewActivity extends AppCompatActivity {

    private ListView mlistView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_list_view);

        mlistView=(ListView)findViewById(R.id.listView);

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://fireapp-1fdad.firebaseio.com/users");
        FirebaseListAdapter<String> firebaseListAdapter=new FirebaseListAdapter<String>(
                this,
                String.class,
                android.R.layout.simple_list_item_1,
                databaseReference
        ) {
            @Override
            protected void populateView(View v, String model, int position) {
                TextView textView=(TextView)v.findViewById(android.R.id.text1);
                textView.setText(model);
            }
        };
        mlistView.setAdapter(firebaseListAdapter);
    }
}
