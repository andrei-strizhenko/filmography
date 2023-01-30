package com.example.filmography.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
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
    //   @Id
    //   @Column(name = "id")
    //   private Long id;

    @Column(name = "director_fio")
    private String directorFIO;

    @Column(name = "position")
    private String position;

    // @ManyToMany(mappedBy = "directors", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
 //   @JsonIgnore // убирает рекурсию
    @JoinTable(
            name = "films_directors",
            joinColumns = @JoinColumn(name = "director_id"),
            foreignKey = @ForeignKey(name = "FK_DIRECTORS_FILMS"),
            inverseJoinColumns = @JoinColumn(name = "film_id"),
            inverseForeignKey = @ForeignKey(name = "FK_FILMS_DIRECTORS"))
    private Set<Film> films = new HashSet<>();


    @Builder
    public Director(Long id, String directorFIO, String position, Set<Film> films) {
        super(id);
        this.directorFIO = directorFIO;
        this.position = position;
        this.films = films;

    }
}