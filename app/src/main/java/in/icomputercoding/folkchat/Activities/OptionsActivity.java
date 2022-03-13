package in.icomputercoding.folkchat.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import in.icomputercoding.folkchat.R;

public class OptionsActivity extends AppCompatActivity {

    TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Options");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());

        logout = findViewById(R.id.logout);

        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(OptionsActivity.this, WelcomeActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        });
    }
}