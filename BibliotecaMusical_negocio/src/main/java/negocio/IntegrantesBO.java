/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;


import conversiones.ConvertidorGeneral;
import datos.IntegrantesDAO;
import dto.IntegrantesDTO;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import interfaces.IIntegrantesBO;
import interfaces.IIntegrantesDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import objetos.Integrantes;
import org.bson.types.ObjectId;

/**
 *
 * @author pauli
 */
public class IntegrantesBO implements IIntegrantesBO{

    private final IIntegrantesDAO integrantesDAO;

    public IntegrantesBO() {
        this.integrantesDAO = new IntegrantesDAO(); 
    }

    /**
     * Agrega un nuevo integrante a la banda.
     *
     * @param idBanda ID de la banda.
     * @param integranteDTO Datos del integrante en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    public void agregarIntegrante(ObjectId idBanda, IntegrantesDTO integranteDTO) throws NegocioException {
        if (integranteDTO == null || integranteDTO.getNombreCompleto() == null || integranteDTO.getRol() == null) {
            throw new NegocioException("El integrante o sus datos obligatorios no pueden ser nulos.");
        }
        // Convertir DTO a entidad
        Integrantes integrante = ConvertidorGeneral.convertidorEntidad(integranteDTO, Integrantes.class);
        try {
            // Llamar al DAO para agregar el integrante
            integrantesDAO.agregarIntegrante(idBanda, integrante);
        } catch (PersistenciaException ex) {
            Logger.getLogger(IntegrantesBO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  
    /**
     * Obtiene los integrantes activos de una banda.
     *
     * @param idBanda ID de la banda.
     * @return Lista de integrantes activos en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    public List<IntegrantesDTO> obtenerIntegrantesActivos(ObjectId idBanda) throws NegocioException {
        try {
            List<Integrantes> integrantes = integrantesDAO.obtenerIntegrantesActivos(idBanda);
            return ConvertidorGeneral.convertidoraListaDTO(integrantes, IntegrantesDTO.class);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al obtener los integrantes activos: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene los integrantes inactivos de una banda.
     *
     * @param idBanda ID de la banda.
     * @return Lista de integrantes inactivos en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    public List<IntegrantesDTO> obtenerIntegrantesInactivos(ObjectId idBanda) throws NegocioException {
        try {
            List<Integrantes> integrantes = integrantesDAO.obtenerIntegrantesInactivos(idBanda);
            return ConvertidorGeneral.convertidoraListaDTO(integrantes, IntegrantesDTO.class);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al obtener los integrantes inactivos: " + e.getMessage(), e);
        }
    }

    
}
