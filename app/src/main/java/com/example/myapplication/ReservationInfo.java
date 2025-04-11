package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

import java.util.List;
public class ReservationInfo {
    private String hotel_name;
    private String checkin;
    private String checkout;
    private List<GuestInfo> guests_list;

    public ReservationInfo(String hotel_name, String checkin, String checkout, List<GuestInfo> guests_list) {
        this.hotel_name = hotel_name;
        this.checkin = checkin;
        this.checkout = checkout;
        this.guests_list = guests_list;
    }

    public static class GuestInfo {
        @SerializedName("guest_name")
        private String guestName;

        private String gender;

        public GuestInfo(String guestName, String gender) {
            this.guestName = guestName;
            this.gender = gender;
        }
    }
}
