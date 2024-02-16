package com.example.MoviesApp.Activities;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.example.MoviesApp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailActivity extends AppCompatActivity {
    private ProgressBar loading;
    private TextView titleTxt, movieSummaryInfo, typeTxt;
    private String idFilm;
    private ImageView pic2, backImg, imageView5;
    private NestedScrollView scrollView;
    private SharedPreferences sharedPreferences;
    private VideoView videoView;
    private DatabaseReference databaseReference;
    private boolean isFavorite = false;
    private Button watchMovieBtn;
    private String movieType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        movieType = getIntent().getStringExtra("movieType");
        databaseReference = getDatabaseReference(movieType);

        idFilm = getIntent().getStringExtra("id");
        initView();
        sendRequest();
    }

    private DatabaseReference getDatabaseReference(String movieType) {
        if (TextUtils.equals(movieType, "paid")) {
            return FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregister-34ec8-default-rtdb.firebaseio.com/PaidMovies");
        } else {
            return FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregister-34ec8-default-rtdb.firebaseio.com/Movies");
        }
    }

    private void addFavoriteMovie(String username, String idFilm) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregister-34ec8-default-rtdb.firebaseio.com");
        String favoriteNode = TextUtils.equals(movieType, "best") ? "favorite_best_movies" : "favorite_paid_movies";
        databaseReference.child("users").child(username).child(favoriteNode).child(idFilm + movieType).setValue(true);
        Toast.makeText(DetailActivity.this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
    }

    private void sendRequest() {
        databaseReference.orderByChild("id").equalTo(idFilm).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        loading.setVisibility(View.GONE);
                        // Lấy thông tin phim từ snapshot và hiển thị trong giao diện
                        String title = snapshot.child("title").getValue(String.class);
                        String plot = snapshot.child("plot").getValue(String.class);
                        String posterUrl = snapshot.child("poster").getValue(String.class);
                        String videoUrl = snapshot.child("film").getValue(String.class);
                        String types = snapshot.child("type").getValue(String.class);

                        // Hiển thị thông tin trong giao diện
                        titleTxt.setText(title);
                        movieSummaryInfo.setText(plot);
                        String[] typeArray = types.split(",");
                        StringBuilder typeBuilder = new StringBuilder();
                        for (String type : typeArray) {
                            typeBuilder.append(type.trim()).append(", ");
                        }
                        // Remove trailing comma and space
                        String allTypes = typeBuilder.toString().replaceAll(", $", "");
                        typeTxt.setText(allTypes);
                        Glide.with(DetailActivity.this).load(posterUrl).into(pic2);

                        // Kiểm tra xem phim có trong danh sách yêu thích của người dùng hay không
                        checkFavorite(idFilm);

                        // Phát video
                        playVideo(videoUrl);
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "Không tìm thấy thông tin phim", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DetailActivity.this, "Lỗi khi truy vấn dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkFavorite(String idFilm) {
        String username = sharedPreferences.getString("username", "");
        if (!TextUtils.isEmpty(username)) {
            String favoriteNode = TextUtils.equals(movieType, "best") ? "favorite_best_movies" : "favorite_paid_movies";
            DatabaseReference favoriteRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregister-34ec8-default-rtdb.firebaseio.com/users/" + username + "/" + favoriteNode);
            favoriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(idFilm + movieType)) {
                        imageView5.setImageResource(R.drawable.faved);
                        isFavorite = true;
                    } else {
                        imageView5.setImageResource(R.drawable.fav);
                        isFavorite = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(DetailActivity.this, "Lỗi khi truy vấn dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void playVideo(String videoUrl) {
        Uri videoUri = Uri.parse(videoUrl);

        videoView.setVideoURI(videoUri);
        videoView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        videoView.setMediaController(new android.widget.MediaController(this));
        videoView.requestFocus();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                scrollView.setVisibility(View.GONE);
                watchMovieBtn.setVisibility(View.GONE);
                videoView.start();
            }
        });
    }

    private void initView() {
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        titleTxt = findViewById(R.id.movieNameTxt);
        scrollView = findViewById(R.id.scrollView3);
        pic2 = findViewById(R.id.picDetail);
        movieSummaryInfo = findViewById(R.id.movieSummery);
        backImg = findViewById(R.id.backfImg);
        loading = findViewById(R.id.progressBar5);
        typeTxt = findViewById(R.id.textView16);
        backImg.setOnClickListener(v -> finish());

        videoView = findViewById(R.id.videoView);

        watchMovieBtn = findViewById(R.id.button2);
        watchMovieBtn.setOnClickListener(v -> videoView.setVisibility(View.VISIBLE));

        imageView5 = findViewById(R.id.imageView5);
        imageView5.setOnClickListener(v -> {
            String username = sharedPreferences.getString("username", "");

            if (!TextUtils.isEmpty(username)) {
                if (isFavorite) {
                    removeFavoriteMovie(username, idFilm);
                    imageView5.setImageResource(R.drawable.fav);
                    isFavorite = false;
                } else {
                    addFavoriteMovie(username, idFilm);
                    imageView5.setImageResource(R.drawable.faved);
                    isFavorite = true;
                }
            } else {
                Toast.makeText(DetailActivity.this, "Bạn cần đăng nhập trước khi thêm film yêu thích", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeFavoriteMovie(String username, String idFilm) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregister-34ec8-default-rtdb.firebaseio.com");
        String favoriteNode = TextUtils.equals(movieType, "best") ? "favorite_best_movies" : "favorite_paid_movies";
        databaseReference.child("users").child(username).child(favoriteNode).child(idFilm + movieType).removeValue();
        Toast.makeText(DetailActivity.this, "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
    }
}
