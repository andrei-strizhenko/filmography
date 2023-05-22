package com.example.filmography;


import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
@Slf4j
public class FilmographyApplication {


    public static void main(String[] args) {
        SpringApplication.run(FilmographyApplication.class, args);
        log.info("address swwager-ui: http://localhost:9095/swagger-ui/index.html");
    }



}
