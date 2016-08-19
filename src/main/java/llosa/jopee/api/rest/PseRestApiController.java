package llosa.jopee.api.rest;

import java.util.ArrayList;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.Document;
import org.slf4j.Logger;
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
import llosa.jopee.model.PseStock;

@RestController
public class PseRestApiController {
	
	private final String databaseName    = "philippine_stock_exchange";
	private final String eodqCollection  = "pse_eodq";
	private final String stockCollection = "pse_stock";
	
	Logger log = PseInvestorApplication.getLogger(PseRestApiController.class);

	@RequestMapping(value = "/api", method = RequestMethod.GET)
	public PseStock getStockNameBySymbol(@RequestParam(value="symbol") String symbol) {
		// lookup MongoDb here
		
		MongoClient mongoClient = new MongoClient();
		MongoDatabase db = mongoClient.getDatabase(databaseName);
        MongoCollection<BsonDocument> collection = db.getCollection(stockCollection, BsonDocument.class);
        FindIterable<BsonDocument> iterable = collection.find(
        		BsonDocument.parse("{stock_symbol: '" + symbol + "'}"));
        MongoCursor<BsonDocument> cursor = iterable.iterator();
        
        BsonDocument doc = null;
        if (cursor.hasNext()) {
            doc = cursor.next();
//	        doc.remove("_id");
        }
        
        cursor.close();
        mongoClient.close();
        
        if (doc != null) {
        	return new PseStock(doc.getString("stock_symbol").getValue(),
	        		doc.getString("stock_name").getValue());
        }
        
        return PseStock.NULL;
	}
	
	@RequestMapping(value = "/api/all-stock-names-and-symbols", method = RequestMethod.GET)
	public List<PseStock> getAllStockNamesAndSymbols() {
		MongoClient mongoClient = new MongoClient();
		MongoDatabase db = mongoClient.getDatabase(databaseName);
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
        mongoClient.close();
        
        log.info("pseStocks.size: " + pseStocks.size());
        
        return pseStocks;
	}

}
