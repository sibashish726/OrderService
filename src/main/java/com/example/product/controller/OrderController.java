package com.example.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.product.model.OrderRequest;
import com.example.product.model.OrderResponse;
import com.example.product.service.OrderService;
import java.util.List;
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
   
   @GetMapping("/getOrderById/{orderId}")
   public ResponseEntity<OrderResponse> getOrderById(@PathVariable long orderId){
	   OrderResponse orderResponse= orderService.getOrderById(orderId);
	   return new ResponseEntity<>(orderResponse, HttpStatus.OK);       
   }
   
   @GetMapping("/getAllOrders")
   public ResponseEntity<List<OrderResponse>> getAllOrders() {
       List<OrderResponse> orderResponses = orderService.getAllOrders();
       return new ResponseEntity<>(orderResponses, HttpStatus.OK);
   }
   
   @PutMapping("/updateOrder/{orderId}")
   public ResponseEntity<Void> updateOrder(@PathVariable long orderId, @RequestBody OrderRequest orderRequest) {
       orderService.updateOrder(orderId, orderRequest);
       return new ResponseEntity<>(HttpStatus.OK);
   }

   @DeleteMapping("/deleteOrder/{orderId}")
   public ResponseEntity<Void> deleteOrderById(@PathVariable long orderId) {
       orderService.deleteOrderById(orderId);
       return new ResponseEntity<>(HttpStatus.OK);
   }
}
