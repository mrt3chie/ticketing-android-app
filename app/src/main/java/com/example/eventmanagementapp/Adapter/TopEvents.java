package com.example.eventmanagementapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.eventmanagementapp.Booking;
import com.example.eventmanagementapp.Model.Events;
import com.example.eventmanagementapp.R;

import java.util.ArrayList;

public class TopEvents extends RecyclerView.Adapter<TopEvents.viewholder> {
    ArrayList<Events> events;
    Context context;

    public TopEvents(ArrayList<Events> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public TopEvents.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_event_card, parent,false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull TopEvents.viewholder holder, int position) {
        holder.eventnameTxt.setText(events.get(position).getEventName());
        holder.eventlocationTxt.setText(events.get(position).getEventLocation());
        holder.eventPriceTxt.setText("Rs."+events.get(position).getEventPrice());
        holder.eventDateTxt.setText(events.get(position).getEventDate());

        Glide.with(context)
                .load(events.get(position).getEventimagePath())
                .transform(new CenterCrop(),new RoundedCorners(30))
                .into(holder.eventImage);

        //Pass Data to Booking Screen - Start
        holder.buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();

                Events eventsdata = events.get(adapterPosition);
                String eventNameData = eventsdata.getEventName();
                String eventDateData = eventsdata.getEventDate();
                String eventPriceData = eventsdata.getEventPrice();
                String eventLocationData = eventsdata.getEventLocation();
                String eventImageData = eventsdata.getEventimagePath();

                Intent intent = new Intent(context, Booking.class);
                intent.putExtra("eventName",eventNameData);
                intent.putExtra("eventDate",eventDateData);
                intent.putExtra("eventPrice",eventPriceData);
                intent.putExtra("eventLocation",eventLocationData);
                intent.putExtra("eventImage",eventImageData);
                context.startActivity(intent);
            }
        });

        //Pass Data to Booking Screen - End


    }

    @Override
    public int getItemCount() {
        return events.size();
    }


    public class viewholder extends RecyclerView.ViewHolder{
        TextView eventnameTxt,eventlocationTxt,eventPriceTxt, eventDateTxt;
        ImageView eventImage;
        Button buyButton;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            eventnameTxt = itemView.findViewById(R.id.eventName);
            eventlocationTxt = itemView.findViewById(R.id.eventLocation);
            eventPriceTxt = itemView.findViewById(R.id.eventPrice);
            eventDateTxt = itemView.findViewById(R.id.eventDate);
            eventImage = itemView.findViewById(R.id.eventImage);
            buyButton = itemView.findViewById(R.id.buyButton);

        }
    }
}
