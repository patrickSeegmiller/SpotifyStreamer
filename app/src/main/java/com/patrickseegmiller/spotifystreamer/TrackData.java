package com.patrickseegmiller.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.Track;

/**
 * More fun with implementing the Parcelable interface!
 */
public class TrackData implements Parcelable {

    protected String albumTitle;
    protected String trackTitle;
    protected String imageURL;

    public TrackData(Track track){
        albumTitle = track.album.name;
        trackTitle = track.name;
        if(track.album.images.size() > 0){
            imageURL = track.album.images.get(0).url;
        } else {
            imageURL = null;
        }

    }

    public TrackData(Parcel in){
        albumTitle = in.readString();
        trackTitle = in.readString();
        imageURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(albumTitle);
        dest.writeString(trackTitle);
        dest.writeString(imageURL);
    }

    public static final Parcelable.Creator<TrackData> CREATOR
            = new Parcelable.Creator<TrackData>() {
        public TrackData createFromParcel(Parcel in) {
            return new TrackData(in);
        }

        public TrackData[] newArray(int size) {
            return new TrackData[size];
        }
    };
}
