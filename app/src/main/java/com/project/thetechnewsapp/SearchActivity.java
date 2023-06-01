package com.project.thetechnewsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.thetechnewsapp.adapters.HomeTopNewsAdapter;
import com.project.thetechnewsapp.models.Root;
import com.project.thetechnewsapp.retrofit.APIInterface;
import com.project.thetechnewsapp.retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private CardView cvSearchBarSection;
    private EditText etSearchBar;
    private ImageView ivSearchButton;
    private RecyclerView newsRv;
    private TextView searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();

        ivSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etSearchBar.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter Something to Search", Toast.LENGTH_SHORT).show();
                } else {
                    apiTopNewsApiCall(etSearchBar.getText().toString());
                }
            }
        });


    }

    void apiTopNewsApiCall(String searchData) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.SEARCHAPI(searchData).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                        newsRv.setLayoutManager(linearLayoutManager);
                        HomeTopNewsAdapter homeTopNewsAdapter = new HomeTopNewsAdapter(root, getApplicationContext());
                        newsRv.setAdapter(homeTopNewsAdapter);
                    } else {
                        Toast.makeText(getApplicationContext(), root.message, Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        cvSearchBarSection = findViewById(R.id.cv_search_bar_section);
        etSearchBar = findViewById(R.id.et_search_bar);
        ivSearchButton = findViewById(R.id.iv_search_button);
        newsRv = findViewById(R.id.news_rv);
        searchButton = findViewById(R.id.search_button);
    }
}