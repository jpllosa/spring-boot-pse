package llosa.jopee.api.rest;

import java.net.URLDecoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.bson.BsonDocument;
import org.bson.Document;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import llosa.jopee.PseInvestorApplication;
import llosa.jopee.model.PseEodq;
import llosa.jopee.model.PseStock;

@RestController
@RequestMapping("/api")
public class PseRestApiController {
	
	private final String databaseName    = "philippine_stock_exchange";
	private final String eodqCollection  = "pse_eodq";
	private final String stockCollection = "pse_stock";
	
	Logger log = PseInvestorApplication.getLogger(PseRestApiController.class);
	
	MongoClient mongoClient;
	MongoDatabase db;
	
	public PseRestApiController() {
		mongoClient = new MongoClient();
		db = mongoClient.getDatabase(databaseName);
	}
	
	@PreDestroy
	void destroy() {
		mongoClient.close();
	}

	// trying out @RequestParam but this should be better with @PathVariable
	@RequestMapping(method = RequestMethod.GET)
	public PseStock getStockNameBySymbol(@RequestParam(value="symbol") String symbol) {
		// lookup MongoDb here
		MongoCollection<BsonDocument> collection = db.getCollection(stockCollection, BsonDocument.class);
        FindIterable<BsonDocument> iterable = collection.find(
        		BsonDocument.parse("{stock_symbol: '" + symbol + "'}"));
        MongoCursor<BsonDocument> cursor = iterable.iterator();
        
        BsonDocument doc = null;
        if (cursor.hasNext()) {
            doc = cursor.next();
        }
        
        cursor.close();
        
        if (doc != null) {
        	return new PseStock(doc.getString("stock_symbol").getValue(),
	        		doc.getString("stock_name").getValue());
        }
        
        return PseStock.NULL;
	}
	
	@RequestMapping(value = "/startDate/{startDate}", method = RequestMethod.GET)
	public PseEodq getNearestDate(@PathVariable String startDate) {
		MongoCollection<BsonDocument> collection = db.getCollection(eodqCollection, BsonDocument.class);
		LocalDate localDate = LocalDate.parse(startDate);
        FindIterable<BsonDocument> iterable = collection.find(
        		BsonDocument.parse("{'Date': ISODate('" + localDate + "T16:00:00.000Z')}"))
        		.limit(1);
        MongoCursor<BsonDocument> cursor = iterable.iterator();
        
        BsonDocument doc = null;
        if (cursor.hasNext()) {
            doc = cursor.next();
        }
        
        cursor.close();

        if (doc != null) {
        	PseEodq temp =  new PseEodq(doc.getString("stock_symbol").getValue(), doc.getDateTime("Date"),
        			doc.getNumber("Open"), doc.getNumber("High"), doc.getNumber("Low"),
        			doc.getNumber("Close"), doc.getNumber("Volume"));
        	return temp;
        }
        
        return PseEodq.NULL;
	}
	
	@RequestMapping(value = "/all-stock-names-and-symbols", method = RequestMethod.GET)
	public List<PseStock> getAllStockNamesAndSymbols() {
		MongoCollection<BsonDocument> collection = db.getCollection(stockCollection, BsonDocument.class);
        FindIterable<BsonDocument> iterable = collection.find(BsonDocument.parse("{}"))
        		.sort(new Document("stock_symbol", 1));
        MongoCursor<BsonDocument> cursor = iterable.iterator();

        BsonDocument doc = null;
        ArrayList<PseStock> pseStocks = new ArrayList<PseStock>();
        while (cursor.hasNext()) {
        	doc = cursor.next();
        	pseStocks.add(new PseStock(doc.getString("stock_symbol").getValue(),
	        		doc.getString("stock_name").getValue()));
        }
		
        cursor.close();
        
        return pseStocks;
	}
	
	@RequestMapping(value = "/all-stock-symbols", method = RequestMethod.GET)
	public List<PseStock> getAllStockSymbols() {
		MongoCollection<BsonDocument> collection = db.getCollection(stockCollection, BsonDocument.class);
        FindIterable<BsonDocument> iterable = collection.find(BsonDocument.parse("{}, {stock_symbol:1}"))
        		.sort(new Document("stock_symbol", 1));
        MongoCursor<BsonDocument> cursor = iterable.iterator();

        BsonDocument doc = null;
        ArrayList<PseStock> pseStocks = new ArrayList<PseStock>();
        while (cursor.hasNext()) {
        	doc = cursor.next();
        	pseStocks.add(new PseStock(doc.getString("stock_symbol").getValue(),""));
        }
		
        cursor.close();
        
        return pseStocks;
	}

	@RequestMapping(value = "/closingPrice/{date}/{stockSymbol}", method = RequestMethod.GET)
	public PseEodq getClosingPrice(@PathVariable String date, @PathVariable String stockSymbol) {
		MongoCollection<BsonDocument> collection = db.getCollection(eodqCollection, BsonDocument.class);
		LocalDate localDate = LocalDate.parse(date);
        FindIterable<BsonDocument> iterable = collection.find(
        		BsonDocument.parse("{'Date': ISODate('" + localDate + "T16:00:00.000Z'), "
        				+ "'stock_symbol': '" + stockSymbol + "'}"))
        		.limit(1);
        MongoCursor<BsonDocument> cursor = iterable.iterator();
        
        BsonDocument doc = null;
        if (cursor.hasNext()) {
            doc = cursor.next();
        }
        
        cursor.close();

        if (doc != null) {
        	PseEodq temp =  new PseEodq(doc.getString("stock_symbol").getValue(), doc.getDateTime("Date"),
        			doc.getNumber("Open"), doc.getNumber("High"), doc.getNumber("Low"),
        			doc.getNumber("Close"), doc.getNumber("Volume"));
        	return temp;
        }
        
        return PseEodq.NULL;
	}
	
	@RequestMapping(value = "/chart-data", method = RequestMethod.POST)
	public List<PseEodq> getChartData(@RequestBody String data) throws Exception {
		ArrayList<PseEodq> quotes = new ArrayList<PseEodq>();
		data = URLDecoder.decode(data, "UTF-8");
		//{startDate: "2005-02-17", yearsHeld: 3, stockSymbols: ["SMC","MBT","GLO"]}=
		//what's with the equal sign at the end? it's verified that it was not sent.
		data = data.substring(0, data.length()-1);
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = mapper.readValue(data, new TypeReference<Map<String, Object>>(){});
		String startDate = (String) map.get("startDate");
		int yearsHeld = (int) map.get("yearsHeld");
		List<String> stockSymbols = (List<String>) map.get("stockSymbols");
		log.info("startDate: " + startDate);
		log.info("yearsHeld: " + yearsHeld);
		log.info("stockSymbols: " + stockSymbols);
		
		//query mongodb
		MongoCollection<BsonDocument> collection = db.getCollection(eodqCollection, BsonDocument.class);
		LocalDate startingDate = LocalDate.parse(startDate);
		LocalDate endingDate = startingDate.plusYears(yearsHeld);
		
		long startTime = System.currentTimeMillis();
		
		for (String symbol : stockSymbols) {
			FindIterable<BsonDocument> iterable = collection.find(
					BsonDocument.parse("{'Date': { $gte: ISODate('" + startingDate + "T16:00:00.000Z'), "
							+ " $lt : ISODate('" + endingDate + "T16:00:00.000Z')}, "
	        				+ "'stock_symbol': '" + symbol + "'}"))
				;
			
	        MongoCursor<BsonDocument> cursor = iterable.iterator();
	        
	        BsonDocument doc = null;
	        while (cursor.hasNext()) {
	        	doc = cursor.next();
	        	quotes.add(new PseEodq(doc.getString("stock_symbol").getValue(), doc.getDateTime("Date"),
	        			doc.getNumber("Open"), doc.getNumber("High"), doc.getNumber("Low"),
	        			doc.getNumber("Close"), doc.getNumber("Volume")));
	        }
	        
	        cursor.close();
			
		}
		
		long endTime = System.currentTimeMillis();
		log.info("elapsed time (sec): " + ((double)(endTime - startTime) / 1000));
		log.info("size: " + quotes.size());
		
		return quotes;
	}
}
