package com.patrickseegmiller.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * I was happy someone pointed me toward this page (where I learned about View Holders):
 * http://developer.android.com/training/improving-layouts/smooth-scrolling.html
 */
public class TracksSearchAdapter extends ArrayAdapter<TrackData>{

    public TracksSearchAdapter(Context context, ArrayList<TrackData> tracks){
        super(context,0,tracks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_track, parent, false);

            viewHolder.albumName = (TextView) convertView.findViewById(R.id.album_name_textView);
            viewHolder.trackName = (TextView) convertView.findViewById(R.id.track_name_textView);
            viewHolder.albumImage = (ImageView) convertView.findViewById(R.id.album_cover_imageview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TrackData track = getItem(position);
        viewHolder.albumName.setText(track.albumTitle);
        viewHolder.trackName.setText(track.trackTitle);

        if(track.imageURL != null){
            Picasso.with(getContext()).load(track.imageURL).into(viewHolder.albumImage);
        } else {
            Picasso.with(getContext()).load(R.drawable.ic_content_block).into(viewHolder.albumImage);
        }

        return convertView;
    }

    private class ViewHolder{
        TextView albumName;
        TextView trackName;
        ImageView albumImage;
    }
}
