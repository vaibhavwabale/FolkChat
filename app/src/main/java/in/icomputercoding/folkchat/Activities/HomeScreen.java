package in.icomputercoding.folkchat.Activities;

import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import in.icomputercoding.folkchat.Chats.ChatFragment;
import in.icomputercoding.folkchat.Fragments.AddPostFragment;
import in.icomputercoding.folkchat.Fragments.HomeFragment;
import in.icomputercoding.folkchat.Fragments.ProfileFragment;
import in.icomputercoding.folkchat.Fragments.SearchFragment;
import in.icomputercoding.folkchat.R;

public class HomeScreen extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                fragment = new HomeFragment();
            } else if (itemId == R.id.Search) {
                fragment = new SearchFragment();
            } else if (itemId == R.id.chats) {
                fragment = new ChatFragment();
            } else if (itemId == R.id.profile) {
                fragment = new ProfileFragment();
            } else if (itemId == R.id.addPost) {
                fragment = new AddPostFragment();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.container, Objects.requireNonNull(fragment)).commit();

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
