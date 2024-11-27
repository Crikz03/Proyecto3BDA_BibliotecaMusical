/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import dto.AlbumDTO;
import dto.DetallesCancionDTO;
import excepciones.NegocioException;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author pauli
 */
public interface IAlbumBO {

    /**
     * Busca álbumes asociados a un artista.
     *
     * @param idArtista ID del artista.
     * @return Lista de álbumes del artista en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    List<AlbumDTO> buscarPorArtista(ObjectId idArtista) throws NegocioException;

    /**
     * Busca álbumes dentro de un rango de fechas de lanzamiento.
     *
     * @param fechaInicio Fecha inicial del rango.
     * @param fechaFin Fecha final del rango.
     * @return Lista de álbumes encontrados en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    List<AlbumDTO> buscarPorFechaLanzamiento(Date fechaInicio, Date fechaFin) throws NegocioException;

    /**
     * Busca álbumes por género musical.
     *
     * @param genero El género musical.
     * @return Lista de álbumes encontrados en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    List<AlbumDTO> buscarPorGenero(String genero) throws NegocioException;

    /**
     * Busca álbumes por nombre.
     *
     * @param nombre El nombre del álbum (completo o parcial).
     * @return Lista de álbumes encontrados en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    List<AlbumDTO> buscarPorNombre(String nombre) throws NegocioException;

    /**
     * Inserta un nuevo álbum en la base de datos.
     *
     * @param albumDTO El álbum a insertar en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    void insertarAlbum(AlbumDTO albumDTO) throws NegocioException;

    /**
     * Obtiene las canciones de un álbum.
     *
     * @param idAlbum ID del álbum.
     * @return Lista de canciones del álbum en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    List<DetallesCancionDTO> obtenerCancionesDeAlbum(ObjectId idAlbum) throws NegocioException;
    
}