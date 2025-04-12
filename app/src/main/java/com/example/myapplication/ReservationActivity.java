package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationActivity extends AppCompatActivity {

    private LinearLayout guestContainer;
    private Button submitButton;
    private int guestCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation_layout);

        // Initialize view elements
        guestContainer = findViewById(R.id.guestContainer);
        submitButton = findViewById(R.id.submitButton);
        TextView hotelNameText = findViewById(R.id.hotelNameText);
        TextView checkInText = findViewById(R.id.checkInText);
        TextView checkOutText = findViewById(R.id.checkOutText);
        TextView priceText = findViewById(R.id.priceText);

        // Get data from intent
        Intent intent = getIntent();
        String hotelName = intent.getStringExtra("hotel name");
        String checkIn = intent.getStringExtra("check in date");
        String checkOut = intent.getStringExtra("check out date");
        String price = intent.getStringExtra("hotel price");
        String availability = intent.getStringExtra("availability");  // not used but fetched
        guestCount = Integer.parseInt(intent.getStringExtra("number of guests"));

        // Populate basic hotel info
        hotelNameText.setText(hotelName);
        checkInText.setText("Check-in: " + checkIn);
        checkOutText.setText("Check-out: " + checkOut);
        priceText.setText("Price: " + price);

        // Dynamically generate guest input fields
        generateGuestInputs();

        // Submit button click listener
        submitButton.setOnClickListener(v -> submitReservation());
    }

    // Create input forms for each guest
    private void generateGuestInputs() {
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < guestCount; i++) {
            View guestView = inflater.inflate(R.layout.guest_input, guestContainer, false);
            TextView label = guestView.findViewById(R.id.guestLabel);
            label.setText("Guest " + (i + 1) + ":");
            guestContainer.addView(guestView);
        }
    }

    // Validate input and prepare reservation data
    private void submitReservation() {
        List<ReservationInfo.GuestInfo> guestList = new ArrayList<>();

        for (int i = 0; i < guestContainer.getChildCount(); i++) {
            View guestView = guestContainer.getChildAt(i);
            EditText nameInput = guestView.findViewById(R.id.nameInput);
            RadioGroup genderGroup = guestView.findViewById(R.id.genderGroup);
            int selectedId = genderGroup.getCheckedRadioButtonId();

            String name = nameInput.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter name for Guest " + (i + 1), Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedId == -1) {
                Toast.makeText(this, "Please select gender for Guest " + (i + 1), Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedGender = guestView.findViewById(selectedId);
            String gender = selectedGender.getText().toString();

            guestList.add(new ReservationInfo.GuestInfo(name, gender));
        }

        // Retrieve reservation details again
        Intent intent = getIntent();
        String hotelName = intent.getStringExtra("hotel name");
        String checkIn = intent.getStringExtra("check in date");
        String checkOut = intent.getStringExtra("check out date");

        ReservationInfo reservationInfo = new ReservationInfo(hotelName, checkIn, checkOut, guestList);
        postReservationInfo(reservationInfo);
    }

    // Send reservation data to server via API
    private void postReservationInfo(ReservationInfo reservationInfo) {
        ApiInterface apiInterface = Api.getClient();
        Call<Map<String, String>> call = apiInterface.createReservation(reservationInfo);

        call.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String confirmationNumber = response.body().get("confirmation_number");

                    // Redirect to confirmation screen
                    Intent confirmationIntent = new Intent(ReservationActivity.this, ReservationNumberActivity.class);
                    confirmationIntent.putExtra("reservation_number", confirmationNumber);
                    startActivity(confirmationIntent);
                } else {
                    Toast.makeText(ReservationActivity.this, "Reservation failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Toast.makeText(ReservationActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
