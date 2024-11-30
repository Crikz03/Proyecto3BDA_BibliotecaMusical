/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import conexion.ConexionBD;
import excepciones.PersistenciaException;
import interfaces.IArtistaDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import objetos.Artistas;
import objetos.Integrantes;
import org.bson.conversions.Bson;
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
        try {
            return coleccionArtistas.find(Filters.regex("nombre", Pattern.compile(nombre, Pattern.CASE_INSENSITIVE)))
                    .into(new ArrayList<>());
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar artistas por nombre: " + e.getMessage());
        }
    }
    
    @Override
public List<Artistas> obtenerSeisArtistas() throws PersistenciaException {
    try {
        return coleccionArtistas.find()
                .limit(6) // Limita el resultado a 6 documentos
                .into(new ArrayList<>());
    } catch (Exception e) {
        throw new PersistenciaException("Error al obtener 6 artistas: " + e.getMessage());
    }
}

    @Override
    public List<Artistas> buscarArtistasPorGenero(String genero) throws PersistenciaException {
        try {
            return coleccionArtistas.find(Filters.eq("genero", genero)).into(new ArrayList<>());
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar artistas por género: " + e.getMessage());
        }
    }

    @Override
    public List<Integrantes> obtenerIntegrantesActivos(ObjectId artistaId) throws PersistenciaException {
        try {
            Artistas artista = coleccionArtistas.find(Filters.eq("_id", artistaId)).first();
            if (artista == null) {
                throw new PersistenciaException("No se encontró el artista con ID: " + artistaId);
            }
            return artista.getIntegrantes().stream().filter(Integrantes::isActivo).collect(Collectors.toList());
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener integrantes activos: " + e.getMessage());
        }
    }

    @Override
    public List<Integrantes> obtenerIntegrantesInactivos(ObjectId artistaId) throws PersistenciaException {
        try {
            Artistas artista = coleccionArtistas.find(Filters.eq("_id", artistaId)).first();
            if (artista == null) {
                throw new PersistenciaException("No se encontró el artista con ID: " + artistaId);
            }
            return artista.getIntegrantes().stream().filter(i -> !i.isActivo()).collect(Collectors.toList());
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener integrantes inactivos: " + e.getMessage());
        }
    }

    @Override
    public void agregarAFavoritos(ObjectId usuarioId, ObjectId artistaId) throws PersistenciaException {
        try {
            Bson filtroUsuario = Filters.eq("_id", usuarioId);
            Bson updateFavorito = Updates.push("favoritos.artistas", artistaId);
            coleccionArtistas.updateOne(filtroUsuario, updateFavorito);
        } catch (Exception e) {
            throw new PersistenciaException("Error al agregar el artista a favoritos: " + e.getMessage());
        }
    }

    @Override
    public List<Artistas> obtenerArtistasFavoritos(ObjectId usuarioId) throws PersistenciaException {
        try {
            return coleccionArtistas.find(Filters.in("_id", usuarioId)).into(new ArrayList<>());
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener artistas favoritos: " + e.getMessage());
        }
    }




    @Override
    public boolean verificarArtistaConGenero(ObjectId artistaId, List<String> generosNoDeseados) throws PersistenciaException {
        try {
            Artistas artista = coleccionArtistas.find(Filters.eq("_id", artistaId)).first();
            if (artista == null) {
                throw new PersistenciaException("No se encontró el artista con ID: " + artistaId);
            }
            return generosNoDeseados.contains(artista.getGenero());
        } catch (Exception e) {
            throw new PersistenciaException("Error al verificar el género del artista: " + e.getMessage());
        }
    }
}
