package com.microservice.orderservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.microservice.orderservice.dto.OrderItemsDto;
import com.microservice.orderservice.dto.OrderRequest;
import com.microservice.orderservice.model.Order;
import com.microservice.orderservice.model.OrderItems;
import com.microservice.orderservice.repository.OrderRepository;
 
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        // orderRequest.getOrderItemsDtos().stream().map(orderitem
        // ->mapToDto(orderitem));
        List<OrderItems> orderItems = orderRequest.getOrderItemsDtos().stream().map(this::mapToDto).toList();
        order.setOrderItems(orderItems);

        orderRepository.save(order);

    }

    private OrderItems mapToDto(OrderItemsDto orderitemdto) {
        OrderItems orderItems = new OrderItems();
        orderItems.setPrice(orderitemdto.getPrice());
        orderItems.setQty(orderitemdto.getQty());
        orderItems.setSku(orderitemdto.getSku());
        return orderItems;

    }

}
