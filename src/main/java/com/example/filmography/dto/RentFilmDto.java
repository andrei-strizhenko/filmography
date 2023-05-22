package com.example.filmography.dto;


import lombok.Data;

@Data
public class RentFilmDto {
    private Long userId;
    private Long filmId;
    private Integer rentPeriod;
    private boolean isPurchased;
}
