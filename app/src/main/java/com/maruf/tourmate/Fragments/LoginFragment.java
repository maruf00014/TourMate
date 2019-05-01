package com.maruf.tourmate.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.maruf.tourmate.R;
import com.maruf.tourmate.databinding.FragmentLoginBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;

    LoginFragmentInterface loginFragmentInterface;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        loginFragmentInterface = (LoginFragmentInterface) context;
    }

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_login, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {



        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.loginEmailEt.getText().toString().matches("") ||
                        binding.loginPassEt.getText().toString().matches("")) {
                    Toast.makeText(getActivity(), "Please enter Email and Password!", Toast.LENGTH_SHORT).show();

                } else {

                    loginFragmentInterface.onLoginButtonClicked(binding.loginEmailEt.getText().toString(),
                            binding.loginPassEt.getText().toString());


                }
            }
        });

        binding.signUpNowTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFragmentInterface.onSignUpNowTVClicked();
            }
        });





    }


    public  interface LoginFragmentInterface {

        void onLoginButtonClicked(String email, String pass);
        void onSignUpNowTVClicked();
    }


}

