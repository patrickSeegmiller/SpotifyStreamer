package com.patrickseegmiller.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Fun with implementing the Parcelable interface!
 */
public class ArtistData implements Parcelable{

    protected String artistName;
    protected String artistID;
    protected String imageURL;

    public ArtistData(Artist artist){
        artistName = artist.name;
        artistID = artist.id;
        if(artist.images.size() > 0){
            imageURL = artist.images.get(0).url;
        } else {
            imageURL = null;
        }
    }

    public ArtistData(Parcel in){
        artistName = in.readString();
        artistID  = in.readString();
        imageURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artistName);
        dest.writeString(artistID);
        dest.writeString(imageURL);
    }

    public static final Parcelable.Creator<ArtistData> CREATOR
            = new Parcelable.Creator<ArtistData>() {
        public ArtistData createFromParcel(Parcel in) {
            return new ArtistData(in);
        }

        public ArtistData[] newArray(int size) {
            return new ArtistData[size];
        }
    };
}
