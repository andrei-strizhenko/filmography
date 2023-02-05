package com.example.filmography.dto;

import com.example.filmography.model.Director;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmWithDirectorDto extends FilmDto {
    private Set<DirectorDto> directors;
}
