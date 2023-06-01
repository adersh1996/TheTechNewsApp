package com.project.thetechnewsapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.thetechnewsapp.adapters.FavoriteNewsAdapter;
import com.project.thetechnewsapp.adapters.HomeTopNewsAdapter;
import com.project.thetechnewsapp.models.Root;
import com.project.thetechnewsapp.retrofit.APIInterface;
import com.project.thetechnewsapp.retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavoriteFragment extends Fragment {


    private RecyclerView recyclerViewFavoriteNews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        initView(view);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login_data", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        favoritesApiCall(userId);

        return view;

    }

    void favoritesApiCall(String userId) {

        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.VIEWFAVORITE(userId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                        recyclerViewFavoriteNews.setLayoutManager(linearLayoutManager);
                        FavoriteNewsAdapter favoriteNewsAdapter = new FavoriteNewsAdapter(root,getContext());
                        recyclerViewFavoriteNews.setAdapter(favoriteNewsAdapter);
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
        recyclerViewFavoriteNews = view.findViewById(R.id.recycler_view_favorite_news);
    }
}