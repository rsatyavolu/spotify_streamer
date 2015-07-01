package com.rsatyavolu.nanodegree.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Pager;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ArtistDisplayAdapter<Artist> spotifyArtistListAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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

        ListView spotifySearchResultsList = (ListView)rootView.findViewById(
                R.id.search_result_list);

        spotifyArtistListAdapter = new ArtistDisplayAdapter<Artist>(getActivity(),
                R.layout.list_artist_search_result, R.id.artist, new ArrayList<Artist>());
        spotifySearchResultsList.setAdapter(spotifyArtistListAdapter);

        spotifySearchResultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent explicitIntent = new Intent(getActivity(), ArtistDetailActivity.class);
                Artist selectedArtist = spotifyArtistListAdapter.getItem(position);

                SelectedArtistModel model = new SelectedArtistModel();
                model.setId(selectedArtist.id);
                model.setName(selectedArtist.name);
                if(spotifyArtistListAdapter.getArtistIcon(selectedArtist) != null) {
                    model.setImageUrl(spotifyArtistListAdapter.getArtistIcon(selectedArtist).url);
                }
                model.setUri(selectedArtist.uri);

                Bundle b = new Bundle();
                b.putSerializable("selected_artist", model);
                explicitIntent.putExtras(b);

                startActivity(explicitIntent);
            }
        });

        return rootView;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public class SpotifySearchTask extends AsyncTask<String, Void, Pager<Artist>> {

        private final String LOG_TAG = SpotifySearchTask.class.getSimpleName();
        private final String PRE_MSG = "Artist ";
        private final String POST_MSG = " not found. Please refine your search.";
        private String artistName = null;

        @Override
        protected Pager<Artist> doInBackground(String... params) {
            artistName = params[0];

            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService service = spotifyApi.getService();

            ArtistsPager pager = service.searchArtists(artistName);
            Pager<Artist> artists = pager.artists;

            return artists;
        }

        @Override
        protected void onPostExecute(Pager<Artist> artists) {
            List<Artist> artistList = artists.items;

            if(artistList.size() > 0) {
                spotifyArtistListAdapter.clear();
                Iterator<Artist> itr = artistList.iterator();
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
    }
}
