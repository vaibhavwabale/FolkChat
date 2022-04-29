package in.icomputercoding.folkchat.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import in.icomputercoding.folkchat.Model.User;
import in.icomputercoding.folkchat.R;
import in.icomputercoding.folkchat.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends AppCompatActivity {

    ActivityEditProfileBinding binding;

    FirebaseUser firebaseUser;
    Uri imageUri;
    ActivityResultLauncher<Intent> launcher;
    StorageReference storageReference;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                assert user != null;
                binding.username.setText(user.getName());
                binding.bio.setText(user.getBio());
                Picasso.get()
                        .load(user.getProfileImage())
                        .placeholder(R.drawable.profile_user)
                        .into(binding.imageProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.close.setOnClickListener(v -> finish());

        binding.save.setOnClickListener(v -> {
            updateProfile(binding.username.getText().toString(),binding.bio.getText().toString());
            finish();
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    if (data.getData() != null) {
                        Uri uri = data.getData(); // filepath
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        long time = new Date().getTime();
                        StorageReference storageReference = storage.getReference().child("Profiles").child(time + "");
                        storageReference.putFile(uri).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                storageReference.getDownloadUrl().addOnSuccessListener(uri1 -> {
                                    String filePath = uri1.toString();
                                    HashMap<String, Object> obj = new HashMap<>();
                                    obj.put("profileImage", filePath);
                                    database.getReference().child("users")
                                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                                            .updateChildren(obj).addOnSuccessListener(aVoid -> {

                                    });
                                });
                            }
                        });


                        binding.imageProfile.setImageURI(data.getData());
                        imageUri = data.getData();
                    }
                }
            }
        });

        binding.imageProfile.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            launcher.launch(intent);
        });

        binding.tvChange.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            launcher.launch(intent);
        });




    }

    private void updateProfile(String name, String bio) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(firebaseUser.getUid());

        HashMap<String, Object> map = new HashMap<>();
        map.put("name",name);
        map.put("bio",bio);
        reference.updateChildren(map);

        Toast.makeText(EditProfileActivity.this, "Successfully updated!", Toast.LENGTH_SHORT).show();

    }

}