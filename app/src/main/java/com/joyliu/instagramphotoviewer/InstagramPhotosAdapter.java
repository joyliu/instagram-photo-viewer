package com.joyliu.instagramphotoviewer;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

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
        TextView tvUser = (TextView) convertView.findViewById(R.id.tvUser);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        ImageView ivProfilePhoto = (ImageView) convertView.findViewById(R.id.ivProfilePhoto);

        // CONVERT CREATED TIME TO RELATIVE TIME
        CharSequence relativeTimeSpan = DateUtils.getRelativeTimeSpanString(photo.createdTime * 1000);

        // INSERT MODEL DATA INTO EACH VIEW ITEM
        tvUser.setText(photo.username);
        tvTime.setText(relativeTimeSpan);
        tvLikes.setText(photo.likesCount + " likes");
        if (photo.caption != null) {
            String formattedText = "<b>" + photo.username.toString() + "</b> " + photo.caption.toString();
            tvCaption.setText(Html.fromHtml(formattedText));
        }

        // ROUNDED IMAGE
        Transformation transformation = new RoundedTransformationBuilder()
                .borderWidthDp(1)
                .borderColor(Color.GRAY)
                .cornerRadiusDp(100)
                .oval(false)
                .build();

        // CLEAR OUT ANY EXISTING IMAGE, INSERT NEW IMAGE USING PICASSO
        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.imageUrl).placeholder(R.drawable.placeholder_photo).into(ivPhoto);
        ivProfilePhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.profileUrl).transform(transformation).placeholder(R.drawable.placeholder_profile).into(ivProfilePhoto);

        // RETURN CREATED ITEM AS A VIEW
        return convertView;
    }
}
