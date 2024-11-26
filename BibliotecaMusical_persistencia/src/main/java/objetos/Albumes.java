/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetos;

import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;
import recursos.Imagen;

/**
 *
 * @author Chris
 */
public class Albumes {

    private ObjectId id;
    private String nombre;
    private Date fechaLanzamiento;
    private List<String> genero;
    private Imagen imagenPortada;
    private ObjectId artistaId;
    private List<DetallesCancion> detallesCanciones;

    public Albumes() {
    }

    public Albumes(ObjectId id, String nombre, Date fechaLanzamiento, List<String> genero, Imagen imagenPortada, ObjectId artistaId, List<DetallesCancion> detallesCanciones) {
        this.id = id;
        this.nombre = nombre;
        this.fechaLanzamiento = fechaLanzamiento;
        this.genero = genero;
        this.imagenPortada = imagenPortada;
        this.artistaId = artistaId;
        this.detallesCanciones = detallesCanciones;
    }

    public Albumes(String nombre, Date fechaLanzamiento, List<String> genero, Imagen imagenPortada, ObjectId artistaId, List<DetallesCancion> detallesCanciones) {
        this.nombre = nombre;
        this.fechaLanzamiento = fechaLanzamiento;
        this.genero = genero;
        this.imagenPortada = imagenPortada;
        this.artistaId = artistaId;
        this.detallesCanciones = detallesCanciones;
    }

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

    public Imagen getImagenPortada() {
        return imagenPortada;
    }

    public void setImagenPortada(Imagen imagenPortada) {
        this.imagenPortada = imagenPortada;
    }

    public ObjectId getArtistaId() {
        return artistaId;
    }

    public void setArtistaId(ObjectId artistaId) {
        this.artistaId = artistaId;
    }

    public List<DetallesCancion> getDetallesCanciones() {
        return detallesCanciones;
    }

    public void setDetallesCanciones(List<DetallesCancion> detallesCanciones) {
        this.detallesCanciones = detallesCanciones;
    }

}
