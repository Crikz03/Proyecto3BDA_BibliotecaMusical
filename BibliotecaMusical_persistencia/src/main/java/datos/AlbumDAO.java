/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import conexion.ConexionBD;
import excepciones.PersistenciaException;
import interfaces.IAlbumDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import objetos.Albumes;
import objetos.DetallesCancion;
import org.bson.types.ObjectId;

/**
 *
 * @author pauli
 */
public class AlbumDAO implements IAlbumDAO {

    private final MongoCollection<Albumes> coleccionAlbumes;

    public AlbumDAO() {
        this.coleccionAlbumes = ConexionBD.crearConexion().getCollection("albumes", Albumes.class);
    }

    /**
     *
     * @param album
     * @throws PersistenciaException
     */
    @Override
    public void insertarAlbum(Albumes album) throws PersistenciaException {
        try {
            if (album == null || album.getNombre() == null || album.getFechaLanzamiento() == null) {
                throw new PersistenciaException("El álbum o sus campos obligatorios no pueden ser nulos.");
            }
            this.coleccionAlbumes.insertOne(album);
        } catch (MongoException e) {
            throw new PersistenciaException("Error al insertar el álbum: " + e.getMessage());
        }
    }

    /**
     *
     * @param nombre
     * @return
     * @throws PersistenciaException
     */
    @Override
    public List<Albumes> buscarPorNombre(String nombre) throws PersistenciaException {
        try {
            return coleccionAlbumes.find(Filters.regex("nombre", Pattern.compile(nombre, Pattern.CASE_INSENSITIVE)))
                    .into(new ArrayList<>());
        } catch (MongoException e) {
            throw new PersistenciaException("Error al buscar álbumes por nombre: " + e.getMessage());
        }
    }

    /**
     *
     * @param genero
     * @return
     * @throws PersistenciaException
     */
    @Override
    public List<Albumes> buscarPorGenero(String genero) throws PersistenciaException {
        try {
            return coleccionAlbumes.find(Filters.in("genero", genero)).into(new ArrayList<>());
        } catch (MongoException e) {
            throw new PersistenciaException("Error al buscar álbumes por género: " + e.getMessage());
        }
    }

    /**
     *
     * @param fechaInicio
     * @param fechaFin
     * @return
     * @throws PersistenciaException
     */
    @Override
    public List<Albumes> buscarPorFechaLanzamiento(Date fechaInicio, Date fechaFin) throws PersistenciaException {
        try {
            return coleccionAlbumes.find(Filters.and(
                    Filters.gte("fechaLanzamiento", fechaInicio),
                    Filters.lte("fechaLanzamiento", fechaFin)
            )).into(new ArrayList<>());
        } catch (MongoException e) {
            throw new PersistenciaException("Error al buscar álbumes por rango de fechas: " + e.getMessage());
        }
    }

    /**
     *
     * @param artistaId
     * @return
     * @throws PersistenciaException
     */
    @Override
    public List<Albumes> buscarPorArtista(ObjectId artistaId) throws PersistenciaException {
        try {
            return coleccionAlbumes.find(Filters.eq("artistaId", artistaId)).into(new ArrayList<>());
        } catch (MongoException e) {
            throw new PersistenciaException("Error al buscar álbumes por artista: " + e.getMessage());
        }
    }

    /**
     *
     * @param albumId
     * @return
     * @throws PersistenciaException
     */
    @Override
    public List<DetallesCancion> obtenerCancionesDeAlbum(ObjectId albumId) throws PersistenciaException {
        try {
            Albumes album = coleccionAlbumes.find(Filters.eq("_id", albumId)).first();
            if (album == null) {
                throw new PersistenciaException("No se encontró el álbum con el ID proporcionado.");
            }
            return album.getDetallesCanciones();
        } catch (MongoException e) {
            throw new PersistenciaException("Error al obtener canciones del álbum: " + e.getMessage());
        }
    }

    @Override
    public List<Albumes> obtenerSeisAlbumes() throws PersistenciaException {
        try {
            // Consulta para obtener 6 artistas
            return this.coleccionAlbumes.find()
                    .limit(1) // Limita el resultado a 6 documentos
                    .into(new ArrayList<>());
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener 6 artistas: " + e.getMessage());
        }
    }

    @Override
    public List<DetallesCancion> obtenerCancionesDeAlbumes() throws PersistenciaException {
        try {
            // Consulta para obtener los álbumes
            List<Albumes> albumes = this.coleccionAlbumes.find()
                    .into(new ArrayList<>());

            // Lista para almacenar las canciones
            List<DetallesCancion> canciones = new ArrayList<>();

            // Itera sobre cada álbum y agrega las canciones a la lista
            for (Albumes album : albumes) {
                List<DetallesCancion> detallesCanciones = album.getDetallesCanciones();
                if (detallesCanciones != null) {
                    canciones.addAll(detallesCanciones);
                }
            }

            // Devuelve la lista de todas las canciones
            return canciones;

        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener las canciones de los álbumes: " + e.getMessage());
        }
    }
}
