package com.example.filmography.controller;

import com.example.filmography.model.Director;
import com.example.filmography.repository.DirectorRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/director")
public class DirectorController {
    private final DirectorRepository directorRepository;


    public DirectorController(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }


    //methods read:
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Director> list() {
        return directorRepository.findAll();
    }


    @GetMapping("/{id}")
    public Director getById(@PathVariable Long id) {
        return directorRepository.findById(id).orElseThrow();
    }

    //method create:
    @PostMapping
    public Director create(@RequestBody Director director) {
        return directorRepository.save(director);
    }

    //method update:
    @PutMapping("/{id}")
    public Director update(@RequestBody Director director, Long id) {
        director.setId(id);
        return directorRepository.save(director);
    }

    //method delete:
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        directorRepository.delete(directorRepository.findById(id).orElseThrow()); //check director by this id and throw exception
    }
}
