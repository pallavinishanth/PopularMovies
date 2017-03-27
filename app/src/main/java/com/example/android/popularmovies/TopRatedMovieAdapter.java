package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.Network.MovieData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by PallaviNishanth on 2/23/17.
 */

public class TopRatedMovieAdapter extends RecyclerView.Adapter<TopRatedMovieAdapter.ViewHolder> {

    private Context mContext;

    private ArrayList<MovieData> mData;

    String baseURL = "http://image.tmdb.org/t/p/w185/";

    private final String LOG_TAG = TopRatedMovieAdapter.class.getSimpleName();


    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    private static OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public TopRatedMovieAdapter(Context context, ArrayList<MovieData> m_data){

        this.mContext = context;
        this.mData = m_data;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public TopRatedMovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View mview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item_movie, parent, false);

        return new ViewHolder(mview);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
         Picasso.with(mContext)
                 .load(baseURL+ mData.get(position).getPosterPath()).into(holder.mImageView);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return mData.size();

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private final ImageView mImageView;

        public ViewHolder(final View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.grid_item_movie_image_view);
            mImageView.setPadding(1, 1, 1, 1);

            view.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View iview) {

                    if(listener!=null){

                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listener.onItemClick(view, position);
                        }
                    }

                }
            });
        }
    }

}