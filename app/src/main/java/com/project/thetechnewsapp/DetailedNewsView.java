package com.project.thetechnewsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.project.thetechnewsapp.models.Root;
import com.project.thetechnewsapp.retrofit.APIInterface;
import com.project.thetechnewsapp.retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailedNewsView extends AppCompatActivity {

    private ImageView newsImageOne;
    private TextView newsHeadlineTv;
    private TextView newsDescriptionTv;
    private ImageView newsImageTwo;
    private ImageView newsImageThree;
    private VideoView videoViewOne;
    private LinearLayout addComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_news_view);
        initView();
        String newsId = getIntent().getStringExtra("newsId");
       // Toast.makeText(this, newsId, Toast.LENGTH_SHORT).show();
        detailedNewsApiCall(newsId);

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ViewCommentsActivity.class);
                intent.putExtra("newsId",newsId);
                startActivity(intent);

            }
        });

    }

    private void initView() {
        newsImageOne = findViewById(R.id.news_image_one);
        newsHeadlineTv = findViewById(R.id.news_headline_tv);
        newsDescriptionTv = findViewById(R.id.news_description_tv);
        newsImageTwo = findViewById(R.id.news_image_two);
        newsImageThree = findViewById(R.id.news_image_three);
        videoViewOne = findViewById(R.id.video_view_one);
        addComment = findViewById(R.id.news_comment_bt);
    }

    void detailedNewsApiCall(String newsId) {

        APIInterface api = ApiClient.getClient().create(APIInterface.class);

        api.VIEW_DETAILED_NEWS(newsId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        try {
                            Glide.with(getApplicationContext()).load(root.newsView.get(0).photos.get(0)).into(newsImageOne);
                            newsHeadlineTv.setText(root.newsView.get(0).headline);
                            newsDescriptionTv.setText(root.newsView.get(0).description);
                            Glide.with(getApplicationContext()).load(root.newsView.get(0).photos.get(1)).into(newsImageTwo);
                            Glide.with(getApplicationContext()).load(root.newsView.get(0).photos.get(2)).into(newsImageThree);
                            Uri uri = Uri.parse(root.newsView.get(0).video);
                            videoViewOne.setVideoURI(uri);
                            MediaController mediaController = new MediaController(DetailedNewsView.this);
                            mediaController.setAnchorView(videoViewOne);
                            mediaController.setMediaPlayer(videoViewOne);
                            videoViewOne.setMediaController(mediaController);
                            videoViewOne.start();


                        } catch (Exception e) {

                        }

                        //Wait for 500 ms then check!.
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (newsImageTwo.getDrawable() == null) {
                                    newsImageTwo.setVisibility(View.GONE);
                                }
                                if (newsImageThree.getDrawable() == null) {
                                    newsImageThree.setVisibility(View.GONE);
                                }
                            }
                        }, 5000);

                    } else {
                        Toast.makeText(DetailedNewsView.this, root.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DetailedNewsView.this, "ServerError", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(DetailedNewsView.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}