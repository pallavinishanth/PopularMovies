package com.example.android.popularmovies.Network;

import java.util.ArrayList;

/**
 * Created by PallaviNishanth on 3/19/17.
 */

public class ReviewsJSON {

    private Long tresults, tpages, page, id;
    private ArrayList<ReviewsData> results;

    public Long getpage(){

        return page;
    }

    public Long getpages(){

        return tpages;
    }

    public Long getresults(){

        return tresults;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ArrayList<ReviewsData> getReviewsresults(){

        return results;
    }

    public void setReviewresults(ArrayList<ReviewsData> results){

        this.results = results;
    }
}
