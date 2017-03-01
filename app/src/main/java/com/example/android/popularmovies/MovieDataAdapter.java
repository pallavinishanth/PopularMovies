package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by PallaviNishanth on 2/23/17.
 */

public class MovieDataAdapter extends RecyclerView.Adapter<MovieDataAdapter.ViewHolder> {

    private Context mContext;

    private ArrayList<MovieData> mData;

    String baseURL = "http://image.tmdb.org/t/p/w185/";

    private final String LOG_TAG = MovieDataAdapter.class.getSimpleName();


    private String[] mURL = {"http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"};

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    private static OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MovieDataAdapter(Context context, ArrayList<MovieData> m_data){

        this.mContext = context;
        this.mData = m_data;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public MovieDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View mview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item_movie, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //imageView.setLayoutParams(new GridView.LayoutParams(800, 800));
        //imageView.setPadding(1, 1, 1, 1);
        //ViewHolder vh = new ViewHolder(imageView);
        return new ViewHolder(mview);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mImageView.setImageBitmap(mData[position]);
        //Log.v(LOG_TAG, "poster path in adapter" + mData.get(position).getPosterPath());
         Picasso.with(mContext).load(baseURL+ mData.get(position).getPosterPath()).into(holder.mImageView);
        //Picasso.with(mContext).load(mURL[position]).into(holder.mImageView);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return mData.size();
        //return mURL.length;
    }

    public void updatemoviedata(ArrayList<MovieData> moviedata){

        for(int i=0; i<moviedata.size(); i++){

            mData.add(i, moviedata.get(i));
        }

        notifyDataSetChanged();

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