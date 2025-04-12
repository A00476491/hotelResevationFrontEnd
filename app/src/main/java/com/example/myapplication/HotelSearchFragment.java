package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HotelSearchFragment extends Fragment {

    // UI elements
    private View view;
    private TextView titleTextView, searchTextConfirmationTextView;
    private Button searchButton, clearButton, confirmButton, retrieveButton;
    private DatePicker checkInDatePicker, checkOutDatePicker;
    private EditText guestsCountEditText, nameEditText;

    // SharedPreferences keys
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "myPref";
    private static final String NAME_KEY = "nameKey";
    private static final String GUESTS_COUNT_KEY = "guestsCount";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.hotel_search_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);        // Initialize UI components
        setupButtonListeners();       // Set up event handlers for buttons
    }

    // Bind views to their IDs
    private void initializeViews(View view) {

        titleTextView = view.findViewById(R.id.title_text_view);

        guestsCountEditText = view.findViewById(R.id.number_input);
        nameEditText = view.findViewById(R.id.guest_name);

        checkInDatePicker = view.findViewById(R.id.checkin_calendar_view);
        checkOutDatePicker = view.findViewById(R.id.checkout_calendar_view);

        searchButton = view.findViewById(R.id.search_button);
        clearButton = view.findViewById(R.id.clear_button);
        confirmButton = view.findViewById(R.id.confirm_button);
        retrieveButton = view.findViewById(R.id.retrieve_button);

        titleTextView.setText(R.string.Welcome_to_hotel_platform);
    }

    // Attach click listeners to each button
    private void setupButtonListeners() {
        searchButton.setOnClickListener(v -> handleSearch());

        clearButton.setOnClickListener(v -> {
            guestsCountEditText.setText("");
            nameEditText.setText("");
        });

        confirmButton.setOnClickListener(v -> handleConfirm());

        retrieveButton.setOnClickListener(v -> {
            sharedPreferences = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            nameEditText.setText(sharedPreferences.getString(NAME_KEY, ""));
            guestsCountEditText.setText(sharedPreferences.getString(GUESTS_COUNT_KEY, ""));
        });
    }

    // Handle search button logic: validate inputs and start next activity
    private void handleSearch() {
        String checkInDateStr = getDateFromCalendar(checkInDatePicker);
        String checkOutDateStr = getDateFromCalendar(checkOutDatePicker);
        String guestName = nameEditText.getText().toString().trim();
        String guestNumberStr = guestsCountEditText.getText().toString().trim();

        if (!isDateValid(checkInDateStr, checkOutDateStr)) return;
        if (!isGuestNumberValid(guestNumberStr)) return;
        if (!isNameValid(guestName)) return;

        // Pass values to HotelListActivity
        Intent intent = new Intent(getActivity(), HotelListActivity.class);
        intent.putExtra("check in date", checkInDateStr);
        intent.putExtra("check out date", checkOutDateStr);
        intent.putExtra("number of guests", guestNumberStr);
        startActivity(intent);
    }

    // Save user input to SharedPreferences and display confirmation message
    private void handleConfirm() {
        String checkInDate = getDateFromCalendar(checkInDatePicker);
        String checkOutDate = getDateFromCalendar(checkOutDatePicker);
        String numberOfGuests = guestsCountEditText.getText().toString();
        String guestName = nameEditText.getText().toString();

        sharedPreferences = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putString(NAME_KEY, guestName)
                .putString(GUESTS_COUNT_KEY, numberOfGuests)
                .apply();
    }

    // Format selected date from DatePicker to yyyy-MM-dd
    private String getDateFromCalendar(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }

    // Validate that check-out date is not before check-in date
    private boolean isDateValid(String checkInStr, String checkOutStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date checkIn = sdf.parse(checkInStr);
            Date checkOut = sdf.parse(checkOutStr);
            if (checkOut.before(checkIn)) {
                Toast.makeText(getContext(), "Check-out date cannot be before check-in date", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // Validate that number of guests is not empty or zero
    private boolean isGuestNumberValid(String guestNumberStr) {
        if (guestNumberStr.isEmpty() || "0".equals(guestNumberStr)) {
            guestsCountEditText.setError("Please enter guest number");
            return false;
        }
        return true;
    }

    // Validate that guest name is not empty
    private boolean isNameValid(String name) {
        if (name.isEmpty()) {
            nameEditText.setError("Please enter guest name");
            return false;
        }
        return true;
    }
}
