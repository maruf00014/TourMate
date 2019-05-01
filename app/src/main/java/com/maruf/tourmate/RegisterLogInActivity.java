package com.maruf.tourmate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maruf.tourmate.Fragments.LoginFragment;
import com.maruf.tourmate.Fragments.RegistrationFragment;

import java.util.HashMap;
import java.util.Map;

public class RegisterLogInActivity extends AppCompatActivity implements LoginFragment.LoginFragmentInterface,
        RegistrationFragment.RegistrationFragmentInterface {
    FragmentManager fragmentManager;

    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_log_in);

        firebaseAuth = FirebaseAuth.getInstance();



        fragmentManager = getSupportFragmentManager();

        FragmentTransaction ft = fragmentManager.beginTransaction();

        LoginFragment loginFragment = new LoginFragment();

        ft.add(R.id.fragmentContainer, loginFragment);
        ft.commit();


        if (getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("logedin", false)) {

            ft.remove(loginFragment);

            login(getSharedPreferences("PREFERENCE",
                    MODE_PRIVATE).getString("email", ""),
                    getSharedPreferences("PREFERENCE",
                    MODE_PRIVATE).getString("pass", ""));


        }





    }

    @Override
    public void onLoginButtonClicked(final String email, final String pass) {

            login(email,pass);

    }

    @Override
    public void onSignUpNowTVClicked() {

        FragmentTransaction ft = fragmentManager.beginTransaction();
        RegistrationFragment registrationFragment = new RegistrationFragment();
        ft.replace(R.id.fragmentContainer, registrationFragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    @Override
    public void onSignUPButtonClicked(final String name, final String email, final String pass) {
        progressDialog = ProgressDialog.show(
                RegisterLogInActivity.this, "Please wait...", "Proccessing...", true);


        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {
                            storeData(name, email, pass);

                        } else {
                            Log.e("ERROR", task.getException().toString());
                            if(progressDialog!=null) progressDialog.dismiss();
                            Toast.makeText(RegisterLogInActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    @Override
    public void onLogInNowTVClicked() {

        FragmentTransaction ft = fragmentManager.beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        ft.replace(R.id.fragmentContainer, loginFragment);
        ft.addToBackStack(null);
        ft.commit();


    }


    private void storeData(final String name, final String email,final String pass) {

        DatabaseReference databaseReference =
                FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(firebaseAuth.getCurrentUser().getUid());

        Map addUser = new HashMap();
        addUser.put("name", name);
        addUser.put("email", email);

        databaseReference.setValue(addUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                        .edit()
                        .putBoolean("logedin", true)
                        .putString("email", email)
                        .putString("pass", pass)
                        .apply();
                Intent i = new Intent(RegisterLogInActivity.this, EventActivity.class);
                startActivity(i);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterLogInActivity.this,
                        "Failed to store data!", Toast.LENGTH_SHORT).show();
                if(progressDialog != null) progressDialog.dismiss();
            }
        });




        }


    public void login(final String email,final String pass){

        progressDialog = ProgressDialog.show(
                RegisterLogInActivity.this, "Please wait...", "Proccessing...", true);


        firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {

                            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                    .edit()
                                    .putBoolean("logedin", true)
                                    .putString("email", email)
                                    .putString("pass", pass)
                                    .apply();

                            progressDialog.dismiss();
                            Intent i = new Intent(RegisterLogInActivity.this, EventActivity.class);
                            startActivity(i);
                        } else {
                            progressDialog.dismiss();
                            Log.e("ERROR", task.getException().toString());
                            Toast.makeText(RegisterLogInActivity.this,
                                    task.getException().toString(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }


}