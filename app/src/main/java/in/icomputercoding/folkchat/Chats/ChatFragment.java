package in.icomputercoding.folkchat.Chats;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import in.icomputercoding.folkchat.Chats.Adapter.ChatAdapter;
import in.icomputercoding.folkchat.Model.User;
import in.icomputercoding.folkchat.databinding.FragmentChatBinding;

public class ChatFragment extends Fragment {

    FragmentChatBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<User> users = new ArrayList<>();
    ChatAdapter chatAdapter;
    FirebaseApp app;
    private FirebaseUser firebaseUser;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentChatBinding.inflate(getLayoutInflater());

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        auth = FirebaseAuth.getInstance();

        app = FirebaseApp.initializeApp(requireContext());
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance());

        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        database = FirebaseDatabase.getInstance(Objects.requireNonNull(FirebaseApp.initializeApp(requireContext())));

        FirebaseMessaging.getInstance()
                .getToken()
                .addOnSuccessListener(token -> {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("token", token);
                    database.getReference()
                            .child("users")
                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                            .updateChildren(map);
                });

        chatAdapter = new ChatAdapter(getContext(), users);
        binding.recyclerView.setAdapter(chatAdapter);
        binding.recyclerView.showShimmerAdapter();


        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    User user = snapshot1.getValue(User.class);
                    assert user != null;
                    if(!Objects.equals(user.getUid(), firebaseUser.getUid()))
                        users.add(user);
                }
                binding.recyclerView.hideShimmerAdapter();
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        return binding.getRoot();
    }

        @Override
        public void onResume() {
            super.onResume();
            String currentId = FirebaseAuth.getInstance().getUid();
            database.getReference().child("presence").child(Objects.requireNonNull(currentId)).setValue("Online");
        }

        @Override
        public void onPause() {
            super.onPause();
            String currentId = FirebaseAuth.getInstance().getUid();
            database.getReference().child("presence").child(Objects.requireNonNull(currentId)).setValue("Offline");
        }


    }