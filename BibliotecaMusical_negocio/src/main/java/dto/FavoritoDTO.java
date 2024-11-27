/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.util.Date;
import org.bson.types.ObjectId;

/**
 *
 * @author pauli
 */
public class FavoritoDTO {

    private ObjectId idReferencia; // Referencia al ID del artista, álbum o canción
    private String tipo;           // Tipo de favorito (Artista, Álbum, Canción)
    private String titulo;         // Título del favorito (nombre del artista, álbum o canción)
    private Date fechaAgregado;    // Fecha en la que se agregó a favoritos
    private boolean activo;        // Estado del favorito (activo/inactivo)

    public FavoritoDTO() {
    }

    public FavoritoDTO(ObjectId idReferencia, String tipo, String titulo, Date fechaAgregado, boolean activo) {
        this.idReferencia = idReferencia;
        this.tipo = tipo;
        this.titulo = titulo;
        this.fechaAgregado = fechaAgregado;
        this.activo = activo;
    }

    // Getters y Setters
    public ObjectId getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(ObjectId idReferencia) {
        this.idReferencia = idReferencia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFechaAgregado() {
        return fechaAgregado;
    }

    public void setFechaAgregado(Date fechaAgregado) {
        this.fechaAgregado = fechaAgregado;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "FavoritoDTO{" +
                "idReferencia=" + idReferencia +
                ", tipo='" + tipo + '\'' +
                ", titulo='" + titulo + '\'' +
                ", fechaAgregado=" + fechaAgregado +
                ", activo=" + activo +
                '}';
    }
}
