package com.example.myapplication;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.HotelListAdapter;
//import com.example.myapplication.models.Hotel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelListActivity extends AppCompatActivity implements ItemClickListener {
    TextView headingTextView, checkInOutDateTextView, guestNumberTextView;
    private List<Hotel> hotelList;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotel_list_fragment); // 复用 layout

        headingTextView = findViewById(R.id.hotel_list);
        checkInOutDateTextView = findViewById(R.id.hotel_list_check_in_out_date);
        guestNumberTextView = findViewById(R.id.hotel_list_guest_number_text_view);

        Intent intent = getIntent();
        String checkInDate = intent.getStringExtra("check in date");
        String checkOutDate = intent.getStringExtra("check out date");
        String numberOfGuests = intent.getStringExtra("number of guests");

        headingTextView.setText("Welcome user 🙂");
        guestNumberTextView.setText("Guest number: " + numberOfGuests);
        checkInOutDateTextView.setText("Checkin: " + checkInDate + "\nCheckout: " + checkOutDate);

        getHotelsListData(checkInDate, checkOutDate, numberOfGuests);
    }

    private void getHotelsListData(String checkInDate, String checkOutDate, String numberOfGuests) {
        ApiInterface apiInterface = Api.getClient();
        Call<List<Hotel>> call = apiInterface.getHotelsLists();
        call.enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                if (response.isSuccessful()) {
                    hotelList = response.body();
                    setupRecyclerView();
                } else {
                    Toast.makeText(HotelListActivity.this, "Failed to fetch hotel list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable t) {
                Toast.makeText(HotelListActivity.this, "Fetch error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.hotel_list_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        HotelListAdapter hotelListAdapter = new HotelListAdapter(hotelList);
        recyclerView.setAdapter(hotelListAdapter);
        hotelListAdapter.setClickListener(this);
    }

    @Override
    public void onClick(View view, int position) {
        Hotel hotel = hotelList.get(position);

        Intent intent = getIntent();
        String checkInDate = intent.getStringExtra("check in date");
        String checkOutDate = intent.getStringExtra("check out date");
        String numberOfGuests = intent.getStringExtra("number of guests");

        Intent reservationIntent = new Intent(this, ReservationActivity.class);
        reservationIntent.putExtra("hotel name", hotel.getName());
        reservationIntent.putExtra("hotel price", hotel.getPrice());
        reservationIntent.putExtra("hotel availability", hotel.getAvailable());
        reservationIntent.putExtra("check in date", checkInDate);
        reservationIntent.putExtra("check out date", checkOutDate);
        reservationIntent.putExtra("number of guests", numberOfGuests);
        startActivity(reservationIntent);
    }
}
