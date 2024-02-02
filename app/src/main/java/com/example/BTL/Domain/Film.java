package com.example.BTL.Domain;

public class Film {
    private String id;
    private String title;
    private String plot;
    private String poster;

    public Film() {
        // Default constructor required for calls to DataSnapshot.getValue(Film.class)
    }

    public Film(String id, String title, String plot, String poster) {
        this.id = id;
        this.title = title;
        this.plot = plot;
        this.poster = poster;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}

