package com.example.filmography.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "films")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "films_seq", allocationSize = 1)

public class Film {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "premier_year")
    private String premierYear;

    @Column(name = "genre")
    private String genre;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "films_directors",
            joinColumns = @JoinColumn(name = "film_id"),
            foreignKey = @ForeignKey(name = "FK_FILMS_DIRECTORS"),
            inverseJoinColumns = @JoinColumn(name = "director_id"),
            inverseForeignKey = @ForeignKey(name = "FK_DIRECTORS_FILMS"))
    private Set<Director> directors = new HashSet<>();

    @Builder
    public Film( String title, String premierYear, String genre) {
        //  super(id);
      //  this.id = id;
        this.title = title;
        this.premierYear = premierYear;
        this.genre = genre;
        //    this.users = users;
    }
}
