package com.example.android.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MovieDataAdapter movieAdapter;

    ArrayList<MovieData> MOarray = new ArrayList<MovieData>();
    ArrayList<MovieData> Movieobjectarray = new ArrayList<MovieData>();

    String MOVIE_KEY = "Movie";


    private final String LOG_TAG = MovieFragment.class.getSimpleName();


    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

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

        if(isOnline()) {

            MovieDBTask moviedbTask = new MovieDBTask();

            SharedPreferences sharedPrefs =
                    PreferenceManager.getDefaultSharedPreferences(getActivity());

            String sortorder = sharedPrefs.getString(getString(R.string.pref_sort_order_key),
                    getString(R.string.pref_sort_order_default));

            if (sortorder.equals(getString(R.string.pref_sort_order_mostpopular))) {

                moviedbTask.execute(sortorder);
            } else if (sortorder.equals(getString(R.string.pref_sort_order_highestrated))) {

                moviedbTask.execute(sortorder);
            } else {

                Log.d(LOG_TAG, "Sort Order Not Found: " + sortorder);
            }
        } else {

            Toast.makeText(getActivity(), " Not Connected to Network", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.movie_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        movieAdapter = new MovieDataAdapter(getActivity(), Movieobjectarray);
        mRecyclerView.setAdapter(movieAdapter);

        movieAdapter.setOnItemClickListener(new MovieDataAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(View itemView, int position) {
                //Toast.makeText(getActivity(), "Item clicked", Toast.LENGTH_SHORT).show();
                String forecast = "Hello detail Activity";

                MovieData md = Movieobjectarray.get(position);

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
                        setvoteAverage(moviedataArray.getJSONObject(i).getString(VOTE_AVERAGE));

                Movieobjectarray.add(i, moviedataobject);

                Log.v(LOG_TAG, "PosterPath " + moviedataobject.getPosterPath());
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