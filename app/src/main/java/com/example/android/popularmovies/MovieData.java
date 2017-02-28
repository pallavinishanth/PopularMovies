package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by PallaviNishanth on 2/24/17.
 */

public class MovieData implements Parcelable {

    String posterPath;
    String overview;
    String releaseDate;
    Long id;
    String originalTitle;
    String vote_Average;

    public MovieData(){

    }

    // Setter Methods
    public void setPosterPath(String pp){

        posterPath = pp;
    }

    public void setoverview(String ov){

        overview = ov;
    }

    public void setreleaseDate(String rd){

        releaseDate = rd;
    }

    public void setoriginalTitle(String ot){

        originalTitle = ot;
    }

    public void setvoteAverage(String va){

        vote_Average = va;
    }

    public void setMovieID(Long ID){

        id = ID;
    }

    // Getter Methods
    public String getPosterPath(){

        return posterPath;
    }

    public String getoverview(){

        return overview;
    }

    public String getreleaseDate(){

        return releaseDate;
    }

    public String getoriginalTitle(){

        return originalTitle;
    }

    public String getvoteAverage(){

        return vote_Average;
    }

    public Long getMovieID(){

        return id;
    }

    private MovieData(Parcel in){

        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        originalTitle = in.readString();
        vote_Average = in.readString();
        id = in.readLong();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeString(originalTitle);
        parcel.writeString(vote_Average);
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
