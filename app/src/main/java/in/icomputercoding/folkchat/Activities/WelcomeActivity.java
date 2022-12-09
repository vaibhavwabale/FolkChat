package in.icomputercoding.folkchat.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import in.icomputercoding.folkchat.databinding.ActivityWelcomeScreenBinding;

public class WelcomeActivity extends AppCompatActivity {

    ActivityWelcomeScreenBinding binding;
    private long pressedTime;

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Tap Again To Exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        binding.btnLogin.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));

        binding.btnRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));

    }

}