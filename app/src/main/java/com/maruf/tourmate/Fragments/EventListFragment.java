package com.maruf.tourmate.Fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maruf.tourmate.Adapters.EventsAdapter;
import com.maruf.tourmate.EventActivity;
import com.maruf.tourmate.Models.Event;
import com.maruf.tourmate.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventListFragment extends Fragment implements EventActivity.EventListChangedInterface {

    RecyclerView recyclerView;
    TextView emptyTourTV;
    EventsAdapter eventsAdapter;
    FloatingActionButton eventFab;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String uid = "";
    List<Event> eventList = new ArrayList<>();

    Context context;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;



    }

    public EventListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = view.findViewById(R.id.event_recycler_view);
        emptyTourTV = view.findViewById(R.id.emptyListTV);
        eventFab = view.findViewById(R.id.eventFab);


        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getUid() != null) {
            uid = firebaseAuth.getUid();
        }
        firebaseDatabase = FirebaseDatabase.getInstance();


        databaseReference = firebaseDatabase.getReference().child("Users").child(uid)
                .child("Events");



        eventFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog addDialog = new Dialog(context);
                addDialog.setTitle("Add Event");
                addDialog.setContentView(R.layout.add_event_layout);

                final TextInputEditText titleET = addDialog.findViewById(R.id.titletET);
                final TextInputEditText startET = addDialog.findViewById(R.id.startET);
                final TextInputEditText destinationET = addDialog.findViewById(R.id.destinationET);
                final TextInputEditText tourDateET = addDialog.findViewById(R.id.tourDateET);
                final TextInputEditText tourBudgetET = addDialog.findViewById(R.id.tourBudgetET);

                TextView addTV = addDialog.findViewById(R.id.addTV);
                addTV.setText("Add");
                TextView cancelTV = addDialog.findViewById(R.id.cancelTV);

                final Calendar myCalendar = Calendar.getInstance();

                tourDateET.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {



                        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {

                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                tourDateET.setText( new SimpleDateFormat( "dd-MM-yy", Locale.US)
                                        .format(myCalendar.getTime()));
                            }

                        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                addTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        databaseReference = firebaseDatabase.getReference("Users")
                                .child(uid).child("Events").push();

                        HashMap<String, String> event = new HashMap<>();

                        event.put("id", databaseReference.getKey());
                        event.put("eventTitle", titleET.getText().toString());
                        event.put("fromTo", startET.getText().toString()
                                +" To "+destinationET.getText().toString());
                        event.put("startDate", String.valueOf(myCalendar.getTimeInMillis()));
                        event.put("budget", tourBudgetET.getText().toString());
                        event.put("balance", tourBudgetET.getText().toString());

                        databaseReference.setValue(event);

                        addDialog.dismiss();


                    }


                });


                cancelTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addDialog.dismiss();
                    }
                });

                addDialog.show();

            }
        });




    }








    @Override
    public void onEventListChanged(List<Event> eventList) {
        if(eventList!=null)  emptyTourTV.setVisibility(View.INVISIBLE);

        eventsAdapter = new EventsAdapter(getActivity(),eventList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(eventsAdapter);
    }
}
