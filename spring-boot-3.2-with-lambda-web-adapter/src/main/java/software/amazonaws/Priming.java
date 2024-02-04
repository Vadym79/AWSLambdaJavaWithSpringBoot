package software.amazonaws;

import org.crac.Context;
import org.crac.Core;
import org.crac.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import software.amazonaws.example.product.dao.DynamoProductDao;

@Configuration
public class Priming implements Resource {

	@Autowired
	private DynamoProductDao productDao;

	private static final Logger logger = LoggerFactory.getLogger(Priming.class);

	public Priming() {
		logger.info("entered constructor for priming ");
		Core.getGlobalContext().register(this);
	}

	@Override
	public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
		logger.info("entered before checkpoint method ");
		productDao.getProduct("0");
		logger.info("finished finished before checkpoint method ");
	}

	@Override
	public void afterRestore(Context<? extends Resource> context) throws Exception {

	}
}