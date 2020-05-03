package com.trinetraapp.model.test_history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghvendra Sahu on 03-May-20.
 */
public class Test_History_Data {
    @SerializedName("test_history_id")
    @Expose
    private String testHistoryId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("test_id")
    @Expose
    private String testId;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("tips")
    @Expose
    private String tips;
    @SerializedName("create_timestamp")
    @Expose
    private String createTimestamp;

    public String getTestHistoryId() {
        return testHistoryId;
    }

    public void setTestHistoryId(String testHistoryId) {
        this.testHistoryId = testHistoryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(String createTimestamp) {
        this.createTimestamp = createTimestamp;
    }
}
