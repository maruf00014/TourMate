package com.maruf.tourmate.Fragments;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.maruf.tourmate.R;
import com.maruf.tourmate.databinding.FragmentRegistrationBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {

    FragmentRegistrationBinding binding;
    RegistrationFragmentInterface registrationFragmentInterface;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        registrationFragmentInterface = (RegistrationFragmentInterface) context;
    }

    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_registration,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        binding.signupBtn
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(binding.regNameEt.getText().toString())
                        || TextUtils.isEmpty(binding.regEmailEt.getText().toString()) ||
                        TextUtils.isEmpty(binding.regPassEt.getText().toString()) ||
                        TextUtils.isEmpty(binding.regRepassEt.getText().toString())) {
                    Toast.makeText(getActivity(), "All field are required!", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!binding.regPassEt.getText().toString().matches(binding.regRepassEt.getText().toString())){
                    Toast.makeText(getActivity(), "Password not match!", Toast.LENGTH_SHORT).show();
                    return;
                }else if(binding.regRepassEt.getText().toString().length()<6){
                    Toast.makeText(getActivity(), "Password too short!", Toast.LENGTH_SHORT).show();
                    return;
                }else {

                    registrationFragmentInterface.onSignUPButtonClicked(binding.regNameEt.getText().toString(),
                            binding.regEmailEt.getText().toString(),binding.regPassEt.getText().toString());

                }
            }
        });

        binding.loginnowTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrationFragmentInterface.onLogInNowTVClicked();
            }
        });

    }

    public interface RegistrationFragmentInterface {

        void onSignUPButtonClicked(String name,String email, String pass);
        void onLogInNowTVClicked();
    }

}


