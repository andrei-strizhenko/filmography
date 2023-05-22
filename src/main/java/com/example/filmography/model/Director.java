package com.example.filmography.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "directors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name = "default_generator", sequenceName = "directors_seq", allocationSize = 1)

public class Director extends GenericModel {

    @Column(name = "director_fio")
    private String directorFIO;

    @Column(name = "position")
    private String position;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    //@JsonIgnore
    @JoinTable(
            name = "film_directors",
            joinColumns = @JoinColumn(name = "director_id"),
            foreignKey = @ForeignKey(name = "FK_DIRECTORS_FILMS"),
            inverseJoinColumns = @JoinColumn(name = "film_id"),
            inverseForeignKey = @ForeignKey(name = "FK_FILMS_DIRECTORS")
    )
    private Set<Film> films = new HashSet<>();

    @Builder
    public Director(Long id, LocalDateTime createdWhen, String createdBy, LocalDateTime updatedWhen, String updatedBy, LocalDateTime deletedWhen, String deletedBy, boolean isDeleted, String directorFIO, String position, Set<Film> films) {
        super(id, createdWhen, createdBy, updatedWhen, updatedBy, deletedWhen, deletedBy, isDeleted);
        this.directorFIO = directorFIO;
        this.position = position;
        this.films = films;
    }
}
