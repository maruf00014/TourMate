package com.maruf.tourmate.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maruf.tourmate.Adapters.FullScreenImageAdapter;
import com.maruf.tourmate.Models.Moment;
import com.maruf.tourmate.R;



import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FullScreenFragment extends Fragment {

    private List<Moment> momentList = new ArrayList<>();

    private ViewPager viewPager;
    private FullScreenImageAdapter adapter;

    public FullScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_full_screen, container, false);

        viewPager = view.findViewById(R.id.galleryViewPager);


        Bundle bundle = getArguments();
        if (bundle!=null){
            int position = bundle.getInt("position");
            momentList = bundle.getParcelableArrayList("momentList");
            adapter = new FullScreenImageAdapter(getContext(),momentList);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(position);

        }
        return view;
    }


}
