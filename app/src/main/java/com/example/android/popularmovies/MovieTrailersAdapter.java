package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by PallaviNishanth on 3/23/17.
 */

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.ViewHolder> {

    ArrayList<String> Name, Key;

    int no_of_trailers;
    Context context;

    private String YOUTUBE_THUMBNAIL_BASE_URL = "http://img.youtube.com/vi/";

    private final String LOG_TAG = MovieTrailersAdapter.class.getSimpleName();

    public MovieTrailersAdapter(Context context, ArrayList<String> name,
                                ArrayList<String> key, int no_of_trailers){

        this.context = context;
        this.Name = name;
        this.Key = key;
        this.no_of_trailers = no_of_trailers;

    }

    @Override
    public int getItemCount() {
        return no_of_trailers;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Picasso.with(context)
                .load(YOUTUBE_THUMBNAIL_BASE_URL + Key.get(position) + "/maxresdefault.jpg")
                .into(holder.trailer_thumbnail_image);

        holder.trailer_title.setText(Name.get(position));

    }


    @Override
    public MovieTrailersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        // create a new view
        View mview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_view, parent, false);

        return new ViewHolder(mview);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView trailer_thumbnail_image;
        public TextView trailer_title;

        public ViewHolder(View view){
            super(view);
            trailer_thumbnail_image = (ImageView) view.findViewById(R.id.trailer_image);
            trailer_title = (TextView) view.findViewById(R.id.trailer_title);

            view.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    if(position!= RecyclerView.NO_POSITION) {
                        listener.onItemClick(view, position);
                    }
                }
            });

        }
    }

    public interface OnItemClickListener {

        void onItemClick(View itemView, int position);
    }

    private static MovieTrailersAdapter.OnItemClickListener listener;

    public void setOnItemClickListener(MovieTrailersAdapter.OnItemClickListener listener) {

        this.listener = listener;
    }
}