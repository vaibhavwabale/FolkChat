package in.icomputercoding.folkchat.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import in.icomputercoding.folkchat.Activities.HomeScreen;
import in.icomputercoding.folkchat.Model.Comment;
import in.icomputercoding.folkchat.Model.User;
import in.icomputercoding.folkchat.R;
import in.icomputercoding.folkchat.databinding.CommentSampleBinding;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ImageViewHolder> {

    private final Context mContext;
    private final List<Comment> mComment;
    private final String postId;


    public CommentAdapter(Context mContext, List<Comment> mComment, String postId) {
        this.mContext = mContext;
        this.mComment = mComment;
        this.postId = postId;
    }

    @NonNull
    @Override
    public CommentAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_sample, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ImageViewHolder holder, int position) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final Comment comment = mComment.get(position);

        holder.binding.comment.setText(comment.getComment());
        getUserInfo(holder.binding.imageProfile, holder.binding.name, comment.getProfile());

        holder.binding.name.setOnClickListener(v -> {
            Intent i = new Intent(mContext, HomeScreen.class);
            i.putExtra("profileId", comment.getProfile());
            mContext.startActivity(i);
        });

        holder.binding.imageProfile.setOnClickListener(v -> {
            Intent i = new Intent(mContext, HomeScreen.class);
            i.putExtra("profileId", comment.getProfile());
            mContext.startActivity(i);
        });

        holder.itemView.setOnLongClickListener(view -> {
            if (comment.getProfile().equals(Objects.requireNonNull(user).getUid())) {

                AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                alertDialog.setTitle("Do you want to delete?");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "No",
                        (dialog, which) -> dialog.dismiss());
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        (dialog, which) -> {
                            FirebaseDatabase.getInstance().getReference("Comments")
                                    .child(postId).child(String.valueOf(comment.getCommentId()))
                                    .removeValue().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog.dismiss();
                        });
                alertDialog.show();
            }
            return true;
        });


    }


    @Override
    public int getItemCount() {
        return mComment.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        CommentSampleBinding binding;

        public ImageViewHolder(View view) {
            super(view);
            binding = CommentSampleBinding.bind(view);
        }

    }

    private void getUserInfo(CircleImageView imageProfile, TextView name, String profile) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(profile);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(mContext).load(Objects.requireNonNull(user).getProfileImage()).into(imageProfile);
                name.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

