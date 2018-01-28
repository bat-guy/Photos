package com.example.photos.webservice;


import com.example.photos.Constants;
import com.example.photos.webservice.model.PhotosCallBack;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface WebServiceInterface {

    /**
     * Interface responsible for making the API call to fetch the photos details
     * @param page the page number
     * @return The WebServiceInterface object
     */
    @GET(Constants.FETCH_URL)
    Call<PhotosCallBack> fetchPhotos(@Query("page") int page);

}
