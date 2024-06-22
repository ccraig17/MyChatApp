package com.craig.mychatapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyLoginActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail, editeTextPassword;
    private TextView textViewForgetPassword;
    private Button btnSignIn, btnSignUp;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            Intent intent = new Intent(MyLoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editeTextPassword = findViewById(R.id.editTextPassword);
        textViewForgetPassword = findViewById(R.id.textViewForgetPassword);
        btnSignIn = findViewById(R.id.buttonSignIn);
        btnSignUp = findViewById(R.id.buttonSignUp);

        auth = FirebaseAuth.getInstance();


        btnSignIn.setOnClickListener((v -> {
            String email = editTextEmail.getText().toString();
            String password = editeTextPassword.getText().toString();
            if (!email.isEmpty() && (!password.isEmpty())) {
                signIn(email, password);
            }
            else{
                Toast.makeText(MyLoginActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }

        }));

        btnSignUp.setOnClickListener((v -> {
            Intent intent = new Intent(MyLoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        }));
        textViewForgetPassword.setOnClickListener((v -> {

        }));
    }

    public void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if(task.isSuccessful()){
                    Intent intent = new Intent(MyLoginActivity.this, MainActivity.class);
                    Toast.makeText(MyLoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MyLoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
        });
    }
}
