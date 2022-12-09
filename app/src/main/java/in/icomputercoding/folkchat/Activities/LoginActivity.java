package in.icomputercoding.folkchat.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import in.icomputercoding.folkchat.R;
import in.icomputercoding.folkchat.databinding.ActivityLoginBinding;


public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Dialog dialog;
    private long pressedTime;
    String email, pass;
    ActivityLoginBinding binding;

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            Intent i = new Intent(this, WelcomeActivity.class);
            startActivity(i);
            super.onBackPressed();
            finish();
        }
        pressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


        dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        binding.arrowBack.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
            finish();
        });

        binding.reset.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            finish();
        });

        binding.tvRegister.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUser();
            }

            private void validateUser() {
                email = binding.etEmail.getText().toString();
                pass = binding.etPass.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {
                    Toast.makeText(LoginActivity.this, "Enter a  valid email address", Toast.LENGTH_SHORT).show();
                } else if (pass.length() < 6) {
                    Toast.makeText(LoginActivity.this, "Password should contain 6 characters!", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser();
                }

            }

            private void loginUser() {
                dialog.show();
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                    dialog.hide();
                    if (task.isSuccessful()) {
                        Intent i = new Intent(LoginActivity.this, ProfileActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }
}