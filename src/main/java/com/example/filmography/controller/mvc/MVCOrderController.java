package com.example.filmography.controller.mvc;

import com.example.filmography.dto.ExtendOrderDto;
import com.example.filmography.dto.OrderDto;
import com.example.filmography.mapper.FilmMapper;
import com.example.filmography.mapper.OrderMapper;
import com.example.filmography.model.Order;
import com.example.filmography.repository.OrderRepository;
import com.example.filmography.service.FilmService;
import com.example.filmography.service.OrderService;
import com.example.filmography.service.userDetails.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
public class MVCOrderController {

    private final FilmMapper filmMapper;
    private final FilmService filmService;
    private final OrderService service;
    private final OrderMapper mapper;
    private final OrderRepository orderRepository;

    public MVCOrderController(FilmMapper filmMapper, FilmService filmService, OrderService service, OrderMapper mapper,
                              OrderRepository orderRepository) {
        this.filmMapper = filmMapper;
        this.filmService = filmService;
        this.service = service;
        this.mapper = mapper;
        this.orderRepository = orderRepository;
    }

    @GetMapping("")
    public String getAll(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int pageSize,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        Page<Order> orderPage;
        if (getPrincipal().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            orderPage = service.listAllPaginatedForUsers(pageRequest, getPrincipal().getUserId());
        } else {
            orderPage = service.listAllPaginated(pageRequest);
        }
        List<OrderDto> orderDtos = orderPage
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute("orders", new PageImpl<>(orderDtos, pageRequest, orderPage.getTotalElements()));
        return "orders/viewAllOrders";
    }

    @GetMapping("/{orderId}")
    public String getOne(@PathVariable Long orderId, Model model) {
        Order order = service.getOne(orderId);
        if (!(getPrincipal().getUserId().equals(order.getUser().getId()))) {
            return HttpStatus.FORBIDDEN.toString();
        } else {
            model.addAttribute("order", mapper.toDto(order));
            model.addAttribute("isExtendable",
                    !order.isPurchased() &&
                            order.getRentDate().plusDays(order.getRentPeriod()).isAfter(LocalDate.now()));
            return "orders/viewOrder";
        }
    }

    @GetMapping("/get-film/{filmId}")
    public String getFilm(@PathVariable Long filmId, Model model) {
        model.addAttribute("film", filmMapper.toDto(filmService.getOne(filmId)));
        boolean isActiveRentOrder = service.isActiveRentOrder(getPrincipal().getUserId(),filmId);
        boolean isPurchasedOrder = service.isPurchasedOrder(getPrincipal().getUserId(), filmId);
        model.addAttribute("isActiveRent", isActiveRentOrder);
        model.addAttribute("isPurchased", isPurchasedOrder);
        if (isPurchasedOrder) {
            model.addAttribute("order",
                    service.findPurchasedOrderByUserIdAndFilmId(getPrincipal().getUserId(), filmId));
        } else {
            if (isActiveRentOrder) {
                model.addAttribute("order",
                        service.findRentOrderByUserIdAndFilmId(getPrincipal().getUserId(), filmId));
            }
        }
        return "userFilms/rentFilm";
    }

    @PostMapping("/get-film")
    public String getFilm(@ModelAttribute("orderForm") OrderDto orderDto) {
        orderDto.setUserId(Long.valueOf(getPrincipal().getUserId()));
        if (orderDto.getRentPeriod() == -1) {
            orderDto.setPurchased(true);
        }
        service.addOrder(orderDto);
        return "redirect:/orders/user-films/" + getPrincipal().getUserId();
    }

    @GetMapping("/user-films/{id}")
    public String userFilms(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int pageSize,
            @PathVariable Long id,
            Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<Order> orderPage = service.getUserOrdersPaginated(pageRequest, id);
        List<OrderDto> orderDtos = orderPage
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute("orders", new PageImpl<>(orderDtos, pageRequest, orderPage.getTotalElements()));
        model.addAttribute("userId", id);
        return "orders/viewAllOrders";
    }

    @PostMapping("/user-films/{id}/search")
    public String searchByTitle(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int pageSize,
            @PathVariable Long id,
            Model model,
            @ModelAttribute("searchOrders") OrderDto orderDto
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "rentDate"));
        Page<Order> orderPage;
        if (orderDto.getFilm().getTitle().trim().equals("")) {
            orderPage = service.getUserOrdersPaginated(pageRequest, id);
        } else {
            orderPage = service.findUserOrdersByTitleContainingPaginated(pageRequest, id, orderDto.getFilm().getTitle());
        }
        List<OrderDto> orderDtos = orderPage
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute("orders", new PageImpl<>(orderDtos, pageRequest, orderPage.getTotalElements()));
        model.addAttribute("userId", id);
        return "users/viewAllUserFilms";
    }

    private CustomUserDetails getPrincipal() {
        return (CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    @PostMapping("/extend")
    public String extendOrderRent(@ModelAttribute("extendOrderForm") ExtendOrderDto extendOrderDto) {
        Order order = service.getOne(extendOrderDto.getOrderId());
        if (!(order.isPurchased()) &&
                (order.getRentDate().plusDays(order.getRentPeriod())
                        .isAfter(LocalDate.now()))) {
            order.setRentPeriod(order.getRentPeriod() + extendOrderDto.getExtendRentPeriod());
            service.update(order);
            return "redirect:/orders/" + order.getId();
        } else {
            return HttpStatus.FORBIDDEN.toString();
        }
    }

    @PostMapping("/purchase")
    public String purchase(@ModelAttribute("extendOrderForm") ExtendOrderDto extendOrderDto) {
        Order order = service.getOne(extendOrderDto.getOrderId());
        if (order.isPurchased()) {
            return HttpStatus.FORBIDDEN.toString();
        } else {
           order.setRentPeriod(-1);
           order.setPurchased(true);
           service.update(order);
           return "redirect:/orders/" + order.getId();
        }
    }
}