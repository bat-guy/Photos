package com.example.photos.webservice;

import com.example.photos.Constants;
import com.example.photos.webservice.model.PhotosCallBack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class responsible for making the network calls
 */
public class WebServiceManager {
    private static Retrofit retrofit = null;

    /**
     * Method to set the Retrofit client
     * @return retrofit object
     */
    private static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    /**
     * Method to fetch the data from the API
     * @param resultInterface resultInterface object to carry the response back to MainActivity
     * @param pageNumber the pageNumber
     */
    public static void getData(final ResultInterface resultInterface, int pageNumber) {

        WebServiceInterface anInterface = getClient().create(WebServiceInterface.class);
        anInterface.fetchPhotos(pageNumber).enqueue(new Callback<PhotosCallBack>() {
            @Override
            public void onResponse(Call<PhotosCallBack> call, Response<PhotosCallBack> response) {
                PhotosCallBack data = response.body();
                resultInterface.onSuccess(data);
            }

            @Override
            public void onFailure(Call<PhotosCallBack> call, Throwable t) {
                resultInterface.onFailure();

                t.printStackTrace();
            }
        });

    }
}
