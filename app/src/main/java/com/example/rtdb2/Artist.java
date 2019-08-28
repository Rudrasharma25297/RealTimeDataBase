package com.example.rtdb2;

public class Artist {

    String artistId;
    String artistName;
    String artistGenres;
    public Artist(){

    }

    public Artist(String artistId, String artistName, String artistGenres) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.artistGenres = artistGenres;
    }

    public String getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtistGenres() {
        return artistGenres;
    }
}
