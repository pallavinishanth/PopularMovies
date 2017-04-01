package com.example.android.popularmovies.Network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by PallaviNishanth on 3/23/17.
 */

public class TrailerJSON {

    private long id;
    private ArrayList<TrailerData> results;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    /**
     * @return The id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The results
     */
    public ArrayList<TrailerData> getResults() {
        return results;
    }

    /**
     * @param results The results
     */
    public void setResults(ArrayList<TrailerData> results) {
        this.results = results;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
