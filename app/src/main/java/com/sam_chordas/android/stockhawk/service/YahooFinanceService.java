package com.sam_chordas.android.stockhawk.service;

import retrofit2.Call;
import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import com.sam_chordas.android.stockhawk.model.HistoricalData;

public interface YahooFinanceService {

    @GET("v1/public/yql?")
    Call<HistoricalData> getHistoricalData(@Query("q") String query,
                                           @Query("format") String format,
                                           @Query("env") String environment);

    class Factory {

        public static YahooFinanceService create(String baseUrl) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            return retrofit.create(YahooFinanceService.class);
        }
    }
}