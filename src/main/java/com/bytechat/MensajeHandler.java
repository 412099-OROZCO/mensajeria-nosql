package com.bytechat;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bson.Document;

import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MensajeHandler implements HttpHandler {

    private final MongoCollection<Document> collection;
    private final Gson gson = new Gson();

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
                InputStreamReader reader = new InputStreamReader(exchange.getRequestBody());
                Mensaje recibido = gson.fromJson(reader, Mensaje.class);

                // Generar hora actual como string
                String horaActual = LocalTime.now().withNano(0).toString();
                recibido.setFechaHora(horaActual);

                // Crear documento con fechaCreacion como Date (requerido por TTL)
                Document doc = new Document("usuario", recibido.getUsuario())
                        .append("contenido", recibido.getContenido())
                        .append("fechaHora", recibido.getFechaHora())
                        .append("imagenUrl", recibido.getImagenUrl())
                        .append("fechaCreacion", new Date()); // ✅ clave para TTL

                collection.insertOne(doc);

                System.out.println("✅ Mensaje guardado con fechaCreacion: " + doc.getDate("fechaCreacion"));

                String respuesta = "{\"mensaje\":\"Guardado con éxito\"}";
                byte[] bytes = respuesta.getBytes();
                exchange.sendResponseHeaders(200, bytes.length);
                exchange.getResponseBody().write(bytes);

            } else if ("GET".equalsIgnoreCase(method)) {
                List<Mensaje> mensajes = new ArrayList<>();
                for (Document doc : collection.find()) {
                    mensajes.add(new Mensaje(
                            doc.getString("usuario"),
                            doc.getString("contenido"),
                            doc.getString("fechaHora"),
                            doc.getString("imagenUrl")
                    ));
                }

                String json = gson.toJson(mensajes);
                byte[] bytes = json.getBytes();
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
