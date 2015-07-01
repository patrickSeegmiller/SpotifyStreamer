package com.patrickseegmiller.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Artists;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Most of the code here is based off of Sunshine. I based the material regarding Parcelable
 * on this video that someone posted in the Discussion board:
 * https://www.youtube.com/watch?v=qIhwPaa6rlU
 */
public class MainActivityFragment extends Fragment {

    private ArtistSearchAdapter mArtistSearchAdapter;
    private ArrayList<ArtistData> mArtistDataList;
    private static final String ARTIST_KEY = "artist_log_key";

    public MainActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mArtistDataList != null){
            outState.putParcelableArrayList(ARTIST_KEY,mArtistDataList);
        }
        super.onSaveInstanceState(outState);
    }

    private void searchArtist(String artistName){
        SearchArtistTask searchArtistTask = new SearchArtistTask();
        searchArtistTask.execute(artistName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main,container,false);

        if(savedInstanceState != null) {
            mArtistDataList = savedInstanceState.getParcelableArrayList(ARTIST_KEY);
            mArtistSearchAdapter = new ArtistSearchAdapter(
                    getActivity(),
                    mArtistDataList
            );
        } else {
            mArtistDataList = new ArrayList<>();
            mArtistSearchAdapter = new ArtistSearchAdapter(
                    getActivity(),
                    new ArrayList<ArtistData>()
            );
        }
        final ListView listView = (ListView) rootView.findViewById(R.id.artist_listView);
        listView.setAdapter(mArtistSearchAdapter);

        // For responding to clicking Items in our ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArtistData artist = mArtistSearchAdapter.getItem(position);
                String artistID = artist.artistID;

                Intent intent = new Intent(getActivity(),
                        TopTenTracks.class).putExtra(Intent.EXTRA_TEXT,artistID);
                startActivity(intent);
            }
        });

        // We need to be able to respond to user input in the EditText
        final EditText editText = (EditText) rootView.findViewById(R.id.artist_search_editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchArtist(editText.getText().toString());
                listView.smoothScrollToPosition(0);
                return false;
            }
        });

        return rootView;
    }

    /**
     * My AsyncTasks were structured after the one in Sunshine
     */
    private class SearchArtistTask extends AsyncTask<String, Void, ArtistsPager> {

        @Override
        protected ArtistsPager doInBackground(String... params) {
            if(params.length == 0){
                return null;
            }
            try {
                SpotifyApi spotifyApi = new SpotifyApi();
                SpotifyService spotifyService = spotifyApi.getService();
                return spotifyService.searchArtists(params[0]);
            } catch (RetrofitError retrofitError){
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArtistsPager artistsPager) {
            super.onPostExecute(artistsPager);

            if(artistsPager != null) {
                mArtistSearchAdapter.clear();
                mArtistDataList = new ArrayList<>();
                for(Artist artist : artistsPager.artists.items) {
                    mArtistSearchAdapter.add(new ArtistData(artist));
                    mArtistDataList.add(new ArtistData(artist));
                }

                mArtistSearchAdapter.notifyDataSetChanged();

                if(artistsPager.artists.items.size() == 0){
                    Context context = getActivity().getApplicationContext();
                    CharSequence text = getString(R.string.no_result);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context,text,duration);
                    toast.show();
                }
            }
        }
    }
}
