package com.example.filmography.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.text.DateFormat;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "orders_seq", allocationSize = 1)

public class Order {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "rent_date")
    private LocalDateTime rentDate;

    @Column(name = "rent_period")
    private Integer rentPeriod;

    @Column(name = "purchase")
    private boolean purchase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(name = "FK_USERS_FILMS")
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "film_id",
            foreignKey = @ForeignKey(name = "FK_FILMS_USERS")
    )
    private Film film;

}
