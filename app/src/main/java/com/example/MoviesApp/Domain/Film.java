package com.example.MoviesApp.Domain;

public class Film {
    private String id;
    private String title;
    private String plot;
    private String poster;
    private String type;
    public Film() {}

    public Film(String id, String title, String plot, String poster, String type) {
        this.id = id;
        this.title = title;
        this.plot = plot;
        this.poster = poster;
        this.type = type;
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
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
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

