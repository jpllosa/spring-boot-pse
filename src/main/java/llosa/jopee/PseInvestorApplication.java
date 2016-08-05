package llosa.jopee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class PseInvestorApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(PseInvestorApplication.class, args);
		Logger logger = getLogger(PseInvestorApplication.class);
	    logger.info("PSE Investor Application started");

	}

	public static Logger getLogger(Class<?> clazz) {
		return LoggerFactory.getLogger(clazz);
	}
}
