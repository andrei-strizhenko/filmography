package com.example.filmography.service;

import com.example.filmography.dto.OrderDto;
import com.example.filmography.model.Director;
import com.example.filmography.model.Film;
import com.example.filmography.model.Order;
import com.example.filmography.model.User;
import com.example.filmography.repository.FilmRepository;
import com.example.filmography.repository.OrderRepository;
import com.example.filmography.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class OrderService extends GenericService<Order> {

    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final OrderRepository repository;

    protected OrderService(OrderRepository repository, UserRepository userRepository, FilmRepository filmRepository) {
        super(repository);
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
        this.repository = repository;
    }

    public Order addOrder(OrderDto orderDto) {
        User user = userRepository.findById(orderDto.getUserId()).orElseThrow();
        Film film = filmRepository.findById(orderDto.getFilmId()).orElseThrow();
        Order order = Order.builder()
                .createdWhen(LocalDateTime.now())
                .user(user)
                .film(film)
                .rentDate(LocalDate.now())
                .isPurchased(orderDto.isPurchased())
                .rentPeriod(orderDto.isPurchased() ? -1 : orderDto.getRentPeriod())
                .build();

        return create(order);
    }


    public Page<Order> getUserOrdersPaginated(Pageable pageable, Long userId) {
        return repository.findAllByUserId(pageable, userId);
    }

    public Page<Order> listAllPaginated(Pageable pageable) {
        return repository.findAll(pageable);
    }
    public Page<Order> listAllPaginatedForUsers(Pageable pageable, Long userId) {
        return repository.findAllByUserId(pageable, userId);
    }

    public Page<Order> findUserOrdersByTitleContainingPaginated(Pageable pageable, Long userId, String title) {
        return repository.findAllByUserIdAndAndFilmTitleContaining(pageable, userId, title);
    }

    public Boolean isActiveRentOrder(Long userId, Long filmId) {
        return repository.isActiveRentOrder(userId, filmId);
    }

    public Boolean isPurchasedOrder(Long userId, Long filmId) {
        return repository.isPurchasedOrder(userId, filmId);
    }

    public Order findPurchasedOrderByUserIdAndFilmId(Long userId, Long filmId) {
        return repository.findOrderByUserIdAndFilmIdAndIsPurchasedTrue(userId, filmId);
    }

    public Order findRentOrderByUserIdAndFilmId(Long userId, Long filmId) {
        return repository.findOrderByUserIdAndFilmIdAndIsPurchasedFalseOrderByRentDateDesc(userId, filmId);
    }

}



