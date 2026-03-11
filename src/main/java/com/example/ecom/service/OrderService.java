package com.example.ecom.service;

import com.example.ecom.model.Order;
import com.example.ecom.model.OrderItem;
import com.example.ecom.model.Product;
import com.example.ecom.model.dto.OrderItemRequest;
import com.example.ecom.model.dto.OrderItemResponse;
import com.example.ecom.model.dto.OrderRequest;
import com.example.ecom.model.dto.OrderResponse;
import com.example.ecom.repo.OrderRepo;
import com.example.ecom.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private OrderRepo orderRepo;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        String orderId = "ORD" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        order.setOrderId(orderId);
        order.setCustomerName(orderRequest.customerName());
        order.setEmail(orderRequest.email());
        order.setStatus("PLACED");
        order.setOrderDate(LocalDate.now());

        List<OrderItem> orderItems = new ArrayList<>();

        // Build order items from the request
        for (OrderItemRequest itemReq : orderRequest.items()) {
            Product product = productRepo.findById(itemReq.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Reduce stock
            product.setStockQuantity(product.getStockQuantity() - itemReq.quantity());
            productRepo.save(product);

            // Create OrderItem
            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemReq.quantity())
                    .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity())))
                    .order(order)
                    .build();

            orderItems.add(orderItem);
        }

        order.setOrderitems(orderItems);

        // Save order
        Order savedOrder = orderRepo.save(order);

        // Build response
        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for (OrderItem item : savedOrder.getOrderitems()) {
            OrderItemResponse response = new OrderItemResponse(
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getTotalPrice()
            );
            itemResponses.add(response);
        }

        // ✅ Calculate total using lambda (safe and compatible)
        BigDecimal totalAmount = itemResponses.stream()
                .map(r -> r.totalPrice()) // using lambda instead of method reference
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Return response
        return new OrderResponse(
                savedOrder.getOrderId(),
                savedOrder.getCustomerName(),
                savedOrder.getEmail(),
                savedOrder.getStatus(),
                itemResponses,
                savedOrder.getOrderDate(),
                totalAmount
        );
    }

    public List<OrderResponse> getAllordersResponses() {
        List<Order> orders = orderRepo.findAll();
        List<OrderResponse> responses = new ArrayList<>();

        for (Order order : orders) {
            List<OrderItemResponse> itemResponses = new ArrayList<>();

            for (OrderItem item : order.getOrderitems()) {
                OrderItemResponse response = new OrderItemResponse(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getTotalPrice()
                );
                itemResponses.add(response);
            }

            BigDecimal totalAmount = itemResponses.stream()
                    .map(r -> r.totalPrice())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            OrderResponse orderResponse = new OrderResponse(
                    order.getOrderId(),
                    order.getCustomerName(),
                    order.getEmail(),
                    order.getStatus(),
                    itemResponses,
                    order.getOrderDate(),
                    totalAmount
            );

            responses.add(orderResponse);
        }

        return responses;
    }
}
