package com.example.android.popularmovies.Network;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by PallaviNishanth on 2/24/17.
 */

public class MovieData implements Parcelable {

    String poster_path;
    String overview;
    String release_date;
    Long id;
    String original_title;
    double vote_average;

    public MovieData(){

    }

    // Setter Methods
    public void setPosterPath(String poster_path){

        this.poster_path = poster_path;
    }

    public void setoverview(String overview){

        this.overview = overview;
    }

    public void setreleaseDate(String release_date){

        this.release_date = release_date;
    }

    public void setoriginalTitle(String original_title){

        this.original_title = original_title;
    }

    public void setvoteAverage(double vote_average){

        this.vote_average = vote_average;
    }

    public void setMovieID(Long id){

        this.id = id;
    }

    // Getter Methods
    public String getPosterPath(){

        return poster_path;
    }

    public String getoverview(){

        return overview;
    }

    public String getreleaseDate(){

        return release_date;
    }

    public String getoriginalTitle(){

        return original_title;
    }

    public double getvoteAverage(){

        return vote_average;
    }

    public Long getMovieID(){

        return id;
    }

    private MovieData(Parcel in){

        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
        original_title = in.readString();
        vote_average = in.readDouble();
        id = in.readLong();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(poster_path);
        parcel.writeString(overview);
        parcel.writeString(release_date);
        parcel.writeString(original_title);
        parcel.writeDouble(vote_average);
        parcel.writeLong(id);

    }

    public static final Parcelable.Creator<MovieData> CREATOR = new Parcelable.Creator<MovieData>(){

        @Override
        public MovieData createFromParcel(Parcel parcel) {
            return new MovieData(parcel);
        }

        @Override
        public MovieData[] newArray(int i) {
            return new MovieData[i];
        }
    };

}
