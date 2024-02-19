package com.example.BTL.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BTL.Adapters.FilmAdapter;
import com.example.BTL.Domain.Film;
import com.example.BTL.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBestMovies, recyclerviewPaidMovies;
    private FilmAdapter adapterBestMovies, adapterPaidMovies;
    private ArrayList<Film> filmList, filmpaidList;
    private DatabaseReference databaseReference, databaseReference1;
    private ProgressBar loading1, loading2;
    private ImageView HomeImg;
    private TextView HomeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        sendRequestBtn();
        sendRequestBestMovies();
        sendRequestPaidMovies();
        setupSearch();
    }

    private void setupSearch() {
        EditText searchEditText = findViewById(R.id.Search);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
                performSearchPaidMovies(s.toString());
                if (s.toString().isEmpty()) {
                    clearSearch();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        searchEditText.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL && searchEditText.getText().toString().isEmpty()) {
                clearSearch();
            }
            return false;
        });
    }

    private void performSearch(String query) {
        ArrayList<Film> searchResultList = new ArrayList<>();
        if (query.isEmpty()) {
            searchResultList.addAll(filmList);
        } else {
            for (Film film : filmList) {
                if (film.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        film.getTitle().equalsIgnoreCase(query) ||
                        film.getTitle().contains(query)) {
                    searchResultList.add(film);
                }
            }
        }
        adapterBestMovies.updateList(searchResultList);
    }

    private void performSearchPaidMovies(String query) {
        ArrayList<Film> searchResultList = new ArrayList<>();
        if (query.isEmpty()) {
            searchResultList.addAll(filmpaidList);
        } else {
            for (Film film : filmpaidList) {
                if (film.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        film.getTitle().equalsIgnoreCase(query) ||
                        film.getTitle().contains(query)) {
                    searchResultList.add(film);
                }
            }
        }
        adapterPaidMovies.updateList(searchResultList);
    }

    private void clearSearch() {
        performSearch("");
    }

    private void sendRequestPaidMovies() {
        databaseReference1 = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregister-34ec8-default-rtdb.firebaseio.com/PaidMovies");
        filmpaidList = new ArrayList<>();
        adapterPaidMovies = new FilmAdapter(this, filmpaidList);
        recyclerviewPaidMovies.setAdapter(adapterPaidMovies);
        loading2.setVisibility(View.VISIBLE);
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                filmpaidList.clear();
                loading2.setVisibility(View.GONE);

                int count = 0;
                for (DataSnapshot movieSnapshot : dataSnapshot.getChildren()) {
                    if (count < 6) {
                        String id = String.valueOf(movieSnapshot.child("id").getValue());
                        String title = String.valueOf(movieSnapshot.child("title").getValue());
                        String plot = String.valueOf(movieSnapshot.child("plot").getValue());
                        String poster = String.valueOf(movieSnapshot.child("poster").getValue());
                        Film film = new Film(id, title, plot, poster, "paid");
                        filmpaidList.add(film);
                    }
                    count++;
                }

                adapterPaidMovies.notifyDataSetChanged();

                TextView textViewMore = findViewById(R.id.textViewMore1);
                textViewMore.setVisibility(View.VISIBLE);

                textViewMore.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, AllMoviesActivity.class);
                    intent.putExtra("movieType", "paid");
                    startActivity(intent);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendRequestBestMovies() {
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregister-34ec8-default-rtdb.firebaseio.com/Movies");
        filmList = new ArrayList<>();
        adapterBestMovies = new FilmAdapter(this, filmList);
        recyclerViewBestMovies.setAdapter(adapterBestMovies);
        loading1.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                filmList.clear();
                loading1.setVisibility(View.GONE);

                int count = 0;
                for (DataSnapshot movieSnapshot : dataSnapshot.getChildren()) {
                    if (count < 6) {
                        String id = String.valueOf(movieSnapshot.child("id").getValue());
                        String title = String.valueOf(movieSnapshot.child("title").getValue());
                        String plot = String.valueOf(movieSnapshot.child("plot").getValue());
                        String poster = String.valueOf(movieSnapshot.child("poster").getValue());
                        Film film = new Film(id, title, plot, poster, "best");
                        filmList.add(film);
                    }
                    count++;
                }

                adapterBestMovies.notifyDataSetChanged();

                TextView textViewMore = findViewById(R.id.textViewMore);
                textViewMore.setVisibility(View.VISIBLE);

                textViewMore.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, AllMoviesActivity.class);
                    intent.putExtra("movieType", "best");
                    startActivity(intent);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendRequestBtn() {
        LinearLayout favBtn = findViewById(R.id.FavBTN);
        favBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,FavoriteActivity.class)));
        LinearLayout ProBtn = findViewById(R.id.ProBTN);
        ProBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ProfileActivity.class)));
    }

    private void initView() {
        recyclerViewBestMovies = findViewById(R.id.view1);
        recyclerViewBestMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerviewPaidMovies = findViewById(R.id.view2);
        recyclerviewPaidMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        loading1 = findViewById(R.id.progressBar1);
        loading2 = findViewById(R.id.progressBar2);

        HomeImg = findViewById(R.id.HomeImg);
        HomeImg.setImageResource(R.drawable.btn_1_selected);

        HomeTV = findViewById(R.id.HomeTV);
        HomeTV.setTextColor(Color.YELLOW);
    }
}
