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

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by rsatyavolu on 6/26/15.
 */
public class ArtistDisplayAdapter<A> extends ArrayAdapter<Artist> {


    public ArtistDisplayAdapter(Context context, int resource, int textViewResourceId, List<Artist> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        TextView artistView = (TextView)view.findViewById(R.id.artist);
        ImageView thumb_image=(ImageView)view.findViewById(R.id.list_image);

        Artist artist = super.getItem(position);

        Image artistIcon = getArtistIcon(artist);

        if(artistIcon != null) {
            Picasso.with(super.getContext()).load(artistIcon.url).into(thumb_image);
        } else {
            thumb_image.setImageResource(R.drawable.artist_default);
        }

        artistView.setText(artist.name);

        return view;
    }

    public static Image getArtistIcon(Artist artist) {

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

        return icon;
    }
}
