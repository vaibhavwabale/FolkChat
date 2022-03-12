package in.icomputercoding.folkchat.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import in.icomputercoding.folkchat.Activities.EditProfileActivity;
import in.icomputercoding.folkchat.Activities.FollowersActivity;
import in.icomputercoding.folkchat.Activities.OptionsActivity;
import in.icomputercoding.folkchat.Adapters.MyFotosAdapter;
import in.icomputercoding.folkchat.Model.Post;
import in.icomputercoding.folkchat.Model.User;
import in.icomputercoding.folkchat.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    private List<String> mySaves;
    private String profileId;
    private FirebaseUser firebaseUser;
    private MyFotosAdapter fotosAdapter;
    private List<Post> postList;
    private MyFotosAdapter fotosAdapter_saves;
    private List<Post> postList_saves;


    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences prefs = requireContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileId = prefs.getString("profileId","none");

        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new GridLayoutManager(getContext(),3);
        binding.recyclerView.setLayoutManager(manager);
        postList = new ArrayList<>();
        fotosAdapter = new MyFotosAdapter(getContext(),postList);
        binding.recyclerView.setAdapter(fotosAdapter);

        binding.recyclerViewSave.setHasFixedSize(true);
        LinearLayoutManager manager1 = new GridLayoutManager(getContext(),3);
        binding.recyclerViewSave.setLayoutManager(manager1);
        postList_saves = new ArrayList<>();
        fotosAdapter_saves = new MyFotosAdapter(getContext(),postList_saves);
        binding.recyclerViewSave.setAdapter(fotosAdapter_saves);

        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.recyclerViewSave.setVisibility(View.GONE);

        userInfo();
        getFollowers();
        getPosts();
        myFotos();
        mySaves();

        if (profileId.equals(firebaseUser.getUid())) {
            binding.editProfile.setText("Edit Profile");
        } else {
            checkFollow();
            binding.savedFotos.setVisibility(View.GONE);
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

        binding.myFotos.setOnClickListener(v -> {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.recyclerViewSave.setVisibility(View.GONE);
        });

        binding.savedFotos.setOnClickListener(v -> {
            binding.recyclerView.setVisibility(View.GONE);
            binding.recyclerViewSave.setVisibility(View.VISIBLE);
        });

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
                    if (Objects.requireNonNull(post).getPostProfile().equals(profileId)){
                        i++;
                    }
                }
                binding.posts.setText(""+i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void myFotos(){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("posts");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if (Objects.requireNonNull(post).getPostProfile().equals(profileId)){
                        postList.add(post);
                    }
                }
                Collections.reverse(postList);
                fotosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void mySaves(){
        mySaves = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Saves").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    mySaves.add(snapshot.getKey());
                }
                readSaves();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readSaves(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList_saves.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);

                    for (String id : mySaves) {
                        assert post != null;
                        if (post.getPostId().equals(id)) {
                            postList_saves.add(post);
                        }
                    }
                }
                fotosAdapter_saves.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void userInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getContext() == null) {
                    return;
                }
                User user = snapshot.getValue(User.class);
                Picasso.get().load(Objects.requireNonNull(user).getProfileImage()).into(binding.imageProfile);
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

