package com.example.eventmanagementapp.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eventmanagementapp.Adapter.TopEvents;
import com.example.eventmanagementapp.Booking;
import com.example.eventmanagementapp.Model.Events;
import com.example.eventmanagementapp.MyBookings;
import com.example.eventmanagementapp.Profile;
import com.example.eventmanagementapp.R;
import com.example.eventmanagementapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity  {
    private ActivityMainBinding binding;

    FirebaseAuth auth;
    Button button;
    TextView textView;
    ProgressBar progressBar;
    FirebaseUser user;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.topEventsView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.Adapter adapter = new TopEvents(new ArrayList<>()); // Provide an empty list initially
        binding.topEventsView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logoutBtn);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();

        initTopEvents();

        //Get Current User Details
        db.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                      textView.setText("Welcome Back, "+(String)document.get("name"));

//                        Intent intent = new Intent(Dashboard.this, Booking.class);
//                        intent.putExtra("userID",(String) document.get("userid") );
//                        startActivity(intent);
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });


//Bottom Navigation Menu
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int menuitemId = item.getItemId();

            if(menuitemId == R.id.home){
                Intent intent = new Intent(this, Dashboard.class);
                startActivity(intent);
            }
            else if(menuitemId == R.id.bookings){
                Intent intent = new Intent(this, MyBookings.class);
                startActivity(intent);
            }

            else if(menuitemId == R.id.profile){
                Intent intent = new Intent(this, Profile.class);
                startActivity(intent);
            }
            return false;
        });

    }

    private void initTopEvents() {
        progressBar = findViewById(R.id.dashboardProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        CollectionReference eventsRef = FirebaseFirestore.getInstance().collection("events");
        ArrayList<Events> list = new ArrayList<>();

        eventsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Events event = document.toObject(Events.class);
                        list.add(event);
                    }

                    if (!list.isEmpty()) {
                        RecyclerView.Adapter adapter = new TopEvents(list);
                        binding.topEventsView.setAdapter(adapter);
                        binding.topEventsView.setLayoutManager(new LinearLayoutManager(Dashboard.this, LinearLayoutManager.VERTICAL, false));
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        progressBar.setVisibility(View.GONE);
}




}