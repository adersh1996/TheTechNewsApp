package com.project.thetechnewsapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.thetechnewsapp.DetailedNewsView;
import com.project.thetechnewsapp.R;
import com.project.thetechnewsapp.models.Root;


public class ViewCommentsAdapter extends RecyclerView.Adapter<ViewCommentsAdapter.MyViewHolder> {

    Root root;
    Context context;



    public ViewCommentsAdapter(Root root, Context context) {
        this.root = root;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout_view_comments,
                parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.userNameCommentsTv.setText(root.commentsView.get(position).userName);
        holder.commentTv.setText(root.commentsView.get(position).comments);
        holder.commentDateTv.setText(root.commentsView.get(position).date);
        holder.commentTimeTv.setText(root.commentsView.get(position).time);


    }

    @Override
    public int getItemCount() {
        return root.commentsView.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView userNameCommentsTv;
        private TextView commentTv;
        private TextView commentDateTv;
        private TextView commentTimeTv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }
        private void initView(View itemView) {
            userNameCommentsTv = itemView.findViewById(R.id.user_name_comments_tv);
            commentTv = itemView.findViewById(R.id.comment_tv);
            commentDateTv = itemView.findViewById(R.id.comment_date_tv);
            commentTimeTv = itemView.findViewById(R.id.comment_time_tv);
        }

    }
}
