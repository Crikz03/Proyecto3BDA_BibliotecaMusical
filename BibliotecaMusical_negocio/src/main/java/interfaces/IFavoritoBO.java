/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import dto.FavoritoDTO;
import excepciones.NegocioException;
import java.util.List;
import org.bson.types.ObjectId;
import recursos.tipoFavoritos;

/**
 *
 * @author pauli
 */
public interface IFavoritoBO {

    /**
     * Agrega un favorito para un usuario.
     *
     * @param idUsuario ID del usuario.
     * @param favoritoDTO Datos del favorito en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    void agregarFavorito(ObjectId idUsuario, FavoritoDTO favoritoDTO) throws NegocioException;

    /**
     * Desactiva un favorito de un usuario.
     *
     * @param idUsuario ID del usuario.
     * @param idReferencia ID del favorito a desactivar.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    void desactivarFavorito(ObjectId idUsuario, ObjectId idReferencia) throws NegocioException;

    /**
     * Elimina un favorito de un usuario.
     *
     * @param idUsuario ID del usuario.
     * @param idReferencia ID del favorito a eliminar.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    void eliminarFavorito(ObjectId idUsuario, ObjectId idReferencia) throws NegocioException;

    /**
     * Obtiene todos los favoritos de un usuario.
     *
     * @param idUsuario ID del usuario.
     * @return Lista de favoritos en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    List<FavoritoDTO> obtenerFavoritos(ObjectId idUsuario) throws NegocioException;

    /**
     * Obtiene los favoritos de un usuario por tipo.
     *
     * @param idUsuario ID del usuario.
     * @param tipo Tipo de favorito (ARTISTA, ALBUM, CANCION).
     * @return Lista de favoritos en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    List<FavoritoDTO> obtenerFavoritosPorTipo(ObjectId idUsuario, tipoFavoritos tipo) throws NegocioException;
    
}
