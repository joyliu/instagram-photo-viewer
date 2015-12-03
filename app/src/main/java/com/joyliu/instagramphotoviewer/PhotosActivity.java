package com.joyliu.instagramphotoviewer;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeContainer;

    public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        // CREATE THE ADAPTER AND LINK TO SOURCE
        photos = new ArrayList<>();
        aPhotos = new InstagramPhotosAdapter(this, photos);

        // LINK VIEWS FROM LAYOUT
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // SET ADAPTER BINDING TO LISTVIEW
        lvPhotos.setAdapter(aPhotos);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchPopularPhotos();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // FETCH POPULAR PHOTOS
        fetchPopularPhotos();
    }

    // TRIGGER API REQUEST
    public void fetchPopularPhotos() {

        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, null, new JsonHttpResponseHandler() {

            // onSuccess
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                aPhotos.clear();

                // ITERATE EACH PHOTO ITEM INTO A JAVA OBJECT
                JSONArray photosJSON;
                try {
                    photosJSON = response.getJSONArray("data"); // ARRAY OF POSTS
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        // Author Name: { "data" => [x] => "user" => "username" }
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        photo.profileUrl = photoJSON.getJSONObject("user").getString("profile_picture");
                        // Caption: { "data" => [x] => "caption" }
                        if (photoJSON.optJSONObject("caption") != null) {
                            photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        }
                        // URL: { "data" => [x] => "images" => "standard_resolution" => "url" }
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        photo.createdTime = photoJSON.getLong("created_time");


                        // ADD TO ARRAY
                        photos.add(photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // CALLBACK
                aPhotos.notifyDataSetChanged();

                // STOP SWIPE REFRESH
                swipeContainer.setRefreshing(false);
            }

            // onFailure
            @Override
            public void onFailure(int StatusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", "Error: " + throwable.toString());
            }
        });
    }
}
