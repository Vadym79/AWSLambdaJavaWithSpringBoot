// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package software.amazonaws.example.product.handler;

import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

import software.amazonaws.example.product.dao.DynamoProductDao;
import software.amazonaws.example.product.entity.Product;

@Component
public class GetProductByIdHandler implements Function<APIGatewayProxyRequestEvent, Optional<Product>> {

	@Autowired
	private DynamoProductDao productDao;
	
    private static final Logger logger = LoggerFactory.getLogger(GetProductByIdHandler.class);

	@Override
	public Optional<Product> apply(APIGatewayProxyRequestEvent event) {
		String id = event.getPathParameters().get("id");
		Optional<Product> optionalProduct = productDao.getProduct(id);
		if (optionalProduct.isPresent())
			logger.info(" product : " + optionalProduct.get());
		else
			logger.info(" product not found ");
		return optionalProduct;
	}

}
