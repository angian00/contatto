package com.ag.android.contatto;

import java.util.Date;
import java.util.List;

public class Profile {
    private String mPhone;
    private String mEmail;
    private String mName;
    private Date mBirthday;
    private Gender mGender;
    private List<Gender> mInterests;
    private String mDescription;
    private String mPhotoPath;


    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Date getBirthday() {
        return mBirthday;
    }
    public void setBirthday(Date birthday) {
        mBirthday = birthday;
    }

    public Gender getGender() {
        return mGender;
    }

    public void setGender(Gender gender) {
        mGender = gender;
    }

    public List<Gender> getInterests() {
        return mInterests;
    }

    public void setInterests(List<Gender> interests) {
        mInterests = interests;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getPhotoPath() {
        return mPhotoPath;
    }

    public void setPhotoPath(String photoPath) {
        mPhotoPath = photoPath;
    }


    public boolean isValid() {
        if (mName == null || mName.equals(""))
            return false;

        if (mPhone == null || mPhone.equals(""))
            return false;

        if (mEmail == null || mEmail.equals(""))
            return false;

        if (mBirthday == null)
            return false;

        if (mGender == null)
            return false;

        if (mInterests == null || mInterests.isEmpty())
            return false;


        return true;
    }
}
