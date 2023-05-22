package com.example.filmography.repository;

import com.example.filmography.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


@Repository
public interface UserRepository extends GenericRepository<User> {

    List<User> findAllByFirstName(String firstName);

    List<User> findAllByFirstNameAndMiddleName(String firstName, String middleName);

    @Query(nativeQuery = true, value = "  select * from users where created_by = :createdBy\n")  //такой запрос работает в 11 версии java вместо блочных тройных ковычек(""")
    List<User> findAllByCreatedBy(@Param(value = "createdBy") String createdBy);

    @Query(nativeQuery = true, value = "  select * from users where login = :login and is_deleted = false\n")  //такой запрос работает в 11 версии java
    User findUserByLoginAndIsDeletedFalse(@Param(value = "login") String login);

    User findUserByLogin(String login);

    User findByEmail(String email);

    User findByChangePasswordToken(String token);


}




