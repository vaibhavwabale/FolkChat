package in.icomputercoding.folkchat.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import in.icomputercoding.folkchat.Model.Post;
import in.icomputercoding.folkchat.Model.User;
import in.icomputercoding.folkchat.R;
import in.icomputercoding.folkchat.SettingActivity;
import in.icomputercoding.folkchat.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    FirebaseDatabase database;
    private int mPostsCount = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        getFollowersCount();
        getFollowingCount();
        getPostsCount();

        binding.textEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            intent.putExtra(getString(R.string.calling_activity), getString(R.string.profile_activity));
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        database.getReference().child("users")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    Picasso.get()
                            .load(Objects.requireNonNull(user).getProfileImage())
                            .placeholder(R.drawable.profile_user)
                            .into(binding.profileImage);
                    binding.displayName.setText(user.getName());
                    binding.bio.setText(user.getBio());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return binding.getRoot();
    }

    private void getFollowingCount() {
    }

    private void getPostsCount() {
        mPostsCount = 0;

           Post post = new Post();

        database.getReference()
                .child("posts")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Post post = dataSnapshot.getValue(Post.class);
                            mPostsCount++;
                        }

                        binding.tvPosts.setText(String.valueOf(mPostsCount));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void getFollowersCount() {

        database.getReference()
                .child("users")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .child("followerCount")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            User user = dataSnapshot.getValue(User.class);
                            assert user != null;
                            binding.tvFollowing.setText(user.getFollowerCount());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

}

