package com.bytechat;

public class Mensaje {
    private String usuario;
    private String contenido;
    private String fechaHora;      // 🕒 Hora del mensaje
    private String imagenUrl;      // 🖼️ URL de imagen (opcional)

    public Mensaje() {}

    public Mensaje(String usuario, String contenido, String fechaHora, String imagenUrl) {
        this.usuario = usuario;
        this.contenido = contenido;
        this.fechaHora = fechaHora;
        this.imagenUrl = imagenUrl;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContenido() {
        return contenido;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}

