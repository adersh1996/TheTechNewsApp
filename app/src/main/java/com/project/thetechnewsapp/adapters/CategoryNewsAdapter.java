package com.project.thetechnewsapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.thetechnewsapp.DetailedNewsView;
import com.project.thetechnewsapp.R;
import com.project.thetechnewsapp.models.Root;


public class CategoryNewsAdapter extends RecyclerView.Adapter<CategoryNewsAdapter.MyViewHolder> {

    Root root;
    Context context;



    public CategoryNewsAdapter(Root root, Context context) {
        this.root = root;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout_category_detailes,
                parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.newsHeadlineTxt.setText(root.newsView.get(position).headline);
        holder.timeTxt.setText(root.newsView.get(position).time);

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



    }

    @Override
    public int getItemCount() {
        return root.newsView.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView newsImage;
        private TextView newsHeadlineTxt;
        private TextView timeTxt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            newsImage = itemView.findViewById(R.id.news_image);
            newsHeadlineTxt = itemView.findViewById(R.id.news_headline_txt);
            timeTxt = itemView.findViewById(R.id.time_txt);
        }

    }
}
