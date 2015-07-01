package com.patrickseegmiller.spotifystreamer;

import android.content.Context;
import android.util.Log;
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
public class ArtistSearchAdapter extends ArrayAdapter<ArtistData>{

    public ArtistSearchAdapter(Context context, ArrayList<ArtistData> artists){
        super(context,0,artists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_artist, parent, false);

            viewHolder.artistName = (TextView) convertView.findViewById(R.id.artist_name_textView);
            viewHolder.artistImage = (ImageView) convertView.findViewById(R.id.artist_imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ArtistData artist = getItem(position);
        viewHolder.artistName.setText(artist.artistName);

        if(artist.imageURL != null){
            Picasso.with(getContext()).load(artist.imageURL).into(viewHolder.artistImage);
        } else {
            Picasso.with(getContext()).load(R.drawable.ic_content_block).into(viewHolder.artistImage);
        }

        return convertView;
    }

    private class ViewHolder {
        TextView artistName;
        ImageView artistImage;
    }
}
