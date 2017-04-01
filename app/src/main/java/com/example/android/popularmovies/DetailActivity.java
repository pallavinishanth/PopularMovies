package com.example.android.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Network.MovieData;
import com.example.android.popularmovies.Network.MovieRetrofitAPI;
import com.example.android.popularmovies.Network.ReviewsData;
import com.example.android.popularmovies.Network.ReviewsJSON;
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
        final String YOUTUBE_URL = "http://www.youtube.com/watch";
        final String baseURL = "http://image.tmdb.org/t/p/w185/";

        private Retrofit movieRetrofit = new Retrofit.Builder().baseUrl(MOVIEDB_API).
                addConverterFactory(GsonConverterFactory.create()).build();
        MovieRetrofitAPI retrofitAPI = movieRetrofit.create(MovieRetrofitAPI.class);

        private RecyclerView tRecyclerView;
        private RecyclerView.LayoutManager tLayoutManager;
        private MovieTrailersAdapter trailerReAdapter;
        private TextView trailer_header;

        private RecyclerView rRecyclerView;
        private RecyclerView.LayoutManager rLayoutManager;
        private MovieReviewsAdapter reviewsReAdapter;
        private TextView review_header;

        static Long m_id_d;
        static Long movie_id;

        TrailerJSON t_JSON = new TrailerJSON();
        ReviewsJSON r_JSON = new ReviewsJSON();
        static int trailers_count;
        static int reviews_count;
        private ArrayList<String> Name = new ArrayList<String>();
        private ArrayList<String> Key = new ArrayList<String>();
        private ArrayList<String> AuthorName = new ArrayList<String>();
        private ArrayList<String> AuthorReview = new ArrayList<String>();
        private static ArrayList<TrailerData> movietrailersdata = new ArrayList<>();
        private static ArrayList<ReviewsData> moviereviewsdata = new ArrayList<>();
        private Uri uri;

        ImageButton favMovieStar;
        private static Bundle bundle = new Bundle();
        boolean markedFav, insert_result, delete_result;

        private final String LOG_TAG = DetailFragment.class.getSimpleName();

        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);


        }

        @Override
        public void onStart() {
            super.onStart();

            Log.v(LOG_TAG, "On Start");

            if(Name.isEmpty() && AuthorName.isEmpty()) {

                Log.v(LOG_TAG, "Data is Empty");
                getTrailers(movie_id);
                getReviews(movie_id);

            }

        }

        public DetailFragment(){

        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);

            Log.v(LOG_TAG, "onSaveInstanceState");

            outState.putBoolean("M_marked", markedFav);
            outState.putLong("M_ID", movie_id);
            outState.putStringArrayList("Name", Name);
            outState.putStringArrayList("Key", Key);
            outState.putStringArrayList("A_Name", AuthorName);
            outState.putStringArrayList("A_Review", AuthorReview);
            outState.putInt("No_Trailers", trailers_count);
            outState.putInt("No_Reviews", reviews_count);

        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            if(savedInstanceState!=null){

                Log.v(LOG_TAG, "savedInstanceState not null");

                markedFav = savedInstanceState.getBoolean("M_marked");
                movie_id = savedInstanceState.getLong("M_ID");
                Name = savedInstanceState.getStringArrayList("Name");
                Key = savedInstanceState.getStringArrayList("Key");
                AuthorName = savedInstanceState.getStringArrayList("A_Name");
                AuthorReview = savedInstanceState.getStringArrayList("A_Review");
                trailers_count = savedInstanceState.getInt("No_Trailers");
                reviews_count = savedInstanceState.getInt("No_Reviews");

                trailerReAdapter = new MovieTrailersAdapter(getActivity(), Name, Key, trailers_count);
                tRecyclerView.setAdapter(trailerReAdapter);
                reviewsReAdapter = new MovieReviewsAdapter(getActivity(), AuthorName, AuthorReview, reviews_count);
                rRecyclerView.setAdapter(reviewsReAdapter);
            }
        }

        @Override
        public void onPause() {
            super.onPause();


        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            Log.v(LOG_TAG, "OnCreate View");

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            trailer_header = (TextView) rootView.findViewById(R.id.Trailer);
            review_header = (TextView) rootView.findViewById(R.id.Reviews);


            final Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Intent i = getActivity().getIntent();


            if(i !=null && i.hasExtra(EXTRA_NAME)){

                final MovieData mdata = i.getParcelableExtra(EXTRA_NAME);

                movie_id = mdata.getMovieID();

                Log.v(LOG_TAG, "Popular Movies ID" + movie_id);

                favMovieStar = (ImageButton) rootView.findViewById(R.id.movie_favorite);

                markedFav = MovieMarkedFav(mdata.getMovieID());

                if(markedFav){

                    favMovieStar.setImageResource(android.R.drawable.btn_star_big_on);

                }else {

                    favMovieStar.setImageResource(android.R.drawable.btn_star_big_off);
                }

                favMovieStar.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        if(markedFav){

                            delete_result = DeleteFavMovieData(mdata.getMovieID());

                            if(delete_result){
                                markedFav = false;
                                favMovieStar.setImageResource(android.R.drawable.btn_star_big_off);
                                Toast.makeText(getActivity(),
                                        "Deleted from Favorites", Toast.LENGTH_SHORT).show();

                            }else{

                                favMovieStar.setImageResource(android.R.drawable.btn_star_big_on);
                                Toast.makeText(getActivity(),
                                        "Deletion Failed", Toast.LENGTH_SHORT).show();
                            }
                        }else {

                            insert_result = insertFavMovieData(mdata.getMovieID(),
                                    mdata.getoriginalTitle(), mdata.getreleaseDate(),
                                    mdata.getPosterPath(), mdata.getvoteAverage(), mdata.getoverview());

                            if(insert_result){

                                markedFav = true;
                                favMovieStar.setImageResource(android.R.drawable.btn_star_big_on);
                                Toast.makeText(getActivity(),
                                        "Added to Favorites", Toast.LENGTH_SHORT).show();

                            }else{

                                favMovieStar.setImageResource(android.R.drawable.btn_star_big_off);
                                Toast.makeText(getActivity(),
                                        "Adding to Fav Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });

                String title = mdata.getoriginalTitle();
                CollapsingToolbarLayout collapsingToolbar =
                        (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);

                if(!title.isEmpty()){

                    collapsingToolbar.setTitle(title);
                    collapsingToolbar.setExpandedTitleGravity(Gravity.CENTER_HORIZONTAL);
                    collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
                    collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);


                } else {
                    title = "Movie Title not found";
                    collapsingToolbar.setTitle(title);

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
                TextView otv = (TextView) rootView.findViewById(R.id.movie_detail_overview);

                if(!overview.isEmpty()){

                    otv.setText(overview);

                } else {
                    overview = "Movie overview not found";
                    otv.setText(overview);

                }

                String rdate = mdata.getreleaseDate();
                TextView rd_tv = (TextView) rootView.findViewById(R.id.movie_detail_release_date);

                if(!rdate.isEmpty()){

                    rd_tv.setText(rdate);

                } else {
                    rdate = "Movie release date not found";
                    rd_tv.setText(rdate);

                }

                double vaverage = mdata.getvoteAverage();

                String stringdouble= Double.toString(vaverage);

                TextView tv = (TextView) rootView.findViewById(R.id.movie_detail_vote_average);
                tv.setText(stringdouble);

                tRecyclerView = (RecyclerView) rootView.findViewById(R.id.trailer_recycler_view);
                tRecyclerView.setHasFixedSize(true);

                tLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                tRecyclerView.setLayoutManager(tLayoutManager);

                trailerReAdapter = new MovieTrailersAdapter(getActivity(), Name, Key, trailers_count);

                trailerReAdapter.setOnItemClickListener(new MovieTrailersAdapter.OnItemClickListener(){

                    @Override
                    public void onItemClick(View itemView, int position) {

                        Toast.makeText(getActivity(), "Trailer clicked", Toast.LENGTH_SHORT).show();

                        uri = uri.parse(YOUTUBE_URL).buildUpon()
                                .appendQueryParameter("v", Key.get(position)).build();
                        Intent YT_Intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(YT_Intent);
                    }
                });

                rRecyclerView = (RecyclerView) rootView.findViewById(R.id.reviews_recycler_view);
                rRecyclerView.setHasFixedSize(true);

                rLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                rRecyclerView.setLayoutManager(rLayoutManager);

                reviewsReAdapter = new MovieReviewsAdapter(getActivity(), AuthorName, AuthorReview, reviews_count);

            }

            return rootView;
        }

        public boolean insertFavMovieData(long m_id, String title, String release_date, String poster_path,
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

            Long result_id = ContentUris.parseId(insertedUri);

            if(result_id>0 && result_id!=-1){
                Log.v(LOG_TAG, "Insertion Successfuly "+ title);
                return true;

            }else {

                Log.v(LOG_TAG, "Insertion failed "+ title);
                return false;
            }

        }

        public boolean DeleteFavMovieData(long favM_id){

            int no_rows_deleted = getActivity().getContentResolver()
                    .delete(MovieContract.MovieEntry.buildFavMovieUri(favM_id),
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=" + favM_id, null);

            Log.v(LOG_TAG, "Deleted Movie Data "+ favM_id + "rows " + no_rows_deleted);

            if(no_rows_deleted > 0){

                return true;

            }else {

                return false;
            }

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

                    if(trailers_count==0){
                        trailer_header.setText("No Trailers are found for this Movie");
                    }

                    for(TrailerData tdata : movietrailersdata){

                        Log.v(LOG_TAG, "Trailers Names" + tdata.getName());
                        Name.add(tdata.getName());
                        Key.add(tdata.getKey());
                    }
                    trailerReAdapter = new MovieTrailersAdapter(getActivity(), Name, Key, trailers_count);
                    tRecyclerView.setAdapter(trailerReAdapter);

                }

                @Override
                public void onFailure(Throwable t) {

                    Log.v(LOG_TAG, "On Failure" + t.toString());

                }

            });

        }

        public void getReviews(Long R_id){

            Log.v(LOG_TAG, "Get Reviews list" + R_id);

            Call<ReviewsJSON> reviewslistcall = retrofitAPI.REVIEWS_DATA_CALL(R_id, BuildConfig.MOVIEDB_API_KEY);

            reviewslistcall.enqueue(new Callback<ReviewsJSON>() {
                @Override
                public void onResponse(Response<ReviewsJSON> response, Retrofit retrofit) {

                    Log.v(LOG_TAG, "Reviews Response is " + response.isSuccess());

                    moviereviewsdata = response.body().getReviewsresults();
                    reviews_count = moviereviewsdata.size();

                    Log.v(LOG_TAG, "Reviews count" + reviews_count);

                    if(reviews_count==0){

                        review_header.setText("No Reviews are found for this Movie");
                    }

                    for(ReviewsData rdata : moviereviewsdata){
                        Log.v(LOG_TAG, "Reviews Author Names : " + rdata.getAuthor());

                        AuthorName.add(rdata.getAuthor());
                        AuthorReview.add(rdata.getContent());

                    }
                    reviewsReAdapter = new MovieReviewsAdapter(getActivity(), AuthorName, AuthorReview, reviews_count);
                    rRecyclerView.setAdapter(reviewsReAdapter);
                }

                @Override
                public void onFailure(Throwable t) {

                    Log.v(LOG_TAG, "On Failure" + t.toString());
                }
            });
        }
    }
}