package com.example.product.model;

import java.io.Serializable;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
   private long orderId;
   private Instant orderDate;
   private String orderStatus;
   private long amount;
   private ProductDetails productDetails;
   

   @Data
   @Builder
   @AllArgsConstructor
   @NoArgsConstructor
   public static class ProductDetails {
    private String productName;
    private long productId;
    private long quantity;
    private long price;
   }

}
