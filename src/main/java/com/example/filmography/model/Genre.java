package com.example.filmography.model;

public enum Genre {
    FANTASTIC("Fantastic"),
    SCIENCE_FICTION("Science Fantastic"),
    DRAMA("Drama"),
    COMEDY("Comedy"),
    ACTION("Action"),
    HORROR("Horror"),
    VESTERN("Western");

    private final String genreText;

    Genre(String genreText) {
        this.genreText = genreText;
    }

    public String getGenreText() {
        return this.genreText;
    }
}

