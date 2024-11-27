/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import conversiones.ConvertidorGeneral;
import datos.DetallesCancionDAO;
import dto.DetallesCancionDTO;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import interfaces.IDetallesCancionDAO;
import java.util.List;
import objetos.DetallesCancion;
import org.bson.types.ObjectId;
import interfaces.IDetallesCancionBO;

/**
 *
 * @author pauli
 */
public class DetallesCancionBO implements IDetallesCancionBO {

    private final IDetallesCancionDAO cancionDAO;

    public DetallesCancionBO() {
        this.cancionDAO = new DetallesCancionDAO();
    }

    /**
     * Agrega una nueva canción al sistema.
     *
     * @param detallesDTO Detalles de la canción en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    @Override
    public void agregarCancion(DetallesCancionDTO detallesDTO) throws NegocioException {
        try {
            if (detallesDTO == null || detallesDTO.getTitulo() == null || detallesDTO.getDuracion() == null) {
                throw new NegocioException("Los detalles de la canción no pueden ser nulos.");
            }

            // Convertir DTO a entidad
            DetallesCancion detallesCancion = ConvertidorGeneral.convertidorEntidad(detallesDTO, DetallesCancion.class);

            // Llamar al DAO para insertar la canción
            cancionDAO.insertarCancion(detallesCancion);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al agregar la canción: " + e.getMessage(), e);
        }
    }


    /**
     * Busca canciones por título.
     *
     * @param titulo El título parcial o completo de la canción.
     * @return Lista de canciones encontradas en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    @Override
    public List<DetallesCancionDTO> buscarPorTitulo(String titulo) throws NegocioException {
        try {
            List<DetallesCancion> canciones = cancionDAO.buscarPorTitulo(titulo);
            return ConvertidorGeneral.convertidoraListaDTO(canciones, DetallesCancionDTO.class);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar canciones por título: " + e.getMessage(), e);
        }
    }


}
