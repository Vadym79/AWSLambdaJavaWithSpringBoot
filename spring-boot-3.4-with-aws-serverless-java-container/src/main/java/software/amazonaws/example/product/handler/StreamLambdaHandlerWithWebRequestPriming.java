package software.amazonaws.example.product.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.crac.Core;
import org.crac.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.ApiGatewayRequestIdentity;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyRequestContext;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import software.amazonaws.Application;


public class StreamLambdaHandlerWithWebRequestPriming implements RequestStreamHandler, Resource {
	
	
	private static final Logger logger = LoggerFactory.getLogger(StreamLambdaHandlerWithWebRequestPriming.class);
	
    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
    static {
        try {
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(Application.class);
            // For applications that take longer than 10 seconds to start, use the async builder:
            // handler = new SpringBootProxyHandlerBuilder<AwsProxyRequest>()
            //                    .defaultProxy()
            //                    .asyncInit()
            //                    .springBootApplication(Application.class)
            //                    .buildAndInitialize();
        } catch (ContainerInitializationException e) {
            // if we fail here. We re-throw the exception to force another cold start
            e.printStackTrace();
            throw new RuntimeException("Could not initialize Spring Boot application", e);
        }
    }
    
	
	public StreamLambdaHandlerWithWebRequestPriming () {
		Core.getGlobalContext().register(this);
	}


    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException {
    	logger.info("entered generic stream lambda handler");
        handler.proxyStream(inputStream, outputStream, context);
    }
    
	@Override
	public void beforeCheckpoint(org.crac.Context<? extends Resource> context) throws Exception {
		 logger.info("Before Checkpoint");
		 handler.proxy(getAwsProxyRequest(), new MockLambdaContext());
		 /*
		 handler.proxyStream(new ByteArrayInputStream(getAPIGatewayRequest().getBytes(StandardCharsets.UTF_8)), 
				 new ByteArrayOutputStream(), new MockLambdaContext());
	     */
		 logger.info("After Checkpoint"); 
	}

	@Override
	public void afterRestore(org.crac.Context<? extends Resource> context) throws Exception {
		logger.info("After Restore");	
	}
	
    private static AwsProxyRequest getAwsProxyRequest () {
    	final AwsProxyRequest awsProxyRequest = new AwsProxyRequest ();
    	awsProxyRequest.setHttpMethod("GET");
    	awsProxyRequest.setPath("/products/0");
    	awsProxyRequest.setResource("/products/{id}");
    	awsProxyRequest.setPathParameters(Map.of("id","0"));
    	final AwsProxyRequestContext awsProxyRequestContext = new AwsProxyRequestContext();
    	final ApiGatewayRequestIdentity apiGatewayRequestIdentity= new ApiGatewayRequestIdentity();
    	apiGatewayRequestIdentity.setApiKey("blabla");
    	awsProxyRequestContext.setIdentity(apiGatewayRequestIdentity);
    	awsProxyRequest.setRequestContext(awsProxyRequestContext);
    	return awsProxyRequest;		
    }
}