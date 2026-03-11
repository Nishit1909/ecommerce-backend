package com.example.ecom.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OrderResponse(
        String orderId,
        String customerName,
        String email,
        String status,
        List<OrderItemResponse> items,
        LocalDate orderDate,
        BigDecimal totalAmount
) {}
