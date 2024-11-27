/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author pauli
 */
public class AlbumDTO {

    private ObjectId id;                  // ID del álbum
    private String nombre;                // Nombre del álbum
    private Date fechaLanzamiento;        // Fecha de lanzamiento
    private List<String> genero;          // Géneros del álbum
    private String imagenPortada;         // Ruta o base64 de la imagen de portada
    private ObjectId artistaId;           // ID del artista
    private String nombreArtista;         // Nombre del artista
    private List<DetallesCancionDTO> canciones; // Lista de canciones en formato DTO

    public AlbumDTO() {
    }

    public AlbumDTO(ObjectId id, String nombre, Date fechaLanzamiento, List<String> genero, String imagenPortada, ObjectId artistaId, String nombreArtista, List<DetallesCancionDTO> canciones) {
        this.id = id;
        this.nombre = nombre;
        this.fechaLanzamiento = fechaLanzamiento;
        this.genero = genero;
        this.imagenPortada = imagenPortada;
        this.artistaId = artistaId;
        this.nombreArtista = nombreArtista;
        this.canciones = canciones;
    }

    // Getters y Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public void setFechaLanzamiento(Date fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public List<String> getGenero() {
        return genero;
    }

    public void setGenero(List<String> genero) {
        this.genero = genero;
    }

    public String getImagenPortada() {
        return imagenPortada;
    }

    public void setImagenPortada(String imagenPortada) {
        this.imagenPortada = imagenPortada;
    }

    public ObjectId getArtistaId() {
        return artistaId;
    }

    public void setArtistaId(ObjectId artistaId) {
        this.artistaId = artistaId;
    }

    public String getNombreArtista() {
        return nombreArtista;
    }

    public void setNombreArtista(String nombreArtista) {
        this.nombreArtista = nombreArtista;
    }

    public List<DetallesCancionDTO> getCanciones() {
        return canciones;
    }

    public void setCanciones(List<DetallesCancionDTO> canciones) {
        this.canciones = canciones;
    }

    @Override
    public String toString() {
        return "AlbumDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", fechaLanzamiento=" + fechaLanzamiento +
                ", genero=" + genero +
                ", imagenPortada='" + imagenPortada + '\'' +
                ", artistaId=" + artistaId +
                ", nombreArtista='" + nombreArtista + '\'' +
                ", canciones=" + canciones +
                '}';
    }
}
