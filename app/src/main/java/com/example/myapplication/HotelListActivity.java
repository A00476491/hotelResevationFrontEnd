package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelListActivity extends AppCompatActivity implements HotelClickListener {

    private TextView headingTextView, checkInOutDateTextView, guestNumberTextView;
    private List<Hotel> hotelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotel_list_layout);

        // Initialize UI components
        headingTextView = findViewById(R.id.hotel_list);
        checkInOutDateTextView = findViewById(R.id.hotel_list_check_in_out_date);
        guestNumberTextView = findViewById(R.id.hotel_list_guest_number_text_view);

        // Get data from Intent
        Intent intent = getIntent();
        String checkInDate = intent.getStringExtra("check in date");
        String checkOutDate = intent.getStringExtra("check out date");
        String numberOfGuests = intent.getStringExtra("number of guests");

        // Display basic information
        headingTextView.setText("Welcome user 🙂");
        guestNumberTextView.setText("Guest number: " + numberOfGuests);
        checkInOutDateTextView.setText("Checkin: " + checkInDate + "\nCheckout: " + checkOutDate);

        // Fetch hotel list data from API
        fetchHotelList();
    }

    // Call API to fetch hotel list
    private void fetchHotelList() {
        ApiInterface apiInterface = Api.getClient();
        Call<List<Hotel>> call = apiInterface.getHotelsLists();

        call.enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    hotelList = response.body();
                    setupRecyclerView();  // Show the data using RecyclerView
                } else {
                    Toast.makeText(HotelListActivity.this, "Failed to fetch hotel list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable t) {
                Toast.makeText(HotelListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Set up RecyclerView to display hotel data
    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.hotel_list_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        HotelListAdapter adapter = new HotelListAdapter(hotelList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    // Handle hotel item click
    @Override
    public void onClick(View view, int position) {
        Hotel selectedHotel = hotelList.get(position);

        // Get original intent data
        Intent originalIntent = getIntent();
        String checkInDate = originalIntent.getStringExtra("check in date");
        String checkOutDate = originalIntent.getStringExtra("check out date");
        String numberOfGuests = originalIntent.getStringExtra("number of guests");

        // Prepare new intent to move to ReservationActivity
        Intent reservationIntent = new Intent(this, ReservationActivity.class);
        reservationIntent.putExtra("hotel name", selectedHotel.getName());
        reservationIntent.putExtra("hotel price", selectedHotel.getPrice());
        reservationIntent.putExtra("hotel availability", selectedHotel.getAvailable());
        reservationIntent.putExtra("check in date", checkInDate);
        reservationIntent.putExtra("check out date", checkOutDate);
        reservationIntent.putExtra("number of guests", numberOfGuests);

        startActivity(reservationIntent);
    }
}
