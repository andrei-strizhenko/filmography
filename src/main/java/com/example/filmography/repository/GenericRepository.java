package com.example.filmography.repository;

import com.example.filmography.model.GenericModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenericRepository < T extends GenericModel> extends JpaRepository< T, Long> {
}
