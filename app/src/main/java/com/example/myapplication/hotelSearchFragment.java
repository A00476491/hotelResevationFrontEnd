package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;


public class hotelSearchFragment extends Fragment {
    View view;
    TextView titleTextView, searchTextConfirmationTextView;
    Button searchButton, clearButton, confirmButton, retrieveButton;
    DatePicker checkInDatePicker, checkOutDatePicker;
    String checkInDate, checkOutDate, numberOfGuests, guestName;
    EditText guestsCountEditText, nameEditText;
    ConstraintLayout mainLayout;
    SharedPreferences sharedPreferences;
    public static final String myPreference = "myPref";
    public static final String name = "nameKey";
    public static final String guestsCount = "guestsCount";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.hotel_search_layout,container,false);
        return view;
    }
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        mainLayout =view.findViewById(R.id.main_layout);
        guestsCountEditText =view.findViewById(R.id.number_input);

        titleTextView = view.findViewById(R.id.title_text_view);
        titleTextView.setText(R.string.Welcome_to_hotel_platform);

        nameEditText = view.findViewById(R.id.guest_name);

        checkInDatePicker = view.findViewById(R.id.checkin_calendar_view);
        checkOutDatePicker = view.findViewById(R.id.checkout_calendar_view);

        searchButton = view.findViewById(R.id.search_button);
        clearButton = view.findViewById(R.id.clear_button);
        confirmButton = view.findViewById(R.id.confirm_button);
        retrieveButton = view.findViewById(R.id.retrieve_button);
        searchTextConfirmationTextView = view.findViewById(R.id.search_confirm_text_view);

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // get the value of check-in date & check-out date
                String checkInDateStr = getDateFromCalendar(checkInDatePicker);
                String checkOutDateStr = getDateFromCalendar(checkOutDatePicker);
                String guestName = nameEditText.getText().toString().trim();
                String guestNumberStr = guestsCountEditText.getText().toString().trim();

                // Check if check-out date is less than check-in date
                String checkInDateFormat = "dd-MM-yyyy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(checkInDateFormat);
                try {
                    Date checkInDate = dateFormat.parse(checkInDateStr);
                    Date checkOutDate = dateFormat.parse(checkOutDateStr);
                    if (checkOutDate.before(checkInDate)) {
                        Toast.makeText(getContext(), "Check-out date cannot be before check-in date", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Check if the number of guests is empty or 0
                int guestNumber = 0;
                if (guestNumberStr.isEmpty() || "0".equals(guestNumberStr)) {
                    // 如果客人数量为空或者为0，显示提示消息
                    guestsCountEditText.setError("Please enter guest number");
                    return;
                } else {
                    guestNumber = Integer.parseInt(guestNumberStr);
                }

                // Check if name is empty
                if (guestName.isEmpty()) {
                    nameEditText.setError("Please enter guest name");
                    return;
                }


                Intent intent = new Intent(getActivity(), HotelListActivity.class);
                intent.putExtra("check in date", checkInDateStr);
                intent.putExtra("check out date", checkOutDateStr);
                intent.putExtra("number of guests", guestNumberStr);
                startActivity(intent);
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guestsCountEditText.setText("");
                nameEditText.setText("");
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInDate = getDateFromCalendar(checkInDatePicker);
                checkOutDate = getDateFromCalendar(checkOutDatePicker);
                //searchTextConfirmationTextView.setText("Test");
                //get the number of guest and guest name
                numberOfGuests = guestsCountEditText.getText().toString();
                guestName = nameEditText.getText().toString();
                sharedPreferences = getActivity().getSharedPreferences(myPreference, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(name,guestName);
                editor.putString(guestsCount,numberOfGuests);
                editor.commit();

                searchTextConfirmationTextView.setText("Dear Customer, Your check in date is " + checkInDate + ", " +
                        "your checkout date is " + checkOutDate + ".The number of guests are " + numberOfGuests);
            }
        });

        retrieveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getActivity().getSharedPreferences(myPreference, Context.MODE_PRIVATE);
                if (sharedPreferences.contains(name)){
                    nameEditText.setText(sharedPreferences.getString(name,""));
                }
                if (sharedPreferences.contains(guestsCount)){
                    guestsCountEditText.setText(sharedPreferences.getString(guestsCount,""));
                }
            }
        });
    }
    private String getDateFromCalendar(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//如果这里MM都是小写，则会有问题
        String formattedDate = simpleDateFormat.format(calendar.getTime());
        return formattedDate;
    }
}
