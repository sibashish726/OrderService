package com.example.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.product.model.OrderRequest;
import com.example.product.service.OrderService;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/order")
@Log4j2
public class OrderController {
	
   @Autowired	
   private OrderService orderService;
   
   @PostMapping("/createOrder")
   public ResponseEntity<Long> createOrder(@RequestBody OrderRequest orderRequest ){
	   long orderId= orderService.createOrder(orderRequest);
	   log.info("Order id "+orderId);
	   
	   return new ResponseEntity<>(orderId,HttpStatus.OK);   
   }
}
