package com.joyliu.instagramphotoviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by joy.liu on 12/2/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    // DATA NEEDED FOR THE ACTIVITY
    // Context, Data source
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    // WHAT AN ITEM LOOKS LIKE
    // USE TEMPLATE TO DISPLAY EACH POST
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // GET DATA ITEM FOR POSITION
        InstagramPhoto photo = getItem(position);

        // CHECK IF USING RECYCLED VIEW, IF NOT INFLATE
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }

        // LOOK UP VIEWS FOR POPULATING DATA
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);

        // INSERT MODEL DATA INTO EACH VIEW ITEM
        tvCaption.setText(photo.caption);

        // CLEAR OUT ANY EXISTING IMAGE, INSERT NEW IMAGE USING PICASSO
        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);

        // RETURN CREATED ITEM AS A VIEW
        return convertView;
    }
}
