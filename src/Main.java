import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/mensajes", new MensajeHandler());
        server.createContext("/", exchange -> {
            String path = "static/index.html";
            exchange.sendResponseHeaders(200, 0);
            java.nio.file.Files.copy(java.nio.file.Path.of(path), exchange.getResponseBody());
            exchange.close();
        });
        server.setExecutor(null);
        server.start();
        System.out.println("Servidor iniciado en http://localhost:8080");
    }
}