package com.example.filmography.controller;

import com.example.filmography.model.User;
import com.example.filmography.repository.UserRepository;
import com.example.filmography.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }


    //methods read:
    @Operation(description = "Получить список всех записей", method = "GetAll")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    //  @RequestMapping(value = "/list")
    public List<User> list() {
        return userService.getList();
    }

    @Operation(description = "Получить запись по id", method = "GetOne")
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getOne(id);
    }

    //method create:
    @Operation(description = "Создать запись", method = "Create")
    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    //method update:
    @Operation(description = "Обновить запись", method = "Update")
    @PutMapping("/{id}")
    public User update(@RequestBody User user, @PathVariable Long id) {
        user.setId(id);
        return userService.update(user);
    }

    //method delete:
    @Operation(description = "Удалить запись", method = "Delete")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        //   userRepository.deleteById(userRepository.findById(id).orElseThrow()); //check user by this id and throw exception
        userService.delete(id); //check user by this id and throw exception


    }


}
