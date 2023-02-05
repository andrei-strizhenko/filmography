package com.example.filmography.controller;

import com.example.filmography.dto.FilmDto;
import com.example.filmography.mapper.FilmMapper;
import com.example.filmography.model.Film;
import com.example.filmography.model.Genre;
import com.example.filmography.service.FilmService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/film")
public class FilmController extends GenericController<Film, FilmDto>{
    private final FilmService filmService;
    private final FilmMapper filmMapper;

    public FilmController(FilmService filmService, FilmMapper filmMapper) {
        super(filmService, filmMapper);
        this.filmService = filmService;
        this.filmMapper = filmMapper;
    }


    //methods read:
    @RequestMapping(value = "/list", method = RequestMethod.GET)
       public List<Film> getList() {
        return filmService.getList();
    }


    @GetMapping("/{id}")
    public Film getById(@PathVariable Long id) {
        return (Film) filmService.getOne(id);
    }

    //method create:
    @PostMapping
    public Film create(@RequestBody Film film) {
        return (Film) filmService.create(film);
    }

    //method update:
    @PutMapping("/{id}")
    public Film update(@RequestBody Film film, Long id) {
        film.setId(id);
        return (Film) filmService.update(film);
    }

    //method delete:
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        filmService.delete(id); //check user by this id and throw exception
    }



    @GetMapping("/search")
    public List<FilmDto> list(
            @RequestParam(value = "title", required = false)String title,
            @RequestParam(value = "country", required = false)String country,
            @RequestParam(value = "genre", required = false) Genre genre
    ) {
        return filmMapper.toDtos(filmService.getByTitleOrCountryOrGenre(title, country, genre));
    }

  /*  @PostMapping("/director-films")
    public Film addDirector(@RequestBody AddFilmsDto addFilmsDto) {
        return filmService.addDirector(addFilmsDto);
    }*/

//return mapper.toDtos(service.search(title, genre));
}
