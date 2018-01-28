package com.example.photos.webservice.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhotosCallBack implements Parcelable {

    @SerializedName("photos")
    @Expose
    private Photos photos;
    @SerializedName("stat")
    @Expose
    private String stat;
    public final static Parcelable.Creator<PhotosCallBack> CREATOR = new Creator<PhotosCallBack>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PhotosCallBack createFromParcel(Parcel in) {
            return new PhotosCallBack(in);
        }

        public PhotosCallBack[] newArray(int size) {
            return (new PhotosCallBack[size]);
        }

    };

    protected PhotosCallBack(Parcel in) {
        this.photos = ((Photos) in.readValue((Photos.class.getClassLoader())));
        this.stat = ((String) in.readValue((String.class.getClassLoader())));
    }

    public PhotosCallBack() {
    }

    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(photos);
        dest.writeValue(stat);
    }

    public int describeContents() {
        return 0;
    }

}