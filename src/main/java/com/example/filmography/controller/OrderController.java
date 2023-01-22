package com.example.filmography.controller;

import com.example.filmography.model.Film;
import com.example.filmography.model.Order;
import com.example.filmography.repository.FilmRepository;
import com.example.filmography.repository.OrderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderRepository orderRepository;


    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    //methods read:
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public List<Order> list() {
        return orderRepository.findAll();
    }


    @GetMapping("/{id}")
    public Order getById(@PathVariable Long id) {
        return orderRepository.findById(id).orElseThrow();
    }

    //method create:
    @PostMapping
    public Order create(@RequestBody Order order) {
        return orderRepository.save(order);
    }

    //method update:
    @PutMapping("/{id}")
    public Order update(@RequestBody Order order, Long id) {
        order.setId(id);
        return orderRepository.save(order);
    }

    //method delete:
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        orderRepository.delete(orderRepository.findById(id).orElseThrow()); //check user by this id and throw exception
    }

}