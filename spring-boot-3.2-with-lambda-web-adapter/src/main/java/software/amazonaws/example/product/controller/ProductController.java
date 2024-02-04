package software.amazonaws.example.product.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import software.amazonaws.example.product.dao.DynamoProductDao;
import software.amazonaws.example.product.entity.Product;

@RestController
@EnableWebMvc
public class ProductController {

	@Autowired
	private DynamoProductDao productDao;

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	@RequestMapping(path = "/products/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Optional<Product> getProductById(@PathVariable("id") String id) {
		logger.info("entered getProductById method with id " + id);
		Optional<Product> optionalProduct = productDao.getProduct(id);
		if (optionalProduct.isPresent())
			logger.info(" product : " + optionalProduct.get());
		else
			logger.info(" product not found ");
		return optionalProduct;

	}

	@RequestMapping(path = "/products", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void createProduct(@RequestBody Product product) {
		String id=product.getId();		
		logger.info("entered createProduct method with id " + id);
		product.setId(id);
		productDao.putProduct(product);
	}
}
