package com.example.filmography.repository;

import com.example.filmography.model.Film;
import com.example.filmography.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long>{

    List<Film> findAllByTitleOrCountryOrGenre(String title, String country, Genre genre);
}
