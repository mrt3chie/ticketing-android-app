package com.example.eventmanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eventmanagementapp.Activity.Dashboard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Booking extends AppCompatActivity {


    Button incrementBtn, DecrementBtn, payBtn;
    TextView ticketQtyTxt, eventName, eventLocation, eventDate, ticketCount, TicketTotal;
    ImageView eventImg, backBtn;
    ProgressBar progressBarLoad;
    Context context;
    private int value = 1;
    private int initialTicketCost=0;
    private String bookingId="";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_checkout);

        Intent intent = getIntent();
        String eventNameData = intent.getStringExtra("eventName");
        String eventDateData = intent.getStringExtra("eventDate");
        String eventPriceData = intent.getStringExtra("eventPrice");
        String eventLocationData = intent.getStringExtra("eventLocation");
        String eventImageData = intent.getStringExtra("eventImage");

        initialTicketCost = Integer.parseInt(eventPriceData);

        eventData(eventNameData,eventDateData,eventPriceData,eventLocationData,eventImageData);

        incrementBtn = findViewById(R.id.buttonIncrement);
        DecrementBtn = findViewById(R.id.buttonDecrement);
        ticketQtyTxt = findViewById(R.id.ticketValueTxt);
        progressBarLoad = findViewById(R.id.progressBarBook);

        progressBarLoad.setVisibility(View.GONE);
        // Set initial value
        updateValue();

        // Set click listeners
        DecrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementValue();
            }
        });

        incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementValue();
            }
        });


        //Back Img Icon
        backBtn = findViewById(R.id.backIcon);
        backBtn.setOnClickListener(v -> {
            Intent intent1 = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(intent1);
            finish();
        });

        //Book the Event & Store Data and redirect to success page
        payBtn = findViewById(R.id.buttonPay);

        payBtn.setOnClickListener(v -> {
            progressBarLoad.setVisibility(View.VISIBLE);
            initStoreBooking(eventNameData, eventDateData);
            progressBarLoad.setVisibility(View.GONE);
            //GetTimestamp
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String millisInString  = dateFormat.format(new Date());

            Intent intent1 = new Intent(getApplicationContext(),BookingSucess.class);
            intent1.putExtra("bookingId", bookingId);
            intent1.putExtra("bookingDate",millisInString);
            startActivity(intent1);
            finish();

        });


    }

    //Store the Bookings
    private void initStoreBooking(String eName, String eDate) {
        Intent intent1 = getIntent();

        Random random = new Random();
        int rand1 = random.nextInt(10000);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date(); // Current date

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        String userId = firebaseUser.getUid();
        bookingId = rand1 + (eName.substring(0, 2));
        String bookingDate = formatter.format(date); // Format the date using SimpleDateFormat


        //Create Bookings Collection
        Map<String, Object> userData = new HashMap<>();
        userData.put("bookingId",bookingId);
        userData.put("bookingDate",bookingDate);
        userData.put("userID",userId);
        userData.put("eventName",eName);
        userData.put("eventDate",eDate);
        userData.put("totalTickets",value);
        userData.put("amount",value*initialTicketCost);
        userData.put("scannedStatus","false");

        DocumentReference userRef = db.collection("bookings").document(bookingId);
        userRef.set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Booking.this,"Successfull", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Booking.this,"Error Initiating", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void eventData(String name,String date,String price,String location,String Img) {
        eventName = findViewById(R.id.eventName);
        eventLocation = findViewById(R.id.eventLocation);
        eventDate = findViewById(R.id.eventDate);
        eventImg = findViewById(R.id.eventImage);

        Glide.with(this).load(Img).into(eventImg);


        eventName.setText(name);
        eventLocation.setText(location);
        eventDate.setText(date);


    }

    private void decrementValue() {
        if (value > 1) {
            value--;
            updateValue();
        }
    }

    private void incrementValue() {
        if (value < 10) {
            value++;
            updateValue();
        }
    }

    private void updateValue() {
        ticketQtyTxt.setText(String.valueOf(value));
        updateCost();
    }

    private void updateCost(){
        //Total Count
        ticketCount = findViewById(R.id.ticketCount);
        TicketTotal = findViewById(R.id.ticketValue);
        ticketCount.setText(value+" x Ticket");
        TicketTotal.setText("Rs."+(value*initialTicketCost));
    }
}