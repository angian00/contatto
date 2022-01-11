package com.ag.android.contatto.providers;

import android.graphics.Bitmap;

import com.ag.android.contatto.Gender;
import com.google.gson.annotations.SerializedName;

public class MatchProposal {
    @SerializedName(value="name")
    private String mName;

    @SerializedName(value="description")
    private String mDescription;

    @SerializedName(value="age")
    private int mAge;

    @SerializedName(value="gender")
    private Gender mGender;

    @SerializedName(value="imageUrl")
    private String mImageUrl;

    private transient Bitmap mImageData;


    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }


    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }


    public Gender getGender() {
        return mGender;
    }

    public void setGender(Gender gender) {
        mGender = gender;
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(int age) {
        mAge = age;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }

    public Bitmap getImageData() {
        return mImageData;
    }

    public void setImageData(Bitmap imageData) {
        this.mImageData = imageData;
    }
}
