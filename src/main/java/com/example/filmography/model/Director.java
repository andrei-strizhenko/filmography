package com.example.filmography.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "directors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "directors_seq", allocationSize = 1)

public class Director extends GenericModel{
 //   @Id
 //   @Column(name = "id")
 //   private Long id;

    @Column(name = "director_fio")
    private String directorFIO;

    @Column(name = "position")
    private String position;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "films_directors",
            joinColumns = @JoinColumn(name = "film_id"),
            foreignKey = @ForeignKey(name = "FK_FILMS_DIRECTORS"),
            inverseJoinColumns = @JoinColumn(name = "director_id"),
            inverseForeignKey = @ForeignKey(name = "FK_DIRECTORS_FILMS"))
    private Set<Film> films = new HashSet<>();

    public Director(Long id, LocalDateTime createdWhen, String directorFIO, String position){
        super(id, createdWhen);
        this.directorFIO = directorFIO;
        this.position = position;
    }

}
