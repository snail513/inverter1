package com.example.naragr.project1.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.naragr.project1.R;

public class ItemFragment extends Fragment {

    private String title;
    private int page;


    // newInstance constructor for creating fragment with arguments
    public static ItemFragment newInstance(int page) {
        ItemFragment fragment = new ItemFragment();
        fragment.title = "title"+page;
        fragment.page = page;
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", fragment.title);
        fragment.setArguments(args);
        return fragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.editable_item_layout, container, false);
        EditText tvLabel = (EditText) view.findViewById(R.id.editText);
        tvLabel.setText(page + " -- " + title);
        return view;
    }
}
