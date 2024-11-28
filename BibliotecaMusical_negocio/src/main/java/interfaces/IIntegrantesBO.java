/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import dto.IntegrantesDTO;
import excepciones.NegocioException;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author pauli
 */
public interface IIntegrantesBO {

    /**
     * Agrega un nuevo integrante a la banda.
     *
     * @param idBanda ID de la banda.
     * @param integranteDTO Datos del integrante en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    void agregarIntegrante(ObjectId idBanda, IntegrantesDTO integranteDTO) throws NegocioException;

    /**
     * Obtiene los integrantes activos de una banda.
     *
     * @param idBanda ID de la banda.
     * @return Lista de integrantes activos en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    List<IntegrantesDTO> obtenerIntegrantesActivos(ObjectId idBanda) throws NegocioException;

    /**
     * Obtiene los integrantes inactivos de una banda.
     *
     * @param idBanda ID de la banda.
     * @return Lista de integrantes inactivos en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    List<IntegrantesDTO> obtenerIntegrantesInactivos(ObjectId idBanda) throws NegocioException;
    
}
