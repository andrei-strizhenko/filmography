package com.example.filmography.controller;

import com.example.filmography.model.Role;
import com.example.filmography.repository.RoleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    private final RoleRepository roleRepository;


    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    //methods read:
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Role> list() {
        return roleRepository.findAll();
    }


    @GetMapping("/{id}")
    public Role getById(@PathVariable Long id) {
        return roleRepository.findById(id).orElseThrow();
    }

    //method create:
     @PostMapping
    public Role create(@RequestBody Role role) {
        return roleRepository.save(role);
    }

    //method update:
    @PutMapping("/{id}")
    public Role update(@RequestBody Role role, Long id) {
        role.setId(id);
        return roleRepository.save(role);
    }

    //method delete:
     @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        roleRepository.delete(roleRepository.findById(id).orElseThrow()); //check user by this id and throw exception
    }
}
