package com.example.photos.webservice;


import com.example.photos.webservice.model.PhotosCallBack;

/**
 * Interface used to fetch the result of the API
 */
public interface ResultInterface {
    /**
     * Method called when the API hit is successful
     * @param data the PhotoCallBack object
     */
    void onSuccess(PhotosCallBack data);

    /**
     * Method called when the API hit is unsuccessful
     */
    void onFailure();
}
