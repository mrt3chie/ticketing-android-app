package com.example.eventmanagementapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.eventmanagementapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Signup extends AppCompatActivity {

    EditText editTextEmail, editTextPassword, editTextName,editTextPasswordConfirm;
    Button signupBtn, loginBtn;

    ProgressBar progressBar;
    FirebaseAuth mAuth;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(intent);
            finish();
        }
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmailAddress);
        editTextName = findViewById(R.id.editTextName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordConfirm = findViewById(R.id.editTextPasswordconfirm);
        progressBar = findViewById(R.id.progressbar);

        loginBtn = findViewById(R.id.Login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        progressBar.setVisibility(View.GONE);

        signupBtn = findViewById(R.id.Signup_btn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password, passwordconfirm,fullname,userrandID;

                Random random = new Random();
                int rand1 = random.nextInt(10000);
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                passwordconfirm = editTextPasswordConfirm.getText().toString();
                fullname = editTextName.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Signup.this,"Enter your Email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Signup.this,"Enter your Password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                    return;
                }

                if(password.equals(passwordconfirm)){
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        progressBar.setVisibility(View.GONE);

                                        //Get UserID
                                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                                        if(firebaseUser != null){
                                            String userId = firebaseUser.getUid();

                                            DocumentReference userRef = db.collection("users").document(userId);

                                            //Create Users Collection
                                            Map<String, Object> userData = new HashMap<>();
                                            userData.put("userid",rand1);
                                            userData.put("name",fullname);
                                            userData.put("email",email);

                                            userRef.set(userData)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(Signup.this,"Account Created", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(Signup.this,"Error Creating Account", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                        }else{
                                            Intent intent = new Intent(getApplicationContext(), Login.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(Signup.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                }









            }
        });

    }

}