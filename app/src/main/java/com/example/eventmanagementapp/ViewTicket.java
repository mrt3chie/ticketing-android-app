package com.example.eventmanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventmanagementapp.Activity.Dashboard;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class ViewTicket extends AppCompatActivity {

    ImageView qrImg, backIcon;
    TextView bookingId, bookingDate, eventName, eventDate, amountTxt, ticketStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ticket);

        bookingId = findViewById(R.id.transactionIDTxt);
        bookingDate = findViewById(R.id.BookedDate);
        eventName = findViewById(R.id.EventNameTxt);
        eventDate = findViewById(R.id.eventDateTxt);
        amountTxt = findViewById(R.id.ticketAmt);
        qrImg = findViewById(R.id.imageQrBooking);
        backIcon = findViewById(R.id.backIcon);
        ticketStatus = findViewById(R.id.ticketStatus);

        Intent intent = getIntent();

        String bookingIDData = intent.getStringExtra("eventBookingID");
        String bookingTimestamp = intent.getStringExtra("eventBookingDate");
        String eventNameData = intent.getStringExtra("eventName");
        String eventDateData = intent.getStringExtra("eventDate");
        String Amount = intent.getStringExtra("eventPrice");
        String ScanStatus = intent.getStringExtra("scannedStatus");

        bookingId.setText("Booking Id: "+bookingIDData);
        bookingDate.setText("Booking Date: "+bookingTimestamp);
        eventName.setText("Event Name: "+eventNameData);
        eventDate.setText("Event Date: "+eventDateData);
        amountTxt.setText("Amount: Rs."+Amount);
        ticketStatus.setText("Ticket Scanned: "+ScanStatus);
        //QR Code Gen
        BitMatrix bitMatrix;
        MultiFormatWriter mWriter = new MultiFormatWriter();
        try {
            bitMatrix = mWriter.encode(bookingIDData, BarcodeFormat.QR_CODE,700,700 );
            BarcodeEncoder mEncoder = new BarcodeEncoder();
            Bitmap bitmap = mEncoder.createBitmap(bitMatrix);
            qrImg.setImageBitmap(bitmap);

        }catch (WriterException e){
            e.printStackTrace();
        }

        //Back Img Icon
        backIcon.setOnClickListener(v -> {
            Intent intent1 = new Intent(getApplicationContext(), MyBookings.class);
            startActivity(intent1);
            finish();
        });

    }
}