// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package software.amazonaws.example.product.handler;

import java.math.BigDecimal;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

import software.amazonaws.example.product.dao.DynamoProductDao;
import software.amazonaws.example.product.entity.Product;

@Component
public class CreateProductHandler implements Function<APIGatewayProxyRequestEvent, String> {

  @Autowired
  private DynamoProductDao productDao;
  
  private static final Logger logger = LoggerFactory.getLogger(CreateProductHandler.class);

  @Override
  public String apply(APIGatewayProxyRequestEvent event) {
	String id=event.getPathParameters().get("id");
	logger.info("create product with id "+id);
	Product product= new Product(id, "Calender ", BigDecimal.valueOf(35.99));
    productDao.putProduct(product);
    logger.info("created product : "+product);
    return id;
  }
 
}
