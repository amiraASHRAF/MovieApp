package com.example.amiraahabeeb.movieapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Amira A. habeeb on 10/08/2016.
 */
public class ImageAdapter extends BaseAdapter {
    Mouvie_parsing[] POSTER_MOVIES;
    private Context mContext;

    public ImageAdapter(Context c, Mouvie_parsing[] POSTER_MOVIES) {
        mContext = c;
        this.POSTER_MOVIES = POSTER_MOVIES;
    }

    public int getCount() {
        return POSTER_MOVIES.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);

            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1, 1, 1, 1);
        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/" + POSTER_MOVIES[position].getPoster_path()).into(imageView);
        //  imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    //  public class MovieViewHolder extends Re{
    //    ImageView poster_image;

    //   public MovieViewHolder(View view) {
    //       super(view);
    //      poster_image=//find view
    //    }
    // }
}
