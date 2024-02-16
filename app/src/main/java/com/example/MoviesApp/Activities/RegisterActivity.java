package com.example.MoviesApp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MoviesApp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregister-34ec8-default-rtdb.firebaseio.com/");
    private EditText fullname, email, phone, password, conPassword;
    private Button registerBtn;
    private TextView loginNowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.username);
        password = findViewById(R.id.password);
        conPassword = findViewById(R.id.conPassword);
        registerBtn = findViewById(R.id.registerBtn);
        loginNowBtn = findViewById(R.id.loginNow);

        registerBtn.setOnClickListener(view -> registerUser());
        loginNowBtn.setOnClickListener(view -> finish());
    }

    private void registerUser() {
        String fullnameTxt = fullname.getText().toString();
        String emailTxt = email.getText().toString();
        String phoneTxt = phone.getText().toString();
        String passwordTxt = password.getText().toString();
        String conPasswordTxt = conPassword.getText().toString();

        if (TextUtils.isEmpty(fullnameTxt) || TextUtils.isEmpty(emailTxt) || TextUtils.isEmpty(phoneTxt) || TextUtils.isEmpty(passwordTxt) || TextUtils.isEmpty(conPasswordTxt)) {
            showToast("Điền đủ thông tin");
            return;
        }

        if (!passwordTxt.equals(conPasswordTxt)) {
            showToast("Mật khẩu không khớp");
            return;
        }

        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.hasChild(phoneTxt)) {
                    showToast("Tài khoản đã tồn tại");
                } else {
                    DatabaseReference newUserRef = databaseReference.child("users").child(phoneTxt);
                    newUserRef.child("fullname").setValue(fullnameTxt);
                    newUserRef.child("email").setValue(emailTxt);
                    newUserRef.child("password").setValue(passwordTxt);

                    showToast("Đăng ký thành công");
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    private void showToast(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
