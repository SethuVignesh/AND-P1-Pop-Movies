package com.sethu.andpopularmoviesstage1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

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
    RefreshGridView refreshGridView=new ItemListActivity();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }
    View rootView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.item_detail, container, false);

         movie= (BeanMovies)getArguments().getParcelable(ARG_ITEM_ID);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageViewPoster);
        Picasso.with(getActivity()).load(movie.getImage_url()).into(imageView);

        TextView releaseDate=(TextView)rootView.findViewById(R.id.textViewYear);
        TextView rating=(TextView)rootView.findViewById(R.id.textViewRating);
        TextView overView=(TextView)rootView.findViewById(R.id.textViewOverview);
        TextView movieLength=(TextView)rootView.findViewById(R.id.textViewMovieLength);
        TextView movieTitle=(TextView)rootView.findViewById(R.id.textViewTitle);
        ImageView backButton=(ImageView)rootView.findViewById(R.id.ivBack);
        final ImageView favorites=(ImageView)rootView.findViewById(R.id.ivFavorites);
        Button markAsFav=(Button)rootView.findViewById(R.id.buttonFavorite);
        final HashMap<String,BeanMovies> favoritesList= Utils.getFavoritesList(getActivity());

        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favoritesList.remove(movie.getId());
                Utils.saveInSharedPref(favoritesList,getActivity());
                favorites.setVisibility(View.GONE);
                refreshGridView.refreshAdapter();
            }
        });
        markAsFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                favoritesList.put(movie.getId(),movie);
                Utils.saveInSharedPref(favoritesList,getActivity());
                favorites.setVisibility(View.VISIBLE);
                refreshGridView.refreshAdapter();
            }
        });


        if(favoritesList.containsKey(movie.getId())){
            favorites.setVisibility(View.VISIBLE);
        }else{
            favorites.setVisibility(View.GONE);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        backButton.setVisibility(View.GONE);
        releaseDate.setText(movie.getRelease_date());
        rating.setText(movie.getUser_rating());
        overView.setText(movie.getOverview());
        movieTitle.setText(movie.getTitle());
        if(Utils.isNetworkAvailable(getActivity())){
        new FetchReview().execute();}else{
            AlertDialog adb = new AlertDialog.Builder(getActivity()).setTitle("Internet Error").setMessage("Please check your Internet connection")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            adb.setCancelable(false);
        }
        return rootView;

    }

    public class FetchReview extends AsyncTask<Void, Void, BeanMovies> {
        private String LOG_TAG = FetchReview.class.getSimpleName();
        ArrayList<BeanReviews> beanReviewsArrayList=new ArrayList<>();

        @Override
        protected BeanMovies doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            HttpURLConnection urlConnectionReview = null;
            BufferedReader readerReview = null;

            String movieslist = null;
            String moviesListReview=null;

            try {

                SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
                String moviesType=sharedPreferences.getString(getString(R.string.sort_by_key),getString(R.string.default_value));
//                http://api.themoviedb.org/3/movie/333484/videos?api_key=fe235de65f31a04c729abbe71fc33ac3
                String BASE_URL = "http://api.themoviedb.org/3/movie/"+movie.getId()+"/videos";
                String BASE_URL_REVIEWS = "http://api.themoviedb.org/3/movie/"+movie.getId()+"/reviews";

                final String APPID_PARAM = "api_key";
                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY).build();
                Uri builtUriReviews = Uri.parse(BASE_URL_REVIEWS).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY).build();


                URL url = new URL(builtUri.toString());
                URL urlReviews = new URL(builtUriReviews.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                urlConnectionReview = (HttpURLConnection) urlReviews.openConnection();
                urlConnectionReview.setRequestMethod("GET");
                urlConnectionReview.connect();


                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieslist = buffer.toString();
                Log.v(LOG_TAG, "Trailer Json String " + movieslist);

                // Read the input stream into a String
                InputStream inputStreamReview = urlConnectionReview.getInputStream();
                StringBuffer bufferReview = new StringBuffer();
                if (inputStreamReview == null) {
                    return null;
                }
                readerReview = new BufferedReader(new InputStreamReader(inputStreamReview));

                String lineReview;
                while ((lineReview = readerReview.readLine()) != null) {
                    bufferReview.append(lineReview + "\n");
                }

                if (bufferReview.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesListReview = bufferReview.toString();
                Log.v(LOG_TAG, "Reviews Json String " + moviesListReview);


            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            {
                if (urlConnectionReview != null) {
                    urlConnectionReview.disconnect();
                }
                if (readerReview != null) {
                    try {
                        readerReview.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            try {
                return getTrailerDataFromJson(movieslist,moviesListReview);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(final BeanMovies beanMovies) {
            super.onPostExecute(beanMovies);

            if(beanMovies!=null){
                if(rootView!=null) {
                    ImageView imageViewTrailer = (ImageView) rootView.findViewById(R.id.ivThumbNail);
                    Picasso.with(getActivity()).load("http://img.youtube.com/vi/"+beanMovies.getTrailer_key()+"/0.jpg").into(imageViewTrailer);
              imageViewTrailer.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+beanMovies.getTrailer_key())));
                  }
              });
                }
                if(beanReviewsArrayList.isEmpty()==false){
                    ListView reviewsList=(ListView)rootView.findViewById(R.id.listViewReviews);
                    reviewsList.setAdapter( new CustomListAdapter(getActivity(),beanReviewsArrayList));
                    final ScrollView mainScrollView= (ScrollView)rootView.findViewById(R.id.scrollView);
//                    mainScrollView.fullScroll(ScrollView.FOCUS_UP);
//                    mainScrollView.scrollTo(0, mainScrollView.getTop());
//                    mainScrollView.post(new Runnable() {
//                        public void run() {
//                            mainScrollView.fullScroll(mainScrollView.FOCUS_DOWN);
//                        }
//                    });
                }

            }

            //todo populate results
            else{
                //throw error dialog
                AlertDialog adb = new AlertDialog.Builder(getActivity()).setTitle("Error").setMessage("Please try again")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                               new FetchReview().execute();
                                dialog.dismiss();
                            }
                        })
                        .show();
                adb.setCancelable(false);
            }

        }
        private BeanMovies getTrailerDataFromJson(String trailerString,String reviewString)
                throws JSONException {
            Log.d(LOG_TAG,"trailerString"+trailerString+"\n"+"reviewString"+reviewString);

            ArrayList<BeanMovies> moviesHashMap = new ArrayList<>();
            final String TRAILER_TYPE = "type";
            final String TRAILER_KEY = "key";
            final String REVIEW_RESULTS = "results";
            final String REVIEW_ID = "id";
            final String REVIEW_CONTENT = "content";
            final String REVIEW_AUTHOR = "author";
            final String MOVIES_RESULTS = "results";
//            final String MOVIES_ID = "id";
//
//
            JSONObject trailerJson = new JSONObject(trailerString);
            JSONObject reviewJson = new JSONObject(reviewString);
            JSONArray trailerArr = trailerJson.getJSONArray(MOVIES_RESULTS);
            JSONArray reviewArr = reviewJson.getJSONArray(MOVIES_RESULTS);
//
//            moviesHashMap.clear();
            for (int i = 0; i < trailerArr.length(); i++) {
                // For now, using the format "Day, description, hi/low"
                String type;
                String trailerKey;

                 JSONObject trailerArrJSONObject = trailerArr.getJSONObject(i);

                type = trailerArrJSONObject.getString(TRAILER_TYPE);
                trailerKey = trailerArrJSONObject.getString(TRAILER_KEY);
            if(type.equals("Trailer")){
                movie.setTrailer_key(trailerKey);
            }
            }
            beanReviewsArrayList.clear();
            BeanReviews beanReviews;
            for (int i = 0; i < reviewArr.length(); i++) {

                String reviewResults;
                String reviewId;
                String reviewContent;
                String reviewAuthor;
                 JSONObject reviewArrJSONObject = reviewArr.getJSONObject(i);

//                reviewResults = reviewArrJSONObject.getString(REVIEW_RESULTS);
                reviewId = reviewArrJSONObject.getString(REVIEW_ID);
                reviewContent = reviewArrJSONObject.getString(REVIEW_CONTENT);
                reviewAuthor=reviewArrJSONObject.getString(REVIEW_AUTHOR);
                beanReviews=new BeanReviews();
                beanReviews.setAuthor(reviewAuthor);
                beanReviews.setContent(reviewContent);
                beanReviews.setId(reviewId);
                beanReviewsArrayList.add(beanReviews);


            }
            movie.setBeanReviews(beanReviewsArrayList);
//
            for (BeanMovies s : moviesHashMap) {
                Log.v(LOG_TAG, "Movies entry: " + s.getTitle());
            }
            return movie;

        }
    }



}
