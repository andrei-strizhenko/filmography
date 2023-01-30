package com.example.filmography.controller;

import com.example.filmography.dto.AddFilmsDto;
import com.example.filmography.model.Director;
import com.example.filmography.service.DirectorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/director")
public class DirectorController {
    private final DirectorService directorService;


    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;

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
    public Director addFilm(@RequestBody AddFilmsDto addFilmsDto) {
        return directorService.addFilm(addFilmsDto);
    }

}
