package com.example.BTL.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        initView();
        sendRequestBtn();
        loadFavoriteMovies();
    }

    private void sendRequestBtn() {
        LinearLayout HomeBtn = findViewById(R.id.HomeBTN);
        HomeBtn.setOnClickListener(v -> startActivity(new Intent(FavoriteActivity.this, MainActivity.class)));
        // để mở profile
        LinearLayout ProBtn = findViewById(R.id.ProBTN);
        ProBtn.setOnClickListener(v -> startActivity(new Intent(FavoriteActivity.this, ProfileActivity.class)));
    }

    private void loadFavoriteMovies() {
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        if (!username.isEmpty()) {
            DatabaseReference favoriteMoviesRef = FirebaseDatabase.getInstance().getReference().child("users").child(username).child("favorite_movies");

            favoriteMoviesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<String> favoriteMovieIds = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String filmId = snapshot.getKey();
                        favoriteMovieIds.add(filmId);
                        Log.d("FavoriteActivity", "Film ID: " + filmId);
                    }

                    showList(favoriteMovieIds);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Xử lý khi có lỗi xảy ra
                    loading.setVisibility(View.GONE);
                    emptyTXT.setVisibility(View.VISIBLE);
                    emptyTXT.setText("Có lỗi xảy ra khi tải danh sách yêu thích");
                }
            });
        } else {
            loading.setVisibility(View.GONE);
            emptyTXT.setVisibility(View.VISIBLE);
            emptyTXT.setText("Vui lòng đăng nhập để xem danh sách yêu thích");
        }
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
//        ImageView favBtn = findViewById(R.id.FavImg);
    }
}