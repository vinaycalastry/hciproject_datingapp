package com.example.android.testamante.models;

/**
 * Created by suresh on 11/28/17.
 */

public class MatchedProfile {

    private String profileID;
    private String profilePicURL;
    private String description;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

}

