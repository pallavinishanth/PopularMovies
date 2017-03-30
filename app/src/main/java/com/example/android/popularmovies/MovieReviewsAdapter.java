package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by PallaviNishanth on 3/23/17.
 */

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.ViewHolder> {

    ArrayList<String> AName, R_Content;

    int no_of_reviews;
    Context context;

    private final String LOG_TAG = MovieReviewsAdapter.class.getSimpleName();

    public MovieReviewsAdapter(Context context, ArrayList<String> a_name,
                               ArrayList<String> a_content, int no_of_reviews){

        this.context = context;
        this.AName = a_name;
        this.R_Content = a_content;
        this.no_of_reviews = no_of_reviews;

    }

    @Override
    public int getItemCount() {
        return no_of_reviews;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.review_author_name.setText(AName.get(position));
        holder.review_content.setText(R_Content.get(position));

    }


    @Override
    public MovieReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        // create a new view
        View mview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_view, parent, false);

        return new ViewHolder(mview);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView review_author_name;
        public TextView review_content;

        public ViewHolder(View view){
            super(view);
            review_author_name = (TextView) view.findViewById(R.id.author_name);
            review_content = (TextView) view.findViewById(R.id.author_content);

        }
    }

}