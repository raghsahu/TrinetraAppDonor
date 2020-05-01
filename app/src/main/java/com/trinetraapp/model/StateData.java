package com.trinetraapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghvendra Sahu on 01-May-20.
 */
public class StateData {


    @SerializedName("city_state")
    @Expose
    private String cityState;
    @SerializedName("city_name")
    @Expose
    private String city_name;

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCityState() {
        return cityState;
    }

    public void setCityState(String cityState) {
        this.cityState = cityState;
    }
}
