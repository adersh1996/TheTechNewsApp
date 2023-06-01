package com.project.thetechnewsapp.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.thetechnewsapp.DetailedNewsView;
import com.project.thetechnewsapp.R;
import com.project.thetechnewsapp.models.Root;
import com.project.thetechnewsapp.retrofit.APIInterface;
import com.project.thetechnewsapp.retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeTopNewsAdapter extends RecyclerView.Adapter<HomeTopNewsAdapter.MyViewHolder> {

    Root root;
    Context context;


    public HomeTopNewsAdapter(Root root, Context context) {
        this.root = root;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout_top_news, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.categoryNameTxt.setText(root.newsView.get(position).category);
        holder.newsHeadlineTxt.setText(root.newsView.get(position).headline);
        holder.timeTxt.setText(root.newsView.get(position).date);
        SharedPreferences sharedPreferences = context.getSharedPreferences("login_data", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        try {
            Glide.with(context).load(root.newsView.get(position).photos.get(0)).into(holder.newsImage);
        } catch (Exception e) {

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailedNewsView.class);
                intent.putExtra("newsId", root.newsView.get(position).id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favoriteApiCall(userId, root.newsView.get(position).id);
            }
        });


    }

    @Override
    public int getItemCount() {
        return root.newsView.size();
    }

    void favoriteApiCall(String userId, String newsId) {

        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.ADD_TO_FAVORITE_API(userId, newsId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        Toast.makeText(context, root.message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, root.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Server Error!!Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView newsImage;
        private TextView newsHeadlineTxt;
        private TextView categoryNameTxt;
        private TextView timeTxt;
        private ImageView favoriteBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            newsImage = itemView.findViewById(R.id.news_image);
            newsHeadlineTxt = itemView.findViewById(R.id.news_headline_txt);
            categoryNameTxt = itemView.findViewById(R.id.category_name_txt);
            timeTxt = itemView.findViewById(R.id.time_txt);
            favoriteBtn = itemView.findViewById(R.id.favorite_btn);
        }
    }
}
