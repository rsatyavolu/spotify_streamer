package com.rsatyavolu.nanodegree.spotifystreamer.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rsatyavolu.nanodegree.spotifystreamer.R;
import com.rsatyavolu.nanodegree.spotifystreamer.model.ArtistTrackModel;
import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.List;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by rsatyavolu on 6/29/15.
 */
public class TopTracksDisplayAdapter<A> extends ArrayAdapter<ArtistTrackModel> {

    public TopTracksDisplayAdapter(Context context, int resource, int textViewResourceId, List<ArtistTrackModel> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        TextView albumView = (TextView)view.findViewById(R.id.album_name);
        TextView trackView = (TextView)view.findViewById(R.id.track);
        ImageView thumb_image = (ImageView)view.findViewById(R.id.album_image);

        ArtistTrackModel track = super.getItem(position);

        albumView.setText(track.getAlbumName());
        trackView.setText(track.getName());

        if(track.getImageUrl() != null) {
            Picasso.with(super.getContext()).load(track.getImageUrl()).into(thumb_image);
        } else {
            thumb_image.setImageResource(R.drawable.artist_default);
        }

        return view;
    }
}
