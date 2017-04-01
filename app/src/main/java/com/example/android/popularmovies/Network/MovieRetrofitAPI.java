package com.example.android.popularmovies.Network;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by PallaviNishanth on 3/19/17.
 */

public interface MovieRetrofitAPI {

    @GET("{id}")
    Call<MovieDataJSON> MOVIE_DATA_CALL(@Path("id") String sort_order, @Query("api_key") String API_KEY);

    @GET("{id}/videos")
    Call<TrailerJSON> TRAILERS_DATA_CALL(@Path("id") Long id, @Query("api_key") String API_KEY);

    @GET("{id}/reviews")
    Call<ReviewsJSON> REVIEWS_DATA_CALL(@Path("id") Long id, @Query("api_key") String API_KEY);

}
