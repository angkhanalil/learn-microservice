package com.microservice.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.microservice.orderservice.dto.InventoryResponse;
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
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest) {
        Order orderIn = new Order();
        orderIn.setOrderNumber(UUID.randomUUID().toString());

        // orderRequest.getOrderItemsDtos().stream().map(orderitem
        // ->mapToDto(orderitem));
        List<OrderItems> orderItems = orderRequest.getOrderItemsDtos().stream().map(this::mapToDto).toList();

        // orderIn.setOrderItems(orderItems);
        // System.out.println("order insert :" + orderIn);
 /// Check Stock 
        // Boolean result = webClient.get().uri("http://localhost:8082/api/inventory")
        // .retrieve()
        // .bodyToMono(Boolean.class).block();


        List<String> skuCodes = orderIn.getOrderItems().stream().map(OrderItems::getSku).toList();

        InventoryResponse[] inventoryResponsesArray = webClient.get().uri("http://localhost:8082/api/inventory",
                uriBuilder -> uriBuilder.queryParam("sku", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class).block();

             Boolean allProductInStock =   Arrays.stream(inventoryResponsesArray).allMatch(InventoryResponse::isInStock);
        if (allProductInStock) {

            orderRepository.save(orderIn);
        } else {
            throw new IllegalArgumentException("Product is not in Stock");
        }

    }

    private OrderItems mapToDto(OrderItemsDto orderitemdto) {
        OrderItems orderItems = new OrderItems();
        orderItems.setPrice(orderitemdto.getPrice());
        orderItems.setQty(orderitemdto.getQty());
        orderItems.setSku(orderitemdto.getSku());
        return orderItems;

    }

}
