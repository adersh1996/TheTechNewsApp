package com.project.thetechnewsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.thetechnewsapp.R;
import com.project.thetechnewsapp.models.Root;


public class HomeViewPagerAdapter extends RecyclerView.Adapter<HomeViewPagerAdapter.MyViewHolder> {

    Root root;
    Context context;



    public HomeViewPagerAdapter(Root root, Context context) {
        this.root = root;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout_home_view_pager, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.newsTitleTv.setText(root.newsView.get(position).headline);
        holder.timeTv.setText(root.newsView.get(position).date);

        try {
            Glide.with(context).load(root.newsView.get(position).photos.get(0)).into(holder.viewPagerIv);
        } catch (Exception e) {

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, WorkshopListActivity.class);
//                intent.putExtra("service_id", root.serviceDetails.get(position).id);
//                intent.putExtra("service_name", root.serviceDetails.get(position).service_name);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return root.newsView.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView viewPagerIv;
        private TextView newsTitleTv;
        private TextView timeTv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);

        }
        private void initView(View itemView) {
            viewPagerIv = itemView.findViewById(R.id.view_pager_iv);
            newsTitleTv = itemView.findViewById(R.id.news_title_tv);
            timeTv = itemView.findViewById(R.id.time_tv);
        }

    }
}
