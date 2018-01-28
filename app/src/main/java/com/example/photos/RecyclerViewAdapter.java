package com.example.photos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.photos.webservice.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private ArrayList<Photo> photoArrayList = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_photo, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (photoArrayList.size() > 0) {
            Photo photo = photoArrayList.get(position);
            //The url of the image
            String url = "https://farm" + photo.getFarm() + ".staticflickr.com/" + photo.getServer() + "/" +
                    photo.getId() + "_" + photo.getSecret() + "_c.jpg";
            Picasso.with(context).load(url).into(holder.ivPhoto);
            holder.tvTitle.setText(photo.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return photoArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public ImageView ivPhoto;

        public MyViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
        }
    }

    /**
     * Method to set the datumArrayList to the adapter
     *
     * @param photoArrayList the datumArrayList
     */
    public void setDataList(ArrayList<Photo> photoArrayList) {
        this.photoArrayList = null;
        this.photoArrayList = photoArrayList;
    }
}
