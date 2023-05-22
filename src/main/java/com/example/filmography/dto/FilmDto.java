package com.example.filmography.dto;


import com.example.filmography.model.Genre;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Set;



@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilmDto extends GenericDto {

    private String title;
    private Integer premierYear;
    private String country;
    private Genre genre;
    private Set<Long> directorsIds;
}

