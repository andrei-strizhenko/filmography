package com.example.filmography.service;

import com.example.filmography.dto.AddFilmsDto;
import com.example.filmography.dto.FilmDto;
import com.example.filmography.model.Director;
import com.example.filmography.model.Film;
import com.example.filmography.model.Genre;
import com.example.filmography.repository.FilmRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmService extends GenericService<Film>{
    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        super(filmRepository);
        this.filmRepository = filmRepository;

    }

    public List<Film> getList() {
        return filmRepository.findAll();
    }

    public Film getOne(Long id) {
        return filmRepository.findById(id).orElseThrow();
    }

    public Film create(Film film) {
        return filmRepository.save(film);
    }

    public Film update(Film film) {
        return filmRepository.save(film);
    }

    public void delete(Long id) {
        filmRepository.delete(filmRepository.findById(id).orElseThrow());
    }

    public List<Film> getByTitleOrCountryOrGenre(String title, String country, Genre genre) {
        return filmRepository.findAllByTitleOrCountryOrGenre(title, country, genre);
    }


  /*  public List<FilmDto> getList() {
        return filmRepository.findAll();
    }*/


   /* public Film addDirector(AddFilmsDto addFilmsDto) {
        Director director = directorService.getOne(addFilmsDto.getDirectorId());
        Film film = getOne(addFilmsDto.getFilmId());
        film.getDirectors().add(director);
      //  director.getFilms().add(film);
      //  directorService.update(director);
        return update(film);
    }*/
}