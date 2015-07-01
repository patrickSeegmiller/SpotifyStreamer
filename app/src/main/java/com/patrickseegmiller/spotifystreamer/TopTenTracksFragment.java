package com.patrickseegmiller.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import kaaes.spotify.webapi.android.models.TracksPager;

/**
 * Once again, most of the code here is an extension of ideas I picked up working on
 * Sunshine. And again, the Parcelable stuff I learned on this video that someone posted
 * in the Discussion board (and a few visits to
 * the documentation): https://www.youtube.com/watch?v=qIhwPaa6rlU
 */
public class TopTenTracksFragment extends Fragment {

    private TracksSearchAdapter mTracksSearchAdapter;
    private ArrayList<TrackData> mTrackDataList;
    private static final String TRACK_KEY = "track_key";

    public TopTenTracksFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mTrackDataList != null){
            outState.putParcelableArrayList(TRACK_KEY,mTrackDataList);
        }
        super.onSaveInstanceState(outState);
    }

    private void retrieveTracks(String artistID){
        RetrieveTracksTask retrieveTracksTask = new RetrieveTracksTask();
        retrieveTracksTask.execute(artistID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top_ten_tracks, container,false);
        
        if(savedInstanceState != null){
            mTrackDataList = savedInstanceState.getParcelableArrayList(TRACK_KEY);
            mTracksSearchAdapter = new TracksSearchAdapter(
                    getActivity(),
                    mTrackDataList
            );
        } else {
            mTrackDataList = new ArrayList<>();
            mTracksSearchAdapter = new TracksSearchAdapter(
                    getActivity(),
                    new ArrayList<TrackData>()
            );
        }
        ListView listView = (ListView) rootView.findViewById(R.id.top_ten_tracks_listView);
        listView.setAdapter(mTracksSearchAdapter);

        // We need to get the artist.id String from the Intent
        Intent intent = getActivity().getIntent();
        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            String artistID = intent.getStringExtra(Intent.EXTRA_TEXT);
            retrieveTracks(artistID);
        }

        return rootView;
    }

    /**
     * My AsyncTasks were structured after the one in Sunshine
     */
    private class RetrieveTracksTask extends AsyncTask<String,Void,Tracks>{

        protected Tracks doInBackground(String... params){
            if(params.length == 0){
                return null;
            }


            // Someone posted this little snippet in Discussions.
            // https://discussions.udacity.com/t/finding-top-tracks/21321/2
            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService spotifyService = spotifyApi.getService();
            // .. But, I wonder if there is a better way to do this...
            Map<String,Object> options = new HashMap<>();
            options.put(SpotifyService.COUNTRY, Locale.getDefault().getCountry());

            return spotifyService.getArtistTopTrack(params[0],options);
        }

        @Override
        protected void onPostExecute(Tracks tracks) {
            super.onPostExecute(tracks);

            if(tracks.tracks != null){
                mTracksSearchAdapter.clear();
                mTrackDataList = new ArrayList<>();
                for(Track track : tracks.tracks){
                    mTracksSearchAdapter.add(new TrackData(track));
                    mTrackDataList.add(new TrackData(track));
                }

                mTracksSearchAdapter.notifyDataSetChanged();

                if(tracks.tracks.size() == 0){
                    Context context = getActivity().getApplicationContext();
                    CharSequence text = getString(R.string.no_tracks);
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context,text,duration);
                    toast.show();
                }
            }
        }
    }
}
