package com.example.filmography.dto;


import com.example.filmography.model.Genre;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class FilmDto extends GenericDto {
    @NotBlank(message = "Поле не должно быть пустым")
    private String title;
    @NotBlank(message = "Поле не должно быть пустым")
    private Genre genre;
    @NotBlank(message = "Поле не должно быть пустым")
    private String premierYear;
    private Set<Long> directorsIds;

}
