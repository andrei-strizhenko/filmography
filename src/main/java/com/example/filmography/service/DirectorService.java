package com.example.filmography.service;

import com.example.filmography.dto.AddFilmsDto;
import com.example.filmography.dto.DirectorDto;
import com.example.filmography.model.Director;
import com.example.filmography.model.Film;
import com.example.filmography.repository.DirectorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectorService {
    private final DirectorRepository directorRepository;
    private final FilmService filmService;


    public DirectorService(DirectorRepository directorRepository, FilmService filmService) {
        this.directorRepository = directorRepository;
        this.filmService = filmService;

    }

    public List<Director> getList() {
        return directorRepository.findAll();
    }

    public Director getOne(Long id) {
        return directorRepository.findById(id).orElseThrow();
    }

    public Director create(Director director) {
        return directorRepository.save(director);
    }

    public Director update(Director director) {
        return directorRepository.save(director);
    }

    public void delete(Long id) {
        directorRepository.delete(directorRepository.findById(id).orElseThrow());
    }


    public Director addFilm(AddFilmsDto addFilmsDto) {
        Director director = getOne(addFilmsDto.getDirectorId());
        Film film = filmService.getOne(addFilmsDto.getFilmId());
        director.getFilms().add(film);
        //    film.getDirectors().add(director);
        // film.getDirectors().add(director);
        //   filmService.update(film);
        return update(director);
    }

  /*  public DirectorDto getListWithFilmIds(AddFilmsDto addFilmsDto, DirectorDto directorDto) {
        Director director = getOne(addFilmsDto.getDirectorId());
        for( filmId : addFilmsDto.getFilmId()) {
            directorDto.getFilmsIds();
        }
        return (DirectorDto) directorRepository.findAll();
    }*/

}
