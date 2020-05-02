package com.trinetraapp.model.test_name;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghvendra Sahu on 02-May-20.
 */
public class TestNameData {
    @SerializedName("test_id")
    @Expose
    private String testId;
    @SerializedName("test_name")
    @Expose
    private String testName;
    @SerializedName("status")
    @Expose
    private String status;

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
