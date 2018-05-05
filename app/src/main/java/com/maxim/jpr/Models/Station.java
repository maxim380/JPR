package com.maxim.jpr.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Serializable;
import java.net.URL;

/**
 * Created by maxim on 3/17/2018.
 */

public class Station implements Serializable {

    private String url;
    private String title;
    private String description;
    private transient Bitmap albumArt;
    private String infoURL;

    public Station(String url, String title, String description, String albumArt, String infoURL) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.infoURL = infoURL;
        if(albumArt == "") {
            this.albumArt = null;
        } else {
            try {
                URL imgURL = new URL(albumArt);
                this.albumArt = BitmapFactory.decodeStream(imgURL.openConnection().getInputStream());
            } catch(Exception e) {
                this.albumArt = null;
            }
        }
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

    public String getInfoURL() {
        return infoURL;
    }

    public void setInfoURL(String infoURL) {
        this.infoURL = infoURL;
    }
}
