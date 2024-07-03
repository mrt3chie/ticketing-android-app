package com.example.eventmanagementapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.eventmanagementapp.Activity.Dashboard;
import com.example.eventmanagementapp.Adapter.MyBookingsAdapter;
import com.example.eventmanagementapp.Adapter.TopEvents;
import com.example.eventmanagementapp.Model.BookingModel;
import com.example.eventmanagementapp.Model.Events;
import com.example.eventmanagementapp.databinding.ActivityMainBinding;
import com.example.eventmanagementapp.databinding.ActivityMyBookingsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyBookings extends AppCompatActivity {


    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    ProgressBar bookingProgressBar;

    private ActivityMyBookingsBinding binding; // Declare binding object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyBookingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.myBookingView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter adapter = new MyBookingsAdapter(new ArrayList<>());
        binding.myBookingView.setAdapter(adapter);

        initMyBookings();

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

    private void initMyBookings() {

        bookingProgressBar = findViewById(R.id.bookingProgressBar);
        bookingProgressBar.setVisibility(View.VISIBLE);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        // Get the current user's ID
        String userId = firebaseUser.getUid();

        // Reference to the "bookings" collection in Firestore
        CollectionReference bookingsRef = FirebaseFirestore.getInstance().collection("bookings");

        // ArrayList to store bookings data
        ArrayList<BookingModel> list1 = new ArrayList<>();

        //Get Today Date

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date(); // Current date
        String todayDateTime = formatter.format(date); // Format the date using SimpleDateFormat

        // Query bookings where userId equals the current user's ID
        bookingsRef.whereEqualTo("userID",userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                BookingModel bookings = document.toObject(BookingModel.class);
                                list1.add(bookings);
                            }

                            if (!list1.isEmpty()) {
                                RecyclerView.Adapter adapter = new MyBookingsAdapter(list1);
                                binding.myBookingView.setAdapter(adapter);
                                binding.myBookingView.setLayoutManager(new LinearLayoutManager(MyBookings.this, LinearLayoutManager.VERTICAL, false));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        bookingProgressBar.setVisibility(View.GONE);
    }

}