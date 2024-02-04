package software.amazonaws.example.product.handler;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.nio.charset.Charset;


/**
 * Mock LambdaLogger object that prints output to the console
 */
public class MockLambdaConsoleLogger implements LambdaLogger {

    //-------------------------------------------------------------
    // Implementation - LambdaLogger
    //-------------------------------------------------------------

    @Override
    public void log(String s) {
        System.out.println(s);
    }


    @Override
    public void log(byte[] bytes) {
        System.out.println(new String(bytes, Charset.defaultCharset()));
    }
}
