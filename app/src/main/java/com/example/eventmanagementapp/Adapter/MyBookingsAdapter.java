package com.example.eventmanagementapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.eventmanagementapp.Booking;
import com.example.eventmanagementapp.Model.BookingModel;
import com.example.eventmanagementapp.Model.Events;
import com.example.eventmanagementapp.R;
import com.example.eventmanagementapp.ViewTicket;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;

public class MyBookingsAdapter extends RecyclerView.Adapter<MyBookingsAdapter.viewholder> {

    ArrayList<BookingModel> bookings;
    Context context;

    public MyBookingsAdapter(ArrayList<BookingModel> bookings){
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public MyBookingsAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookings_card, parent,false);
        return new viewholder(inflate);
    }


    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.eventnameTxt.setText(bookings.get(position).getEventName());
        holder.eventDateTxt.setText(bookings.get(position).getEventDate());
        holder.bookingId.setText(bookings.get(position).getBookingId());
        holder.bookingDateTime.setText(bookings.get(position).getBookingDate());
        holder.eventPriceTxt.setText("Rs."+bookings.get(position).getAmount());
        holder.ticketStatus.setText("Ticket \nScanned: "+bookings.get(position).getScannedStatus());

        String bookingID = bookings.get(position).getBookingId();
        BitMatrix bitMatrix;
        MultiFormatWriter mWriter = new MultiFormatWriter();
        try {
            bitMatrix = mWriter.encode(bookingID, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder mEncoder = new BarcodeEncoder();
            Bitmap bitmap = mEncoder.createBitmap(bitMatrix);
            holder.qrImg.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        //Pass Data to Booking Screen - Start
        holder.qrImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();

                BookingModel bookingModel = bookings.get(adapterPosition);
                String eventNameData = bookingModel.getEventName();
                String eventDateData = bookingModel.getEventDate();
                String eventPriceData = String.valueOf(bookingModel.getAmount());
                String eventID = bookingModel.getBookingId();
                String eventBookingDate = bookingModel.getBookingDate();
                String scanStatus = bookingModel.getScannedStatus();

                Intent intent = new Intent(context, ViewTicket.class);
                intent.putExtra("eventName",eventNameData);
                intent.putExtra("eventDate",eventDateData);
                intent.putExtra("eventPrice",eventPriceData);
                intent.putExtra("eventBookingID",eventID);
                intent.putExtra("eventBookingDate",eventBookingDate);
                intent.putExtra("scannedStatus", scanStatus);
                context.startActivity(intent);
            }
        });

        //Pass Data to Booking Screen - End

    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView eventnameTxt,eventDateTxt,bookingId, bookingDateTime, eventPriceTxt, ticketStatus;
        ImageView qrImg;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            eventnameTxt = itemView.findViewById(R.id.eventName);
            eventDateTxt = itemView.findViewById(R.id.eventDate);
            bookingId = itemView.findViewById(R.id.bookingId);
            bookingDateTime = itemView.findViewById(R.id.bookingDateTime);
            eventPriceTxt = itemView.findViewById(R.id.eventPrice);
            qrImg = itemView.findViewById(R.id.qrCodeImg);
            ticketStatus = itemView.findViewById(R.id.ticketStatus);

        }
    }

}
