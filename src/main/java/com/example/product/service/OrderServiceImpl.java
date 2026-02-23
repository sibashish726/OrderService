package com.example.product.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.product.entity.Order;
import com.example.product.exception.CustomException;
import com.example.product.external.client.PaymentService;
import com.example.product.external.client.ProductService;
import com.example.product.external.request.PaymentRequest;
import com.example.product.model.OrderRequest;
import com.example.product.model.OrderResponse;
import com.example.product.repository.OrderRepository;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ProductService productService;
	
	private final PaymentService paymentService;
	
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
		log.info("Calling payment service to process payment");
		PaymentRequest paymentRequest= PaymentRequest.builder()
				                                      .orderId(order.getId())
				                                      .paymentmode(orderRequest.getPaymentMode())
				                                      .amount(orderRequest.getTotalAmount())
				                                      .build();
		String orderStatus=null;
		try {
			paymentService.doPayment(paymentRequest);
			log.info("Payment completed");
			orderStatus="PLACED";
		} catch (Exception e) {
			log.info("Error occured while posting payment");
			orderStatus="PAYMENT_FAILED";
		}
		
		order.setOrderStatus(orderStatus);
		orderRepository.save(order);
		log.info("New order created for id "+order.getId());
		
		return order.getId();
	}

	@Override
	public OrderResponse getOrderById(long orderId) {
	    log.info("get order details for order id: " + orderId);
	    Order order = orderRepository.findById(orderId)
	            .orElseThrow(() -> new CustomException("Order not found with id " + orderId, "ORDER_NOT_FOUND", 404));

	    OrderResponse orderResponse = OrderResponse.builder()
	                 .orderId(order.getId())
	                 .orderStatus(order.getOrderStatus())
	                 .amount(order.getAmount())
	                 .orderDate(Instant.now())
	                 .build();

	    return orderResponse; 
	}

}
