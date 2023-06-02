package com.project.thetechnewsapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.thetechnewsapp.adapters.HomeTopNewsAdapter;
import com.project.thetechnewsapp.adapters.HomeViewPagerAdapter;
import com.project.thetechnewsapp.models.Root;
import com.project.thetechnewsapp.retrofit.APIInterface;
import com.project.thetechnewsapp.retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {


    private RecyclerView homeViewPagerRecyclerView;
    private RecyclerView topNewsRecyclerView;
    private CardView cvSearchBarSection;
    private EditText etSearchBar;
    private ImageView ivSearchButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login_data", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");

        homeViewPagerApiCall(userId);
        apiTopNewsApiCall(userId);


        etSearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    void apiTopNewsApiCall(String userId) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.VIEWALLNEWSAPI(userId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                        topNewsRecyclerView.setLayoutManager(linearLayoutManager);
                        HomeTopNewsAdapter homeTopNewsAdapter = new HomeTopNewsAdapter(root, getContext());
                        topNewsRecyclerView.setAdapter(homeTopNewsAdapter);
                    } else {
                        Toast.makeText(getActivity(), root.message, Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void homeViewPagerApiCall(String userId) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.VIEWALLNEWSAPI(userId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
                        homeViewPagerRecyclerView.setLayoutManager(linearLayoutManager);
                        HomeViewPagerAdapter homeViewPagerAdapter = new HomeViewPagerAdapter(root, getContext());
                        homeViewPagerRecyclerView.setAdapter(homeViewPagerAdapter);
                    } else {
                        Toast.makeText(getActivity(), root.message, Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(View view) {
        homeViewPagerRecyclerView = view.findViewById(R.id.home_view_pager_recycler_view);
        topNewsRecyclerView = view.findViewById(R.id.top_news_recycler_view);
        cvSearchBarSection = view.findViewById(R.id.cv_search_bar_section);
        etSearchBar = view.findViewById(R.id.et_search_bar);
        ivSearchButton = view.findViewById(R.id.iv_search_button);
    }
}