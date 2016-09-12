package com.sethu.andpopularmoviesstage1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomGridAdapter extends BaseAdapter {
    private Context mContext;
    HashMap<String,BeanMovies> moviesList;

    private ArrayList<String> mKeys;

    public CustomGridAdapter(Context c,HashMap<String,BeanMovies> _moviesList) {
        mContext = c;
        moviesList=_moviesList;
        mKeys= new ArrayList<>(moviesList.keySet());
//        mKeys = moviesList.keySet().toArray(new String[_moviesList.size()]);
    }

    public int getCount() {
        return moviesList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {


        View rootView=null;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            rootView = inflater.inflate(R.layout.grid_single, null);

            ImageView imageView = (ImageView)rootView.findViewById(R.id.grid_image);
            Picasso.with(mContext).load(moviesList.get(mKeys.get(position)).getImage_url()).into(imageView);
            TextView title=(TextView)rootView.findViewById(R.id.grid_text);
            title.setText(moviesList.get(mKeys.get(position)).getTitle());


        } else {

            rootView = (View) convertView;
        }

        return rootView;
    }

}