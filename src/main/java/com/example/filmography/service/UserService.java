package com.example.filmography.service;


import com.example.filmography.model.Role;
import com.example.filmography.model.User;
import com.example.filmography.repository.RoleRepository;
import com.example.filmography.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getList(){
        return userRepository.findAll();
    }

    public User getOne(Long id){
        return userRepository.findById(id).orElseThrow();
    }

    public User create (User user){
        return userRepository.save(user);
    }

    public User update (User user){
        return userRepository.save(user);
    }

    public void delete (Long id){
        userRepository.delete(userRepository.findById(id).orElseThrow());
    }
}
