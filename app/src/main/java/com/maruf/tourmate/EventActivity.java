package com.maruf.tourmate;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maruf.tourmate.Adapters.EventsAdapter;
import com.maruf.tourmate.Adapters.ExpenseAdapter;
import com.maruf.tourmate.Fragments.EventListFragment;
import com.maruf.tourmate.Fragments.NearByFragment;
import com.maruf.tourmate.Models.Event;
import com.maruf.tourmate.Models.Expense;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class EventActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        EventsAdapter.EventAdapterInterface
         {

    ProgressDialog progressDialog;
    private double lat = 0.0;
    private double lon = 0.0;
    private boolean isLocationPermissionGranted = false;
    LocationManager locationManager;
    FusedLocationProviderClient locationProviderClient;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String uid = "";
    String userName ="";
    String userEmail ="";

    List<Event> eventList = new ArrayList<>();

    FragmentManager fragmentManager;

    TextView navNameTV,navEmailTV;

    EventListChangedInterface eventListChangedInterface;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog = ProgressDialog.show(EventActivity.this, "", "Loading...", true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        navNameTV =  headerView.findViewById(R.id.navNameTV);
        navEmailTV = headerView.findViewById(R.id.navEmailTV);


        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        checkLocationPermission();


        fragmentManager = getSupportFragmentManager();

        FragmentTransaction ft = fragmentManager.beginTransaction();

        EventListFragment eventListFragment = new EventListFragment();


        eventListChangedInterface = eventListFragment;


        ft.add(R.id.fragmentContainer, eventListFragment);
        ft.commit();




        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getUid() != null) {
            uid = firebaseAuth.getUid();
        }
        firebaseDatabase = FirebaseDatabase.getInstance();


        databaseReference = firebaseDatabase.getReference().child("Users").child(uid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                eventList.clear();
                userEmail = dataSnapshot.child("email").getValue(String.class);
                userName = dataSnapshot.child("name").getValue(String.class);

                navNameTV.setText(userName);
                navEmailTV.setText(userEmail);


                for (DataSnapshot eSnapshot : dataSnapshot.child("Events").getChildren()) {


                    eventList.add(eSnapshot.getValue(Event.class));


                }
                eventListChangedInterface.onEventListChanged(eventList);
                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }


    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    111);
        } else {
            isLocationPermissionGranted = true;
            getDeviceCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 111) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isLocationPermissionGranted = true;
                getDeviceCurrentLocation();
            } else checkLocationPermission();
        }
    }


    private void getDeviceCurrentLocation() {

        if (isLocationPermissionGranted) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        lat = location.getLatitude();
                        lon = location.getLongitude();
                    }
                }
            });

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            int count = getSupportFragmentManager().getBackStackEntryCount();

            if (count == 0) {
                moveTaskToBack(true);

            } else {
                startActivity(getIntent());
                finish();
            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.log_out) {

            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("logedin", false)
                    .putString("email", "")
                    .putString("pass", "")
                    .apply();

            Intent intent = new Intent(EventActivity.this,
                    RegisterLogInActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_event) {
            startActivity(getIntent());
            finish();


        } else if (id == R.id.nav_weather) {

            Intent intent = new Intent(EventActivity.this,WeatherActivity.class);
            intent.putExtra("lat",lat);
            intent.putExtra("lon",lon);
            startActivity(intent);

        } else if (id == R.id.nav_map) {

            FragmentTransaction ft = fragmentManager.beginTransaction();

            NearByFragment nearByFragment = new NearByFragment();

            Bundle bundle = new Bundle();
            bundle.putDouble("lat",lat);
            bundle.putDouble("lon",lon);
            nearByFragment.setArguments(bundle);
            ft.replace(R.id.fragmentContainer, nearByFragment);
            ft.addToBackStack(null);
            ft.commit();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





    private void updateEvent(final int position){

        final Dialog updateDialog = new Dialog(EventActivity.this);
        updateDialog.setTitle("Update Event");
        updateDialog.setContentView(R.layout.add_event_layout);
        final TextInputEditText titleET = updateDialog.findViewById(R.id.titletET);
        final TextInputEditText startET = updateDialog.findViewById(R.id.startET);
        final TextInputEditText destinationET = updateDialog.findViewById(R.id.destinationET);
        final TextInputEditText tourDateET = updateDialog.findViewById(R.id.tourDateET);
        final TextInputEditText tourBudgetET = updateDialog.findViewById(R.id.tourBudgetET);

        titleET.setText(eventList.get(position).getEventTitle());
        tourDateET.setText(new SimpleDateFormat("dd-MM-yyyy")
                .format(new Date(Long.valueOf(eventList.get(position).getStartDate()))));
        tourBudgetET.setText(eventList.get(position).getBudget());

        String[] toFrom = eventList.get(position).getFromTo().split(" To ",2);

        startET.setText(toFrom[0]);
        destinationET.setText(toFrom[1]);

        final TextView addTV = updateDialog.findViewById(R.id.addTV);
        addTV.setText("Update");
        TextView cancelTV = updateDialog.findViewById(R.id.cancelTV);

        final Calendar myCalendar = Calendar.getInstance();

        tourDateET.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                new DatePickerDialog(EventActivity.this, new DatePickerDialog.OnDateSetListener() {

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
                        .child(uid).child("Events").child(eventList.get(position).getId());

                HashMap<String, String> event = new HashMap<>();

                event.put("id", eventList.get(position).getId());
                event.put("eventTitle", titleET.getText().toString());
                event.put("fromTo", startET.getText().toString()
                        +" To "+destinationET.getText().toString());
                event.put("startDate", String.valueOf(myCalendar.getTimeInMillis()));
                event.put("budget", tourBudgetET.getText().toString());
                event.put("balance", tourBudgetET.getText().toString());

                databaseReference.setValue(event);

                updateDialog.dismiss();


            }


        });


        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDialog.dismiss();
            }
        });

        updateDialog.show();

    }

    @Override
    public void onEventDeleteImageClicked(final int position) {

        new AlertDialog.Builder(EventActivity.this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete?")

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        firebaseDatabase.getReference().child("Users").child(uid).child("Events").child(eventList.get(position).getId()).removeValue();


                    }
                })

                .setNegativeButton(android.R.string.no, null)
                .show();

    }

    @Override
    public void onEventEditImageClicked(int position) {

        updateEvent(position);
    }

    @Override
    public void onEventViewClicked(int position) {


        Intent intent = new Intent(EventActivity.this,EventDetailActivity.class);

        intent.putExtra("event",(Serializable) eventList.get(position));
        startActivity(intent);


    }




    public interface EventListChangedInterface{
        void onEventListChanged(List<Event> events);
    }



}
