package com.joyliu.instagramphotoviewer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        // SEND OUT API REQUEST TO POPULAR PHOTOS
        photos = new ArrayList<>();

        // CREATE THE ADAPTER AND LINK TO SOURCE
        aPhotos = new InstagramPhotosAdapter(this, photos);

        // FIND THE LISTVIEW FROM LAYOUT
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);

        // SET ADAPTER BINDING TO LISTVIEW
        lvPhotos.setAdapter(aPhotos);

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
            }

            // onFailure
            @Override
            public void onFailure(int StatusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });

    }
}
