package in.icomputercoding.folkchat.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import in.icomputercoding.folkchat.CommentActivity;
import in.icomputercoding.folkchat.Model.Notification;
import in.icomputercoding.folkchat.Model.Post;
import in.icomputercoding.folkchat.Model.User;
import in.icomputercoding.folkchat.R;
import in.icomputercoding.folkchat.databinding.PostsRvDesignBinding;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.viewHolder> {

    ArrayList<Post> list;
    Context context;
    boolean clicked = false;
    int likeCount;

    public PostsAdapter(ArrayList<Post> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.posts_rv_design, parent, false);
        return new viewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Post model = list.get(position);
        Picasso.get()
                .load(model.getPostImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.postImg);
        holder.binding.like.setText(model.getPostLike() + "");
        String description = model.getPostDescription();
        if (description.equals("")) {
            holder.binding.postDescription.setVisibility(View.GONE);
        } else {
            holder.binding.postDescription.setText(model.getPostDescription());
            holder.binding.postDescription.setVisibility(View.VISIBLE);
        }

        holder.binding.postImg.setOnClickListener(v -> {
            if (!clicked) {
                holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                likeCount = Integer.parseInt(holder.binding.like.getText() + "");
                //holder.like.setText(likeCount++);
                clicked = true;
            } else {
                holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_red, 0, 0, 0);
                likeCount = Integer.parseInt(holder.binding.like.getText() + "");
                // holder.like.setText(Integer.parseInt(model.getLike())-1);
                clicked = false;

            }
        });

        holder.binding.saveImg.setOnClickListener(v -> {

            if (!clicked) {
                holder.binding.saveImg.setImageResource(R.drawable.saved);
                clicked = true;
            } else {
                holder.binding.saveImg.setImageResource(R.drawable.ic_bookmark);
                clicked = false;
            }

        });
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(model.getPostedBy()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Picasso.get()
                        .load(Objects.requireNonNull(user).getProfileImage())
                        .placeholder(R.drawable.profile_user)
                        .into(holder.binding.profileImage);
                holder.binding.userName.setText(user.getName());
                holder.binding.bio.setText(user.getBio());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(model.getPostId())
                .child("likes")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_red, 0, 0, 0);
                } else {
                    holder.binding.like.setOnClickListener(v -> FirebaseDatabase.getInstance().getReference()
                            .child("posts")
                            .child(model.getPostId())
                            .child("likes")
                            .child(FirebaseAuth.getInstance().getUid())
                            .setValue(true).addOnSuccessListener(unused -> FirebaseDatabase.getInstance().getReference()
                                    .child("posts")
                                    .child(model.getPostId())
                                    .child("postLike")
                                    .setValue(model.getPostLike() + 1).addOnSuccessListener(unused1 -> {

                                        holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_red, 0, 0, 0);

                                        Notification notification = new Notification();
                                        notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                        notification.setNotificationAt(new Date().getTime());
                                        notification.setPostID(model.getPostId());
                                        notification.setPostedBy(model.getPostedBy());
                                        notification.setType("like");

                                        FirebaseDatabase.getInstance().getReference()
                                                .child("notification")
                                                .child(model.getPostedBy())
                                                .push()
                                                .setValue(notification);

                                    })));


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.binding.comment.setOnClickListener(v -> {
            Intent intent = new Intent(context, CommentActivity.class);
            context.startActivity(intent);
        });

        holder.binding.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(context, CommentActivity.class);
                Intent.putExtra("postId", model.getPostId());
                Intent.putExtra("postedBy", model.getPostedBy());
                Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(Intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        PostsRvDesignBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = PostsRvDesignBinding.bind(itemView);

        }
    }
}

