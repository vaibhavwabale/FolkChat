package in.icomputercoding.folkchat;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.Date;

import in.icomputercoding.folkchat.Model.Post;
import in.icomputercoding.folkchat.Model.User;
import in.icomputercoding.folkchat.Model.Comment;
import in.icomputercoding.folkchat.databinding.ActivityCommentBinding;

public class CommentActivity extends AppCompatActivity {

    ActivityCommentBinding binding;

    Intent intent;
    String postId;
    String postedBy;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intent = getIntent();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        postId = intent.getStringExtra("postId");
        postedBy = intent.getStringExtra("postedBy");

        database.getReference()
                .child("posts")
                .child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                Picasso.get()
                        .load(post.getPostImage())
                        .placeholder(R.drawable.placeholder)
                        .into(binding.postImg);
                binding.postDescription.setText(post.getPostDescription());
                binding.like.setText(post.getPostLike()+"");
                binding.comment.setText(post.getCommentCount());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getReference()
                .child("Users")
                .child(postedBy).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Picasso.get()
                        .load(user.getProfile())
                        .placeholder(R.drawable.placeholder)
                        .into(binding.profileImage);
                binding.name.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        Toast.makeText(this, "Post ID:" + postId, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "User ID:" + postedBy, Toast.LENGTH_SHORT).show();

        binding.backArrow.setOnClickListener(v -> {
            Log.d(TAG, "onClick: navigating back to 'ProfileActivity'");
            finish();
        });

    binding.commentBtn.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){

            Comment comment = new Comment();
            comment.setCommentBody(binding.commentBox.getText().toString());
            comment.setCommentAt(new Date().getTime());
            comment.setCommentedBy(FirebaseAuth.getInstance().getUid());

            database.getReference()
                    .child("posts")
                    .child(postId)
                    .child("commnets")
                    .setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    database.getReference()
                            .child("posts")
                            .child(postId)
                            .child("commentCount").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int commentCount = 0;
                            if(snapshot.exists()){
                                commentCount = snapshot.getValue(Integer.class);
                            }
                            database.getReference()
                                    .child("posts")
                                    .child(postId)
                                    .child("commentCount")
                                    .setValue(commentCount + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    binding.commentBox.setText("");
                                    Toast.makeText(CommentActivity.this, "Commented", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                });
            }


    });
    }
}

