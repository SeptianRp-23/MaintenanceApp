package com.azis.skripsiproject.Server;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://mydbskripsi.000webhostapp.com/maintenance-api/";
//    private static final String BASE_URL = "http://192.168.43.25/maintenance-api/";
    private static Retrofit retrofit;

    public static Retrofit getApiClient(){

        if (retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }
}
