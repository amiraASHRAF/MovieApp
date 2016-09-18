package com.example.amiraahabeeb.movieapp;

/**
 * Created by Amira A. habeeb on 30/08/2016.
 */
public class Mouvie_parsing {
    String OWM_LIST;
    String poster_path;
    String overview;
    String release_date;
    String original_title;
    String popularity;
    String id;
    String content;

    public Mouvie_parsing() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getid() {
        return id;
    }

    public String getOWM_LIST() {
        return OWM_LIST;
    }

    public void setOWM_LIST(String OWM_LIST) {
        this.OWM_LIST = OWM_LIST;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public void setid(String id) {
        this.id = id;
    }
}
