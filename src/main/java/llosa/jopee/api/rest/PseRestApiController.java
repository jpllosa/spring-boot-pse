package llosa.jopee.api.rest;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import llosa.jopee.PseInvestorApplication;
import llosa.jopee.model.PseStock;

@RestController
public class PseRestApiController {
	
	Logger log = PseInvestorApplication.getLogger(PseRestApiController.class);

	@RequestMapping(value = "/api", method = RequestMethod.GET)
	public PseStock getStockNameBySymbol(@RequestParam(value="symbol") String symbol) {
		// lookup MongoDb here
		
		if (symbol.equals("AC")) {
			return new PseStock("AC", "Ayala Corporation");
		} else if (symbol.equals("ALI")) {
			return new PseStock("AC", "Ayala Land, Inc.");
		}
		
		return PseStock.NULL;
	}

}
