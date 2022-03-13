package in.icomputercoding.folkchat.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import in.icomputercoding.folkchat.R;
import in.icomputercoding.folkchat.databinding.ActivitySplashScreenBinding;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    ActivitySplashScreenBinding binding;
    private long pressedTime;
    FirebaseAuth auth;


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
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        if (!isOnline()) {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.no_internet);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnCancelListener(dialogInterface -> SplashActivity.this.finish());
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
            TextView retry = dialog.findViewById(R.id.retry);
            retry.setOnClickListener(view -> recreate());
            dialog.show();
        } else {
            if (auth.getCurrentUser() != null) {
                new Handler().postDelayed(() -> {
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }, 1000);
            } else {
                new Handler().postDelayed(() -> {
                    Intent i = new Intent(SplashActivity.this, WelcomeActivity.class);
                    startActivity(i);
                    finish();
                }, 1000);
            }
        }


    }

    private boolean isOnline() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
}