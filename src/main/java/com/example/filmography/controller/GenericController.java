package com.example.filmography.controller;

import com.example.filmography.dto.GenericDto;
import com.example.filmography.mapper.DirectorMapper;
import com.example.filmography.mapper.DirectorWithFilmMapper;
import com.example.filmography.mapper.GenericMapper;
import com.example.filmography.model.GenericModel;
import com.example.filmography.service.DirectorService;
import com.example.filmography.service.GenericService;

public abstract class GenericController <T extends GenericModel, N extends GenericDto> {
    private final GenericService genericService;
    private final GenericMapper genericMapper;


    protected GenericController(GenericService genericService, GenericMapper genericMapper) {
        this.genericService = genericService;
        this.genericMapper = genericMapper;
    }

    public GenericController(DirectorService directorService, DirectorMapper directorMapper, DirectorWithFilmMapper directorWithFilmMapper, GenericService genericService, GenericMapper genericMapper) {

        this.genericService = genericService;
        this.genericMapper = genericMapper;
    }
}
