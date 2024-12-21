package software.amazonaws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

import software.amazonaws.example.product.controller.ProductController;


@Import({ProductController.class })
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class Application {
	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
    public static void main(String[] args) {
    	logger.info("entered main method ");    	
        SpringApplication.run(Application.class, args);
        logger.info("finished main method "); 
    }
}