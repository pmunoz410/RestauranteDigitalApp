package com.project.restaurantedigitalapp.entity.service;

public class Categoria {

    private int id;
    private String nombre;
    private boolean vigencia;
    private DocumentoAlmacenado documentoAlmacenado;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isVigencia() {
        return vigencia;
    }

    public void setVigencia(boolean vigencia) {
        this.vigencia = vigencia;
    }

    public DocumentoAlmacenado getDocumentoAlmacenado() {
        return documentoAlmacenado;
    }

    public void setDocumentoAlmacenado(DocumentoAlmacenado documentoAlmacenado) {
        this.documentoAlmacenado = documentoAlmacenado;
    }
}