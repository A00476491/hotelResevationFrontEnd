package com.example.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;

//RestAdapter;
public class Api {
    public static ApiInterface getClient(){

        //please modify the url here (在这里修改自己的URL)
        Retrofit retrofit =  new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);
        return api;
    }

}

