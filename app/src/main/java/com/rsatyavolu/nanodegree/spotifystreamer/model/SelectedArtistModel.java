package com.rsatyavolu.nanodegree.spotifystreamer.model;

import java.io.Serializable;

/**
 * Created by rsatyavolu on 6/29/15.
 */
public class SelectedArtistModel implements Serializable {

    private String id;
    private String name;
    private String imageUrl;
    private String uri;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
