package com.example.filmography.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "films")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name = "default_generator", sequenceName = "films_seq", allocationSize = 1)

public class Film extends GenericModel {
    //  @Id
    //  @Column(name = "id")
    //   private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "premier_year")
    private String premierYear;

    @Column(name = "country")
    private String country;

    @Column(name = "genre")
    @Enumerated
    private Genre genre;


    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JsonIgnore // убирает рекурсию
    @JoinTable(
            name = "films_directors",
            joinColumns = @JoinColumn(name = "film_id"),
            foreignKey = @ForeignKey(name = "FK_FILMS_DIRECTORS"),
            inverseJoinColumns = @JoinColumn(name = "director_id"),
            inverseForeignKey = @ForeignKey(name = "FK_DIRECTORS_FILMS"))
    private Set<Director> directors = new HashSet<>();

    @Builder
    public Film(Long id, String title, String premierYear, String country, Genre genre, Set<Director> directors) {
        super(id);
        this.title = title;
        this.premierYear = premierYear;
        this.country = country;
        this.genre = genre;
        this.directors = directors;
    }
}
