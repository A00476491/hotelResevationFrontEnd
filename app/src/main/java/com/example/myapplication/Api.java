package com.example.myapplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Api {
    public static ApiInterface getClient(){

        //please modify the url here (在这里修改自己的URL)
        Retrofit retrofit =  new Retrofit.Builder()
                // .baseUrl("http://10.0.2.2:8000/")
                // .baseUrl("http://13.58.53.22:8000/")
                .baseUrl("http://13.58.53.22/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);
        return api;
    }
}

