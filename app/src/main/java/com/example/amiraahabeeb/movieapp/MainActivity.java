package com.example.amiraahabeeb.movieapp;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    // TODO - insert your themoviedb.org API KEY here
    private final static String API_KEY = "580f90d7628d7abbfba2cec11823fbcc";
    Detail_fragmentFragment detail_fragmentFragment;
    Mouvie_Database_helper helper;
    boolean largescreen;
    GridView gridview;
    Mouvie_parsing[] mouvie_parsing;
    View farg_detail;
    String state_last = "";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Toolbar myChildToolbar =
                (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);
        farg_detail = findViewById(R.id.fragment_detail);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


//        if (API_KEY.isEmpty()) {
//            Toast.makeText(getApplicationContext(), "Please obtain your API KEY from themoviedb.org first!", Toast.LENGTH_LONG).show();
//            return;
//        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Display display = getWindowManager().getDefaultDisplay();
        if (display.getWidth() > display.getHeight()) {
            View viewGroup = findViewById(R.id.fragment_detail);
            viewGroup.setVisibility(View.INVISIBLE);
            largescreen = true;
        } else {
            largescreen = false;
        }
        fragmentTransaction.commit();

        gridview = (GridView) findViewById(R.id.gridview);
//         gridview.setAdapter(new ImageAdapter(this,movies));
        if(networkConnectivity()){
            if (state_last.equals("top")) {
                FetchTask task = new FetchTask();
                task.execute("top_rated?");
            } else if (state_last.equals("favorite")) {
                DatabaseAdapter databaseAdapter = new DatabaseAdapter(getBaseContext());
                mouvie_parsing = databaseAdapter.getAllData();
                gridview.setAdapter(new ImageAdapter(getBaseContext(), mouvie_parsing));
            } else {
                FetchTask task = new FetchTask();
                task.execute("popular?");
            }
        }
        else{
            Toast.makeText(getBaseContext(), "No Network Available", Toast.LENGTH_LONG).show();
        }
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (largescreen == false) {
                    Detail_fragment.mouvie_parsing = mouvie_parsing[position];
                    Detail_fragmentFragment.mouvie_parsing = mouvie_parsing[position];
                    startActivity(new Intent(MainActivity.this, Detail_fragment.class));

                    //
//                    Detail_fragment.Detail_data();
                } else {
                    Detail_fragmentFragment.mouvie_parsing = mouvie_parsing[position];
                    //
                    Detail_fragmentFragment.Detail_data();
                    farg_detail.setVisibility(View.VISIBLE);


                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_most_popular) {
            FetchTask task = new FetchTask();
            task.execute("popular?");
            state_last = "";
            return true;
        }
        if (id == R.id.action_top_rated) {
            FetchTask task = new FetchTask();
            task.execute("top_rated?");
            state_last = "top";
            return true;
        }
        if (id == R.id.action_favorites) {
            DatabaseAdapter databaseAdapter = new DatabaseAdapter(getBaseContext());
            mouvie_parsing = databaseAdapter.getAllData();
            gridview.setAdapter(new ImageAdapter(getBaseContext(), mouvie_parsing));
            state_last = "favorite";

            return true;

        }
        return true;
    }
    private boolean networkConnectivity() {
        ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public class FetchTask extends AsyncTask<String, Void, Mouvie_parsing[]> {
        private final String LOG_TAG = FetchTask.class.getSimpleName();


        private Mouvie_parsing[] getWeatherDataFromJson(String jsonStr)
                throws JSONException {
            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "results";
            final String poster_path = "poster_path";
            final String overview = "overview";
            final String release_date = "release_date";
            final String original_title = "original_title";
            final String popularity = "popularity";
            final String id = "id";


            JSONObject movieJson = new JSONObject(jsonStr);
            JSONArray movieArray = movieJson.getJSONArray(OWM_LIST);

            mouvie_parsing = new Mouvie_parsing[movieArray.length()];


            for (int i = 0; i < movieArray.length(); i++) {
                mouvie_parsing[i] = new Mouvie_parsing();
                JSONObject movie = movieArray.getJSONObject(i);
                mouvie_parsing[i].setOriginal_title(movie.getString(original_title));
                mouvie_parsing[i].setOverview(movie.getString(overview));
                mouvie_parsing[i].setPoster_path(movie.getString(poster_path));
                mouvie_parsing[i].setRelease_date(movie.getString(release_date));
                mouvie_parsing[i].setPopularity(movie.getString(popularity));
                mouvie_parsing[i].setid(movie.getString(id));
//                mouvie_parsing[i].setContent(movie.getString(content));
            }

            return mouvie_parsing;

        }

        @Override
        protected Mouvie_parsing[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;

            try {

                final String FORECAST_BASE_URL =
                        "http://api.themoviedb.org/3/movie/" + params[0];
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, "580f90d7628d7abbfba2cec11823fbcc")
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

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
                movieJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
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
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getWeatherDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Mouvie_parsing[] result) {
            gridview.setAdapter(new ImageAdapter(getBaseContext(), mouvie_parsing));

        }
    }
}
