package com.project.thetechnewsapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.thetechnewsapp.adapters.ViewCommentsAdapter;
import com.project.thetechnewsapp.models.Root;
import com.project.thetechnewsapp.retrofit.APIInterface;
import com.project.thetechnewsapp.retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewCommentsActivity extends AppCompatActivity {

    private RecyclerView viewCommentsRv;
    private EditText addCommentEt;
    private ImageView addCommentBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comments);
        initView();

        String newsId = getIntent().getStringExtra("newsId");
        // Toast.makeText(this, newsId, Toast.LENGTH_SHORT).show();
        viewCommentsApi(newsId);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("login_data", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");

        addCommentBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addCommentEt.getText().toString().isEmpty()) {
                    Toast.makeText(ViewCommentsActivity.this, "Enter Your Comment", Toast.LENGTH_SHORT).show();
                } else {
                    addCommentApi(newsId, userId, addCommentEt.getText().toString());
                }
            }
        });

    }

    private void addCommentApi(String newsId, String userId, String comment) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.ADD_COMMENT_API(userId, newsId, comment).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {

                    Root root = response.body();
                    if (root.status) {
                        Toast.makeText(ViewCommentsActivity.this, root.message, Toast.LENGTH_SHORT).show();
                        viewCommentsApi(newsId);
                    } else {
                        Toast.makeText(getApplicationContext(), root.message, Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    void viewCommentsApi(String newsId) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.VIEW_COMMENTS(newsId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                        viewCommentsRv.setLayoutManager(linearLayoutManager);
                        ViewCommentsAdapter viewCommentsAdapter = new ViewCommentsAdapter(root, getApplicationContext());
                        viewCommentsRv.setAdapter(viewCommentsAdapter);
                    } else {
                        Toast.makeText(getApplicationContext(), root.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initView() {
        viewCommentsRv = findViewById(R.id.view_comments_rv);
        addCommentEt = findViewById(R.id.add_comment_et);
        addCommentBt = findViewById(R.id.add_comment_bt);
    }
}