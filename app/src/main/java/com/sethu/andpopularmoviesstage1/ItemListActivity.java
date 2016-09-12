package com.sethu.andpopularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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

    static HashMap<String,BeanMovies> moviesList= new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
//        moviesList();

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
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, moviesList.get(selectedMovie).getTitle());
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Intent intent = new Intent(ItemListActivity.this, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, moviesList.get(selectedMovie).getTitle());

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
        new FetchMoviesTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    public class FetchMoviesTask extends AsyncTask<Void, Void, HashMap<String,BeanMovies>> {
        private String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected HashMap<String,BeanMovies> doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            int numDays = 7;
            // Will contain the raw JSON response as a string.
            String movieslist = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
//                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7&appid=9299d60a7ad6bab48c85dcda4a90680a");
                String format = "json";
                String units = "metric";

                final String BASE_URL = "http://api.themoviedb.org/3/movie/popular";

                final String APPID_PARAM = "api_key";
                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY).build();

                URL url = new URL(builtUri.toString());
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
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
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
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
        protected void onPostExecute(final HashMap<String,BeanMovies> beanMovies) {
            super.onPostExecute(beanMovies);
//            ArrayAdapter adapter= new ArrayAdapter(getActivity(),R.layout.list_item_forecasr,R.id.list_item_forecast_textview,strings);
//            listView.setAdapter(adapter);
//            adapter.clear();
//            grid.setAdapter(adapter);
            moviesList=beanMovies;

            adapter = new CustomGridAdapter(ItemListActivity.this, moviesList);


            grid.setAdapter(adapter);

        }
    }


    private HashMap<String,BeanMovies> getMoviesDataFromJson(String moviesJsonStr)
            throws JSONException {
        HashMap<String,BeanMovies> moviesHashMap = new HashMap<>();
        // These are the names of the JSON objects that need to be extracted.
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
            moviesHashMap.put(beanMovies.getTitle(),beanMovies);
        }

        for (String s : moviesHashMap.keySet()) {
            Log.v(LOG_TAG, "Movies entry: " + s);
        }
        return moviesHashMap;

    }
}
