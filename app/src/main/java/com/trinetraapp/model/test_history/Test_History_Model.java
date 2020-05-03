package com.trinetraapp.model.test_history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 03-May-20.
 */
public class Test_History_Model {
    @SerializedName("response")
    @Expose
    private Boolean response;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private List<Test_History_Data> data = null;

    public Boolean getResponse() {
        return response;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Test_History_Data> getData() {
        return data;
    }

    public void setData(List<Test_History_Data> data) {
        this.data = data;
    }
}
