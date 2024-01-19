package com.example.BTL.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.BTL.Adapters.FavoriteListAdapter;
import com.example.BTL.R;

import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageView backImg;
    private FavoriteListAdapter favoriteListAdapter;
    private ScrollView scrollView;
    private TextView emptyTXT;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        initView();
        showList();
        sendRequestBtn();
    }

    private void sendRequestBtn() {
        LinearLayout HomeBtn = findViewById(R.id.HomeBTN);
        HomeBtn.setOnClickListener(v -> startActivity(new Intent(FavoriteActivity.this, MainActivity.class)));
        // để mở profile
//        LinearLayout ProBtn = findViewById(R.id.ProfBTN);
//        ProBtn.setOnClickListener(v -> startActivity(new Intent(FavoriteActivity.this, ProfileActivity.class)));
    }

    private void showList() {
        List<Integer> favoriteMovies = FavoriteMoviesManager.getFavoriteMovies();

        if (favoriteMovies != null && !favoriteMovies.isEmpty()) {
            emptyTXT.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(new FavoriteListAdapter(favoriteMovies));
        } else {
            emptyTXT.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        recyclerView = findViewById(R.id.view4);
        emptyTXT = findViewById(R.id.empTXT);
        scrollView = findViewById(R.id.scrollView4);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}
