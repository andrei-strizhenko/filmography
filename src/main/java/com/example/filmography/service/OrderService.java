package com.example.filmography.service;

import com.example.filmography.model.Order;
import com.example.filmography.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getList(){
        return orderRepository.findAll();
    }
    public Order getOne(Long id){
        return orderRepository.findById(id).orElseThrow();
    }

    public Order create (Order order){
        return orderRepository.save(order);
    }

    public Order update (Order order){
        return orderRepository.save(order);
    }

    public void delete (Long id){
        orderRepository.delete(orderRepository.findById(id).orElseThrow());
    }
}
