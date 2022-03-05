package in.icomputercoding.folkchat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import in.icomputercoding.folkchat.Model.Follow;
import in.icomputercoding.folkchat.Model.User;
import in.icomputercoding.folkchat.R;
import in.icomputercoding.folkchat.databinding.UserSampleBinding;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.viewHolder> {


    Context context;
    ArrayList<User> list;

    public SearchAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        User user = list.get(position);
        Picasso.get()
                .load(user.getProfileImage())
                .placeholder(R.drawable.profile_user)
                .into(holder.binding.profileImage);

        holder.binding.name.setText(user.getName());

        holder.binding.followBtn.setOnClickListener(v -> {
            Follow follow = new Follow();
            follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
            follow.setFollowedAt(new Date().getTime());

            FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(user.getUid())
                    .child("followers")
                    .child(FirebaseAuth.getInstance().getUid())
                    .setValue(follow).addOnSuccessListener(unused -> FirebaseDatabase.getInstance().getReference()
                            .child("users")
                            .child(user.getUid())
                            .child("followers")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child("followerCount")
                            .setValue(user.getFollowerCount() + 1).addOnSuccessListener(unused1 -> Toast.makeText(context,"You Followed " + user.getName(),Toast.LENGTH_SHORT).show()));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        UserSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = UserSampleBinding.bind(itemView);
        }
    }

}
