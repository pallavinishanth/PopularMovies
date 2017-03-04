package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.design.widget.CollapsingToolbarLayout;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

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

        private final String LOG_TAG = DetailFragment.class.getSimpleName();

        String baseURL = "http://image.tmdb.org/t/p/w185/";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        public DetailFragment(){


        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            Intent i = getActivity().getIntent();

            if(i !=null && i.hasExtra("Movie")){

                MovieData mdata = i.getParcelableExtra("Movie");

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
                    Picasso.with(getActivity()).load(baseURL+posterpath).into(tv);

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

                String vaverage = mdata.getvoteAverage();

                if(!vaverage.isEmpty()){
                    TextView tv = (TextView) rootView.findViewById(R.id.movie_detail_vote_average);
                    tv.setText(vaverage);

                } else {
                    vaverage = "Movie vote average not found";

                }

            }

            return rootView;
        }
    }

}
