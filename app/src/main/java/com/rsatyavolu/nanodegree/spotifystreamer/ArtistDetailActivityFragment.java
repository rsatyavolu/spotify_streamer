package com.rsatyavolu.nanodegree.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rsatyavolu.nanodegree.spotifystreamer.adapters.TopTracksDisplayAdapter;
import com.rsatyavolu.nanodegree.spotifystreamer.model.ArtistTrackModel;
import com.rsatyavolu.nanodegree.spotifystreamer.model.SelectedArtistModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistDetailActivityFragment extends Fragment {

    public static final String SELECTED_ARTIST_KEY = "selected_artist";
    public static final String CLASS_NAME = "ArtistDetailActivity";
    public static final String ACTIVITY_TITLE = "Top 10 Tracks";
    private TopTracksDisplayAdapter<ArtistTrackModel> tracksDisplayAdapter;

    public ArtistDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_artist_detail, container, false);

        ListView trackList = (ListView)rootView.findViewById(R.id.artist_top_ten_list);
        tracksDisplayAdapter = new TopTracksDisplayAdapter<ArtistTrackModel>(getActivity(), R.layout.list_artist_track, R.id.track, new ArrayList<ArtistTrackModel>());
        trackList.setAdapter(tracksDisplayAdapter);

        final Intent intent = getActivity().getIntent();

        Bundle b = intent.getExtras();
        SelectedArtistModel selectedArtist = (SelectedArtistModel) b.getSerializable(SELECTED_ARTIST_KEY);
        String artistName = selectedArtist.getName();

        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setTitle(ACTIVITY_TITLE);
        ab.setSubtitle(artistName);

        SpotifyTopTracksTask task = new SpotifyTopTracksTask();
        Log.v(CLASS_NAME, selectedArtist.getId());
        task.execute(selectedArtist.getId());

        return rootView;
    }

    public class SpotifyTopTracksTask extends AsyncTask<String, Void, List<Track>> {

        public static final String COUNTRY_KEY = "country";
        public static final String US_ISO_CODE = "US";

        @Override
        protected List<Track> doInBackground(String... params) {
            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService service = spotifyApi.getService();

            final List<Track> artistTracks = new ArrayList<Track>();
            Map<String, Object> options = new HashMap<String, Object>();
            options.put(COUNTRY_KEY, US_ISO_CODE);

            try {
                Tracks trackList = service.getArtistTopTrack(params[0], options);
                artistTracks.addAll(trackList.tracks);
            } catch (RetrofitError e) {
                Log.e(SpotifyTopTracksTask.class.getName(), e.getMessage());
            }

            return artistTracks;
        }

        @Override
        protected void onPostExecute(List<Track> tracks) {
            List<ArtistTrackModel> modelList = createModels(tracks);
            if(tracks != null && tracks.size() > 0) {
                tracksDisplayAdapter.clear();
                Iterator<ArtistTrackModel> itr = modelList.iterator();
                while(itr.hasNext()) {
                    tracksDisplayAdapter.add(itr.next());
                }
            }
        }

        private List<ArtistTrackModel> createModels(List<Track> tracks) {
            List<ArtistTrackModel> modelList = new ArrayList<ArtistTrackModel>();
            Iterator<Track> itr = tracks.iterator();
            while(itr.hasNext()) {
                Track artistTrack = itr.next();

                ArtistTrackModel model = new ArtistTrackModel();
                model.setAlbumName(artistTrack.album.name);
                model.setName(artistTrack.name);
                model.setImageUrl(getAlbumIcon(artistTrack));

                modelList.add(model);
            }

            return modelList;
        }

        private String getAlbumIcon(Track track) {

            List<Image> imageList = track.album.images;
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
