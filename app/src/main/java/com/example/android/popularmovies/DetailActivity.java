package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Network.MovieData;
import com.example.android.popularmovies.Network.MovieRetrofitAPI;
import com.example.android.popularmovies.Network.TrailerData;
import com.example.android.popularmovies.Network.TrailerJSON;
import com.example.android.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "movie_detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }

    }

    public static class DetailFragment extends Fragment {

        final String MOVIEDB_API = "http://api.themoviedb.org/3/movie/";
        private Retrofit movieRetrofit = new Retrofit.Builder().baseUrl(MOVIEDB_API).
                addConverterFactory(GsonConverterFactory.create()).build();
        MovieRetrofitAPI retrofitAPI = movieRetrofit.create(MovieRetrofitAPI.class);

        static Long m_id_d;
        static Long movie_id;
        GridView trailersListView;
        TrailerJSON t_JSON = new TrailerJSON();
        static int trailers_count;
        private MovieTrailersAdapter trailerAdapter;
        private static ArrayList<String> Name = new ArrayList<String>();
        private static ArrayList<String> Key = new ArrayList<String>();
        private static ArrayList<TrailerData> movietrailersdata = new ArrayList<>();
        private HorizontalScrollView trailerView;
        private LinearLayoutManager trailerLayoutManager;

        ImageButton favMovieStar;
        private static Bundle bundle = new Bundle();
        boolean starClicked = false;

        private final String LOG_TAG = DetailFragment.class.getSimpleName();

        String baseURL = "http://image.tmdb.org/t/p/w185/";

        @Override
        public void onCreate(Bundle savedInstanceState) {

            Log.v(LOG_TAG, "OnCreate");
            super.onCreate(savedInstanceState);


        }

        @Override
        public void onStart() {
            super.onStart();

            Log.v(LOG_TAG, "On Start");

            if((Name.isEmpty() && Key.isEmpty()) || m_id_d !=movie_id){

                getTrailers(movie_id);
                m_id_d = movie_id;
            }

        }

        public DetailFragment(){

        }

//        @Override
//        public void onPause() {
//            super.onPause();
//            bundle.putBoolean("ToggleButtonState", starClicked);
//        }
//
//        @Override
//        public void onResume() {
//            super.onResume();
//            starClicked = bundle.getBoolean("ToggleButtonState",false);
//        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            Log.v(LOG_TAG, "OnCreate View");

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


            final Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Intent i = getActivity().getIntent();
            //getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);


            if(i !=null && i.hasExtra(EXTRA_NAME)){

                final MovieData mdata = i.getParcelableExtra(EXTRA_NAME);

                movie_id = mdata.getMovieID();

                Log.v(LOG_TAG, "Popular Movies ID" + movie_id);

                //getTrailers(movie_id);

                trailersListView = (GridView) rootView.findViewById(R.id.trailers_list_view);

//                trailerAdapter = new MovieTrailersAdapter(getActivity(), Name, Key, trailers_count);
//                trailersListView.setAdapter(trailerAdapter);
//
//                trailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                        Toast.makeText(getActivity(), "Trailer clicked", Toast.LENGTH_SHORT).show();
//                    }
//
//                });

                favMovieStar = (ImageButton) rootView.findViewById(R.id.movie_favorite);

                favMovieStar.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        if(starClicked==true){
                            favMovieStar.setImageResource(android.R.drawable.btn_star_big_off);
                            starClicked = false;
                            Toast.makeText(getActivity(),
                                    "ImageButton is Unclicked!", Toast.LENGTH_SHORT).show();

                            DeleteFavMovieData(mdata.getMovieID());
                        }else {

                            favMovieStar.setImageResource(android.R.drawable.btn_star_big_on);
                            starClicked = true;

                            Toast.makeText(getActivity(),
                                    "ImageButton is clicked!", Toast.LENGTH_SHORT).show();

                            if(MovieMarkedFav(mdata.getMovieID())){

                                Toast.makeText(getActivity(),
                                        "Movie Already Marked Favorite", Toast.LENGTH_SHORT).show();

                            }else{

                                insertFavMovieData(mdata.getMovieID(), mdata.getoriginalTitle(),
                                        mdata.getreleaseDate(), mdata.getPosterPath(),
                                        mdata.getvoteAverage(), mdata.getoverview());
                            }

                        }
                    }
                });

                String title = mdata.getoriginalTitle();

                if(!title.isEmpty()){
//                    TextView tv = (TextView) rootView.findViewById(R.id.movie_detail_title);
//                    tv.setText(title);
                    CollapsingToolbarLayout collapsingToolbar =
                            (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
                    collapsingToolbar.setTitle(title);
                    collapsingToolbar.setExpandedTitleGravity(Gravity.CENTER_HORIZONTAL);
                    collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
                    collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);


                } else {
                    title = "Movie Title not found";

                }

                Log.v(LOG_TAG, "MovieTitle "+ title);

                String posterpath = mdata.getPosterPath();

                if(!posterpath.isEmpty()){
                    ImageView tv = (ImageView) rootView.findViewById(R.id.movie_detail_poster);
                    ImageView tvbackdrop = (ImageView) rootView.findViewById(R.id.backdrop);
                    Picasso.with(getActivity()).load(baseURL+posterpath).into(tv);
                    Picasso.with(getActivity()).load(baseURL+posterpath).into(tvbackdrop);

                }

                String overview = mdata.getoverview();

                if(!overview.isEmpty()){
                    TextView tv = (TextView) rootView.findViewById(R.id.movie_detail_overview);
                    tv.setText(overview);

                } else {
                    overview = "Movie overview not found";

                }

                String rdate = mdata.getreleaseDate();

                if(!rdate.isEmpty()){
                    TextView tv = (TextView) rootView.findViewById(R.id.movie_detail_release_date);
                    tv.setText(rdate);

                } else {
                    rdate = "Movie release date not found";

                }

                double vaverage = mdata.getvoteAverage();

                String stringdouble= Double.toString(vaverage);

                TextView tv = (TextView) rootView.findViewById(R.id.movie_detail_vote_average);
                tv.setText(stringdouble);

            }

            return rootView;
        }

        public void insertFavMovieData(long m_id, String title, String release_date, String poster_path,
                                    double vote_average, String mOverview){


            ContentValues mvalues = new ContentValues();

            mvalues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, m_id);
            mvalues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, title);
            mvalues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASEDATE, release_date);
            mvalues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTERPATH, poster_path);
            mvalues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTEAVERAGE, vote_average);
            mvalues.put(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS, mOverview);

            // Finally, insert movie data into the database.
            Uri insertedUri = getActivity().getContentResolver().insert(
                    MovieContract.MovieEntry.CONTENT_URI,
                    mvalues);

            Log.v(LOG_TAG, "Inserted Movie Data "+ title);

        }

        public void DeleteFavMovieData(long favM_id){

            getActivity().getContentResolver()
                    .delete(MovieContract.MovieEntry.buildFavMovieUri(favM_id),
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=" + favM_id, null);

            Log.v(LOG_TAG, "Deleted Movie Data "+ favM_id);

        }

        public Boolean MovieMarkedFav(long favM_id){

            Cursor cursor;

            cursor = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    new String[] {MovieContract.MovieEntry.COLUMN_MOVIE_ID},
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=" + favM_id,
                    null, null, null);

            if(cursor.getCount()==0)
                return false;
            else
                return true;
        }

        public void getTrailers(Long t_id){

            Log.v(LOG_TAG, "Get Pop Trailer list" + t_id);

            Call<TrailerJSON> trailerlistcall = retrofitAPI.TRAILERS_DATA_CALL(t_id, BuildConfig.MOVIEDB_API_KEY);

            trailerlistcall.enqueue(new Callback<TrailerJSON>() {
                @Override
                public void onResponse(Response<TrailerJSON> response, Retrofit retrofit) {

                    Log.v(LOG_TAG, "Trailers Response is " + response.isSuccess());

                    movietrailersdata = response.body().getResults();
                    trailers_count = movietrailersdata.size();

                    Log.v(LOG_TAG, "Trailers count" + trailers_count);

                    for(TrailerData tdata : movietrailersdata){

                        Log.v(LOG_TAG, "Trailers Names" + tdata.getName());
                        Name.add(tdata.getName());
                        Key.add(tdata.getKey());
                    }
                    trailerAdapter = new MovieTrailersAdapter(getActivity(), Name, Key, trailers_count);
                    trailersListView.setAdapter(trailerAdapter);

                    trailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Toast.makeText(getActivity(), "Trailer clicked", Toast.LENGTH_SHORT).show();
                        }

                    });

                }

                @Override
                public void onFailure(Throwable t) {

                    Log.v(LOG_TAG, "On Failure" + t.toString());
                }

            });

        }
    }
}