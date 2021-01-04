package com.example.dbconnect;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import javax.inject.Singleton;
import java.util.Random;

@Singleton
public class DBUtils {

    private DynamoDB dynamoDB = null;
    public int getNewId(String collectionName, String idValue) {
        int customer_id = 1;
        try {

            Random rdm = new Random();
            customer_id = rdm.nextInt(1000);
            //dynamoDB = DynamoDBConnect.getDBConnection();

            //Table table = dynamoDB.getTable("EventTable");
            /*table.get
            MongoCollection<Document> collection = database.getCollection(collectionName);
            BasicDBObject whereQuery = new BasicDBObject();
            whereQuery.put("_id", idValue);
            synchronized(this) {
                Document doc = collection.find(whereQuery).first();
                double value = Double.parseDouble(doc.get("seq_value").toString());
                int increamentId = (int)value;
                customerid =  increamentId;
                increamentId++;
                collection.updateOne(Filters.eq("_id", idValue), new Document("$set", new Document("seq_value", increamentId)));
            }*/

        }catch(Exception e) {
            e.printStackTrace();;
        }

        return customer_id;
    }
}
