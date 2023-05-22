package com.example.filmography.service;

import com.example.filmography.dto.AddFilmDto;

import com.example.filmography.model.Director;
import com.example.filmography.model.Film;
import com.example.filmography.model.Order;
import com.example.filmography.repository.DirectorRepository;
import com.example.filmography.repository.FilmRepository;
import com.example.filmography.repository.OrderRepository;
import com.example.filmography.service.userDetails.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.Set;

@Service
public class DirectorService extends GenericService<Director> {

    private final DirectorRepository repository;
    private final OrderRepository orderRepository;
    private final FilmRepository filmRepository;

    protected DirectorService(DirectorRepository repository, OrderRepository orderRepository,
                              FilmRepository filmRepository) {
        super(repository);
        this.repository = repository;
        this.orderRepository = orderRepository;
        this.filmRepository = filmRepository;
    }

    //Удалить участника фильма, если нет заказов с его фильмами,
    // удалить фильмы, если он был единственным участником.
    public void delete(Long id) {
        Set<Order> orders = orderRepository.findAllByDirectorId(id);

        if (orders.size() != 0) {
            return;
        }
        repository.deleteById(id);
        Set<Film> singleDirectorFilms = filmRepository.findAllSingleDirectorFilmsByDirectorId(id);

        for (Film film : singleDirectorFilms) {
            filmRepository.deleteById(film.getId());
        }
    }

    public Director addFilm(AddFilmDto addFilmDto) {
        Director director = repository.findById(addFilmDto.getDirectorId()).orElseThrow();
        Film film = filmRepository.findById(addFilmDto.getFilmId()).orElseThrow();
        director.getFilms().add(film);
        return repository.save(director);
    }

    public Page<Director> searchByDirectorFIO(Pageable pageable, String directorFIO) {
        return repository.findAllByDirectorFIO(pageable, directorFIO);
    }

    public Page<Director> searchByDirectorFIOContaining(Pageable pageable, String directorFIO) {
        return repository.findAllByDirectorFIOContaining(pageable, directorFIO);
    }

    public Page<Director> searchByDirectorFIOContainingForUsers(Pageable pageable, String directorFIO) {
        return repository.findAllByDirectorFIOContainingAndIsDeletedFalse(pageable, directorFIO);
    }

    public Page<Director> listAllPaginated(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Director> listAllPaginatedForUsers(Pageable pageable) {
        return repository.findAllByIsDeletedFalse(pageable);
    }

    public void block(Long id) {
        Director director = getOne(id);
        director.setDeleted(true);
        director.setDeletedBy(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getUsername());
        director.setDeletedWhen(LocalDateTime.now());
        update(director);
    }

    public void unblock(Long id) {
        Director director = getOne(id);
        director.setDeleted(false);
        director.setDeletedBy(null);
        director.setDeletedWhen(null);
        director.setUpdatedBy(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getUsername());
        director.setUpdatedWhen(LocalDateTime.now());
        update(director);
    }
}
