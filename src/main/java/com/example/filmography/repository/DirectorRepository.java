package com.example.filmography.repository;

import com.example.filmography.model.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface DirectorRepository extends JpaRepository <Director, Long>{
    Set<Director> findAllByIdIn(Set<Long> ids);

    List<Director> findAllByDirectorFIO(String directorFIO);

}
