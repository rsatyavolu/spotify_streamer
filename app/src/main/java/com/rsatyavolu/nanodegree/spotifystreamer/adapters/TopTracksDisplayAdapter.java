package com.rsatyavolu.nanodegree.spotifystreamer.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rsatyavolu.nanodegree.spotifystreamer.R;
import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.List;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by rsatyavolu on 6/29/15.
 */
public class TopTracksDisplayAdapter<A> extends ArrayAdapter<Track> {

    public TopTracksDisplayAdapter(Context context, int resource, int textViewResourceId, List<Track> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        TextView albumView = (TextView)view.findViewById(R.id.album_name);
        TextView trackView = (TextView)view.findViewById(R.id.track);
        ImageView thumb_image = (ImageView)view.findViewById(R.id.album_image);

        Track track = super.getItem(position);

        albumView.setText(track.album.name);
        trackView.setText(track.name);

        Image artistIcon = getAlbumIcon(track);

        if(artistIcon != null) {
            Picasso.with(super.getContext()).load(artistIcon.url).into(thumb_image);
        } else {
            thumb_image.setImageResource(R.drawable.artist_default);
        }

        return view;
    }

    public static Image getAlbumIcon(Track track) {

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

        return icon;
    }



}
