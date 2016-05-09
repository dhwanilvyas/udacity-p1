package com.example.daniel.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by daniel on 5/1/2016.
 */
public class MovieDetail extends AppCompatActivity{
    private final String LOG_TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        FetchMovieDetails fetchMovieDetails = new FetchMovieDetails();
        AsyncTask<String, Void, String> movieDetailsJson = fetchMovieDetails.execute(getIntent().getStringExtra("id"));


        try {
            new ParseData(movieDetailsJson.get()).getMoiveDetailsFromJson(this);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sort_fragment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class FetchMovieDetails extends AsyncTask<String,Void,String>{

        final private String API_KEY = "7b7239657b20c59003be4fdd339956cf";

        final private String LOG_TAG = getClass().getSimpleName();

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            String movieDetailsJson = null;

            try{
                String BASE_URL = "http://api.themoviedb.org/3/movie/"+params[0]+"?api_key="+API_KEY;
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
                String line = null;
                while((line = bufferedReader.readLine()) != null){
                    stringBuffer.append(line + "\n");
                }

                if(stringBuffer.length() == 0){
                    Log.d(LOG_TAG,"string buffer empty");
                    return null;
                }

                movieDetailsJson = stringBuffer.toString();
                Log.d(LOG_TAG, movieDetailsJson);

                return movieDetailsJson;

            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "malformed url : " + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(LOG_TAG, "IO : " + e.getMessage());
                e.printStackTrace();
            }

            return null;
        }
    }
}
