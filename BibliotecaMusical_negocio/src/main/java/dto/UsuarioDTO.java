/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import recursos.Imagen;

/**
 *
 * @author pauli
 */
public class UsuarioDTO {

    private ObjectId id;                     // ID único del usuario
    private String nombreUsuario;            // Nombre del usuario
    private String correo;                   // Correo electrónico del usuario
    private String contrasena;
    private Imagen fotoPerfil;               // Ruta o base64 de la foto de perfil
    private List<FavoritoDTO> favoritos;     // Lista de favoritos (en formato DTO)
    private List<String> generosNoDeseados;  // Lista de géneros bloqueados

    public UsuarioDTO() {
    }
    
    

    public UsuarioDTO(ObjectId id, String nombreUsuario, String correo, String contrasena, Imagen fotoPerfil, List<FavoritoDTO> favoritos, List<String> generosNoDeseados) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.contrasena = contrasena;
        this.fotoPerfil = fotoPerfil;
        this.favoritos = favoritos;
        this.generosNoDeseados = generosNoDeseados;
    }

    // Getters y Setters
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

    public List<FavoritoDTO> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(List<FavoritoDTO> favoritos) {
        this.favoritos = favoritos;
    }

    public List<String> getGenerosNoDeseados() {
         return generosNoDeseados != null ? generosNoDeseados : new ArrayList<>();
    }

    public void setGenerosNoDeseados(List<String> generosNoDeseados) {
        this.generosNoDeseados = generosNoDeseados;
    }

    @Override
    public String toString() {
        return "UsuarioDTO{"
                + "id=" + id
                + ", nombreUsuario='" + nombreUsuario + '\''
                + ", correo='" + correo + '\''
                + ", fotoPerfil='" + fotoPerfil + '\''
                + ", favoritos=" + favoritos
                + ", generosNoDeseados=" + generosNoDeseados
                + '}';
    }
}
