package com.sethu.andpopularmoviesstage1;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */

    private BeanMovies movie;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        BeanMovies movie= (BeanMovies)getArguments().getParcelable(ARG_ITEM_ID);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageViewPoster);
        Picasso.with(getActivity()).load(movie.getImage_url()).into(imageView);
        TextView releaseDate=(TextView)rootView.findViewById(R.id.textViewYear);
        TextView rating=(TextView)rootView.findViewById(R.id.textViewRating);
        TextView overView=(TextView)rootView.findViewById(R.id.textViewOverview);
        TextView movieLength=(TextView)rootView.findViewById(R.id.textViewMovieLength);
        TextView movieTitle=(TextView)rootView.findViewById(R.id.textViewTitle);
        releaseDate.setText(movie.getRelease_date());
        rating.setText(movie.getUser_rating());
        overView.setText(movie.getOverview());
        movieTitle.setText(movie.getTitle());
        return rootView;

    }
}
