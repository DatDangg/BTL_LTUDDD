package com.example.BTL.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import com.example.BTL.Domain.FilmItem;
import com.example.BTL.R;

import com.google.gson.Gson;


public class DetailActivity extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private ProgressBar loading;
    private TextView titleTxt, movieSummaryInfo;
    private int idFilm;
    private ImageView pic2, backImg;
    private NestedScrollView scrollView;

    private FilmItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        idFilm = getIntent().getIntExtra("id", 0);
        initView();
        sendRequest();

        View imageView5 = findViewById(R.id.imageView5);
        imageView5.setOnClickListener(v -> {
            FavoriteMoviesManager.addFavoriteMovie(idFilm);
            Toast.makeText(DetailActivity.this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
        });

    }

    private void sendRequest() {
        mRequestQueue = Volley.newRequestQueue(this);
        scrollView.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);

        mStringRequest = new StringRequest(Request.Method.GET, "https://movielistjava.000webhostapp.com/" + idFilm, response -> {
            Gson gson = new Gson();
            scrollView.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
            item = gson.fromJson(response, FilmItem.class); // Fix: Use the member variable

            Glide.with(DetailActivity.this)
                    .load(item.getPoster())
                    .into(pic2);

            titleTxt.setText(item.getTitle());
            movieSummaryInfo.setText(item.getPlot());
        }, error -> loading.setVisibility(View.GONE));
        mRequestQueue.add(mStringRequest);
    }
    private void openMovieLink() {
        if (item != null && item.getFilm() != null) {
            String movieLink = item.getFilm();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movieLink));
            startActivity(intent);
        } else {
            Toast.makeText(this, "Không có liên kết phim", Toast.LENGTH_SHORT).show();
        }
    }



    private void initView() {
        titleTxt = findViewById(R.id.movieNameTxt);
        scrollView = findViewById(R.id.scrollView3);
        pic2 = findViewById(R.id.picDetail);
        movieSummaryInfo = findViewById(R.id.movieSummery);
        backImg = findViewById(R.id.backfImg);
        loading = findViewById(R.id.progressBar5);
        backImg.setOnClickListener(v -> finish());

        Button watchMovieBtn = findViewById(R.id.button2);
        watchMovieBtn.setOnClickListener(v -> openMovieLink());
    }
}