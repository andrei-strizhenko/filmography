package com.example.filmography.repository;

import com.example.filmography.model.Film;
import com.example.filmography.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FilmRepository extends GenericRepository<Film>{

    List<Film> findAllByTitleOrCountryOrGenre(String title, String country, Genre genre);
    Set<Film> findAllByIdIn(Set<Long> filmsIds);

  //  Set<Film> findAllByIdIn(Set<Long> filmsIds);
}
