package com.example.filmography.controller;

import com.example.filmography.dto.AddFilmsDto;
import com.example.filmography.dto.DirectorDto;
import com.example.filmography.dto.DirectorWithFilmDto;
import com.example.filmography.mapper.DirectorMapper;
import com.example.filmography.mapper.DirectorWithFilmMapper;
import com.example.filmography.mapper.GenericMapper;
import com.example.filmography.model.Director;
import com.example.filmography.service.DirectorService;
import com.example.filmography.service.GenericService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/director")
public class DirectorController extends GenericController<Director, DirectorDto> {
    private static final GenericService genericService = null;
    private static final GenericMapper genericMapper = null;

    private final DirectorService directorService;
    private final DirectorWithFilmMapper directorWithFilmMapper;

    public DirectorController(DirectorService directorService,DirectorMapper directorMapper, DirectorWithFilmMapper directorWithFilmMapper) {
        super(directorService, directorMapper, directorWithFilmMapper, genericService, genericMapper);
        this.directorService = directorService;
        this.directorWithFilmMapper = directorWithFilmMapper;

    }


    //methods read:
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Director> list() {
        return directorService.getList();
    }


    @GetMapping("/{id}")
    public Director getById(@PathVariable Long id) {
        return directorService.getOne(id);
    }

    //method create:
    @PostMapping
    public Director create(@RequestBody Director director) {
        return directorService.create(director);
    }

    //method update:
    @PutMapping("/{id}")
    public Director update(@RequestBody Director director, Long id) {
        director.setId(id);
        return directorService.update(director);
    }

    //method delete:
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        directorService.delete(id); //check director by this id and throw exception
    }


    @PostMapping("/director-films")
    public DirectorDto addFilm(@RequestBody AddFilmsDto addFilmsDto) {
        return directorWithFilmMapper.toDto(directorService.addFilm(addFilmsDto));
    }

 /*   @RequestMapping(value = "/list-ids-films", method = RequestMethod.GET)
    public Set<Director> listnew() {
        Set <Long> filmIds;

        return directorWithFilmMapper.toDto(directorService.getList(), Long );
    }*/

    @GetMapping("/directors-filmslist")
    public List<DirectorWithFilmDto> getDirectorsWithFilms() {
        return directorService.getList().stream().map(directorWithFilmMapper::toDto).collect(Collectors.toList());
    }


}
