package com.example.filmography.model;

public enum Genre {
    FANTASTIC("Фантастика"),
    SCIENCE_FICTION("Научная фантастика"),
    DRAMA("Драма"),
    COMEDY("Комедия"),
    ACTION("Боевик"),
    HORROR("Ужасы"),
    VESTERN("Вестерн");

    private final String genreText;

    Genre(String genreText) {
        this.genreText = genreText;
    }

    public String getGenreText() {
        return this.genreText;
    }
}

