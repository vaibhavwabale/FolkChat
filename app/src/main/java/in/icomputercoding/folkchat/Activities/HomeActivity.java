package in.icomputercoding.folkchat.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import in.icomputercoding.folkchat.Fragments.AddPostFragment;
import in.icomputercoding.folkchat.Fragments.HomeFragment;
import in.icomputercoding.folkchat.Fragments.NotificationFragment;
import in.icomputercoding.folkchat.Fragments.ProfileFragment;
import in.icomputercoding.folkchat.Fragments.SearchFragment;
import in.icomputercoding.folkchat.R;
import in.icomputercoding.folkchat.databinding.ActivityHomeScreenBinding;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeScreenBinding binding;
    Fragment fragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            String profile = intent.getString("profileId");

            SharedPreferences.Editor editor = getSharedPreferences("PREFS",MODE_PRIVATE).edit();
            editor.putString("profileId",profile);
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    new ProfileFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    new HomeFragment()).commit();
        }


        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();


        binding.bottomNavigationView.setSelectedItemId(R.id.home);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                fragment = new HomeFragment();
            } else if (itemId == R.id.Search) {
                fragment = new SearchFragment();
            } else if (itemId == R.id.addPost) {
                fragment = new AddPostFragment();
            } else if (itemId == R.id.notifications) {
                fragment = new NotificationFragment();
            } else if (itemId == R.id.profile) {
                SharedPreferences.Editor editor = getSharedPreferences("PREFS",MODE_PRIVATE).edit();
                editor.putString("profileId", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                editor.apply();
                fragment = new ProfileFragment();
            }

            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, Objects.requireNonNull(fragment)).commit();
            }

            return true;
        });


    }



    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}


