package com.bytechat;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoHelper {
    private static final String CONNECTION_STRING = "mongodb+srv://carlosenmanuelorozcon:<26090329>@bytechat.tkpe3ym.mongodb.net/?retryWrites=true&w=majority&appName=bytechat";

    public static MongoDatabase getDatabase() {
        MongoClient client = MongoClients.create(CONNECTION_STRING);
        return client.getDatabase("bytechat");
    }
}
