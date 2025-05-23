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
        if ("POST".equals(exchange.getRequestMethod())) {
            InputStream is = exchange.getRequestBody();
            String json = new String(is.readAllBytes());
            Document doc = Document.parse(json);
            collection.insertOne(doc);
            exchange.sendResponseHeaders(200, 0);
            exchange.getResponseBody().write("Guardado".getBytes());
            exchange.close();
        } else if ("GET".equals(exchange.getRequestMethod())) {
            List<String> mensajes = new ArrayList<>();
            for (Document doc : collection.find()) {
                mensajes.add(doc.toJson());
            }
            String response = "[" + String.join(",", mensajes) + "]";
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        }
    }
}