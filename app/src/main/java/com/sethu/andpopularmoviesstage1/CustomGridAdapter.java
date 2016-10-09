package com.sethu.andpopularmoviesstage1;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
    SharedPreferences sharedPreferences;



    public CustomGridAdapter(Context c,ArrayList<BeanMovies> _moviesArrayList) {
        mContext = c;
        moviesArrayListList=_moviesArrayList;
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(mContext);

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
            ImageView imageViewFav = (ImageView)rootView.findViewById(R.id.imageViewFav);
            HashMap<String,BeanMovies> favList=Utils.getFavoritesList(mContext);
        if(favList.containsKey(moviesArrayListList.get(position).getId())){
            imageViewFav.setVisibility(View.VISIBLE);
        }else{
            imageViewFav.setVisibility(View.GONE);
        }
        String moviesType=sharedPreferences.getString(mContext.getString(R.string.sort_by_key),mContext.getString(R.string.default_value));

        if(moviesType.equalsIgnoreCase(mContext.getResources().getString(R.string.favorites))) {
            imageViewFav.setVisibility(View.GONE);
        }
            Picasso.with(mContext).load(moviesArrayListList.get(position).getImage_url()).into(imageView);
//            TextView title=(TextView)rootView.findViewById(R.id.grid_text);
//            title.setText(moviesArrayListList.get(position).getTitle());






        return rootView;
    }


}