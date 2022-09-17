package in.icomputercoding.folkchat.Fragments;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import in.icomputercoding.folkchat.Activities.EditProfileActivity;
import in.icomputercoding.folkchat.Activities.FollowersActivity;
import in.icomputercoding.folkchat.Activities.OptionsActivity;
import in.icomputercoding.folkchat.Adapters.GridImageAdapter;
import in.icomputercoding.folkchat.Model.Photo;
import in.icomputercoding.folkchat.Model.Post;
import in.icomputercoding.folkchat.Model.User;
import in.icomputercoding.folkchat.R;
import in.icomputercoding.folkchat.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    private String profileId;
    private FirebaseUser firebaseUser;

    private static final int NUM_GRID_COLUMNS = 3;


    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);



        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences prefs = requireContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileId = prefs.getString("profileId","none");

        setupGridView();

        userInfo();
        getFollowers();
        getPosts();

        if (profileId.equals(firebaseUser.getUid())) {
            binding.editProfile.setText("Edit Profile");
        } else {
            checkFollow();
        }

        binding.editProfile.setOnClickListener(v -> {
            String btn = binding.editProfile.getText().toString();

            switch (btn) {
                case "Edit Profile":
                    startActivity(new Intent(getContext(), EditProfileActivity.class));
                    break;
                case "follow":
                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(firebaseUser.getUid()).child("following")
                            .child(profileId).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(profileId).child("followers")
                            .child(firebaseUser.getUid()).setValue(true);
                    break;
                case "following":
                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(firebaseUser.getUid()).child("following")
                            .child(profileId).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(profileId).child("followers")
                            .child(firebaseUser.getUid()).removeValue();
                    break;
            }
        });


        binding.options.setOnClickListener(v ->
                startActivity(new Intent(getContext(), OptionsActivity.class)));

        binding.followers.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), FollowersActivity.class);
            i.putExtra("Id",profileId);
            i.putExtra("Title","followers");
            startActivity(i);
        });

        binding.following.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), FollowersActivity.class);
            i.putExtra("Id",profileId);
            i.putExtra("Title","following");
            startActivity(i);
        });

        return binding.getRoot();
    }

    private void setupGridView() {
        Log.d(TAG, "setupGridView: Setting up image grid.");
        ProgressDialog dialog = ProgressDialog.show(getContext(), "",
                "Loading...", true);

        dialog.show();
        final ArrayList<Post> postsList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("posts");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Post post = singleSnapshot.getValue(Post.class);
                    if (post.getProfile().equals(profileId)) {
                        postsList.add(post);
                    }
                    Collections.reverse(postsList);
                }
                dialog.dismiss();
                //setup our image grid
                int gridWidth = getResources().getDisplayMetrics().widthPixels;
                int imageWidth = gridWidth/ NUM_GRID_COLUMNS;
                binding.gridView.setColumnWidth(imageWidth);

                ArrayList<String> imgUrls = new ArrayList<>();
                for(int i = 0; i < postsList.size(); i++){
                    imgUrls.add(postsList.get(i).getPostImage());
                }
                GridImageAdapter adapter = new GridImageAdapter(getActivity(),R.layout.layout_grid_imageview,
                        "", imgUrls);
                binding.gridView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
               dialog.dismiss();
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });
    }

    private void getFollowers(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow")
                .child(profileId).child("followers");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                binding.followers.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Follow")
                .child(profileId).child("following");
        reference1.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                binding.following.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPosts(){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("posts");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if (Objects.requireNonNull(post).getProfile().equals(profileId)){
                        i++;
                    }
                }
                binding.posts.setText(""+i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void userInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("users").child(profileId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Picasso.get().load(Objects.requireNonNull(user).getProfileImage())
                        .placeholder(R.drawable.profile_user)
                        .into(binding.imageProfile);
                binding.username.setText(user.getName());
                binding.profileName.setText(user.getName());
                binding.bio.setText(user.getBio());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkFollow() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(profileId).exists()) {
                    binding.editProfile.setText("following");
                } else {
                    binding.editProfile.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

