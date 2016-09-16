package com.sethu.andpopularmoviesstage1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    GridView grid;
    private static String LOG_TAG = ItemListActivity.class.getSimpleName();
    CustomGridAdapter adapter;
    public static final String ARG_ITEM_ID = "item_id";

     ArrayList<BeanMovies> moviesList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        adapter = new CustomGridAdapter(ItemListActivity.this,moviesList);
        grid = (GridView) findViewById(R.id.gridView);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView text = (TextView) view.findViewById(R.id.grid_text);

                String selectedMovie = text.getText().toString();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(ItemDetailFragment.ARG_ITEM_ID, moviesList.get(position));
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Intent intent = new Intent(ItemListActivity.this, ItemDetailActivity.class);
                    intent.putExtra(ItemListActivity.ARG_ITEM_ID,moviesList.get(position));
                    startActivity(intent);
                }


            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }



    }

    private void fetchMovies() {
        if(Utils.isNetworkAvailable(ItemListActivity.this)){
        new FetchMoviesTask().execute();}else{
            AlertDialog adb = new AlertDialog.Builder(ItemListActivity.this).setTitle("Internet Error").setMessage("Please check your Internet connection")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    // .setNegativeButton(android.R.string.no, new
                    // DialogInterface.OnClickListener() {
                    // public void onClick(DialogInterface dialog,
                    // int which) {
                    // // do nothing
                    // }
                    // })
                    // .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            adb.setCancelable(false);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();


            inflater.inflate(R.menu.item_list, menu);


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

            if (item.getItemId() == R.id.action_settings) {
                Intent intent = new Intent(ItemListActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
                return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchMovies();
    }

    public class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<BeanMovies>> {
        private String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected ArrayList<BeanMovies> doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieslist = null;

            try {

                SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(ItemListActivity.this);
                String moviesType=sharedPreferences.getString(getString(R.string.sort_by_key),getString(R.string.default_value));
                String BASE_URL = "http://api.themoviedb.org/3/movie/"+moviesType;

                final String APPID_PARAM = "api_key";
                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY).build();

                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

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
                Log.v(LOG_TAG, "Popular Movies Json String " + movieslist);

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

            try {
                return getMoviesDataFromJson(movieslist);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(final ArrayList<BeanMovies> beanMovies) {
            super.onPostExecute(beanMovies);
            if(beanMovies!=null){
                moviesList.clear();
                moviesList=beanMovies;
                adapter = new CustomGridAdapter(ItemListActivity.this, moviesList);
                grid.setAdapter(adapter);


                //PROGRAMATICALLY CLICKING THE FIRST ELEMENT
                if(moviesList.size()>0 && mTwoPane){
                     grid.performItemClick(
                        grid.getAdapter().getView(0, null, null),
                        0,
                        grid.getAdapter().getItemId(0));}
            }else{
                //throw error dialog
                AlertDialog adb = new AlertDialog.Builder(ItemListActivity.this).setTitle("Error").setMessage("Please try again")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                fetchMovies();
                                dialog.dismiss();
                            }
                        })
                        .show();
                adb.setCancelable(false);
            }

        }
    }


    private ArrayList<BeanMovies> getMoviesDataFromJson(String moviesJsonStr)
            throws JSONException {
        ArrayList<BeanMovies> moviesHashMap = new ArrayList<>();
        final String MOVIES_RESULT = "results";
        final String MOVIES_ORIGINAL_TITLE = "original_title";
        final String MOVIES_POSTER_PATH = "poster_path";
        final String MOVIES_OVERVIEW = "overview";
        final String MOVIES_AVERAGE_RATE = "vote_average";
        final String MOVIES_RELEASE_DATE = "release_date";


        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray moviesArr = moviesJson.getJSONArray(MOVIES_RESULT);

        moviesHashMap.clear();
        for (int i = 0; i < moviesArr.length(); i++) {
            // For now, using the format "Day, description, hi/low"
            String title;
            String poster_url;
            String overview;
            String rating;
            String release_date;
            JSONObject movieJson = moviesArr.getJSONObject(i);

            title = movieJson.getString(MOVIES_ORIGINAL_TITLE);
            poster_url = "http://image.tmdb.org/t/p/w185/" + movieJson.getString(MOVIES_POSTER_PATH);
            overview = movieJson.getString(MOVIES_OVERVIEW);
            rating = movieJson.getString(MOVIES_AVERAGE_RATE);
            release_date = movieJson.getString(MOVIES_RELEASE_DATE);

            BeanMovies beanMovies = new BeanMovies();
            beanMovies.setTitle(title);
            beanMovies.setImage_url(poster_url);
            beanMovies.setOverview(overview);
            beanMovies.setUser_rating(rating);
            beanMovies.setRelease_date(release_date);
            moviesHashMap.add(beanMovies);
        }

        for (BeanMovies s : moviesHashMap) {
            Log.v(LOG_TAG, "Movies entry: " + s.getTitle());
        }
        return moviesHashMap;

    }
}
