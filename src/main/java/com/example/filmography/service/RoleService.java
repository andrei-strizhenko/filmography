package com.example.filmography.service;

import com.example.filmography.model.Role;
import com.example.filmography.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository repository;


    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public List<Role> getList(){
        return repository.findAll();
    }

    public Role getOne(Long id){
        return repository.findById(id).orElseThrow();
    }

    public Role create (Role role){
        return repository.save(role);
    }

    public Role update (Role role){
        return repository.save(role);
    }

    public void delete (Long id){
        repository.delete(repository.findById(id).orElseThrow());
    }

}
