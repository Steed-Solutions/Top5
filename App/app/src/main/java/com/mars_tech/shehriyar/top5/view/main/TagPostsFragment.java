package com.mars_tech.shehriyar.top5.view.main;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mars_tech.shehriyar.top5.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TagPostsFragment extends Fragment {


    public TagPostsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tag_posts, container, false);
    }

}
