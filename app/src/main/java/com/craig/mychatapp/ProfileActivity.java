package com.craig.mychatapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private CircleImageView imageViewCircleProfile;
    private TextInputEditText editTextUserNameProfile;
    private Button btnUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imageViewCircleProfile = findViewById(R.id.imageCircleImageViewProfile);
        editTextUserNameProfile = findViewById(R.id.editTextUserNameProfile);
        btnUpdate = findViewById(R.id.buttonUpdateProfile);

        btnUpdate.setOnClickListener((v -> {
            String userName = editTextUserNameProfile.getText().toString();
            if (!userName.isEmpty()) {
                updateProfile(userName);
            }
        }));
    }
    public void updateProfile(String userName) {
        //TODO: Update Profile
    }
}