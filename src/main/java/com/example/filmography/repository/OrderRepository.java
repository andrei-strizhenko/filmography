package com.example.filmography.repository;

import com.example.filmography.model.Film;
import com.example.filmography.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Set;

@Repository
public interface OrderRepository extends GenericRepository<Order> {

    @Query(nativeQuery = true, value = "    SELECT *\n" +
                                       "    FROM films\n" +
                                       "    WHERE id IN\n" +
                                       "        (SELECT DISTINCT film_id\n" +
                                       "        FROM orders\n" +
                                       "        WHERE user_id =:userId\n" +
                                       "        )\n")
    Set<Film> findAllFilmsByUserId(@Param(value = "userId") Long userId);

    @Query(nativeQuery = true, value = "    SELECT *\n" +
                                       "    FROM orders\n" +
                                       "    WHERE film_id IN (SELECT DISTINCT film_id\n" +
                                       "                      FROM film_directors\n" +
                                       "                      WHERE director_id =:directorId)\n")
    Set<Order> findAllByDirectorId(@Param(value = "directorId") Long directorId);

    Page<Order> findAllByUserId(Pageable pageable, Long userId);

    Page<Order> findAllByUserIdAndAndFilmTitleContaining(Pageable pageable, Long userId, String title);

    @Query(nativeQuery = true, value = "    SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END\n" +
                                       "    FROM orders o\n" +
                                       "    WHERE\n" +
                                       "        o.user_id=:userId AND\n" +
                                       "        o.film_id=:filmId AND\n" +
                                       "        o.rent_date + o.rent_period * interval '1 day' > now() AND\n" +
                                       "        o.is_purchased = false\n")
    boolean isActiveRentOrder(Long userId, Long filmId);

    Order findOrderByUserIdAndFilmIdAndRentDateAndIsPurchasedFalse(Long userId, Long filmId, LocalDate rentDate);

    @Query(nativeQuery = true, value = "    SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END\n" +
                                       "    FROM orders o\n" +
                                       "    WHERE\n" +
                                       "        o.user_id=:userId AND\n" +
                                       "        o.film_id=:filmId AND\n" +
                                       "        o.is_purchased = true\n")
    boolean isPurchasedOrder(Long userId, Long filmId);

    Order findOrderByUserIdAndFilmIdAndIsPurchasedTrue(Long userId, Long filmId);

    Order findOrderByUserIdAndFilmIdAndIsPurchasedFalseOrderByRentDateDesc(Long userId, Long filmId);
}
