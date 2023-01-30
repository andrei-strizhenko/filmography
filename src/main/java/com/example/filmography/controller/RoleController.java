package com.example.filmography.controller;

import com.example.filmography.model.Role;
import com.example.filmography.repository.RoleRepository;
import com.example.filmography.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
  //  private final RoleRepository roleRepository;
    private final RoleService roleService;


    public RoleController(RoleService roleService) {
  //      this.roleRepository = roleRepository;
        this.roleService = roleService;
    }


    //methods read:
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Role> list() {
        return roleService.getList();
    }


    @GetMapping("/{id}")
    public Role getById(@PathVariable Long id) {
        return roleService.getOne(id);
    }

    //method create:
     @PostMapping
    public Role create(@RequestBody Role role) {
        return roleService.create(role);
    }

    //method update:
    @PutMapping("/{id}")
    public Role update(@RequestBody Role role, Long id) {
        role.setId(id);
        return roleService.update(role);
    }

    //method delete:
     @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        roleService.delete(id); //check user by this id and throw exception
    }
}
