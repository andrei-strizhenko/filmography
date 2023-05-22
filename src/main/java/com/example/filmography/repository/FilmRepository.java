package com.example.filmography.repository;

import com.example.filmography.model.Film;
import com.example.filmography.model.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FilmRepository extends GenericRepository<Film> {

    Set<Film> findAllByTitleOrCountryOrGenre(String title, String country, Genre genre);

    //получить все фильмы по id участника, где этот участник является единственным
    @Query(nativeQuery = true, value = "    SELECT DISTINCT *\n" +
                                       "    FROM films\n" +
                                       "    WHERE id IN (SELECT film_id\n" +
                                       "                 FROM film_directors\n" +
                                       "                 WHERE\n" +
                                       "                         film_id IN (SELECT film_id\n" +
                                       "                                     FROM\n" +
                                       "                                         (SELECT film_id, COUNT(film_id) count\n" +
                                       "                                          FROM film_directors fd\n" +
                                       "                                          GROUP BY film_id) t1\n" +
                                       "                                     WHERE t1.count = 1)\n" +
                                       "                   AND director_id =:directorId)\ncreate table films(	id int,	film_id int,	director_id int);")
    Set<Film> findAllSingleDirectorFilmsByDirectorId(@Param(value = "directorId") Long directorId);

    Set<Film> findAllByIdIn(Set<Long> ids);

    Page<Film> findAllByTitleContaining(Pageable pageable, String title);

    Page<Film> findAllByTitleContainingAndIsDeletedFalse(Pageable pageable, String title);

    Page<Film> findAllByIsDeletedFalse(Pageable pageable);
}
