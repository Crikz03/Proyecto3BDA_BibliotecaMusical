/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetos;

import java.util.List;
import org.bson.types.ObjectId;
import recursos.Imagen;

/**
 *
 * @author Chris
 */
public class Artistas {

    private ObjectId id;
    private String nombre;
    private String tipo;
    private Imagen imagen;
    private String genero;
    private List<Integrantes> integrantes;

    public Artistas() {
    }

    public Artistas(String nombre, String tipo, Imagen imagen, String genero) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.imagen = imagen;
        this.genero = genero;
    }

    public Artistas(String nombre, String tipo, Imagen imagen, String genero, List<Integrantes> integrantes) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.imagen = imagen;
        this.genero = genero;
        this.integrantes = integrantes;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Imagen getImagen() {
        return imagen;
    }

    public void setImagen(Imagen imagen) {
        this.imagen = imagen;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public List<Integrantes> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<Integrantes> integrantes) {
        this.integrantes = integrantes;
    }

}
