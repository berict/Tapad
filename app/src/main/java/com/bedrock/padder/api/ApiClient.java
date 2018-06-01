package com.bedrock.padder.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    static Retrofit retrofit = null;

    public static String BASE_URL = "http://berict.com/api/";

    public static void setRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            setRetrofit();
        }
        return retrofit;
    }
}
