package com.example.android.testamante.ui;

import java.util.Date;

/**
 * Created by Tathari on 11/27/2017.
 */

public class ProfileDetails {
    private String username;
    private Date dob;
    private String gender;
    private String interested_gender;

    public ProfileDetails(String username, Date dob, String gender, String interested_gender) {
        this.username = username;
        this.dob = dob;
        this.gender = gender;
        this.interested_gender = interested_gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getInterested_gender() {
        return interested_gender;
    }

    public void setInterested_gender(String interested_gender) {
        this.interested_gender = interested_gender;
    }


}
