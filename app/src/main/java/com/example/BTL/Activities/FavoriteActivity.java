package com.example.BTL.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.BTL.Adapters.FavoriteListAdapter;
import com.example.BTL.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView emptyTXT;
    private ProgressBar loading;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        initView();
        setOnClickListeners();
        loadFavoriteMovies();
    }

    private void setOnClickListeners() {
        findViewById(R.id.HomeBTN).setOnClickListener(v -> startActivity(new Intent(FavoriteActivity.this, MainActivity.class)));
        findViewById(R.id.ProBTN).setOnClickListener(v -> startActivity(new Intent(FavoriteActivity.this, ProfileActivity.class)));
    }

    private void loadFavoriteMovies() {
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        loading.setVisibility(View.VISIBLE);
        if (!username.isEmpty()) {
            DatabaseReference favoriteMoviesRef = FirebaseDatabase.getInstance().getReference().child("users").child(username);

            favoriteMoviesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<String> favoriteMovieIds = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.child("favorite_best_movies").getChildren())
                        favoriteMovieIds.add(snapshot.getKey());

                    for (DataSnapshot snapshot : dataSnapshot.child("favorite_paid_movies").getChildren())
                        favoriteMovieIds.add(snapshot.getKey());

                    showList(favoriteMovieIds);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    handleDatabaseError();
                }
            });
        }
    }

    private void handleDatabaseError() {
        loading.setVisibility(View.GONE);
        emptyTXT.setVisibility(View.VISIBLE);
        emptyTXT.setText("Có lỗi xảy ra khi tải danh sách yêu thích");
    }


    private void showList(List<String> favoriteMovieIds) {
        loading.setVisibility(View.GONE);
        if (!favoriteMovieIds.isEmpty()) {
            emptyTXT.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(new FavoriteListAdapter(favoriteMovieIds));
        } else {
            emptyTXT.setVisibility(View.VISIBLE);
            emptyTXT.setText("Bạn chưa có bất kỳ phim yêu thích nào");
        }
    }

    private void initView() {
        recyclerView = findViewById(R.id.view4);
        emptyTXT = findViewById(R.id.empTXT);
        loading = findViewById(R.id.progressBar4);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}
