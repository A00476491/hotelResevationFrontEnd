package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReservationNumberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation_number);

        TextView reservationNumberView = findViewById(R.id.messageText);
        String reservationNumber = getIntent().getStringExtra("reservation_number");
        reservationNumberView.setText("Your reservation number is: " + reservationNumber);

        Button backToHomeButton = findViewById(R.id.backToHomeButton);
        backToHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(ReservationNumberActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish(); // 结束当前页面，防止用户按返回键又回到结果页
        });
    }
}


