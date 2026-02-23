package com.example.product.service;

import com.example.product.model.OrderRequest;
import com.example.product.model.OrderResponse;

public interface OrderService {

	long createOrder(OrderRequest orderRequest);

	OrderResponse getOrderById(long orderId);

}
