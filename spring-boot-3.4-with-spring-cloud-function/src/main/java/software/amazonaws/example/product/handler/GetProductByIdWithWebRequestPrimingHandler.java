// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package software.amazonaws.example.product.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.crac.Core;
import org.crac.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.adapter.aws.FunctionInvoker;
import org.springframework.stereotype.Component;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent.ProxyRequestContext;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent.RequestIdentity;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.http.HttpStatusCode;
import software.amazonaws.example.product.dao.DynamoProductDao;
import software.amazonaws.example.product.entity.Product;

@Component
public class GetProductByIdWithWebRequestPrimingHandler implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>, Resource {

	@Autowired
	private DynamoProductDao productDao;
	
	@Autowired
	private ObjectMapper objectMapper;
	
    private static final Logger logger = LoggerFactory.getLogger(GetProductByIdWithWebRequestPrimingHandler.class);
	
	public GetProductByIdWithWebRequestPrimingHandler () {
		Core.getGlobalContext().register(this);
	}
	
	@Override
	public void beforeCheckpoint(org.crac.Context<? extends Resource> context) throws Exception {
		 logger.info("entered beforeCheckpoint method for priming");
		 new FunctionInvoker().handleRequest(new ByteArrayInputStream(this.getAPIGatewayProxyRequestEventAsJson().getBytes(StandardCharsets.UTF_8)), 
				 new ByteArrayOutputStream(), new MockLambdaContext());
	}

	@Override
	public void afterRestore(org.crac.Context<? extends Resource> context) throws Exception {	
	
	}
	
	/*
	private static String getAPIGatewayRequest () {
		StringBuilder sb = new StringBuilder();
	    sb.append("{\n")
		.append(" \"resource\": \"/products/{id}\",\n")
		.append("  \"path\": \"/products/0\",\n")
		.append("   \"httpMethod\": \"GET\",\n")
		.append("   \"pathParameters\": {\n")
		.append("      \"id\": \"0\"  \n")
		.append(" }, \n")
		.append("  \"requestContext\": {\n")
		.append("     \"identity\": {\n")
	    .append("          \"apiKey\": \"blabla\"\n")
	    .append("      }\n")
		.append(" }\n")
		.append("}");
	    return sb.toString();
	}
	*/
	
	@SuppressWarnings("unused")
	private static String getAPIGatewayRequestMultiLine () {
		 return  """
		 		{
		          "resource": "/productsWithWebRequestPriming/{id}",
		          "path":  "/productsWithWebRequestPriming/0",
		          "httpMethod": "GET",
		          "pathParameters": {
		                "id": "0" 
		            },
		           "requestContext": {
		              "identity": {
	                  "apiKey": "blabla"
	                }
		          }
		        }
	     """;
	}
	
    private String getAPIGatewayProxyRequestEventAsJson() throws Exception{
    	final APIGatewayProxyRequestEvent proxyRequestEvent = new APIGatewayProxyRequestEvent ();
    	proxyRequestEvent.setHttpMethod("GET");
    	proxyRequestEvent.setPathParameters(Map.of("id","0"));
        /*
    	proxyRequestEvent.setResource("/productsWithWebRequestPriming/{id}");
        proxyRequestEvent.setPath("/productsWithWebRequestPriming/0");
    
       
    	final ProxyRequestContext proxyRequestContext = new ProxyRequestContext();
    	final RequestIdentity requestIdentity= new RequestIdentity();
    	requestIdentity.setApiKey("blabla");
    	proxyRequestContext.setIdentity(requestIdentity);
    	proxyRequestEvent.setRequestContext(proxyRequestContext);
    	*/
    	return objectMapper.writeValueAsString(proxyRequestEvent);		
    }


	@Override
	public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent requestEvent) {
		String id = requestEvent.getPathParameters().get("id");
		Optional<Product> optionalProduct = productDao.getProduct(id);
		try {
			if (optionalProduct.isEmpty()) {
				logger.info(" product with id " + id + " not found ");
				return new APIGatewayProxyResponseEvent().withStatusCode(HttpStatusCode.NOT_FOUND)
						.withBody("Product with id = " + id + " not found");
			}
			logger.info(" product " + optionalProduct.get() + " found ");
			return new APIGatewayProxyResponseEvent().withStatusCode(HttpStatusCode.OK)
					.withBody(objectMapper.writeValueAsString(optionalProduct.get()));
		} catch (Exception je) {
			je.printStackTrace();
			return new APIGatewayProxyResponseEvent().withStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR)
					.withBody("Internal Server Error :: " + je.getMessage());
		}
	}


}