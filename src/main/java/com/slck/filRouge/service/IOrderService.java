package com.slck.filRouge.service;

import com.slck.filRouge.model.Order;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IOrderService {
    List<Order> findAll();

    Order findById(Long id);

    Order save(Order order);

    void delete(Long id);

    Order findByCustomer(Long id);

    List<Order> findByStatus(String status);

    void deleteByCustomer(Long id);

    void updateOrder(Order order);

    void deleteOrderById(Long id);


    List<Order> findAllWithCustomer();
}
