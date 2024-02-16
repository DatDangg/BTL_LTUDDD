package com.example.MoviesApp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.MoviesApp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private TextView profileName, profileEmail, profileUsername, profilePassword, ProTV;
    private DatabaseReference userRef;
    private SharedPreferences sharedPreferences;
    private Button editBtn;
    private ImageView ProImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initView();
        sendRequestBtn();
        loadUserProfile();
    }

    private void sendRequestBtn() {
        LinearLayout HomeBtn = findViewById(R.id.HomeBTN);
        HomeBtn.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, MainActivity.class)));
        LinearLayout FavBtn = findViewById(R.id.FavBTN);
        FavBtn.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, FavoriteActivity.class)));
    }

    private void initView() {
        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profileUsername = findViewById(R.id.profileUsername);
        profilePassword = findViewById(R.id.profilePassword);
        editBtn = findViewById(R.id.editButton);
        editBtn.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this,EditProfileActivity.class)));

        ProImg=findViewById(R.id.ProImg);
        ProImg.setImageResource(R.drawable.btn_4_selected);

        ProTV = findViewById(R.id.ProTV);
        ProTV.setTextColor(Color.YELLOW);
    }

    private void loadUserProfile() {
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(username);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("fullname").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String password = snapshot.child("password").getValue(String.class);

                    profileName.setText(name);
                    profileEmail.setText(email);
                    profileUsername.setText(username);
                    profilePassword.setText(password);
                } else {
                    Toast.makeText(ProfileActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
