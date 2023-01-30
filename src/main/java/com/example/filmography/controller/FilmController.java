package com.example.filmography.controller;

import com.example.filmography.dto.AddFilmsDto;
import com.example.filmography.model.Director;
import com.example.filmography.model.Film;
import com.example.filmography.model.Genre;
import com.example.filmography.repository.FilmRepository;
import com.example.filmography.service.FilmService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/film")
public class FilmController {
    private final FilmService filmService;


    public FilmController(FilmService filmService) {
        this.filmService = filmService;

    }


    //methods read:
    @RequestMapping(value = "/list", method = RequestMethod.GET)
       public List<Film> list() {
        return filmService.getList();
    }


    @GetMapping("/{id}")
    public Film getById(@PathVariable Long id) {
        return filmService.getOne(id);
    }

    //method create:
    @PostMapping
    public Film create(@RequestBody Film film) {
        return filmService.create(film);
    }

    //method update:
    @PutMapping("/{id}")
    public Film update(@RequestBody Film film, Long id) {
        film.setId(id);
        return filmService.update(film);
    }

    //method delete:
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        filmService.delete(id); //check user by this id and throw exception
    }



    @GetMapping("/search")
    public List<Film> list(
            @RequestParam(value = "title", required = false)String title,
            @RequestParam(value = "country", required = false)String country,
            @RequestParam(value = "genre", required = false) Genre genre
    ) {
        return filmService.getByTitleOrCountryOrGenre(title, country, genre);
    }

  /*  @PostMapping("/director-films")
    public Film addDirector(@RequestBody AddFilmsDto addFilmsDto) {
        return filmService.addDirector(addFilmsDto);
    }*/


}
