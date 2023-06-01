package com.project.thetechnewsapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.thetechnewsapp.adapters.CategoryNewsAdapter;
import com.project.thetechnewsapp.models.Root;
import com.project.thetechnewsapp.retrofit.APIInterface;
import com.project.thetechnewsapp.retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryNewsList extends AppCompatActivity {

    private RecyclerView recyclerViewCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_news_list);
        initView();

        String categoryName = getIntent().getStringExtra("categoryName");
        categoryWiseNewsApiCall(categoryName);

    }

    void categoryWiseNewsApiCall(String category) {

        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.VIEW_NEWS_WITH_CATEGORY(category).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                        recyclerViewCategory.setLayoutManager(linearLayoutManager);
                        CategoryNewsAdapter categoryNewsAdapter = new CategoryNewsAdapter(root, getApplicationContext());
                        recyclerViewCategory.setAdapter(categoryNewsAdapter);
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
        recyclerViewCategory = findViewById(R.id.recycler_view_category);
    }
}