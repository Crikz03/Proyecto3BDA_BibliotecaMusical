/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import conexion.ConexionBD;
import excepciones.PersistenciaException;
import interfaces.IArtistaDAO;
import java.util.List;
import objetos.Artistas;
import objetos.Integrantes;
import org.bson.types.ObjectId;

/**
 *
 * @author Chris
 */
public class ArtistaDAO implements IArtistaDAO {

    private final MongoCollection<Artistas> coleccionArtistas;

    public ArtistaDAO() {
        this.coleccionArtistas = ConexionBD.crearConexion().getCollection("artistas", Artistas.class);
    }

    @Override
    public void insertarArtistasMasivamente(List<Artistas> artistas) throws PersistenciaException {
        try {
            if (artistas == null || artistas.isEmpty()) {
                throw new PersistenciaException("La lista de artistas no puede estar vacía.");
            }

            for (Artistas artista : artistas) {
                if (artista.getNombre() == null || artista.getNombre().isEmpty()) {
                    throw new PersistenciaException("El nombre del artista no puede ser nulo o vacío.");
                }
                if (artista.getTipo() == null || artista.getTipo().isEmpty()) {
                    throw new PersistenciaException("El tipo de artista no puede ser nulo o vacío.");
                }
                if (artista.getGenero() == null || artista.getGenero().isEmpty()) {
                    throw new PersistenciaException("El género del artista no puede ser nulo o vacío.");
                }
            }

            this.coleccionArtistas.insertMany(artistas);

        } catch (MongoException e) {
            throw new PersistenciaException("No se ha podido realizar la inserción masiva de artistas: " +e);
        }
    }

    @Override
    public List<Artistas> buscarArtistasPorNombre(String nombre) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Artistas> buscarArtistasPorGenero(String genero) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Integrantes> obtenerIntegrantesActivos(ObjectId artistaId) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Integrantes> obtenerIntegrantesInactivos(ObjectId artistaId) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void agregarAFavoritos(ObjectId usuarioId, ObjectId artistaId) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Artistas> obtenerArtistasFavoritos(ObjectId usuarioId) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void actualizarArtista(Artistas artista) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void eliminarArtista(ObjectId artistaId) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean verificarArtistaConGenero(ObjectId artistaId, List<String> generosNoDeseados) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
