package com.craig.mychatapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {
    private CircleImageView imageViewCircle;
    private TextInputEditText editTextEmailSignUp, editTextPasswordSignUp, editTextUsernameSignUp;
    private Button btnRegister;
    private boolean imageControl = false;
    ActivityResultLauncher<Intent> activityResultLauncher; //this class is used for imageSelector; to replace the deprecated method "startActivityForResult()"
    /*Firebase Classes and Reference Classes */
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        imageViewCircle = findViewById(R.id.imageCircleImageView);
        editTextEmailSignUp = findViewById(R.id.editTextEmailSignUp);
        editTextPasswordSignUp = findViewById(R.id.editTextPasswordSignUp);
        editTextUsernameSignUp = findViewById(R.id.editTextUserNameSignUp);
        btnRegister = findViewById(R.id.buttonRegister);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();



        registerActivityForSelectImage();

        imageViewCircle.setOnClickListener((v -> {
                imageChooser();
        }));

        btnRegister.setOnClickListener((v -> {
            String email = Objects.requireNonNull(editTextEmailSignUp.getText()).toString();
            String password = Objects.requireNonNull(editTextPasswordSignUp.getText()).toString();
            String userName = Objects.requireNonNull(editTextUsernameSignUp.getText()).toString();
            if (!email.isEmpty() && !password.isEmpty() && !userName.isEmpty()) {
                signup(email, password, userName);
            }

        }));
    }

    public void signup(String email, String password, String userName) {
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task ->{
                    if(task.isSuccessful()){
                        reference.child("Users").child(Objects.requireNonNull(auth.getUid())).child("userName").setValue(userName);//creates a UserId and saves the new User to the dB
                        if(imageControl){
                            UUID randomID = UUID.randomUUID(); //creates a random ID for each image to be saved to the database**
                           final String imageName = "images/"+randomID+".jpg";
                            storageReference.child(imageName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    StorageReference myStorageRef = firebaseStorage.getReference(imageName);
                                    myStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) { //the uri is the url of the saved image
                                            String filePath = uri.toString(); //the url of the image is first converted to a string before being saved to the database
                                            reference.child("Users").child(auth.getUid()).child("image").setValue(filePath).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                   // Log.d("SignUpActivity", "Image URL written to database successfully");
                                                    Toast.makeText(SignUpActivity.this, "Write to database is successful.", Toast.LENGTH_SHORT).show(); // NO Toast message appeared.....hmmmm
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                   // Log.e("SignUpActivity", "Error writing image URL to database", e);
                                                    Toast.makeText(SignUpActivity.this, "Write to database was NOT successful.", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }else{
                            reference.child("Users").child(auth.getUid()).child("image").setValue(null);
                        }
                        Intent intent = new Intent(SignUpActivity.this.getApplicationContext(), MainActivity.class);
                        intent.putExtra("userName", userName);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(SignUpActivity.this, "This email address is already in use by another account " , Toast.LENGTH_SHORT).show();
                    }
            });

    }
    public void imageChooser() {
        //second way to obtain an image from the gallery without permission
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
       // startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1); //depreciated method.
        activityResultLauncher.launch(intent); //solution to the depreciated method
    }
    public void registerActivityForSelectImage(){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent data = result.getData();
                        if (resultCode == RESULT_OK && data != null) {
                            imageUri = data.getData();
                            Picasso.get().load(imageUri).into(imageViewCircle);
                            imageControl = true;
                        } else {
                            imageControl = false;
                            imageViewCircle.setImageResource(R.drawable.signup);
                        }
                    }
                });
    }

}
