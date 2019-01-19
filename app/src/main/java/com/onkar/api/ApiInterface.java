package com.onkar.api;

import com.onkar.dto.TransactionDetailsDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/transaction/details")
    Call<TransactionDetailsDto> getTopRatedMovies(@Query("username") final String username);
}
