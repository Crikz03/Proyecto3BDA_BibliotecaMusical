/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import conversiones.ConvertidorGeneral;
import datos.FavoritoDAO;
import dto.FavoritoDTO;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import interfaces.IFavoritoBO;
import interfaces.IFavoritoDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import objetos.Favorito;
import org.bson.types.ObjectId;
import recursos.tipoFavoritos;

/**
 *
 * @author pauli
 */
public class FavoritoBO implements IFavoritoBO {

    private final IFavoritoDAO favoritoDAO;

    public FavoritoBO() {
        this.favoritoDAO = new FavoritoDAO(); // Inicialización directa; puede usarse inyección de dependencias.
    }

    /**
     * Agrega un favorito para un usuario.
     *
     * @param idUsuario ID del usuario.
     * @param favoritoDTO Datos del favorito en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    @Override
    public void agregarFavorito(ObjectId idUsuario, FavoritoDTO favoritoDTO) throws NegocioException {
        if (favoritoDTO == null || favoritoDTO.getIdReferencia() == null || favoritoDTO.getTipo() == null) {
            throw new NegocioException("El favorito o sus datos obligatorios no pueden ser nulos.");
        }
        // Convertir DTO a entidad
        Favorito favorito = ConvertidorGeneral.convertidorEntidad(favoritoDTO, Favorito.class);
        try {
            // Llamar al DAO para agregar el favorito
            if (!favoritoDAO.agregarFavorito(idUsuario, favorito)) {
                throw new NegocioException("No se pudo agregar el favorito.");
            }
        } catch (PersistenciaException ex) {
            Logger.getLogger(FavoritoBO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Elimina un favorito de un usuario.
     *
     * @param idUsuario ID del usuario.
     * @param idReferencia ID del favorito a eliminar.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    @Override
    public void eliminarFavorito(ObjectId idUsuario, ObjectId idReferencia) throws NegocioException {
        try {
            if (!favoritoDAO.eliminarFavorito(idUsuario, idReferencia)) {
                throw new NegocioException("No se pudo eliminar el favorito.");
            }
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al eliminar el favorito: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene los favoritos de un usuario por tipo.
     *
     * @param idUsuario ID del usuario.
     * @param tipo Tipo de favorito (ARTISTA, ALBUM, CANCION).
     * @return Lista de favoritos en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    @Override
    public List<FavoritoDTO> obtenerFavoritosPorTipo(ObjectId idUsuario, tipoFavoritos tipo) throws NegocioException {
        try {
            List<Favorito> favoritos = favoritoDAO.obtenerFavoritosPorTipo(idUsuario, tipo);
            return ConvertidorGeneral.convertidoraListaDTO(favoritos, FavoritoDTO.class);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al obtener favoritos por tipo: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene todos los favoritos de un usuario.
     *
     * @param idUsuario ID del usuario.
     * @return Lista de favoritos en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    @Override
    public List<FavoritoDTO> obtenerFavoritos(ObjectId idUsuario) throws NegocioException {
        try {
            List<Favorito> favoritos = favoritoDAO.obtenerFavoritos(idUsuario);
            return ConvertidorGeneral.convertidoraListaDTO(favoritos, FavoritoDTO.class);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al obtener la lista de favoritos: " + e.getMessage(), e);
        }
    }

    /**
     * Desactiva un favorito de un usuario.
     *
     * @param idUsuario ID del usuario.
     * @param idReferencia ID del favorito a desactivar.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    @Override
    public void desactivarFavorito(ObjectId idUsuario, ObjectId idReferencia) throws NegocioException {
        try {
            if (!favoritoDAO.desactivarFavorito(idUsuario, idReferencia)) {
                throw new NegocioException("No se pudo desactivar el favorito.");
            }
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al desactivar el favorito: " + e.getMessage(), e);
        }
    }
}
