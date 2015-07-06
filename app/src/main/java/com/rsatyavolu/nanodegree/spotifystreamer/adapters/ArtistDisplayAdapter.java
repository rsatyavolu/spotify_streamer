package com.rsatyavolu.nanodegree.spotifystreamer.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rsatyavolu.nanodegree.spotifystreamer.R;
import com.rsatyavolu.nanodegree.spotifystreamer.model.SelectedArtistModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rsatyavolu on 6/26/15.
 */
public class ArtistDisplayAdapter<A> extends ArrayAdapter<SelectedArtistModel> {

    List<SelectedArtistModel> objects;

    public List<SelectedArtistModel> getObjects() {
        return objects;
    }

    public void setObjects(List<SelectedArtistModel> objects) {
        this.objects = objects;
    }

    public ArtistDisplayAdapter(Context context, int resource, int textViewResourceId, List<SelectedArtistModel> objects) {
        super(context, resource, textViewResourceId, objects);
        this.objects = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        TextView artistView = (TextView)view.findViewById(R.id.artist);
        ImageView thumb_image=(ImageView)view.findViewById(R.id.list_image);

        SelectedArtistModel artist = super.getItem(position);

        if(artist.getImageUrl() != null) {
            Picasso.with(super.getContext()).load(artist.getImageUrl()).into(thumb_image);
        } else {
            thumb_image.setImageResource(R.drawable.artist_default);
        }

        artistView.setText(artist.getName());

        return view;
    }
}
