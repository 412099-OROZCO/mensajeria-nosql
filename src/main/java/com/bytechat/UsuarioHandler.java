package com.bytechat;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bson.Document;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsuarioHandler implements HttpHandler {

    private final MongoCollection<Document> collection;
    private final Gson gson = new Gson();

    public UsuarioHandler() {
        MongoDatabase db = MongoHelper.getDatabase();
        this.collection = db.getCollection("usuarios");
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Content-Type", "application/json");

        try {
            if ("POST".equalsIgnoreCase(method)) {
                InputStreamReader reader = new InputStreamReader(exchange.getRequestBody());
                BufferedReader buffered = new BufferedReader(reader);
                String body = buffered.readLine();

                Document recibido = Document.parse(body);
                String nickname = recibido.getString("nickname");

                // Crear o actualizar al usuario con timestamp actual
                Document filtro = new Document("nickname", nickname);
                Document update = new Document("$set", new Document("nickname", nickname)
                        .append("fechaConexion", new Date()));
                collection.updateOne(filtro, update, new com.mongodb.client.model.UpdateOptions().upsert(true));

                String resp = "{\"status\":\"ok\"}";
                byte[] respBytes = resp.getBytes();
                exchange.sendResponseHeaders(200, respBytes.length);
                exchange.getResponseBody().write(respBytes);

            } else if ("GET".equalsIgnoreCase(method)) {
                List<String> usuarios = new ArrayList<>();
                for (Document doc : collection.find()) {
                    usuarios.add(doc.getString("nickname"));
                }
                String json = gson.toJson(usuarios);
                byte[] bytes = json.getBytes();
                exchange.sendResponseHeaders(200, bytes.length);
                exchange.getResponseBody().write(bytes);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            String err = "{\"error\":\"" + e.getMessage() + "\"}";
            byte[] bytes = err.getBytes();
            exchange.sendResponseHeaders(500, bytes.length);
            exchange.getResponseBody().write(bytes);
        } finally {
            exchange.close();
        }
    }
}

