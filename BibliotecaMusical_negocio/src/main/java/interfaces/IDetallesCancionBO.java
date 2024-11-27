/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import dto.DetallesCancionDTO;
import excepciones.NegocioException;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author pauli
 */
public interface IDetallesCancionBO {

    /**
     * Agrega una nueva canción al sistema.
     *
     * @param detallesDTO Detalles de la canción en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    void agregarCancion(DetallesCancionDTO detallesDTO) throws NegocioException;

    /**
     * Busca canciones por título.
     *
     * @param titulo El título parcial o completo de la canción.
     * @return Lista de canciones encontradas en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    List<DetallesCancionDTO> buscarPorTitulo(String titulo) throws NegocioException;

}
