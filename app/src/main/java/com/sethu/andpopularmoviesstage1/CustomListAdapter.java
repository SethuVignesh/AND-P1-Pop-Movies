package com.sethu.andpopularmoviesstage1;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sethugayu on 10/5/16.
 */
public class CustomListAdapter extends BaseAdapter implements  RefreshGridView {

    private Context mContext;
    ArrayList<BeanReviews> reviewsArrayListList;


    public CustomListAdapter(Context c,ArrayList<BeanReviews> _reviewsArrayList) {
        mContext = c;
        reviewsArrayListList=_reviewsArrayList;

    }

    public int getCount() {
        return reviewsArrayListList.size();
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

            rootView = inflater.inflate(R.layout.review_row, null);
        } else {

            rootView =  convertView;
        }
        TextView tvAuthor=(TextView)rootView.findViewById(R.id.tvAuthor);
        TextView tvReviews=(TextView)rootView.findViewById(R.id.tvReview);

        tvAuthor.setText(reviewsArrayListList.get(position).getAuthor());
        tvReviews.setText(reviewsArrayListList.get(position).getContent());
//            TextView title=(TextView)rootView.findViewById(R.id.grid_text);
//            title.setText(moviesArrayListList.get(position).getTitle());






        return rootView;
    }
    @Override
    public void refreshAdapter() {

         notifyDataSetChanged();

    }

}

