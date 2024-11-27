/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import excepciones.PersistenciaException;
import java.util.List;
import objetos.Integrantes;
import org.bson.types.ObjectId;

/**
 *
 * @author pauli
 */
public interface IIntegrantesDAO {

    void agregarIntegrante(ObjectId idArtista, Integrantes integrante) throws PersistenciaException;

    List<Integrantes> obtenerIntegrantesActivos(ObjectId idArtista) throws PersistenciaException;

    List<Integrantes> obtenerIntegrantesInactivos(ObjectId idArtista) throws PersistenciaException;
    
}
