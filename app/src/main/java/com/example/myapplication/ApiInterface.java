package com.example.myapplication;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @GET("/api/hotels/")
    public Call<List<Hotel>> getHotelsLists();

    @POST("/api/app/reservation/")
    Call<Map<String, String>> createReservation(@Body ReservationInfo reservationInfo);
}
