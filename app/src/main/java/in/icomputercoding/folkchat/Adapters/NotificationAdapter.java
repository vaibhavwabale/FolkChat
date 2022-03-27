package in.icomputercoding.folkchat.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.icomputercoding.folkchat.Fragments.PostDetailsFragment;
import in.icomputercoding.folkchat.Fragments.ProfileFragment;
import in.icomputercoding.folkchat.Model.Notification;
import in.icomputercoding.folkchat.Model.Post;
import in.icomputercoding.folkchat.Model.User;
import in.icomputercoding.folkchat.R;
import in.icomputercoding.folkchat.databinding.NotificationRvDesignBinding;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ImageViewHolder> {

    private final Context mContext;
    private final List<Notification> mNotification;

    public NotificationAdapter(Context context,List<Notification> notification) {
        this.mContext = context;
        this.mNotification = notification;
    }

    @NonNull
    @Override
    public NotificationAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_rv_design,parent,false);
        return new NotificationAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ImageViewHolder holder, int position) {

        final Notification notification = mNotification.get(position);

        holder.binding.comment.setText(notification.getText());

        getUserInfo(holder.binding.imageProfile,holder.binding.username,notification.getUserId());

        if (notification.isPost()) {
            holder.binding.postImageNotify.setVisibility(View.VISIBLE);
            getPostImage(holder.binding.postImageNotify,notification.getPostId());
        } else {
            holder.binding.postImageNotify.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
            if (notification.isPost()) {
                editor.putString("postId", notification.getPostId());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new PostDetailsFragment()).commit();
            } else {
                editor.putString("profileId", notification.getUserId());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new ProfileFragment()).commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mNotification.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        NotificationRvDesignBinding binding;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = NotificationRvDesignBinding.bind(itemView);
        }
    }

    private void getPostImage(ImageView postImg, String postId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("posts").child(postId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                assert post != null;
                Picasso.get()
                        .load(post.getPostImage())
                        .placeholder(R.drawable.placeholder)
                        .into(postImg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserInfo(CircleImageView imageProfile, TextView username, String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                assert user != null;
                Picasso.get()
                        .load(user.getProfileImage())
                        .placeholder(R.drawable.profile_user)
                        .into(imageProfile);
                username.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}



