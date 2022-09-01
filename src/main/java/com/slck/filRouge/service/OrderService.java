/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.slck.filRouge.service;

import com.slck.filRouge.model.Order;
import com.slck.filRouge.repository.OrderRepository;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 *
 * @author maxla
 */
@Service
public class OrderService implements IOrderService {

    final OrderRepository orderRepository;
    final ICustomerService customerService;
    final IOrderService orderService;

    @Autowired
    public OrderService(@Lazy ICustomerService customerService, @Lazy OrderRepository orderRepository, @Lazy IOrderService orderService){
        super();
        this.customerService = customerService;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order save(Order order) {
        orderRepository.save(order);
        return order;
    }

    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Order findByCustomer(Long id) {
        return (Order) orderRepository.findByCustomer(id);
    }

    @Override
    public List<Order> findByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    public void deleteByCustomer(Long id) {
        orderRepository.deleteByCustomer(id);
    }

    @Override
    public void updateOrder(Order order) {
        orderRepository.save(order);
    }

    @Override
    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> findAllWithCustomer() {
        return orderRepository.findAllWithCustomer();
    }

}
