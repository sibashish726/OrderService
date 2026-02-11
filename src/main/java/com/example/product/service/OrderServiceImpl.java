package com.example.product.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.product.entity.Order;
import com.example.product.external.client.ProductService;
import com.example.product.model.OrderRequest;
import com.example.product.repository.OrderRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {
	
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ProductService productService;
	
	@Override
	public long createOrder(OrderRequest orderRequest) {
		log.info("Craeting order");
		productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());
		Order order= Order.builder()
				           .productId(orderRequest.getProductId())
				           .amount(orderRequest.getTotalAmount())
				           .quantity(orderRequest.getQuantity())
				           .OrderDate(Instant.now())
				           .orderStatus("Created")
				           .build();
		order= orderRepository.save(order);
		log.info("New order created for id "+order.getId());
		
		
		return order.getId();
	}

}
