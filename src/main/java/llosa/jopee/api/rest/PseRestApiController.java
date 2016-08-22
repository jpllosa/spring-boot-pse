package llosa.jopee.api.rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.bson.BsonDateTime;
import org.bson.BsonDocument;
import org.bson.BsonNumber;
import org.bson.Document;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import llosa.jopee.PseInvestorApplication;
import llosa.jopee.model.PseEodq;
import llosa.jopee.model.PseStock;

@RestController
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

	@RequestMapping(value = "/api", method = RequestMethod.GET)
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
	// TODO
	@RequestMapping(value = "/api/nearestDate/{nearestDate}", method = RequestMethod.GET)
//	public PseEodq getNearestDate(@RequestParam(value="nearestDate") String nearestDate) {
	public PseEodq getNearestDate(@PathVariable String nearestDate) {
		MongoCollection<BsonDocument> collection = db.getCollection(eodqCollection, BsonDocument.class);
        FindIterable<BsonDocument> iterable = collection.find(
        		BsonDocument.parse("{Date: '" + nearestDate + "'}"))
        		.sort(BsonDocument.parse("{Date:-1}"))
        		.limit(1);
        MongoCursor<BsonDocument> cursor = iterable.iterator();
        
        BsonDocument doc = null;
        if (cursor.hasNext()) {
            doc = cursor.next();
        }
        
        cursor.close();
        log.info("getNearestDate: " + doc);
        if (doc != null) {
        	return new PseEodq(doc.getString("stock_symbol").getValue(), doc.getDateTime("Date"),
        			doc.getNumber("Open"), doc.getNumber("High"), doc.getNumber("Low"),
        			doc.getNumber("Close"), doc.getNumber("Volume"));
        }
        
        return PseEodq.NULL;
	}
	
	@RequestMapping(value = "/api/all-stock-names-and-symbols", method = RequestMethod.GET)
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
	
	@RequestMapping(value = "/api/all-stock-symbols", method = RequestMethod.GET)
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

}
