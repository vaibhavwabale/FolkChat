package in.icomputercoding.folkchat.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import in.icomputercoding.folkchat.Adapters.CommentAdapter;
import in.icomputercoding.folkchat.Model.User;
import in.icomputercoding.folkchat.Model.Comment;
import in.icomputercoding.folkchat.databinding.ActivityCommentBinding;

public class CommentActivity extends AppCompatActivity {

    ActivityCommentBinding binding;

    Intent intent;
    FirebaseUser user;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    String postId;
    String profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        intent = getIntent();
        postId = intent.getStringExtra("postId");
        profileId = intent.getStringExtra("profileId");

        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(manager);
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this,commentList,postId);
        binding.recyclerView.setAdapter(commentAdapter);

        user = FirebaseAuth.getInstance().getCurrentUser();

        binding.post.setOnClickListener(v -> {
            if (binding.addComment.getText().toString().equals("")) {
                Toast.makeText(CommentActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
            } else {
                addComment();
            }
        });

        getImage();
        readComments();


    }

    private void readComments() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments")
                .child(postId).child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    commentList.add(comment);
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getImage() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users")
                .child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(getApplicationContext()).load(Objects.requireNonNull(user).getProfileImage()).into(binding.imageProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addComment() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments")
                .child(postId);
        String commentId = reference.push().getKey();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("comment",binding.addComment.getText().toString());
        hashMap.put("profile",user.getUid());
        hashMap.put("commentId",commentId);

        reference.child(Objects.requireNonNull(commentId)).setValue(hashMap);
        addNotification();
        binding.addComment.setText("");
    }

    private void addNotification() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications")
                .child(profileId);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId",user.getUid());
        hashMap.put("text","commented: "+binding.addComment.getText().toString());
        hashMap.put("postId",postId);
        hashMap.put("isPost",true);

        reference.push().setValue(hashMap);

    }
}

