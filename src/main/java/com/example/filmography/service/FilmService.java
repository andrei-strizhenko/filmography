package com.example.filmography.service;


import com.example.filmography.model.Director;
import com.example.filmography.model.Film;
import com.example.filmography.model.Genre;
import com.example.filmography.repository.FilmRepository;
import com.example.filmography.service.userDetails.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class FilmService extends GenericService<Film> {

    private final FilmRepository repository;

    public FilmService(FilmRepository repository) {
        super(repository);
        this.repository = repository;
    }

    //поиск фильма в сервисе по названию/стране/жанру
    public Set<Film> findAllByTitleOrCountryOrGenre(String title, String country, Genre genre) {
        return repository.findAllByTitleOrCountryOrGenre(title, country, genre);
    }

    public Page<Film> listAllPaginated(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Film> listAllPaginatedForUsers(Pageable pageable) {
        return repository.findAllByIsDeletedFalse(pageable);
    }

    public Page<Film> searchByTitleContaining(Pageable pageable, String title) {
        return repository.findAllByTitleContaining(pageable, title);
    }

    public Page<Film> searchByTitleContainingForUsers(Pageable pageable, String title) {
        return repository.findAllByTitleContainingAndIsDeletedFalse(pageable, title);
    }

    public void block(Long id) {
        Film film = getOne(id);
        film.setDeleted(true);
        film.setDeletedBy(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getUsername());
        film.setDeletedWhen(LocalDateTime.now());
        update(film);
    }

    public void unblock(Long id) {
        Film film = getOne(id);
        film.setDeleted(false);
        film.setDeletedBy(null);
        film.setDeletedWhen(null);
        film.setUpdatedBy(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getUsername());
        film.setUpdatedWhen(LocalDateTime.now());
        update(film);
    }

}
