/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author pauli
 */
public class ArtistaDTO {

    private ObjectId id;                  // ID del artista
    private String nombre;                // Nombre del artista
    private String tipo;                  // Tipo de artista: "Solista" o "Banda"
    private String genero;                // Género musical
    private String imagen;                // Ruta o base64 de la imagen
    private List<IntegrantesDTO> integrantes; // Lista de integrantes (solo si es banda)

    public ArtistaDTO() {
    }

    public ArtistaDTO(ObjectId id, String nombre, String tipo, String genero, String imagen, List<IntegrantesDTO> integrantes) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.genero = genero;
        this.imagen = imagen;
        this.integrantes = integrantes;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public List<IntegrantesDTO> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<IntegrantesDTO> integrantes) {
        this.integrantes = integrantes;
    }

    @Override
    public String toString() {
        return "ArtistaDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", genero='" + genero + '\'' +
                ", imagen='" + imagen + '\'' +
                ", integrantes=" + integrantes +
                '}';
    }
}
