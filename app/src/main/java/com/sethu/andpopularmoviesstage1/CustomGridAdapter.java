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
    ArrayList<BeanMovies> moviesArrayListList;


    public CustomGridAdapter(Context c,ArrayList<BeanMovies> _moviesArrayList) {
        mContext = c;
        moviesArrayListList=_moviesArrayList;

    }

    public int getCount() {
        return moviesArrayListList.size();
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
        } else {

            rootView =  convertView;
        }

            ImageView imageView = (ImageView)rootView.findViewById(R.id.grid_image);
            Picasso.with(mContext).load(moviesArrayListList.get(position).getImage_url()).into(imageView);
            TextView title=(TextView)rootView.findViewById(R.id.grid_text);
            title.setText(moviesArrayListList.get(position).getTitle());






        return rootView;
    }

}