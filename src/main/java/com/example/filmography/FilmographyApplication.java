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

 /*   @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setCreatedWhen(LocalDateTime.now());
        user.setAddress("Moscow");
        user.setBirthDate(LocalDate.of(1979, 1, 10));
        user.setEmail("New@yandex.ru");
        user.setFirstName("Name");
        user.setLastName("Name1");
        user.setMiddleName("Name2");
        user.setLogin("Test");
        user.setPassword("Test");
        user.setPhone("Test");
        user.setRole(new Role(1L, "USER", "user"));


    }*/

}
