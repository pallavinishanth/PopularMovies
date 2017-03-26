package com.example.android.popularmovies.Network;

import java.util.ArrayList;

/**
 * Created by PallaviNishanth on 3/19/17.
 */

public class MovieDataJSON {

    private Long tresults, tpages, page;
    private ArrayList<MovieData> results;

    public Long getpage(){

        return page;
    }

    public Long getpages(){

        return tpages;
    }

    public Long getresults(){

        return tresults;
    }

    public void setpage(Long page){

       this.page = page;
    }

    public void setpages(Long pages){

        this.tpages = tpages;
    }

    public void setresults(Long results){

        this.tresults = tresults;
    }

    public ArrayList<MovieData> getMovieresults(){

        return results;
    }

    public void setMovieresults(ArrayList<MovieData> results){

        this.results = results;
    }
}
