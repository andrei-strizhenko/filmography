package com.example.filmography.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto extends GenericDto {

    private Long userId;
    private Long filmId;
    private LocalDate rentDate;
    private Integer rentPeriod;
    private boolean isPurchased;
    private FilmDto film;
    private UserDto user;
}
