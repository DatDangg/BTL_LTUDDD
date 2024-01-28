package com.example.BTL.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.BTL.Adapters.FilmListAdapter;
import com.example.BTL.Domain.ListFilm;
import com.example.BTL.R;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterBestMovies, adapterPaidMovies;
    private RecyclerView recyclerViewBestMovies, recyclerviewPaidMovies;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest, mStringRequest2;
    private ProgressBar loading1, loading2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        sendRequestBestMovies();
        sendRequestPaidMovies();
        sendRequestBtn();
    }

    private void sendRequestBtn() {
        LinearLayout favBtn = findViewById(R.id.FavBTN);
        favBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,FavoriteActivity.class)));
//        LinearLayout ProBtn = findViewById(R.id.ProBTN);
//        ProBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ProfileActivity.class)));
    }

    private void sendRequestBestMovies() {
        mRequestQueue = Volley.newRequestQueue(this);
        loading1.setVisibility(View.VISIBLE);
        mStringRequest = new StringRequest(Request.Method.GET, "https://movielistjava.000webhostapp.com", response -> {
            Gson gson = new Gson();
            loading1.setVisibility(View.GONE);
            ListFilm items = gson.fromJson(response, ListFilm.class);
            adapterBestMovies = new FilmListAdapter(items);
            recyclerViewBestMovies.setAdapter(adapterBestMovies);
        }, error -> {
            loading1.setVisibility(View.GONE);
        });
        mRequestQueue.add(mStringRequest);
    }

    private void sendRequestPaidMovies() {
        mRequestQueue = Volley.newRequestQueue(this);
        loading2.setVisibility(View.VISIBLE);
        mStringRequest2 = new StringRequest(Request.Method.GET, "https://movielistjava.000webhostapp.com", response -> {
            Gson gson = new Gson();
            loading2.setVisibility(View.GONE);
            ListFilm items = gson.fromJson(response, ListFilm.class);
            adapterPaidMovies = new FilmListAdapter(items);
            recyclerviewPaidMovies.setAdapter(adapterPaidMovies);
        }, error -> {
            loading2.setVisibility(View.GONE);
        });
        mRequestQueue.add(mStringRequest2);
    }


    private void initView() {
        recyclerViewBestMovies = findViewById(R.id.view1);
        recyclerViewBestMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerviewPaidMovies = findViewById(R.id.view2);
        recyclerviewPaidMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        loading1 = findViewById(R.id.progressBar1);
        loading2 = findViewById(R.id.progressBar2);


    }
}