package com.example.filmography.controller;

import com.example.filmography.model.Film;
import com.example.filmography.repository.FilmRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/film")
public class FilmController {
    private final FilmRepository filmRepository;


    public FilmController(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }


    //methods read:
    @RequestMapping(value = "/list", method = RequestMethod.GET)
       public List<Film> list() {
        return filmRepository.findAll();
    }


    @GetMapping("/{id}")
    public Film getById(@PathVariable Long id) {
        return filmRepository.findById(id).orElseThrow();
    }

    //method create:
    @PostMapping
    public Film create(@RequestBody Film film) {
        return filmRepository.save(film);
    }

    //method update:
    @PutMapping("/{id}")
    public Film update(@RequestBody Film film, Long id) {
        film.setId(id);
        return filmRepository.save(film);
    }

    //method delete:
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        filmRepository.delete(filmRepository.findById(id).orElseThrow()); //check user by this id and throw exception
    }

}
