package com.example.product.external.decoder;

import java.io.IOException;

import com.example.product.exception.CustomException;
import com.example.product.external.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CustomErrorDecoder  implements ErrorDecoder{

	@Override
	public Exception decode(String s, Response response) {
		// TODO Auto-generated method stub
		ObjectMapper obj= new ObjectMapper();
		try {
			ErrorResponse errorResponse= obj.readValue(response.body().asInputStream(), ErrorResponse.class);
			return new CustomException(errorResponse.getErrorMessage(),errorResponse.getErrorCode(), response.status());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new CustomException("Internal server error","INTERNAL_SERVER_ERROR",500);
		} 
		
	}

}
