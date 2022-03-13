package in.icomputercoding.folkchat.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import in.icomputercoding.folkchat.Activities.HomeActivity;
import in.icomputercoding.folkchat.Fragments.ProfileFragment;
import in.icomputercoding.folkchat.Model.User;
import in.icomputercoding.folkchat.R;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import in.icomputercoding.folkchat.databinding.UserSampleBinding;

import static android.content.Context.MODE_PRIVATE;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ImageViewHolder> {

    private final Context mContext;
    private final List<User> mUsers;
    private final boolean isFragment;

    private FirebaseUser firebaseUser;

    public SearchAdapter(Context context, List<User> users, boolean isFragment){
        mContext = context;
        mUsers = users;
        this.isFragment = isFragment;
    }

    @NonNull
    @Override
    public SearchAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_sample, parent, false);
        return new SearchAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchAdapter.ImageViewHolder holder, final int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final User user = mUsers.get(position);

        holder.binding.followBtn.setVisibility(View.VISIBLE);
        isFollowing(user.getUid(), holder.binding.followBtn);

        holder.binding.name.setText(user.getName());
        Picasso.get()
                .load(user.getProfileImage())
                .placeholder(R.drawable.profile_user)
                .into(holder.binding.profileImage);



       if (user.getUid().equals(firebaseUser.getUid())) {
            holder.binding.followBtn.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(view -> {
            if (isFragment) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                editor.putString("profileId", user.getUid());
                editor.apply();

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new ProfileFragment()).commit();
            } else {
                Intent intent = new Intent(mContext, HomeActivity.class);
                intent.putExtra("profileId", user.getUid());
                mContext.startActivity(intent);
            }
        });

        holder.binding.followBtn.setOnClickListener(view -> {
            if (holder.binding.followBtn.getText().toString().equals("follow")) {
                FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                        .child("following").child(user.getUid()).setValue(true);
                FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getUid())
                        .child("followers").child(firebaseUser.getUid()).setValue(true);

                addNotification(user.getUid());
            } else {
                FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                        .child("following").child(user.getUid()).removeValue();
                FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getUid())
                        .child("followers").child(firebaseUser.getUid()).removeValue();
            }
        });
    }

    private void addNotification(String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", firebaseUser.getUid());
        hashMap.put("text", "started following you");
        hashMap.put("postId", "");
        hashMap.put("isPost", false);

        reference.push().setValue(hashMap);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        UserSampleBinding binding;

        public ImageViewHolder(View itemView) {
            super(itemView);
            binding = UserSampleBinding.bind(itemView);
        }
    }

    private void isFollowing(final String userId, final Button button){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(Objects.requireNonNull(firebaseUser).getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userId).exists()){
                    button.setText("following");
                } else{
                    button.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
