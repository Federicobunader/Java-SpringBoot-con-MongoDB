package com.example.demo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MongoDBConnection {
    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoDBConnection(@Value("${mongo.uri}") String uri, @Value("${mongo.dbName}") String dbName) {
        this.mongoClient = MongoClients.create(uri);
        this.database = mongoClient.getDatabase(dbName);
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }
}