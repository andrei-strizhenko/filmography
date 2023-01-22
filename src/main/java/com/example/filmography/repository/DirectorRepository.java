package com.example.filmography.repository;

import com.example.filmography.model.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectorRepository extends JpaRepository <Director, Long>{
}
