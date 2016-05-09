package com.example.daniel.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by daniel on 5/1/2016.
 */
public class ParseData {
    private final String LOG_TAG = getClass().getSimpleName();
    JSONObject rootObj = null;

    public ParseData(String jsonStr) throws JSONException {
        rootObj = new JSONObject(jsonStr);
    }

    public HashMap<Integer,String> getData() throws JSONException {

        JSONArray results = rootObj.getJSONArray("results");

        HashMap<Integer,String> finalResult = new HashMap<>(results.length());

        int id = 0;
        String poster_path = null;

        for(int i = 0; i < results.length(); i++){
            id = results.getJSONObject(i).getInt("id");
            poster_path = results.getJSONObject(i).getString("poster_path");

            finalResult.put(id,poster_path);
        }


        return finalResult;
    }

    public void getMoiveDetailsFromJson(Context ctx) throws JSONException {

        ImageView backdrop_img = (ImageView) ((Activity)ctx).findViewById(R.id.backdrop_img);
        TextView title = (TextView) ((Activity)ctx).findViewById(R.id.movie_title);
        TextView released_date = (TextView) ((Activity)ctx).findViewById(R.id.released_date);
        TextView average_vote = (TextView) ((Activity)ctx).findViewById(R.id.average_vote);
        TextView synopsis = (TextView) ((Activity)ctx).findViewById(R.id.synopsis);

        String backdrop_img_path = rootObj.getString("backdrop_path");
        Picasso.with(ctx).load(Uri.parse("http://image.tmdb.org/t/p/w500/" + backdrop_img_path)).into(backdrop_img);
        Picasso.with(ctx).setLoggingEnabled(true);

        title.setText("Name: "+rootObj.getString("original_title"));

        ((Activity)ctx).setTitle(rootObj.getString("original_title"));

        released_date.setText("Released on: " + rootObj.getString("release_date"));
        average_vote.setText("Average vote: " + rootObj.getString("vote_average"));
        synopsis.setText("Synopsis: "+rootObj.getString("overview"));
    }
}
