package com.example.BTL.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BTL.Adapters.AllMoviesAdapter;
import com.example.BTL.Domain.Film;
import com.example.BTL.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllMoviesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMovies;
    private AllMoviesAdapter adapterMovies;
    private ArrayList<Film> movieList;
    private DatabaseReference databaseReference;
    private ImageView backImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_movies);

        initView();
        Intent intent = getIntent();
        if (intent != null) {
            String movieType = intent.getStringExtra("movieType");
            if (movieType != null && movieType.equals("best")) {
                fetchMovies("Movies", "best");
            } else if (movieType != null && movieType.equals("paid")) {
                fetchMovies("PaidMovies", "paid");
            }
        }
    }

    private void initView() {
        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
        recyclerViewMovies.setLayoutManager(new LinearLayoutManager(this));
        movieList = new ArrayList<>();
        adapterMovies = new AllMoviesAdapter(this, movieList);
        recyclerViewMovies.setAdapter(adapterMovies);

        backImg = findViewById(R.id.backfImg);
        backImg.setOnClickListener(v -> finish());
    }

    private void fetchMovies(String firebasePath, String movieType) {
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregister-34ec8-default-rtdb.firebaseio.com/" + firebasePath);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                movieList.clear();
                for (DataSnapshot movieSnapshot : dataSnapshot.getChildren()) {
                    String id = String.valueOf(movieSnapshot.child("id").getValue());
                    String title = String.valueOf(movieSnapshot.child("title").getValue());
                    String plot = String.valueOf(movieSnapshot.child("plot").getValue());
                    String poster = String.valueOf(movieSnapshot.child("poster").getValue());

                    Film film = new Film(id, title, plot, poster, movieType);
                    movieList.add(film);
                }
                adapterMovies.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi
            }
        });
    }
}
