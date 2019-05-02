package com.maruf.tourmate;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailActivity extends AppCompatActivity implements
        ExpenseAdapter.ExpenseAdapterInterface {


    TextView eventDetailTitleTV, detailBudgetStatusTV;
    ProgressBar detailProgressbar;
    RecyclerView expenseListRV;
    FloatingActionButton addExpenseFab;

    ExpenseAdapter expenseAdapter;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String uid = "";
    List<Expense> expensesList = new ArrayList<>();
    Double totalExpense;
    Event currentEvent;

    Toolbar toolbar;


    private static final int CAMERA_REQUEST = 1888;
    private static final int STORAGE_PERMISSION_CODE = 100;
    private static final int CAMERA_PHOTO = 111;
    private Uri imageToUploadUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detailes);


        toolbar = (Toolbar) findViewById(R.id.detailTB);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        eventDetailTitleTV = findViewById(R.id.eventDetailTitleTV);
        detailBudgetStatusTV = findViewById(R.id.detailBudgetStatusTV);
        detailProgressbar = findViewById(R.id.detailProgressbar);
        expenseListRV = findViewById(R.id.detailRV);
        addExpenseFab = findViewById(R.id.detailFab);


        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getUid() != null) {
            uid = firebaseAuth.getUid();
        }
        firebaseDatabase = FirebaseDatabase.getInstance();


        currentEvent = (Event) getIntent().getSerializableExtra("event");

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

                totalExpense = 0.0;

                for (int i = 0; i < expensesList.size(); i++) {

                    totalExpense += Double.valueOf(expensesList.get(i).getAmount());
                }

                detailProgressbar.setMax(Integer.parseInt(currentEvent.getBudget()));
                detailProgressbar.setProgress((int) Math.round(totalExpense));

                detailProgressbar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
/*
                int progressLimt =  Integer.parseInt(currentEvent.getBudget())* (80/100);

                if (detailProgressbar.getProgress() < progressLimt ){

                } else detailProgressbar.setProgressTintList(ColorStateList.valueOf(Color.RED));
*/

                detailBudgetStatusTV.setText("Budget Status(" + Math.round(totalExpense) + "/"
                        + currentEvent.getBudget() + ")");
                expenseAdapter = new ExpenseAdapter(EventDetailActivity.this, expensesList, currentEvent);
                expenseListRV.setLayoutManager(new LinearLayoutManager(EventDetailActivity.this));
                expenseListRV.setAdapter(expenseAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        addExpenseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog addDialog = new Dialog(EventDetailActivity.this);
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


                        new DatePickerDialog(EventDetailActivity.this, new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {

                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                expDateET.setText(new SimpleDateFormat("dd-MM-yy", Locale.US)
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                captureCameraImage();

            } else checkStoragePermission();
        }
    }

    private void captureCameraImage() {
        Intent chooserIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(this.getCacheDir(), "POST_IMAGE.jpg");
        //File f = new File(Environment.getExternalStorageDirectory(), "POST_IMAGE.jpg");
        chooserIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        imageToUploadUri = Uri.fromFile(f);
        startActivityForResult(chooserIntent, CAMERA_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_PHOTO && resultCode == Activity.RESULT_OK) {
            if(imageToUploadUri != null){
                Uri selectedImage = imageToUploadUri;
                getContentResolver().notifyChange(selectedImage, null);
                Bitmap reducedSizeBitmap = getBitmap(imageToUploadUri.getPath());
                if(reducedSizeBitmap != null){

                    /*
                    ImgPhoto.setImageBitmap(reducedSizeBitmap);
                    Button uploadImageButton = (Button) findViewById(R.id.uploadUserImageButton);
                    uploadImageButton.setVisibility(View.VISIBLE);*/
                }else{
                    Toast.makeText(this,"Error while capturing Image",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this,"Error while capturing Image",Toast.LENGTH_LONG).show();
            }
        }
    }

    private Bitmap getBitmap(String path) {

        Uri uri = Uri.fromFile(new File(path));
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b = null;
            in = getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " +
                    b.getHeight());
            return b;
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.event_detail_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cameraIV:
                checkStoragePermission();
                return true;

            case R.id.galleryIV:
                Intent galleryIntent = new Intent(Intent.ACTION_VIEW,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivity(galleryIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void checkStoragePermission(){


        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
        else
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }

    }




    @Override
    public void onExpenseEditImageClicked(final Expense expense, final Event event) {

        final Dialog updateDialog = new Dialog(EventDetailActivity.this);
        updateDialog.setTitle("Update Expense");
        updateDialog.setContentView(R.layout.add_expense_layout);

        final TextInputEditText expTitleET = updateDialog.findViewById(R.id.expTitleET);
        final TextInputEditText expAmountET = updateDialog.findViewById(R.id.expAmountET);
        final TextInputEditText expDateET = updateDialog.findViewById(R.id.expDateET);

        expTitleET.setText(expense.getTitle());
        expDateET.setText(new SimpleDateFormat("dd-MM-yyyy")
                .format(new Date(Long.valueOf(expense.getDate()))));
        expAmountET.setText(expense.getAmount());


        final TextView addTV = updateDialog.findViewById(R.id.addTV);
        addTV.setText("Update");
        TextView cancelTV = updateDialog.findViewById(R.id.cancelTV);

        final Calendar myCalendar = Calendar.getInstance();

        expDateET.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                new DatePickerDialog(EventDetailActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {

                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        expDateET.setText(new SimpleDateFormat("dd-MM-yy", Locale.US)
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
                        .child("Events").child(event.getId())
                        .child("Expenses").child(expense.getId());

                HashMap<String, String> event = new HashMap<>();

                event.put("id", expense.getId());
                event.put("title", expTitleET.getText().toString());
                event.put("date", String.valueOf(myCalendar.getTimeInMillis()));
                event.put("amount", expAmountET.getText().toString());

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
    public void onExpenseDeleteImageClicked(final Expense expense, final Event event) {

        new AlertDialog.Builder(EventDetailActivity.this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete?")

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        firebaseDatabase.getReference()
                                .child("Users").child(uid)
                                .child("Events").child(event.getId())
                                .child("Expenses").child(expense.getId()).removeValue();

                    }
                })

                .setNegativeButton(android.R.string.no, null)
                .show();


    }





}
