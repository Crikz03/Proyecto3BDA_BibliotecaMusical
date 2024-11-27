/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import org.bson.types.ObjectId;

/**
 *
 * @author pauli
 */
public class DetallesCancionDTO {

    private String titulo;          // Título de la canción
    private Integer duracion;       // Duración en segundos
    private String duracionFormateada; // Duración en formato mm:ss
    private ObjectId idArtista;     // ID del artista
    private String nombreArtista;   // Nombre del artista

    public DetallesCancionDTO() {
    }

    public DetallesCancionDTO(String titulo, Integer duracion, ObjectId idArtista, String nombreArtista) {
        this.titulo = titulo;
        this.duracion = duracion;
        this.duracionFormateada = formatearDuracion(duracion);
        this.idArtista = idArtista;
        this.nombreArtista = nombreArtista;
    }

    // Getters y Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
        this.duracionFormateada = formatearDuracion(duracion); // Actualizar formato al cambiar duración
    }

    public String getDuracionFormateada() {
        return duracionFormateada;
    }

    public ObjectId getIdArtista() {
        return idArtista;
    }

    public void setIdArtista(ObjectId idArtista) {
        this.idArtista = idArtista;
    }

    public String getNombreArtista() {
        return nombreArtista;
    }

    public void setNombreArtista(String nombreArtista) {
        this.nombreArtista = nombreArtista;
    }

    // Método adicional para formatear la duración
    private String formatearDuracion(Integer duracion) {
        if (duracion == null) {
            return "0:00";
        }
        int minutos = duracion / 60;
        int segundos = duracion % 60;
        return String.format("%d:%02d", minutos, segundos);
    }

    @Override
    public String toString() {
        return "DetallesDTO{" +
                "titulo='" + titulo + '\'' +
                ", duracion=" + duracion +
                ", duracionFormateada='" + duracionFormateada + '\'' +
                ", idArtista=" + idArtista +
                ", nombreArtista='" + nombreArtista + '\'' +
                '}';
    }
}
