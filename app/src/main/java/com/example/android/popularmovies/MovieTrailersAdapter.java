package com.example.android.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by PallaviNishanth on 3/23/17.
 */

public class MovieTrailersAdapter extends BaseAdapter {


    ArrayList<String> Name, Key;

    int no_of_trailers;
    Context context;


    ImageView trailer_thumbnail_image;
    TextView trailer_title;

    private String YOUTUBE_THUMBNAIL_BASE_URL = "http://img.youtube.com/vi/";

    private final String LOG_TAG = MovieTrailersAdapter.class.getSimpleName();

    public MovieTrailersAdapter(Context context, ArrayList<String> name, ArrayList<String> key, int no_of_trailers){

        this.context = context;
        this.Name = name;
        this.Key = key;
        this.no_of_trailers = no_of_trailers;

    }

    @Override
    public int getCount() {
        return no_of_trailers;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view==null)
        view = LayoutInflater.from(context).inflate(R.layout.trailer_view, viewGroup, false);

        trailer_thumbnail_image = (ImageView) view.findViewById(R.id.trailer_image);
        trailer_title = (TextView) view.findViewById(R.id.trailer_title);

        //Log.v(LOG_TAG, "Trailer Adapter , Trailer Count " + no_of_trailers);
        Log.v(LOG_TAG, "Trailer Adapter , Trailer Name " + Name.get(i));
       // Log.v(LOG_TAG, "Trailer Adapter , Trailer key " + Key.get(i));

        trailer_title.setText(Name.get(i));

        trailer_thumbnail_image = (ImageView) view.findViewById(R.id.trailer_image);

        Picasso.with(context)
                .load(YOUTUBE_THUMBNAIL_BASE_URL + Key.get(i) + "/maxresdefault.jpg")
                .into(trailer_thumbnail_image);
        return view;
    }
}


