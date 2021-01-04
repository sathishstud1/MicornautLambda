package com.example.dbconnect;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import io.micronaut.context.annotation.EachProperty;
import io.micronaut.context.annotation.Value;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class DynamoDBConnect {

    private static  String dbUrl;
    private static  String domainName;
    private static DynamoDB dynamoDB = null;

    private static void loadProperties() throws Exception {
        String workingDir = System.getProperty("user.dir");
        InputStream input = new FileInputStream(workingDir+"/src/main/resources/apps.properties");
        Properties prop = new Properties();
        prop.load(input);
        DynamoDBConnect.dbUrl= prop.getProperty("dbUrl");
        DynamoDBConnect.domainName = prop.getProperty("domainName");
    }

    private static void createConnection() throws Exception{
        loadProperties();
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(DynamoDBConnect.dbUrl, DynamoDBConnect.domainName))
                .build();

        dynamoDB = new DynamoDB(client);
    }

    public static DynamoDB getDBConnection() throws Exception{
        synchronized(DynamoDBConnect.class) {
            if(dynamoDB==null) {
                createConnection();
            }
        }
        return dynamoDB;
    }
}
