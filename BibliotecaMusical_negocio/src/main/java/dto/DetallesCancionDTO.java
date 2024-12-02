/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import org.bson.types.ObjectId;
import recursos.Imagen;

/**
 *
 * @author pauli
 */
public class DetallesCancionDTO {

    private String titulo;          // Título de la canción
    private Integer duracion;       // Duración en segundos
    private ObjectId idReferenciaAlbum;     // ID del artista
    private Imagen fotoAlbum;
    private String nombreArtista;   // Nombre del artista

    public DetallesCancionDTO() {
    }

    public DetallesCancionDTO(String titulo, Integer duracion, ObjectId idReferenciaAlbum, String nombreArtista) {
        this.titulo = titulo;
        this.duracion = duracion;
        this.duracion = duracion;
        this.idReferenciaAlbum = idReferenciaAlbum;
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

    public ObjectId getIdArtista() {
        return idReferenciaAlbum;
    }

    public void setIdArtista(ObjectId idReferenciaAlbum) {
        this.idReferenciaAlbum = idReferenciaAlbum;
    }

    public String getNombreArtista() {
        return nombreArtista;
    }

    public void setNombreArtista(String nombreArtista) {
        this.nombreArtista = nombreArtista;
    }

    public Imagen getFotoAlbum() {
        return fotoAlbum;
    }

    public void setFotoAlbum(Imagen fotoAlbum) {
        this.fotoAlbum = fotoAlbum;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public String getDuracionFormateada() {
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
                ", idArtista=" + idReferenciaAlbum +
                ", nombreArtista='" + nombreArtista + '\'' +
                '}';
    }
}
