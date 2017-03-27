package com.example.android.popularmovies;


import android.content.Intent;
import android.database.Cursor;
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
import com.example.android.popularmovies.data.MovieContract;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavMovieFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FavMovieAdapter movieAdapter;

    public static ArrayList<MovieData> FavMoviedata = new ArrayList<MovieData>();

    private final String LOG_TAG = FavMovieFragment.class.getSimpleName();

    String MOVIE_KEY = "FavMovie";

    public FavMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("P_Movies", FavMoviedata);
    }

    @Override
    public void onStart() {
        super.onStart();

        if(FavMoviedata.isEmpty()){
            getFavMovies();

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //Toast.makeText(getActivity(), "onPause FavoriteFragment():" + TAG, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Toast.makeText(getActivity(), "onResume FavoriteFragment():" + TAG, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v(LOG_TAG, "on create view.. ");

        if(savedInstanceState!=null){

            Log.v(LOG_TAG, "Saved Instance Not null.. ");
            FavMoviedata = savedInstanceState.getParcelableArrayList("P_Movies");
        }

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.movie_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        movieAdapter = new FavMovieAdapter(getActivity(), FavMoviedata);
        mRecyclerView.setAdapter(movieAdapter);

        movieAdapter.setOnItemClickListener(new FavMovieAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(View itemView, int position) {
                Toast.makeText(getActivity(), "Fav Movie clicked", Toast.LENGTH_SHORT).show();

                MovieData md = FavMoviedata.get(position);

                Intent i = new Intent(getActivity(), DetailActivity.class);
                //i.putExtra(MOVIE_KEY, md);
                i.putExtra(DetailActivity.EXTRA_NAME, md);
                startActivity(i);

            }
        });

        return rootView;

    }

    public void getFavMovies(){

        Log.v(LOG_TAG, "Get favorite Movies");

        Cursor favmoviecursor;

        favmoviecursor = getActivity().getContentResolver().
                query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null, null);

        if(favmoviecursor.getCount()!=0){

            Log.v(LOG_TAG, "Fav movies DB has items" + favmoviecursor.getCount());

            favmoviecursor.moveToFirst();
            int i=0;

            do{

                MovieData favMovieDataObject = new MovieData();

                favMovieDataObject.setPosterPath(favmoviecursor.
                        getString(favmoviecursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTERPATH)));
                favMovieDataObject.setoverview(favmoviecursor.
                        getString(favmoviecursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS)));
                favMovieDataObject.setreleaseDate(favmoviecursor.
                        getString(favmoviecursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASEDATE)));
                favMovieDataObject.setoriginalTitle(favmoviecursor.
                        getString(favmoviecursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE)));
                favMovieDataObject.setvoteAverage(favmoviecursor.
                        getDouble(favmoviecursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_VOTEAVERAGE)));
                favMovieDataObject.setMovieID(favmoviecursor.
                        getLong(favmoviecursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));

                Log.v(LOG_TAG, "favorite Movies id.." + favmoviecursor.
                        getLong(favmoviecursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));

                FavMoviedata.add(i, favMovieDataObject);
                i++;

            }while(favmoviecursor.moveToNext());
            favmoviecursor.close();

            movieAdapter = new FavMovieAdapter(getActivity(), FavMoviedata);
            mRecyclerView.setAdapter(movieAdapter);

        }else{

            Toast.makeText(getActivity(), "No Movies in Favorite List", Toast.LENGTH_SHORT).show();
            favmoviecursor.close();
        }

    }

}
