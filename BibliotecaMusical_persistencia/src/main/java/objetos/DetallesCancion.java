/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetos;

import org.bson.types.ObjectId;
import recursos.Imagen;

/**
 *
 * @author Chris
 */
public class DetallesCancion {

    private ObjectId idReferenciaAlbum;
    private String titulo;
    private Imagen fotoAlbum;
    private Integer duracion;
    private ObjectId idArtista;

    public ObjectId getIdArtista() {
        return idArtista;
    }

    public void setIdArtista(ObjectId idArtista) {
        this.idArtista = idArtista;
    }

    public DetallesCancion() {
    }

    public DetallesCancion(ObjectId idReferenciaAlbum, String titulo, Imagen fotoAlbum, Integer duracion) {
        this.idReferenciaAlbum = idReferenciaAlbum;
        this.titulo = titulo;
        this.fotoAlbum = fotoAlbum;
        this.duracion = duracion;
    }
     public DetallesCancion(ObjectId idReferenciaAlbum, String titulo, Imagen fotoAlbum, Integer duracion,ObjectId idArtista) {
        this.idReferenciaAlbum = idReferenciaAlbum;
        this.titulo = titulo;
        this.fotoAlbum = fotoAlbum;
        this.duracion = duracion;
        this.idArtista=idArtista;
    }

    public ObjectId getIdReferenciaAlbum() {
        return idReferenciaAlbum;
    }

    public void setIdReferenciaAlbum(ObjectId idReferenciaAlbum) {
        this.idReferenciaAlbum = idReferenciaAlbum;
    }

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
    }

    public String getDuracionFormateada() {
        if (duracion == null) {
            return "0:00";
        }

        int minutos = duracion / 60;
        int segundos = duracion % 60;
        return String.format("%d:%02d", minutos, segundos);
    }

    public Imagen getFotoAlbum() {
        return fotoAlbum;
    }

    public void setFotoAlbum(Imagen fotoAlbum) {
        this.fotoAlbum = fotoAlbum;
    }

}
