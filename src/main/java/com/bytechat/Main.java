package com.bytechat;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);


        // Ruta para API
        server.createContext("/api/mensajes", new MensajeHandler());
        server.createContext("/api/usuarios", new UsuarioHandler()); // ✅ nuevo handler

        // Ruta para frontend en /docs
        server.createContext("/", (HttpExchange exchange) -> {
            String path = "docs" + exchange.getRequestURI().getPath();
            if (path.equals("docs/")) {
                path = "docs/index.html";
            }

            try {
                Path filePath = Path.of(path);
                String contentType = getContentType(path);

                byte[] content = Files.readAllBytes(filePath);
                exchange.getResponseHeaders().add("Content-Type", contentType);
                exchange.sendResponseHeaders(200, content.length);

                OutputStream os = exchange.getResponseBody();
                os.write(content);
                os.close();
            } catch (Exception e) {
                String msg = "Archivo no encontrado: " + path;
                exchange.sendResponseHeaders(404, msg.length());
                exchange.getResponseBody().write(msg.getBytes());
                exchange.close();
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("Servidor iniciado en http://localhost:8080");
    }

    // Método auxiliar para devolver el Content-Type correcto
    private static String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html; charset=UTF-8";
        if (path.endsWith(".css")) return "text/css; charset=UTF-8";
        if (path.endsWith(".js")) return "application/javascript; charset=UTF-8";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".ico")) return "image/x-icon";
        return "application/octet-stream";
    }
}

