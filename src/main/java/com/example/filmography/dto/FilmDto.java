package com.example.filmography.dto;


import com.example.filmography.model.Genre;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class FilmDto {

    private String title;
    private Genre genre;
    private String premierYear;
    private Set<Long> directorsIds;

}
