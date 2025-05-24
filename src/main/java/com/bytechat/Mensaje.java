package com.bytechat;

public class Mensaje {
    public String usuario;
    public String contenido;

    // Constructor vacío requerido por Gson
    public Mensaje() {}

    public Mensaje(String usuario, String contenido) {
        this.usuario = usuario;
        this.contenido = contenido;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContenido() {
        return contenido;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}
