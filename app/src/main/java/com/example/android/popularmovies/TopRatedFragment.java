package com.example.android.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.popularmovies.Network.MovieData;
import com.example.android.popularmovies.Network.MovieDataJSON;
import com.example.android.popularmovies.Network.MovieRetrofitAPI;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class TopRatedFragment extends Fragment {

    final String MOVIEDB_API = "http://api.themoviedb.org/3/movie/";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TopRatedMovieAdapter movieAdapter;

    private Retrofit movieRetrofit = new Retrofit.Builder().baseUrl(MOVIEDB_API).
            addConverterFactory(GsonConverterFactory.create()).build();
    MovieRetrofitAPI retrofitAPI = movieRetrofit.create(MovieRetrofitAPI.class);

    MovieDataJSON moviedataJson = new MovieDataJSON();
    private static ArrayList<MovieData> topRatedmoviedata = new ArrayList<>();

    final String sort_order = "top_rated";

    private final String LOG_TAG = TopRatedFragment.class.getSimpleName();

    public TopRatedFragment() {
        // Required empty public constructor
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("P_Movies", topRatedmoviedata);
    }

    @Override
    public void onStart() {
        super.onStart();

        if(isOnline() && topRatedmoviedata.isEmpty()){
            getTopRatedMovie();
        }

    }

    public boolean isOnline(){

        ConnectivityManager conn_m =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = conn_m.getActiveNetworkInfo();

        return ((netInfo !=null) && (netInfo.isConnected()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v(LOG_TAG, "on create view.. ");

        if(savedInstanceState!=null){

            Log.v(LOG_TAG, "Saved Instance Not null.. ");
            topRatedmoviedata = savedInstanceState.getParcelableArrayList("P_Movies");
        }

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.movie_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        movieAdapter = new TopRatedMovieAdapter(getActivity(), topRatedmoviedata);
        mRecyclerView.setAdapter(movieAdapter);

        movieAdapter.setOnItemClickListener(new TopRatedMovieAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(View itemView, int position) {
                Toast.makeText(getActivity(), "Top Rated Movie clicked", Toast.LENGTH_SHORT).show();

                MovieData top_md = topRatedmoviedata.get(position);

                Intent i = new Intent(getActivity(), DetailActivity.class);
                i.putExtra(DetailActivity.EXTRA_NAME, top_md);
                startActivity(i);

            }
        });

        return rootView;
    }

    public void getTopRatedMovie(){

        Log.v(LOG_TAG, "Get Top Rated Movies List");

        Call<MovieDataJSON> moviedatacall = retrofitAPI.MOVIE_DATA_CALL(sort_order,
                BuildConfig.MOVIEDB_API_KEY);

        moviedatacall.enqueue(new Callback<MovieDataJSON>() {
            @Override
            public void onResponse(Response<MovieDataJSON> response, Retrofit retrofit) {

                Log.v(LOG_TAG, "Top Rated Movie List Response is " + response.isSuccess());

                topRatedmoviedata = response.body().getMovieresults();

                for(MovieData mdata : topRatedmoviedata){

                    Log.v(LOG_TAG, "Top Rated Movie title" + mdata.getoriginalTitle());
                }
                movieAdapter = new TopRatedMovieAdapter(getActivity(), topRatedmoviedata);
                mRecyclerView.setAdapter(movieAdapter);

            }

            @Override
            public void onFailure(Throwable t) {

                Log.v(LOG_TAG, "On Failure" + t.toString());
            }

        });

    }

}
