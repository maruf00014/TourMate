package com.maruf.tourmate;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.maruf.tourmate.Fragments.LoginFragment;
import com.maruf.tourmate.Fragments.RegistrationFragment;

public class RegisterLogInActivity extends AppCompatActivity implements LoginFragment.LoginFragmentInterface,
        RegistrationFragment.RegistrationFragmentInterface {
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_log_in);

        fragmentManager = getSupportFragmentManager();

        FragmentTransaction ft = fragmentManager.beginTransaction();

        LoginFragment loginFragment = new LoginFragment();

        ft.add(R.id.fragmentContainer, loginFragment);
        ft.commit();

    }

    @Override
    public void onLoginButtonClicked(String email, String pass) {

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
    public void onSignUPButtonClicked(String name, String email, String pass) {

    }

    @Override
    public void onLogInNowTVClicked() {

        FragmentTransaction ft = fragmentManager.beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        ft.replace(R.id.fragmentContainer, loginFragment);
        ft.addToBackStack(null);
        ft.commit();


    }
}
