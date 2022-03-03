package in.icomputercoding.folkchat.Activities;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import in.icomputercoding.folkchat.Fragments.AddPostFragment;
import in.icomputercoding.folkchat.Fragments.ChatFragment;
import in.icomputercoding.folkchat.Fragments.HomeFragment;
import in.icomputercoding.folkchat.Fragments.NotificationFragment;
import in.icomputercoding.folkchat.Fragments.ProfileFragment;
import in.icomputercoding.folkchat.R;
import in.icomputercoding.folkchat.databinding.ActivityHomeScreenBinding;

public class HomeScreen extends AppCompatActivity {

    ActivityHomeScreenBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

        binding.bottomNavigationView.setSelectedItemId(R.id.home);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.home:
                    fragment = new HomeFragment();
                    break;
                case R.id.chats:
                    fragment = new ChatFragment();
                    break;
                case R.id.profile:
                    fragment = new ProfileFragment();
                    break;
                case R.id.addPost:
                    fragment = new AddPostFragment();
                    break;
                case R.id.notification:
                    fragment = new NotificationFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

            return true;
        });


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search) {
            Toast.makeText(HomeScreen.this, "Search clicked.", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.topmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


}
