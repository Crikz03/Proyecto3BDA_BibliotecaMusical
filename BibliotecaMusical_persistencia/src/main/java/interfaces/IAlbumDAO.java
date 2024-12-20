/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import excepciones.PersistenciaException;
import java.util.Date;
import java.util.List;
import objetos.Albumes;
import objetos.Artistas;
import objetos.DetallesCancion;
import org.bson.types.ObjectId;

/**
 *
 * @author pauli
 */
public interface IAlbumDAO {

    List<Albumes> crearAlbumesParaArtista(Artistas artista);

    List<Albumes> buscarPorArtista(ObjectId artistaId) throws PersistenciaException;

    List<Albumes> buscarPorFechaLanzamiento(Date fechaInicio, Date fechaFin) throws PersistenciaException;

    List<Albumes> buscarPorGenero(String genero) throws PersistenciaException;

    List<Albumes> buscarPorNombre(String nombre) throws PersistenciaException;

    void insertarAlbum(Albumes album) throws PersistenciaException;

    List<DetallesCancion> obtenerCancionesDeAlbum(ObjectId albumId) throws PersistenciaException;

    List<Albumes> obtenerCincoAlbumes() throws PersistenciaException;

    List<Albumes> obtenerAlbumes() throws PersistenciaException;

    List<DetallesCancion> obtenerCancionesDeAlbumes() throws PersistenciaException;

    List<DetallesCancion> buscarCancionesPorNombre(String nombre) throws PersistenciaException;

    List<Artistas> buscarArtistasPorNombre(String nombreArtista) throws PersistenciaException;

    List<Albumes> buscarAlbumesPorNombreDeCancion(String nombreCancion) throws PersistenciaException;

}
