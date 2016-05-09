package com.example.daniel.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by daniel on 5/9/2016.
 */
class FetchMovies extends AsyncTask<String, Void, HashMap> {

    private final String LOG_TAG = FetchMovies.class.getSimpleName();
    private final String API_KEY = "7b7239657b20c59003be4fdd339956cf";

    private Context context;

    @Override
    protected HashMap<Integer,String> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;

        String movieJsonStr = null;

        try{
            final String BASE_URL = "http://api.themoviedb.org/3/movie/"+params[0]+"?api_key="+API_KEY;

            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            if(inputStream == null){
                Log.d(LOG_TAG, "input stream empty");
                return null;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line + "\n");
            }

            if(stringBuffer.length() == 0){
                Log.d(LOG_TAG,"string buffer empty");
                return null;
            }

            movieJsonStr = stringBuffer.toString();
            Log.d(LOG_TAG,movieJsonStr);

        }
        catch (Exception e){
            Log.d(LOG_TAG,e.getMessage());
            e.printStackTrace();
        }

        try {
            return (new ParseData(movieJsonStr).getData());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}