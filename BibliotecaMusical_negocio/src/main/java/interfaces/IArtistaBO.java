/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import dto.ArtistaDTO;
import dto.IntegrantesDTO;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author pauli
 */
public interface IArtistaBO {


    /**
     * Agrega un artista a los favoritos de un usuario.
     *
     * @param idUsuario ID del usuario.
     * @param idArtista ID del artista.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    void agregarArtistaAFavoritos(ObjectId idUsuario, ObjectId idArtista) throws NegocioException;

    /**
     * Busca artistas por género musical.
     *
     * @param genero El género musical.
     * @return Lista de artistas encontrados en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    List<ArtistaDTO> buscarArtistasPorGenero(String genero) throws NegocioException;

    /**
     * Busca artistas por nombre.
     *
     * @param nombre El nombre parcial o completo del artista.
     * @return Lista de artistas encontrados en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    List<ArtistaDTO> buscarArtistasPorNombre(String nombre) throws NegocioException;

  

    /**
     * Inserta artistas masivamente en la base de datos.
     *
     * @param artistasDTO Lista de artistas en formato DTO.
     * @throws NegocioException Si hay un error de negocio o persistencia.
     */
    void insertarArtistasMasivamente(List<ArtistaDTO> artistasDTO) throws NegocioException;

    /**
     * Obtiene los integrantes activos de un artista.
     *
     * @param idArtista ID del artista.
     * @return Lista de integrantes activos en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    List<IntegrantesDTO> obtenerIntegrantesActivos(ObjectId idArtista) throws NegocioException;

    /**
     * Obtiene los integrantes inactivos de un artista.
     *
     * @param idArtista ID del artista.
     * @return Lista de integrantes inactivos en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    List<IntegrantesDTO> obtenerIntegrantesInactivos(ObjectId idArtista) throws NegocioException;

    List<String> obtenerGenerosDeArtistas() throws PersistenciaException;
    
}
