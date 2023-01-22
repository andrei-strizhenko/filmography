package com.example.filmography.controller;

import com.example.filmography.model.User;
import com.example.filmography.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;


    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    //methods read:
    @Operation(description = "Получить список всех записей", method = "GetAll")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    //  @RequestMapping(value = "/list")
    public List<User> list() {
        return userRepository.findAll();
    }

    @Operation(description = "Получить запись по id", method = "GetOne")
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    //method create:
    @Operation(description = "Создать запись", method = "Create")
    @PostMapping
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

    //method update:
    @Operation(description = "Обновить запись", method = "Update")
    @PutMapping("/{id}")
    public User update(@RequestBody User user, @PathVariable Long id) {
        user.setId(id);
        return userRepository.save(user);
    }

    //method delete:
    @Operation(description = "Удалить запись", method = "Delete")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        //   userRepository.deleteById(userRepository.findById(id).orElseThrow()); //check user by this id and throw exception
        userRepository.deleteById(id); //check user by this id and throw exception


    }


}
