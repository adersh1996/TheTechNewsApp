package com.project.thetechnewsapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.thetechnewsapp.adapters.CategoryAdapter;


public class TopicsFragment extends Fragment {


    private RecyclerView categoryRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_topics, container, false);
        initView(view);

        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2);
        CategoryAdapter categoryAdapter = new CategoryAdapter(getContext());
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryRecyclerView.setAdapter(categoryAdapter);


        return view;
    }


    private void initView(View view) {
        categoryRecyclerView = view.findViewById(R.id.category_recycler_view);
    }
}