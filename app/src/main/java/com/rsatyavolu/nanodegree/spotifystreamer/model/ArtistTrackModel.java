package com.rsatyavolu.nanodegree.spotifystreamer.model;

import java.io.Serializable;

/**
 * Created by rsatyavolu on 7/5/15.
 */
public class ArtistTrackModel implements Serializable {

    String albumName;
    String name;
    String imageUrl;

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
