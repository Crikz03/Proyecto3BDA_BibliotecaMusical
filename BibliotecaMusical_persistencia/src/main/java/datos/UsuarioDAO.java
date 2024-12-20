/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import conexion.ConexionBD;
import excepciones.PersistenciaException;
import interfaces.IUsuarioDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import objetos.Favorito;
import objetos.Usuarios;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import recursos.Encriptacion;
import recursos.tipoFavoritos;

/**
 *
 * @author Chris
 */
public class UsuarioDAO implements IUsuarioDAO {

    private final MongoCollection<Usuarios> coleccionUsuarios;

    public UsuarioDAO() {
        this.coleccionUsuarios = ConexionBD.crearConexion().getCollection("usuarios", Usuarios.class);
    }

    @Override
    public boolean agregar(Usuarios u) throws PersistenciaException {
        try {
            String hashedPassword = Encriptacion.encriptar(u.getContrasena());
            u.setContrasena(hashedPassword);
            this.coleccionUsuarios.insertOne(u);
            return true;
        } catch (MongoException e) {
            throw new PersistenciaException("No se pudo insertar el usuario." + u.getId());
        }
    }

    @Override
    
   public boolean actualizar(Usuarios u) throws PersistenciaException {
    try {
        Bson filtroID = Filters.eq("_id", u.getId());

        List<Bson> updates = new ArrayList<>();
        if (u.getNombreUsuario() != null) {
            updates.add(Updates.set("nombreUsuario", u.getNombreUsuario()));
        }
        if (u.getCorreo() != null) {
            updates.add(Updates.set("correo", u.getCorreo()));
        }
        if (u.getContrasena() != null) {
            System.out.println("Contraseña original antes de encriptar: " + u.getContrasena());

            // Validar si ya está encriptada
            if (!u.getContrasena().matches("^[a-zA-Z0-9+/=]+$")) { // Ejemplo para verificar encriptación
                String contrasenaEncriptada = Encriptacion.encriptar(u.getContrasena());
                System.out.println("Contraseña encriptada: " + contrasenaEncriptada);
                updates.add(Updates.set("contrasena", contrasenaEncriptada));
            } else {
                updates.add(Updates.set("contrasena", u.getContrasena()));
            }
        }
        if (u.getFotoPerfil() != null) {
            updates.add(Updates.set("fotoPerfil", u.getFotoPerfil()));
        }

        if (!updates.isEmpty()) {
            Bson actualizacionDatos = Updates.combine(updates);
            UpdateResult resultado = this.coleccionUsuarios.updateOne(filtroID, actualizacionDatos);
            return resultado.getModifiedCount() > 0;
        }
        return false;
    } catch (MongoException e) {
        throw new PersistenciaException("No se pudo actualizar el usuario: " + u.getId());
    }
}

    public boolean agregarAFavoritos(ObjectId idUsuario, Favorito favorito) throws PersistenciaException {
        try {
            if (coleccionUsuarios.find(Filters.eq("_id", idUsuario)).first() == null) {
                throw new PersistenciaException("El usuario con ID: " + idUsuario + " no existe.");
            }
            boolean yaEsFavorito = coleccionUsuarios.find(
                    Filters.and(
                            Filters.eq("_id", idUsuario),
                            Filters.elemMatch("favoritos", Filters.eq("id", favorito.getId()))
                    )
            ).first() != null;

            if (yaEsFavorito) {
                throw new PersistenciaException("El favorito ya está en la lista de favoritos del usuario.");
            }

            Document favoritoDoc = new Document("id", favorito.getId())
                    .append("tipo", favorito.getTipo().name()) // Guardar tipo como string
                    .append("titulo", favorito.getTitulo())
                    .append("fechaAgregado", favorito.getFechaAgregado())
                    .append("activo", favorito.isActivo());

            coleccionUsuarios.updateOne(Filters.eq("_id", idUsuario), Updates.push("favoritos", favoritoDoc));
            return true;
        } catch (MongoException e) {
            throw new PersistenciaException("Error al agregar a favoritos para el usuario: " + idUsuario);
        }
    }

    public boolean eliminarFavorito(ObjectId usuarioId, ObjectId idFavorito, tipoFavoritos tipo) throws PersistenciaException {
        try {
            coleccionUsuarios.updateOne(
                    Filters.eq("_id", usuarioId),
                    Updates.pull("favoritos", new Document("id", idFavorito).append("tipo", tipo.name()))
            );
            return true;
        } catch (MongoException e) {
            throw new PersistenciaException("Error al eliminar favorito para el usuario: " + usuarioId);
        }
    }

    public boolean agregarAFavoritos(ObjectId idUsuario, List<Favorito> favoritos) throws PersistenciaException {
        try {
            List<Document> documentosFavoritos = favoritos.stream().map(favorito
                    -> new Document("id", favorito.getId())
                            .append("tipo", favorito.getTipo())
                            .append("titulo", favorito.getTitulo())
                            .append("fechaAgregado", favorito.getFechaAgregado())
            ).collect(Collectors.toList());

            coleccionUsuarios.updateOne(Filters.eq("_id", idUsuario), Updates.pushEach("favoritos", documentosFavoritos));
            return true;
        } catch (MongoException e) {
            throw new PersistenciaException("Error al agregar favoritos en masa para el usuario: " + idUsuario);
        }
    }

    public boolean agregarGeneroNoDeseado(ObjectId idUsuario, String genero) throws PersistenciaException {
        try {
            this.coleccionUsuarios.updateOne(Filters.eq("_id", idUsuario), Updates.addToSet("generosNoDeseados", genero));
            return true;
        } catch (MongoException e) {
            throw new PersistenciaException("Error al agregar género no deseado para el usuario: " + idUsuario);
        }
    }

    public boolean eliminarGeneroNoDeseado(ObjectId idUsuario, String genero) throws PersistenciaException {
        try {
            coleccionUsuarios.updateOne(Filters.eq("_id", idUsuario), Updates.pull("generosNoDeseados", genero));
            return true;
        } catch (MongoException e) {
            throw new PersistenciaException("Error al eliminar género no deseado para el usuario: " + idUsuario);
        }
    }

    public Usuarios consultarPorId(ObjectId idUsuario) throws PersistenciaException {
        try {
            Usuarios usuario = this.coleccionUsuarios.find(eq("_id", idUsuario)).first();
            if (usuario == null) {
                throw new PersistenciaException("No se encontró un usuario con el ID: " + idUsuario);
            }
            return usuario;
        } catch (MongoException e) {
            throw new PersistenciaException("Error al consultar el usuario con ID: " + idUsuario);
        }
    }

    @Override
    public Usuarios consultar(Usuarios u) throws PersistenciaException {
        try {
            Usuarios result = (Usuarios) this.coleccionUsuarios.find(eq("_id", u.getId())).first();
            return result;
        } catch (MongoException e) {
            throw new PersistenciaException("No se pudo encontrar el usuario: " + u.getId());
        }
    }

    @Override
    public Usuarios consultarCorreo(String correo) throws PersistenciaException {
        if (correo == null || correo.isEmpty()) {
            throw new PersistenciaException("El correo proporcionado es inválido.");
        }

        try {
            // Busca el usuario por correo electrónico
            Usuarios result = this.coleccionUsuarios.find(eq("correo", correo)).first();
            if (result == null) {
                System.out.println("No se encontró ningún usuario con el correo: " + correo);
            }
            return result;

        } catch (MongoException e) {
            e.printStackTrace();
            throw new PersistenciaException("Error al buscar el usuario por correo: " + correo);
        }
    }

    @Override
    public List<Usuarios> consultarTodos() throws PersistenciaException {
        try {
            List<Usuarios> usuarios = new ArrayList<>();
            this.coleccionUsuarios.find().into(usuarios);
            return usuarios;
        } catch (MongoException e) {
            throw new PersistenciaException("No se pudieron encontrar los usuarios.");
        }
    }

    public void actualizarGenerosNoDeseados(ObjectId usuarioId, List<String> generosNoDeseados) throws PersistenciaException {
        try {
            coleccionUsuarios.updateOne(
                    Filters.eq("_id", usuarioId),
                    Updates.set("generosNoDeseados", generosNoDeseados)
            );
        } catch (Exception e) {
            throw new PersistenciaException("Error al actualizar los géneros baneados: " + e.getMessage());
        }
    }

    public Usuarios consultarPorNombre(String nombreUsuario) throws PersistenciaException {
    try {
        // Consulta en MongoDB por el nombre de usuario
        return coleccionUsuarios.find(eq("nombreUsuario", nombreUsuario)).first();
    } catch (Exception e) {
        throw new PersistenciaException("Error al consultar el usuario por nombre: " + e.getMessage());
    }
}
}
