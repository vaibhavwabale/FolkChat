package in.icomputercoding.folkchat.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import in.icomputercoding.folkchat.databinding.ActivityOtpverifyScreenBinding;

public class OTPVerifyScreen extends AppCompatActivity {

    ActivityOtpverifyScreenBinding binding;
    FirebaseAuth auth;
    private String code;
    String phoneNumber, CodeOTP;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpverifyScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseApp.initializeApp(this);

        auth = FirebaseAuth.getInstance();



        phoneNumber = getIntent().getStringExtra("phoneNumber");
        CodeOTP = getIntent().getStringExtra("CodeOTP");
        binding.otpDescriptionText.setText("Enter One Time Password Sent On " + phoneNumber);
        sendVerificationCode(phoneNumber);

        binding.VerifyBtn.setOnClickListener(v -> {

            code = Objects.requireNonNull(binding.PinOTP.getText()).toString();

            if (!code.isEmpty()) {
                if (CodeOTP != null) {
                    verifyCode(code);
                } else {
                    Toast.makeText(OTPVerifyScreen.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(OTPVerifyScreen.this, "Enter Enter All Number", Toast.LENGTH_SHORT).show();
            }
        });

        binding.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(OTPVerifyScreen.this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(OTPVerifyScreen.this, "Verification Not Completed! Try again.", Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String newCodeOTP, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(CodeOTP, forceResendingToken);
                                CodeOTP = newCodeOTP;
                                Toast.makeText(OTPVerifyScreen.this, "OTP Sent Successfully", Toast.LENGTH_LONG).show();
                            }

                        }).build();

                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });

    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(OTPVerifyScreen.this, "Verification Completed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OTPVerifyScreen.this, ProfileScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(OTPVerifyScreen.this, "Verification Not Completed! Try again.", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(CodeOTP, code);
        signInWithCredential(credential);
    }

    private void sendVerificationCode(String number) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(OTPVerifyScreen.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        String code = phoneAuthCredential.getSmsCode();
                        if (code != null) {
                            binding.PinOTP.setText(code);
                            verifyCode(code);

                        }
                    }


                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(OTPVerifyScreen.this, "Verification Not Completed! Try again.", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        CodeOTP = s;
                    }

                }).build();

        PhoneAuthProvider.verifyPhoneNumber(options);

    }

}