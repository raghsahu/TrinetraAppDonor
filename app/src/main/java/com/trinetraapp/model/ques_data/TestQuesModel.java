package com.trinetraapp.model.ques_data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 02-May-20.
 */
public class TestQuesModel {
    @SerializedName("response")
    @Expose
    private Boolean response;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private List<TestQuesData> data = null;

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

    public List<TestQuesData> getData() {
        return data;
    }

    public void setData(List<TestQuesData> data) {
        this.data = data;
    }
}
