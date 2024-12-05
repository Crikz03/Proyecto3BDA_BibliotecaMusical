/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import conexion.ConexionBD;
import excepciones.PersistenciaException;
import interfaces.IFavoritoDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import objetos.Favorito;
import objetos.Usuarios;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import recursos.tipoFavoritos;

/**
 *
 * @author pauli
 */
public class FavoritoDAO implements IFavoritoDAO {

    private final MongoCollection<Usuarios> coleccionUsuarios;

    public FavoritoDAO() {
        this.coleccionUsuarios = ConexionBD.crearConexion().getCollection("usuarios", Usuarios.class);
    }

    @Override
    public boolean agregarFavorito(ObjectId idUsuario, Favorito favorito) throws PersistenciaException {
        try {
            // Validar existencia del usuario
            Usuarios usuario = coleccionUsuarios.find(Filters.eq("_id", idUsuario)).first();
            if (usuario == null) {
                throw new PersistenciaException("El usuario con ID: " + idUsuario + " no existe.");
            }

            // Validar que el favorito no exista ya
            boolean yaEsFavorito = coleccionUsuarios.find(
                    Filters.and(
                            Filters.eq("_id", idUsuario),
                            Filters.elemMatch("favoritos", Filters.eq("_id", favorito.getId()))
                    )
            ).first() != null;

            if (yaEsFavorito) {
                throw new PersistenciaException("El favorito ya está en la lista de favoritos del usuario.");
            }

            // Insertar el favorito
            Bson update = Updates.push("favoritos", favorito);
            coleccionUsuarios.updateOne(Filters.eq("_id", idUsuario), update);
            return true;

        } catch (Exception e) {
            throw new PersistenciaException("Error al agregar favorito: " + e.getMessage());
        }
    }

    @Override
    public boolean eliminarFavorito(ObjectId idUsuario, ObjectId idReferencia) throws PersistenciaException {
        try {
            Bson filtro = Filters.eq("_id", idUsuario);
            Bson update = Updates.pull("favoritos", Filters.eq("IdReferecnia", idReferencia));
            coleccionUsuarios.updateOne(filtro, update);
            return true;
        } catch (Exception e) {
            throw new PersistenciaException("Error al eliminar favorito: " + e.getMessage());
        }
    }

    @Override
    public List<Favorito> obtenerFavoritosPorTipo(ObjectId idUsuario, tipoFavoritos tipo) throws PersistenciaException {
        try {
            Usuarios usuario = coleccionUsuarios.find(Filters.eq("_id", idUsuario)).first();
            if (usuario == null) {
                throw new PersistenciaException("El usuario con ID: " + idUsuario + " no existe.");
            }

            List<Favorito> favoritos = usuario.getFavoritos();
            if (favoritos == null) {
                return new ArrayList<>();
            }

            // Filtrar favoritos por tipo
            return favoritos.stream()
                    .filter(f -> f.getTipo() == tipo && f.isActivo())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener favoritos por tipo: " + e.getMessage());
        }
    }

    @Override
    public List<Favorito> obtenerFavoritos(ObjectId idUsuario) throws PersistenciaException {
        try {
            Usuarios usuario = coleccionUsuarios.find(Filters.eq("_id", idUsuario)).first();
            if (usuario == null) {
                throw new PersistenciaException("El usuario con ID: " + idUsuario + " no existe.");
            }

            List<Favorito> favoritos = usuario.getFavoritos();
            return favoritos == null ? new ArrayList<>() : favoritos;
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener la lista de favoritos: " + e.getMessage());
        }
    }

    @Override
    public boolean desactivarFavorito(ObjectId idUsuario, ObjectId idReferencia) throws PersistenciaException {
        try {
            Bson filtro = Filters.and(
                    Filters.eq("_id", idUsuario),
                    Filters.eq("favoritos.IdReferecnia", idReferencia)
            );
            Bson update = Updates.set("favoritos.$.activo", false);
            coleccionUsuarios.updateOne(filtro, update);
            return true;
        } catch (Exception e) {
            throw new PersistenciaException("Error al desactivar favorito: " + e.getMessage());
        }
    }
}
