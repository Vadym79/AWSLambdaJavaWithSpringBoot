FROM public.ecr.aws/lambda/java:21
  
# Copy function code and runtime dependencies from Maven layout
COPY target/classes ${LAMBDA_TASK_ROOT}
COPY target/classes/lib/* ${LAMBDA_TASK_ROOT}/lib/
    
# Set the CMD to your handler (could also be done as a parameter override outside of the Dockerfile)
# CMD [ "software.amazonaws.example.product.handler.StreamLambdaHandler::handleRequest" ]