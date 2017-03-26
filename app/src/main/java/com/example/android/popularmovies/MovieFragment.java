package com.example.android.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.popularmovies.Network.MovieData;
import com.example.android.popularmovies.Network.MovieDataJSON;
import com.example.android.popularmovies.Network.MovieRetrofitAPI;
import com.example.android.popularmovies.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    final String MOVIEDB_API = "http://api.themoviedb.org/3/movie/";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MovieDataAdapter movieAdapter;
    private MovieDataAdapter FavmovieAdapter;
    private Retrofit movieRetrofit = new Retrofit.Builder().baseUrl(MOVIEDB_API).
            addConverterFactory(GsonConverterFactory.create()).build();
    MovieRetrofitAPI retrofitAPI = movieRetrofit.create(MovieRetrofitAPI.class);

    MovieDataJSON moviedataJson = new MovieDataJSON();
    ArrayList<MovieData> moviedata = new ArrayList<>();
    ArrayList<MovieData> MOarray = new ArrayList<MovieData>();
    public static ArrayList<MovieData> Movieobjectarray = new ArrayList<MovieData>();
    public static ArrayList<MovieData> FavMovieobjectarray = new ArrayList<MovieData>();

    String MOVIE_KEY = "Movie";

    static String SOrder = null;


    private final String LOG_TAG = MovieFragment.class.getSimpleName();


    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Log.v(LOG_TAG, "on create.. ");

        if(savedInstanceState!=null){
            Movieobjectarray = savedInstanceState.getParcelableArrayList(MOVIE_KEY);
        }

    }

    public boolean isOnline(){

        ConnectivityManager conn_m =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = conn_m.getActiveNetworkInfo();

        return ((netInfo !=null) && (netInfo.isConnected()));
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_KEY, MOarray);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_fragment, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            Intent intent = new Intent(getActivity(), SettingsActivity.class);

            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart(){

        super.onStart();

        SharedPreferences.OnSharedPreferenceChangeListener prefListener;

        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getActivity());


        String sortorder = sharedPrefs.getString(getString(R.string.pref_sort_order_key),
                    getString(R.string.pref_sort_order_default));

        if(FavMovieobjectarray.isEmpty()){
            getFavMovies();

        }

        if(sortorder.equals(getString(R.string.pref_sort_order_favorites))){

            Log.v(LOG_TAG, "Sort Order is Favorites");

            FavmovieAdapter = new MovieDataAdapter(getActivity(), FavMovieobjectarray);
            mRecyclerView.setAdapter(FavmovieAdapter);

            FavmovieAdapter.setOnItemClickListener(new MovieDataAdapter.OnItemClickListener(){

                @Override
                public void onItemClick(View itemView, int position) {
                    //Toast.makeText(getActivity(), "Item clicked", Toast.LENGTH_SHORT).show();
                    String forecast = "Hello Favorite detail Activity";

                    MovieData md = FavMovieobjectarray.get(position);

                    Intent i = new Intent(getActivity(), DetailActivity.class);
                    i.putExtra(MOVIE_KEY, md);
                    startActivity(i);

                }
            });

        }else if(isOnline() && (Movieobjectarray.isEmpty())
                && (!sortorder.equals(getString(R.string.pref_sort_order_favorites)))
                || (!SOrder.equals(sortorder))){

            Log.v(LOG_TAG, "on start.. Object Array is empty");

            getmoviedata(sortorder);
            SOrder = sortorder;
        }

    }

    public void getmoviedata(String sort_order){

//        MovieDBTask moviedbTask = new MovieDBTask();
//        moviedbTask.execute(sort_order);

        Log.v(LOG_TAG, "Get Movies List" + sort_order);

        Call<MovieDataJSON> moviedatacall = retrofitAPI.MOVIE_DATA_CALL(sort_order,
                BuildConfig.MOVIEDB_API_KEY);

        moviedatacall.enqueue(new Callback<MovieDataJSON>() {
            @Override
            public void onResponse(Response<MovieDataJSON> response, Retrofit retrofit) {

                Log.v(LOG_TAG, "Response is " + response.isSuccess());

                moviedata = response.body().getMovieresults();

//                for(MovieData mdata : moviedata){
//
//                    Log.v(LOG_TAG, "Movie title" + mdata.getoriginalTitle());
//                }
                movieAdapter = new MovieDataAdapter(getActivity(), moviedata);
                mRecyclerView.setAdapter(movieAdapter);
            }

            @Override
            public void onFailure(Throwable t) {

                Log.v(LOG_TAG, "On Failure" + t.toString());
            }

        });
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
                        getLong(favmoviecursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS)));

                FavMovieobjectarray.add(i, favMovieDataObject);
                i++;

            }while(favmoviecursor.moveToNext());
            favmoviecursor.close();
        }else{

            Toast.makeText(getActivity(), "No Movies in Favorite List", Toast.LENGTH_SHORT).show();
            favmoviecursor.close();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v(LOG_TAG, "on create view.. ");

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.movie_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        movieAdapter = new MovieDataAdapter(getActivity(), moviedata);
        mRecyclerView.setAdapter(movieAdapter);

        movieAdapter.setOnItemClickListener(new MovieDataAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(View itemView, int position) {
                //Toast.makeText(getActivity(), "Item clicked", Toast.LENGTH_SHORT).show();
                String forecast = "Hello detail Activity";

                MovieData md = moviedata.get(position);

                Intent i = new Intent(getActivity(), DetailActivity.class);
                i.putExtra(MOVIE_KEY, md);
                startActivity(i);

            }
        });

        return rootView;
    }

    public class MovieDBTask extends AsyncTask<String, Void, ArrayList<MovieData>> {

        private final String LOG_TAG = MovieDBTask.class.getSimpleName();

        private ArrayList<MovieData> getMovieDataFromJSON(String moviedataStr) throws JSONException {

            final String RESULTS = "results";
            final String POSTER_PATH = "poster_path";
            final String OVERVIEW = "overview";
            final String RELEASE_DATE = "release_date";
            final String ID = "id";
            final String ORIGINAL_TITLE = "original_title";
            final String VOTE_AVERAGE = "vote_average";

            JSONObject moviedata = new JSONObject(moviedataStr);

            JSONArray moviedataArray = moviedata.getJSONArray(RESULTS);

            for(int i=0; i<moviedataArray.length(); i++){

                MovieData moviedataobject = new MovieData();

                moviedataobject.
                        setPosterPath(moviedataArray.getJSONObject(i).getString(POSTER_PATH));

                moviedataobject.
                        setoverview(moviedataArray.getJSONObject(i).getString(OVERVIEW));

                moviedataobject.
                        setreleaseDate(moviedataArray.getJSONObject(i).getString(RELEASE_DATE));

                moviedataobject.
                        setMovieID(moviedataArray.getJSONObject(i).getLong(ID));

                moviedataobject.
                        setoriginalTitle(moviedataArray.getJSONObject(i).getString(ORIGINAL_TITLE));

                moviedataobject.
                        setvoteAverage(moviedataArray.getJSONObject(i).getDouble(VOTE_AVERAGE));

                Movieobjectarray.add(i, moviedataobject);

                //Log.v(LOG_TAG, "PosterPath " + moviedataobject.getPosterPath());
            }

            return Movieobjectarray;
        }

        @Override
        protected ArrayList<MovieData> doInBackground(String... params) {

            String moviedataStr = null;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            try {

                final String MOVIEDB_API = "http://api.themoviedb.org/3/movie/";
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(MOVIEDB_API).buildUpon()
                        .appendPath(params[0])
                        .appendQueryParameter(API_KEY, BuildConfig.MOVIEDB_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviedataStr = buffer.toString();
                Log.v(LOG_TAG, "MovieDB JSOn string:" + moviedataStr);
            }catch(IOException e){

                Log.e(LOG_TAG, "Error ", e);

                return null;

            }finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try{

                return getMovieDataFromJSON(moviedataStr);

            }catch (JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieData> movieDatas) {

            super.onPostExecute(movieDatas);
            movieAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(movieAdapter);

        }
    }
}