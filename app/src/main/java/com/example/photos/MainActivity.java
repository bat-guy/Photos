package com.example.photos;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.example.photos.databinding.ActivityMainBinding;
import com.example.photos.webservice.ResultInterface;
import com.example.photos.webservice.WebServiceManager;
import com.example.photos.webservice.model.Photo;
import com.example.photos.webservice.model.PhotosCallBack;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding mBinding;
    private RecyclerViewAdapter adapter;
    private boolean loading = true;
    private boolean isNextPage = false;
    private PhotosCallBack data;
    private int pageNo = 1;
    private boolean isFilterApplied = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        adapter = new RecyclerViewAdapter(this);
        initViews();
        fetchPhotos();
    }

    /**
     * Method to set initialize the views
     */
    private void initViews() {
        final android.support.v7.widget.GridLayoutManager layoutManager = new GridLayoutManager(
                this, 2, LinearLayoutManager.VERTICAL, false);
        mBinding.recyclerView.setLayoutManager(layoutManager);

        mBinding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //Hiding the keyboard and the searchBox when done button on the keyboard is pressed
                    Utils.hideKeyboard(MainActivity.this);
                    showHideSearchBox(false);
                }
                return false;
            }
        });

        mBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkTitles(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Setting up the onScrollListener on the recyclerView for pagination.
        mBinding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!isFilterApplied) {
                    if (dy > 0) {//check for scroll down

                        int visibleItemCount = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                        int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                        //Checking if it is still loading or not
                        if (loading)
                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                isNextPage = true;//setting it to true indicating that next page has to be fetched
                                loading = false;//Setting it to true indicating that page is loading
                                pageNo += 1;//Incrementing the page number
                                fetchPhotos();
                            }
                    }
                }
            }
        });
        mBinding.recyclerView.setAdapter(adapter);

        mBinding.ivSearch.setOnClickListener(this);
    }

    /**
     * Method to search for the characters typed in the searchBox in the titles
     *
     * @param s the character to be searched
     */
    private void checkTitles(CharSequence s) {

        ArrayList<Photo> photoArrayList = new ArrayList<>();
        for (int i = 0; i < data.getPhotos().getPhoto().size(); i++) {
            //Checking if the character is present in the given title or not
            //If yes, then it is added to the temporary arrayList
            if (Pattern.compile(Pattern.quote(s.toString()), Pattern.CASE_INSENSITIVE).matcher(
                    data.getPhotos().getPhoto().get(i).getTitle()).find()) {
                photoArrayList.add(data.getPhotos().getPhoto().get(i));
            }

        }

        //Setting the list in the adapter
        isFilterApplied = true;
        adapter.setDataList(photoArrayList);
        adapter.notifyDataSetChanged();


    }

    /**
     * Method to fetch the photos urls from the API
     */
    private void fetchPhotos() {
        //Checking if network is available or not
        if (Utils.isConnectingToInternet(this)) {
            mBinding.progressBar.setVisibility(View.VISIBLE);
            WebServiceManager.getData(new ResultInterface() {
                @Override
                public void onSuccess(PhotosCallBack data) {

                    mBinding.progressBar.setVisibility(View.GONE);
                    //Checking if the method is called while the recyclerView was scrolled to its last element
                    //If yes then the fetched data is added to the previous list.
                    //else it is passed on as it is.
                    if (!isNextPage) {
                        MainActivity.this.data = data;
                    } else {
                        for (int i = 0; i < data.getPhotos().getPhoto().size(); i++) {
                            MainActivity.this.data.getPhotos().getPhoto().add(data.getPhotos().getPhoto().get(i));
                        }
                        isNextPage = false;
                    }
                    loading = true;

                    adapter.setDataList((ArrayList<Photo>) MainActivity.this.data.getPhotos().getPhoto());
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure() {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.fetch_failed), Toast.LENGTH_SHORT).show();
                    mBinding.progressBar.setVisibility(View.GONE);
                    loading = true;
                }
            }, pageNo);
        } else {
            if (isNextPage) {
                loading = true;
                isNextPage = false;
            }
            Toast.makeText(this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method to show or hide the searchBox
     *
     * @param b boolean value to determine whether to show the screen or not. If true then yes else no
     */
    private void showHideSearchBox(boolean b) {
        if (b) {
            mBinding.etSearch.setVisibility(View.VISIBLE);
            mBinding.etSearch.requestFocus();
            Utils.openKeyboard(mBinding.etSearch, this);
            //Setting the plus image on the search imageView
            mBinding.ivSearch.setImageDrawable(Utils.setDrawable(this, R.drawable.ic_add_white_24dp));
            //Rotating it by 45 degree to make it cross
            mBinding.ivSearch.setRotation(45);

        } else {
            mBinding.etSearch.setVisibility(View.GONE);
            //Setting the plus image on the search imageView
            mBinding.ivSearch.setImageDrawable(Utils.setDrawable(this, R.drawable.ic_search_white_24dp));
            //Rotating it by 45 degree to make it cross
            mBinding.ivSearch.setRotation(0);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivSearch) {
            //Checking if the searchEditText is visible or not
            //If yes, then it is hidden
            if (mBinding.etSearch.getVisibility() == View.VISIBLE) {
                showHideSearchBox(false);
                Utils.hideKeyboard(MainActivity.this);
                //Checking if there is any text in the searchBox
                //If yes, then searchBox is cleared and recyclerView is reset
                if (isFilterApplied) {
                    mBinding.etSearch.setText("");
                    clearFilter();
                }
            } else {
                showHideSearchBox(true);
            }
        }
    }

    /**
     * Method to reset the data arrayList for the adapter.
     */
    private void clearFilter() {
        adapter.setDataList((ArrayList<Photo>) data.getPhotos().getPhoto());
        adapter.notifyDataSetChanged();
        isFilterApplied = false;
    }
}
