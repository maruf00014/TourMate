package com.maruf.tourmate.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maruf.tourmate.Models.Event;
import com.maruf.tourmate.R;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {


    private List<Event> eventList;
    private Context context;

    EventAdapterInterface eventAdapterInterface;



    public EventsAdapter(Context context, List<Event> eventList) {
        this.eventList = eventList;
        this.context = context;
        eventAdapterInterface = (EventAdapterInterface) context;


    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,final int i) {

        View eventView = LayoutInflater.from(context).inflate(
                R.layout.single_event_item, viewGroup, false);


        return new EventViewHolder(eventView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder,final int i) {



        holder.titleTV.setText(eventList.get(i).getEventTitle());
        holder.fromToTV.setText(eventList.get(i).getFromTo());
        holder.budgetTV.setText(eventList.get(i).getBudget()+" $");


        holder.startDateTV.setText(new SimpleDateFormat("dd-MM-yyyy")
                .format(new Date(Long.valueOf(eventList.get(i).getStartDate()))));

        long daysToGo = (Long.valueOf(eventList.get(i).getStartDate())
                - System.currentTimeMillis()) / (1000*60*60*24);




        if(daysToGo < 0 ){

            holder.daysTogoTV.setText("Visited");

        } else  holder.daysTogoTV.setText( ""+daysToGo + " days to go");


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventAdapterInterface.onEventViewClicked(i);

            }
        });

        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventAdapterInterface.onEventDeleteImageClicked(i);
            }
        });

        holder.editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eventAdapterInterface.onEventEditImageClicked(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder{


        TextView titleTV, fromToTV,startDateTV, daysTogoTV, budgetTV;
        ImageView editImage, deleteImage;

        EventViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.eventTitleTV);
            fromToTV = itemView.findViewById(R.id.fromToTV);
            startDateTV = itemView.findViewById(R.id.eventStartTV);
            daysTogoTV = itemView.findViewById(R.id.daysToGoTV);
            budgetTV = itemView.findViewById(R.id.eventBudgetTV);
            editImage = itemView.findViewById(R.id.editIV);
            deleteImage = itemView.findViewById(R.id.deleteIV);



        }


    }

    public interface EventAdapterInterface{
        void onEventDeleteImageClicked(int position);
        void onEventEditImageClicked(int position);
        void onEventViewClicked(int position);
    }


}

