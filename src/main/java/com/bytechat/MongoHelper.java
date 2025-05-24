package com.bytechat;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoHelper {
    // Se obtiene desde una variable de entorno segura
    private static final String CONNECTION_STRING = System.getenv("MONGO_URI");

    public static MongoDatabase getDatabase() {
        MongoClient client = MongoClients.create(CONNECTION_STRING);
        System.out.println("✅ Conexión a MongoDB Atlas exitosa");
        return client.getDatabase("bytechat");
    }
}
