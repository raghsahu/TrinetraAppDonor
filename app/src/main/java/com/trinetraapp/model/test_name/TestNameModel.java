package com.trinetraapp.model.test_name;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 02-May-20.
 */
public class TestNameModel {
    @SerializedName("response")
    @Expose
    private Boolean response;
    @SerializedName("data")
    @Expose
    private List<TestNameData> data = null;

    public Boolean getResponse() {
        return response;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }

    public List<TestNameData> getData() {
        return data;
    }

    public void setData(List<TestNameData> data) {
        this.data = data;
    }
}
