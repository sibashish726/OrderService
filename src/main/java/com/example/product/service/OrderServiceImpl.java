package com.example.product.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.stream.Collectors;
import com.example.product.entity.Order;
import com.example.product.exception.CustomException;
import com.example.product.external.client.PaymentService;
import com.example.product.external.client.ProductService;
import com.example.product.external.request.PaymentRequest;
import com.example.product.model.OrderRequest;
import com.example.product.model.OrderResponse;
import com.example.product.model.OrderResponse.ProductDetails;
import com.example.product.model.ProductResponse;
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
	private final RestTemplate restTemplate;
	
	
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

	    log.info("Invoking product service to fetch the product for id : "+ order.getProductId());
	    ProductResponse productResponse= restTemplate.getForObject("http://PRODUCT_SERVICE/product/getProductById"+order.getProductId(), ProductResponse.class);
	    OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails.builder()
	    		                                                                  .productName(productResponse.getProductName())
	    		                                                                  .productId(productResponse.getProductId())
	    		                                                                  .build();
	    OrderResponse orderResponse = OrderResponse.builder()
	                 .orderId(order.getId())
	                 .orderStatus(order.getOrderStatus())
	                 .amount(order.getAmount())
	                 .orderDate(Instant.now())
	                 .productDetails(productDetails)
	                 .build();

	    return orderResponse; 
	}

	@Override
	public List<OrderResponse> getAllOrders() {
		log.info("Fetching all orders from database");
	    List<Order> orders = orderRepository.findAll();

	    List<OrderResponse> orderResponses = orders.stream()
	            .map(order -> {
	                OrderResponse orderResponse = new OrderResponse();
	                BeanUtils.copyProperties(order, orderResponse);
	                return orderResponse;
	            })
	            .collect(Collectors.toList());

	    log.info("Service: Successfully fetched {} orders", orderResponses.size());
	    return orderResponses;
	}

}
