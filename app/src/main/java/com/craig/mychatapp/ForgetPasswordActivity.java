package com.craig.mychatapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class ForgetPasswordActivity extends AppCompatActivity {
    private TextInputEditText editTextForgetPassword;
    private Button btnResetPassword;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        editTextForgetPassword = findViewById(R.id.editTextForgetPassword);
        btnResetPassword = findViewById(R.id.buttonForget);

    }
}