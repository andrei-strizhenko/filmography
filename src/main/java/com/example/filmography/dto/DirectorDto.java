package com.example.filmography.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DirectorDto extends GenericDto {
    @NotBlank(message = "Поле не должно быть пустым")
    private String directorFIO;
    @NotBlank(message = "Поле не должно быть пустым")
    private String position;

    private Set<Long> filmsIds;

}
