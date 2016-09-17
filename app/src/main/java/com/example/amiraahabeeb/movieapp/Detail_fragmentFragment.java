package com.example.amiraahabeeb.movieapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class Detail_fragmentFragment extends Fragment {
    //    Mouvie_Database_helper helper;
    DatabaseAdapter databaseAdapter;
    static TextView title;
    static TextView vote;
    static TextView originaltitle;
    static TextView release_date;
    static ImageView posterpath_image;
    static String poster_path;
    static Mouvie_parsing mouvie_parsing;
    static ListView trailer;
    static ListView reviews;
    Button favorite;
    static ArrayAdapter<String> traileradapter;
    static ArrayAdapter<String> reviewsadapter;
    static Context context;

    static String[] keyStr;
    static String[] contentstr;
    static View view;

    public Detail_fragmentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_detail_fragment, container, false);
        title = (TextView) view.findViewById(R.id.title_a);
        release_date = (TextView) view.findViewById(R.id.date);
        originaltitle = (TextView) view.findViewById(R.id.original);
        vote = (TextView) view.findViewById(R.id.vote_average);
        posterpath_image = (ImageView) view.findViewById(R.id.poster_image);
        context = getContext();

        reviews = (ListView) view.findViewById(R.id.reviews_list);
        trailer = (ListView) view.findViewById(R.id.trailer_list);
        trailer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + keyStr[position]));
                startActivity(intent);
            }
        });
        favorite = (Button) view.findViewById(R.id.Favorite);
        favorite.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            databaseAdapter = new DatabaseAdapter(context);

                                            if (databaseAdapter.getData(mouvie_parsing.getid()) == null) {
                                                long insrt = databaseAdapter.insertData(mouvie_parsing);
                                                if (insrt != -1) {
                                                    Toast.makeText(context, "Favorite", Toast.LENGTH_SHORT).show();
                                                } else
                                                    Toast.makeText(context, "Error In Insert Data", Toast.LENGTH_SHORT).show();
                                            } else

                                            {
                                                Toast.makeText(context, "Favorite Already", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

        );

        return view;
    }


    public static class FetchTask extends AsyncTask<String, Void, String[]> {
        private final String LOG_TAG = FetchTask.class.getSimpleName();


        private String[] getmouvierDataFromJson(String jsonStr)
                throws JSONException {
            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "results";
            final String key = "key";


            JSONObject movieJson = new JSONObject(jsonStr);
            JSONArray movieArray = movieJson.getJSONArray(OWM_LIST);

            keyStr = new String[movieArray.length()];
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movie = movieArray.getJSONObject(i);
                keyStr[i] = movie.getString(key);
            }

            return keyStr;
        }


        @Override
        protected String[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;

            try {

                final String FORECAST_BASE_URL =
                        "http://api.themoviedb.org/3/movie/" + params[0] + "/videos?";
                final String APPID_PARAM = "api_key";


                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, "580f90d7628d7abbfba2cec11823fbcc")
                        .build();

                URL url = null;
                try {
                    url = new URL(builtUri.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    urlConnection.setRequestMethod("GET");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
                try {
                    urlConnection.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
                return getmouvierDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            String trail[] = new String[result.length];
            for (int i = 0; i<result.length;i++){
                trail[i] = "Trailer "+(i+1);
            }
            ArrayList<String> m = new ArrayList<String>(Arrays.asList(trail));
            traileradapter = new ArrayAdapter<String>(
                    context, // The current context (this activity)
                    android.R.layout.simple_list_item_1,
                    m);
            trailer.setAdapter(traileradapter);

        }
    }

    public static void Detail_data() {
        if (title != null) {
            title.setText(mouvie_parsing.getOriginal_title());
            vote.setText(mouvie_parsing.getPopularity());
            originaltitle.setText(mouvie_parsing.getOriginal_title());
            release_date.setText(mouvie_parsing.getRelease_date());
            poster_path = mouvie_parsing.getPoster_path();
            FetchTask ask = new FetchTask();
            ask.execute(mouvie_parsing.getid());

            FetchReviewTask reviewTask = new FetchReviewTask();
            reviewTask.execute(mouvie_parsing.getid());

            Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + poster_path).into(posterpath_image);
        } else
            Toast.makeText(context, "ERRRRRRRRRRRRRRRO", Toast.LENGTH_SHORT).show();
    }

    public static class FetchReviewTask extends AsyncTask<String, Void, String[]> {
        private final String LOG_TAG = FetchTask.class.getSimpleName();


        private String[] getmouvierDataFromJson(String jsonStr)
                throws JSONException {
            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "results";
            final String content = "content";


            JSONObject movieJson = new JSONObject(jsonStr);
            JSONArray movieArray = movieJson.getJSONArray(OWM_LIST);

            contentstr = new String[movieArray.length()];
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movie = movieArray.getJSONObject(i);
                contentstr[i] = movie.getString(content);
            }

            return contentstr;
        }


        @Override
        protected String[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;

            try {

                final String FORECAST_BASE_URL =
                        "http://api.themoviedb.org/3/movie/" + params[0] + "/reviews?";
                final String APPID_PARAM = "api_key";


                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, "580f90d7628d7abbfba2cec11823fbcc")
                        .build();

                URL url = null;
                try {
                    url = new URL(builtUri.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    urlConnection.setRequestMethod("GET");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
                try {
                    urlConnection.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
                return getmouvierDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            ArrayList<String> mm = new ArrayList<String>(Arrays.asList(result));
            reviewsadapter = new ArrayAdapter<String>(
                    context, // The current context (this activity)
                    android.R.layout.simple_list_item_1,
                    mm);
            reviews.setAdapter(reviewsadapter);

        }
    }


}




