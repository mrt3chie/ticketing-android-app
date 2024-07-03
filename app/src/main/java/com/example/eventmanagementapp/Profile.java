package com.example.eventmanagementapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.eventmanagementapp.Activity.Dashboard;
import com.example.eventmanagementapp.Activity.Login;
import com.example.eventmanagementapp.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText fullname, email;

    Button saveProfileBtn;

    private ActivityProfileBinding binding; // Change to ActivityProfileBinding

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize binding
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Set content view to root of the binding layout


        //Get Profile Details from DB - Start
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        fullname = findViewById(R.id.editTextfullNameProfile);
        email = findViewById(R.id.editTextEmailAddressProfile);

        db.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        fullname.setText((String)document.get("name"));
                        email.setText((String) document.get("email"));
                    } else {
                        Log.d(TAG, "Failed to fetch details", task.getException());
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        //Get Profile Details from DB - End


        //Bottom Navigation Menu
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int menuItemId = item.getItemId();

            if (menuItemId == R.id.home) {
                Intent intent = new Intent(this, Dashboard.class);
                startActivity(intent);
                return true;
            } else if (menuItemId == R.id.bookings) {
                Intent intent = new Intent(this, MyBookings.class);
                startActivity(intent);
                return true;
            } else if (menuItemId == R.id.profile) {
                // You are already in the Profile activity, no need to start it again
                return true;
            }
            return false;
        });
    }
}
