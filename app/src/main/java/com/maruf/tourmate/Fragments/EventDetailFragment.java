package com.maruf.tourmate.Fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maruf.tourmate.Adapters.ExpenseAdapter;
import com.maruf.tourmate.Models.Event;
import com.maruf.tourmate.Models.Expense;
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
public class EventDetailFragment extends Fragment {


    TextView eventDetailTitleTV,detailBudgetStatusTV;
    ProgressBar detailProgressbar;
    RecyclerView expenseListRV;
    FloatingActionButton addExpenseFab;

    ExpenseAdapter expenseAdapter;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String uid = "";
    List<Expense> expensesList = new ArrayList<>();

    Event currentEvent;
    Context context;

    public EventDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_detailes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        eventDetailTitleTV = view.findViewById(R.id.eventDetailTitleTV);
        detailBudgetStatusTV = view.findViewById(R.id.detailBudgetStatusTV);
        detailProgressbar = view.findViewById(R.id.detailProgressbar);
        expenseListRV = view.findViewById(R.id.detailRV);
        addExpenseFab = view.findViewById(R.id.detailFab);


        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getUid() != null) {
            uid = firebaseAuth.getUid();
        }
        firebaseDatabase = FirebaseDatabase.getInstance();


        Bundle bundle = getArguments();
        currentEvent = (Event) bundle.getSerializable("event");

        eventDetailTitleTV.setText(currentEvent.getEventTitle());

        databaseReference = firebaseDatabase.getReference().child("Users").child(uid)
                .child("Events").child(currentEvent.getId()).child("Expenses");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                expensesList.clear();

                for (DataSnapshot eSnapshot : dataSnapshot.getChildren()) {

                    expensesList.add(eSnapshot.getValue(Expense.class));


                }


                expenseAdapter = new ExpenseAdapter(getActivity(),expensesList);
                expenseListRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                expenseListRV.setAdapter(expenseAdapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        addExpenseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog addDialog = new Dialog(context);
                addDialog.setTitle("Add Expense");
                addDialog.setContentView(R.layout.add_expense_layout);

                final TextInputEditText expTitleET = addDialog.findViewById(R.id.expTitleET);
                final TextInputEditText expDateET = addDialog.findViewById(R.id.expDateET);
                final TextInputEditText expAmountET = addDialog.findViewById(R.id.expAmountET);

                TextView addTV = addDialog.findViewById(R.id.addTV);
                addTV.setText("Add");
                TextView cancelTV = addDialog.findViewById(R.id.cancelTV);

                final Calendar myCalendar = Calendar.getInstance();

                expDateET.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {



                        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {

                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                expDateET.setText( new SimpleDateFormat( "dd-MM-yy", Locale.US)
                                        .format(myCalendar.getTime()));
                            }

                        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                addTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        databaseReference = firebaseDatabase.getReference()
                                .child("Users").child(uid)
                                .child("Events").child(currentEvent.getId())
                                .child("Expenses").push();

                        HashMap<String, String> event = new HashMap<>();

                        event.put("id", databaseReference.getKey());
                        event.put("title", expTitleET.getText().toString());
                        event.put("date", String.valueOf(myCalendar.getTimeInMillis()));
                        event.put("amount", expAmountET.getText().toString());


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

}
