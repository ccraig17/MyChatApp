package com.craig.mychatapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Objects;

public class ForgetPasswordActivity extends AppCompatActivity {
    private TextInputEditText editTextForgetPassword;
    private Button btnResetPassword;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        editTextForgetPassword = findViewById(R.id.editTextForgetPassword);
        btnResetPassword = findViewById(R.id.buttonForget);
        auth = FirebaseAuth.getInstance();

        btnResetPassword.setOnClickListener((v -> {
          String email = Objects.requireNonNull(editTextForgetPassword.getText()).toString();
          if(!email.isEmpty()){
              resetPassword(email);
          }

        }
        ));
    }


    public void resetPassword(String email) {
        if (!email.isEmpty()) {
            auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(ForgetPasswordActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ForgetPasswordActivity.this, "Email not sent", Toast.LENGTH_SHORT).show();
            }

            });

        }
    }
}