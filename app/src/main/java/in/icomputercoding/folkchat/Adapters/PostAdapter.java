package in.icomputercoding.folkchat.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import in.icomputercoding.folkchat.Activities.FollowersActivity;
import in.icomputercoding.folkchat.Activities.CommentActivity;
import in.icomputercoding.folkchat.Fragments.PostDetailsFragment;
import in.icomputercoding.folkchat.Fragments.ProfileFragment;
import in.icomputercoding.folkchat.Model.Post;
import in.icomputercoding.folkchat.Model.User;
import in.icomputercoding.folkchat.R;
import in.icomputercoding.folkchat.databinding.PostsRvDesignBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final Context mContext;
    private final List<Post> mPosts;

    private FirebaseUser firebaseUser;

    public PostAdapter(Context context, List<Post> posts){
        mContext = context;
        mPosts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.posts_rv_design, parent, false);
        return new PostViewHolder(view);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, final int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Post post = mPosts.get(position);
             if(post != null) {
                 Picasso.get()
                         .load(post.getPostImage())
                         .placeholder(R.drawable.placeholder)
                         .into(holder.binding.postImage);
                 if (post.getPostDescription().equals("")){
                     holder.binding.description.setVisibility(View.GONE);
                 } else {
                     holder.binding.description.setVisibility(View.VISIBLE);
                     holder.binding.description.setText(post.getPostDescription());
                 }


                 profileInfo(holder.binding.imageProfile, holder.binding.username, holder.binding.profileName, post.getProfile());
                 isLiked(post.getPostId(), holder.binding.like);
                 isSaved(post.getPostId(), holder.binding.save);
                 isLikes(holder.binding.likes, post.getPostId());
                 getComments(post.getPostId(), holder.binding.comments);

                 holder.binding.like.setOnClickListener(view -> {
                     if (holder.binding.like.getTag().equals("like")) {
                         FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId())
                                 .child(firebaseUser.getUid()).setValue(true);
                         addNotification(post.getProfile(), post.getPostId());
                     } else {
                         FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId())
                                 .child(firebaseUser.getUid()).removeValue();
                     }
                 });

                 holder.binding.save.setOnClickListener(view -> {
                     if (holder.binding.save.getTag().equals("save")){
                         FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid())
                                 .child(post.getPostId()).setValue(true);
                     } else {
                         FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid())
                                 .child(post.getPostId()).removeValue();
                     }
                 });

                 holder.binding.imageProfile.setOnClickListener(view -> {
                     SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                     editor.putString("profileId", post.getProfile());
                     editor.apply();

                     ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.container,
                             new ProfileFragment()).commit();
                 });

                 holder.binding.username.setOnClickListener(view -> {
                     SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                     editor.putString("profileId", post.getProfile());
                     editor.apply();

                     ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.container,
                             new ProfileFragment()).commit();
                 });

                 holder.binding.profileName.setOnClickListener(view -> {
                     SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                     editor.putString("profileId", post.getProfile());
                     editor.apply();

                     ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.container,
                             new ProfileFragment()).commit();
                 });

                 holder.binding.comment.setOnClickListener(view -> {
                     Intent intent = new Intent(mContext, CommentActivity.class);
                     intent.putExtra("postId", post.getPostId());
                     intent.putExtra("profileId", post.getProfile());
                     mContext.startActivity(intent);
                 });

                 holder.binding.comments.setOnClickListener(view -> {
                     Intent intent = new Intent(mContext, CommentActivity.class);
                     intent.putExtra("postId", post.getPostId());
                     intent.putExtra("profileId", post.getProfile());
                     mContext.startActivity(intent);
                 });

                 holder.binding.postImage.setOnClickListener(view -> {
                     SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                     editor.putString("postId", post.getPostId());
                     editor.apply();

                     ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.container,
                             new PostDetailsFragment()).commit();
                 });

                 holder.binding.likes.setOnClickListener(view -> {
                     Intent intent = new Intent(mContext, FollowersActivity.class);
                     intent.putExtra("Id", post.getPostId());
                     intent.putExtra("Title", "Likes");
                     mContext.startActivity(intent);
                 });

                 holder.binding.more.setOnClickListener(view -> {
                     PopupMenu popupMenu = new PopupMenu(mContext, view);
                     popupMenu.setOnMenuItemClickListener(menuItem -> {
                         switch (menuItem.getItemId()){
                             case R.id.edit:
                                 editPost(post.getPostId());
                                 return true;
                             case R.id.delete:
                                 final String id = post.getPostId();
                                 FirebaseDatabase.getInstance().getReference("posts")
                                         .child(post.getPostId()).removeValue()
                                         .addOnCompleteListener(task -> {
                                             if (task.isSuccessful()){
                                                 deleteNotifications(id, firebaseUser.getUid());
                                             }
                                         });
                                 return true;
                             case R.id.report:
                                 Toast.makeText(mContext, "Reported clicked!", Toast.LENGTH_SHORT).show();
                                 return true;
                             default:
                                 return false;
                         }
                     });
                     popupMenu.inflate(R.menu.post_menu);
                     if (!post.getProfile().equals(firebaseUser.getUid())){
                         popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                         popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
                     }
                     popupMenu.show();
                 });
             }

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        PostsRvDesignBinding binding;

        public PostViewHolder(View itemView) {
            super(itemView);
            binding = PostsRvDesignBinding.bind(itemView);
        }
    }

    private void addNotification(String userid, String postId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", firebaseUser.getUid());
        hashMap.put("text", "liked your post");
        hashMap.put("postId", postId);
        hashMap.put("isPost", true);

        reference.push().setValue(hashMap);
    }

    private void deleteNotifications(final String postId, String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (Objects.requireNonNull(snapshot.child("postId").getValue()).equals(postId)){
                        snapshot.getRef().removeValue()
                                .addOnCompleteListener(task -> Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void isLikes(final TextView likes, String postId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount()+" Likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getComments(String postId, final TextView comments){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments.setText("View All "+dataSnapshot.getChildrenCount()+" Comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void profileInfo(final ImageView image_profile, final TextView username, final TextView publisher, final String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                Picasso.get()
                        .load(user.getProfileImage())
                        .placeholder(R.drawable.profile_user)
                        .into(image_profile);
                username.setText(user.getName());
                publisher.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void isLiked(final String postId, final ImageView imageView){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Likes").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(Objects.requireNonNull(firebaseUser).getUid()).exists()){
                    imageView.setImageResource(R.drawable.ic_liked);
                    imageView.setTag("Liked");
                } else{
                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setTag("Like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void isSaved(final String postId, final ImageView imageView){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Saves").child(Objects.requireNonNull(firebaseUser).getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postId).exists()){
                    imageView.setImageResource(R.drawable.ic_save_black);
                    imageView.setTag("saved");
                } else{
                    imageView.setImageResource(R.drawable.ic_savee_black);
                    imageView.setTag("save");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void editPost(final String postId){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Edit Post");

        final EditText editText = new EditText(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);
        alertDialog.setView(editText);

        getText(postId, editText);

        alertDialog.setPositiveButton("Edit",
                (dialogInterface, i) -> {

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("description", editText.getText().toString());

                    FirebaseDatabase.getInstance().getReference("posts")
                            .child(postId).updateChildren(hashMap);
                });
        alertDialog.setNegativeButton("Cancel",
                (dialogInterface, i) -> dialogInterface.cancel());
        alertDialog.show();
    }

    private void getText(String postId, final EditText editText){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts")
                .child(postId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                editText.setText(Objects.requireNonNull(dataSnapshot.getValue(Post.class)).getPostDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

