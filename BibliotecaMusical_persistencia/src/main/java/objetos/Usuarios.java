/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import recursos.Imagen;
import recursos.tipoFavoritos;

/**
 *
 * @author Chris
 */
public class Usuarios {

    private ObjectId id;
    private String nombreUsuario;
    private String correo;
    private String contrasena;
    private Imagen fotoPerfil;
    private List<Favorito> favoritos;
    private List<String> generosNoDeseados;

    public Usuarios() {
    }

    public Usuarios(ObjectId id, String nombreUsuario, String correo, String contrasena, Imagen fotoPerfil, List<Favorito> favoritos, List<String> generosNoDeseados) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.contrasena = contrasena;
        this.fotoPerfil = fotoPerfil;
        this.favoritos = favoritos;
        this.generosNoDeseados = generosNoDeseados;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Imagen getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(Imagen fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public List<Favorito> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(List<Favorito> favoritos) {
        this.favoritos = favoritos;
    }

    public boolean agregarAFavoritos(Favorito favorito) {
        if (favoritos == null) {
            favoritos = new ArrayList<>();
        }

        for (Favorito f : favoritos) {
            if (f.getId().equals(favorito.getId()) && f.getTipo() == favorito.getTipo()) {
                return false;
            }
        }

        favoritos.add(favorito);
        return true;
    }

    public boolean eliminarFavorito(ObjectId idFavorito, tipoFavoritos tipo) {
        if (favoritos == null) {
            return false;
        }
        for (Favorito favorito : favoritos) {
            if (favorito.getId().equals(idFavorito) && favorito.getTipo() == tipo) {
                favoritos.remove(favorito);
                return true;
            }
        }

        return false;
    }

    public List<Favorito> obtenerFavoritosPorTipo(tipoFavoritos tipo) {
        if (favoritos == null) {
            return new ArrayList<>();
        }
        return favoritos.stream()
                .filter(favorito -> favorito.getTipo() == tipo && favorito.isActivo())
                .collect(Collectors.toList());
    }

    public List<String> getGenerosNoDeseados() {
        return generosNoDeseados;
    }

    public void setGenerosNoDeseados(List<String> generosNoDeseados) {
        this.generosNoDeseados = generosNoDeseados;
    }

}
