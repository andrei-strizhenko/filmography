package com.example.filmography.controller;

import com.example.filmography.model.Order;
import com.example.filmography.repository.OrderRepository;
import com.example.filmography.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    //methods read:
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public List<Order> list() {
        return orderService.getList();
    }


    @GetMapping("/{id}")
    public Order getById(@PathVariable Long id) {
        return orderService.getOne(id);
    }

    //method create:
    @PostMapping
    public Order create(@RequestBody Order order) {
        return orderService.create(order);
    }

    //method update:
    @PutMapping("/{id}")
    public Order update(@RequestBody Order order, Long id) {
        order.setId(id);
        return orderService.update(order);
    }

    //method delete:
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        orderService.delete(id); //check user by this id and throw exception
    }

}