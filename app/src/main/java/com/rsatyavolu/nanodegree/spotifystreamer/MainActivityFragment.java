package com.rsatyavolu.nanodegree.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rsatyavolu.nanodegree.spotifystreamer.adapters.ArtistDisplayAdapter;
import com.rsatyavolu.nanodegree.spotifystreamer.model.SelectedArtistModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;
import retrofit.RetrofitError;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static final String SELECTED_ARTIST = "selected_artist";
    public static final String ARTIST_SEARCH_RESULTS = "artistSearchResults";

    private ArtistDisplayAdapter<SelectedArtistModel> spotifyArtistListAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        List<SelectedArtistModel> data;

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        EditText searchEditText =  (EditText)rootView.findViewById(R.id.spotify_search_string);

        if(searchEditText != null) {
            searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event==null) {
                    if (actionId==EditorInfo.IME_ACTION_DONE) {
                        SpotifySearchTask task = new SpotifySearchTask();
                        task.execute(v.getText().toString());
                    }
                    else return false;  // Let system handle all other null KeyEvents
                }
                return false;   // Consume the event
                }
            });
        }

        if(savedInstanceState != null) {
            data = (List<SelectedArtistModel>) savedInstanceState.getSerializable(ARTIST_SEARCH_RESULTS);
        } else {
            data = new ArrayList<SelectedArtistModel>();
        }

        ListView spotifySearchResultsList = (ListView)rootView.findViewById(
                R.id.search_result_list);

        spotifyArtistListAdapter = new ArtistDisplayAdapter<SelectedArtistModel>(getActivity(),
                R.layout.list_artist_search_result, R.id.artist, data);
        spotifySearchResultsList.setAdapter(spotifyArtistListAdapter);

        spotifySearchResultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent explicitIntent = new Intent(getActivity(), ArtistDetailActivity.class);
                SelectedArtistModel model = spotifyArtistListAdapter.getItem(position);

                Bundle b = new Bundle();
                b.putSerializable(SELECTED_ARTIST, model);
                explicitIntent.putExtras(b);

                startActivity(explicitIntent);
            }
        });

        return rootView;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(spotifyArtistListAdapter != null) {
            outState.putSerializable(ARTIST_SEARCH_RESULTS, (Serializable) spotifyArtistListAdapter.getObjects());
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public class SpotifySearchTask extends AsyncTask<String, Void, List<Artist>> {

        private final String LOG_TAG = SpotifySearchTask.class.getSimpleName();
        private final String PRE_MSG = "Artist ";
        private final String POST_MSG = " not found. Please refine your search.";
        private String artistName = null;

        @Override
        protected List<Artist> doInBackground(String... params) {

            final List<Artist> artists = new ArrayList<Artist>();
            artistName = params[0];
            SpotifyApi spotifyApi = new SpotifyApi();

            try {
                ArtistsPager pager = spotifyApi.getService().searchArtists(artistName);
                artists.addAll(pager.artists.items);
            } catch (RetrofitError e) {
                Log.e(LOG_TAG, e.getMessage());
            }

            return artists;
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            if(artists.size() > 0) {
                spotifyArtistListAdapter.clear();

                Iterator<SelectedArtistModel> itr = createModels(artists).iterator();
                while(itr.hasNext()) {
                    spotifyArtistListAdapter.add(itr.next());
                }
            } else {
                StringBuffer message = new StringBuffer();
                message.append(PRE_MSG);
                message.append(artistName);
                message.append(POST_MSG);

                Toast toast = Toast.makeText(MainActivityFragment.this.getActivity(),
                        message.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        }

        private List<SelectedArtistModel> createModels(List<Artist> artists) {
            List<SelectedArtistModel> modelList = new ArrayList<SelectedArtistModel>();
            Iterator<Artist> itr = artists.iterator();
            while(itr.hasNext()) {
                Artist artist = itr.next();

                SelectedArtistModel model = new SelectedArtistModel();
                model.setId(artist.id);
                model.setName(artist.name);
                model.setImageUrl(getArtistIcon(artist));
                model.setUri(artist.uri);

                modelList.add(model);
            }

            return modelList;
        }

        private String getArtistIcon(Artist artist) {

            List<Image> imageList = artist.images;
            Image icon = null;
            int leastHeight = 0;

            Iterator<Image> itr = imageList.iterator();
            while(itr.hasNext()) {
                Image img = itr.next();
                int height = img.height;
                if(leastHeight == 0) {
                    leastHeight = height;
                } else if(height < leastHeight) {
                    leastHeight = height;
                    icon = img;
                }
            }

            if(icon != null) {
                return icon.url;
            } else {
                return null;
            }
        }
    }
}
