package com.example.product.external.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.product.exception.CustomException;
import com.example.product.external.request.PaymentRequest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@CircuitBreaker(name= "external", fallbackMethod = "fallback")
@FeignClient(name="PAYMENT-SERVICE/payment")
public interface PaymentService {
	 @PostMapping("/doPayment")
	 public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest);
	 default void fallback(Exception e) {
		 throw new CustomException("Payment service is not available", "UNAVAILABLE", 5000);
	 }
}
