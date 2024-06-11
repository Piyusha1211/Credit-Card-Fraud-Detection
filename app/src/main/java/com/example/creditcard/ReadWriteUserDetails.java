package com.example.creditcard;

public class ReadWriteUserDetails {
    public String dob, gender, mobile;

    // No-argument constructor
    public ReadWriteUserDetails() {
    }

    // Constructor with arguments
    public ReadWriteUserDetails(String textDoB, String textGender, String textMobile) {
        this.dob = textDoB;
        this.gender = textGender;
        this.mobile = textMobile;
    }
}
