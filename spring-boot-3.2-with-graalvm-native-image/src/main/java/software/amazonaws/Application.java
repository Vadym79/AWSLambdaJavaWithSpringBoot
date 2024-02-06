package software.amazonaws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class Application {

	  private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
    public static void main(String[] args) {
    	
    	logger.info("started SpringBoot Application");
        SpringApplication.run(Application.class, args);
        logger.info("finished loading SpringBoot Application");
    }
    
   
}