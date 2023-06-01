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

import com.project.thetechnewsapp.CategoryNewsList;
import com.project.thetechnewsapp.R;

import java.util.Locale;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    Context context;
    String[] categoryArray = {"apps", "automobiles", "gadgets", "gaming", "mobile", "space", "tech",
            "artificial intelligence", "augmented reality", "cloud"};
    int[] categoryImage = {R.drawable.apple,
            R.drawable.apps,
            R.drawable.automobile,
            R.drawable.gadgets,
            R.drawable.gaming,
            R.drawable.mobile,
            R.drawable.space,
            R.drawable.tech,
            R.drawable.ai,
            R.drawable.augmented_reality,
            R.drawable.cloud_computing};


    public CategoryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout_news_category, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.categoryImg.setImageResource(categoryImage[position]);
        holder.categoryName.setText(categoryArray[position]);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(context, CategoryNewsList.class);
               intent.putExtra("categoryName", categoryArray[position]);
//                intent.putExtra("service_name", root.serviceDetails.get(position).service_name);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryArray.length;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView categoryImg;
        private TextView categoryName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);

        }

        private void initView(View itemView) {
            categoryImg = itemView.findViewById(R.id.category_img);
            categoryName = itemView.findViewById(R.id.category_name);
        }

    }
}
