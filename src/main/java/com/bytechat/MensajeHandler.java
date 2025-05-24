package com.bytechat;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bson.Document;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MensajeHandler implements HttpHandler {

    private final MongoCollection<Document> collection;

    public MensajeHandler() {
        MongoDatabase db = MongoHelper.getDatabase();
        collection = db.getCollection("mensajes");
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Content-Type", "application/json");

            if ("POST".equalsIgnoreCase(method)) {
                InputStream is = exchange.getRequestBody();
                String json = new String(is.readAllBytes());
                Document doc = Document.parse(json);
                collection.insertOne(doc);
                String respuesta = "{\"mensaje\":\"Guardado con éxito\"}";
                byte[] bytes = respuesta.getBytes();
                exchange.sendResponseHeaders(200, bytes.length);
                exchange.getResponseBody().write(bytes);
            } else if ("GET".equalsIgnoreCase(method)) {
                List<String> mensajes = new ArrayList<>();
                for (Document doc : collection.find()) {
                    mensajes.add(doc.toJson());
                }
                String response = "[" + String.join(",", mensajes) + "]";
                byte[] bytes = response.getBytes();
                exchange.sendResponseHeaders(200, bytes.length);
                exchange.getResponseBody().write(bytes);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errorJson = "{\"error\":\"" + e.getMessage() + "\"}";
            byte[] bytes = errorJson.getBytes();
            exchange.sendResponseHeaders(500, bytes.length);
            exchange.getResponseBody().write(bytes);
        } finally {
            exchange.close();
        }
    }
}


