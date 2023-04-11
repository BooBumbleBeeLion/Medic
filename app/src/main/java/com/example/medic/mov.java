package com.example.medic;

public class mov {
    String url;
    int movieId;

    public mov(String url, int movieId)
    {
        this.url = url;
        this.movieId = movieId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}
