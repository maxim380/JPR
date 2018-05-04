package com.maxim.jpr.Models;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by maxim on 3/17/2018.
 */

public class Station implements Serializable {

    private String url;
    private String title;
    private String description;
    private transient Bitmap albumArt;

    public Station(String url, String title, String description, Bitmap albumArt) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.albumArt = albumArt;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getAlbumArt() {
        return this.albumArt;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAlbumArt(Bitmap albumArt) {
        this.albumArt = albumArt;
    }
}
