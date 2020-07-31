package com.example.blogapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBlogList=(RecyclerView)findViewById(R.id.main_recycler_view_id);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabase.keepSynced(true);
        mAuth=FirebaseAuth.getInstance();
        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Blog> options=new  FirebaseRecyclerOptions.Builder<Blog>()
                .setQuery(mDatabase,Blog.class)
                .build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(options) {
            
            @Override
            public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_row, parent, false);

                return new BlogViewHolder(view);
            }
            
            @Override
            protected void onBindViewHolder(@NonNull BlogViewHolder holder, int position, @NonNull Blog model) {
                holder.setTitle(model.getTitle());
                holder.setDesc(model.getDesc());
                holder.setImage(getApplicationContext(),model.getImage());
            }

            
        };
//        
//        FirebaseRecyclerAdapter<Blog,BlogViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
//                Blog.class,
//                R.layout.blog_row,
//                BlogViewHolder.class,
//                mDatabase
//        ) {
//            @Override
//            protected void onBindViewHolder(@NonNull BlogViewHolder holder, int position, @NonNull Blog model) {
//
//                holder.setTitle(model.getTitle());
//                holder.setDesc(model.getDesc());
//                holder.setImage(getApplicationContext(),model.getImage());
//            }
//
//            @NonNull
//            @Override
//            public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                return null;
//            }
//        };
        firebaseRecyclerAdapter.startListening();
        mBlogList.setAdapter(firebaseRecyclerAdapter);
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.action_add)
        {
            startActivity(new Intent(MainActivity.this,PostActivity.class));
        }

        if(item.getItemId()==R.id.action_logout)
        {
            mAuth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setTitle(String title){
            TextView main_post_title=(TextView)mView.findViewById(R.id.main_post_title_id);
            main_post_title.setText(title);
        }

        public void setDesc(String desc){
            TextView main_post_desc=(TextView)mView.findViewById(R.id.main_post_desc_id);
            main_post_desc.setText(desc);
        }

        public void setImage(Context ctx, final String image){
            final ImageView image_view=(ImageView)mView.findViewById(R.id.main_post_image_id_1);
//            Picasso.get().load(image).fit().centerCrop().into(iv);
            Picasso.get().load(image).fit().centerCrop().networkPolicy(NetworkPolicy.OFFLINE).into(image_view, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(image).fit().centerCrop().into(image_view);
                }
            });
        }

    }

}
