package com.example.eventmanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.eventmanagementapp.Activity.Dashboard;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class BookingSucess extends AppCompatActivity {

    TextView bookingIDTxt, bookingDateTxt;
    ImageView imageQrBooking;
    Button doneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_sucess);

        dispBookingDetails();
    }

    private void dispBookingDetails(){
        bookingIDTxt = findViewById(R.id.transactionIDTxt);
        bookingDateTxt = findViewById(R.id.DateTimeTxt);
        imageQrBooking = findViewById(R.id.imageQrBooking);
        doneBtn = findViewById(R.id.buttondone);

        Intent intent = getIntent();
        String bookingID = intent.getStringExtra("bookingId");
        String bookingTimestamp = intent.getStringExtra("bookingDate");

        bookingIDTxt.setText("Booking Id: "+bookingID);
        bookingDateTxt.setText("Date & Time: "+bookingTimestamp);

        //QR Code Gen
        BitMatrix bitMatrix;
        MultiFormatWriter mWriter = new MultiFormatWriter();
        try {
            bitMatrix = mWriter.encode(bookingID, BarcodeFormat.QR_CODE,600,600 );
            BarcodeEncoder mEncoder = new BarcodeEncoder();
            Bitmap bitmap = mEncoder.createBitmap(bitMatrix);

            imageQrBooking.setImageBitmap(bitmap);

        }catch (WriterException e){
            e.printStackTrace();
        }

        doneBtn.setOnClickListener(v -> {
            Intent intent1 = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(intent1);
            finish();
        });
    }
}