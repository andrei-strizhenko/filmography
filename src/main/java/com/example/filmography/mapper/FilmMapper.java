package com.example.filmography.mapper;


import com.example.filmography.dto.FilmDto;
import com.example.filmography.model.Film;
import com.example.filmography.model.GenericModel;
import com.example.filmography.repository.DirectorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class FilmMapper extends GenericMapper<Film, FilmDto> {

    private final ModelMapper mapper;
    private final DirectorRepository directorRepository;
    protected FilmMapper(ModelMapper mapper, DirectorRepository directorRepository) {
        super(mapper, Film.class, FilmDto.class);
        this.mapper = mapper;
        this.directorRepository = directorRepository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Film.class, FilmDto.class)
                .addMappings(m -> m.skip(FilmDto::setDirectorsIds)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(FilmDto.class, Film.class)
                .addMappings(m -> m.skip(Film::setDirectors)).setPostConverter(toEntityConverter());
    }

    @Override
    void mapSpecificFields(FilmDto source, Film destination) {
        if (!Objects.isNull(source.getDirectorsIds())) {
            destination.setDirectors(directorRepository.findAllByIdIn(source.getDirectorsIds()));
        } else {
            destination.setDirectors(null);

        }
    }

    @Override
    void mapSpecificFields(Film source, FilmDto destination) {
        destination.setDirectorsIds(getIds(source));
    }

    private Set<Long> getIds(Film Film) {
        return Objects.isNull(Film) || Objects.isNull(Film.getId())
                ? null
                : Film.getDirectors().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet());
    }
}

