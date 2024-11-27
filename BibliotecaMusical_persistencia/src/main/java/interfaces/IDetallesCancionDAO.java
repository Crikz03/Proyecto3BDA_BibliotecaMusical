/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import excepciones.PersistenciaException;
import java.util.List;
import objetos.DetallesCancion;
import org.bson.types.ObjectId;

/**
 *
 * @author pauli
 */
public interface IDetallesCancionDAO {


    List<DetallesCancion> buscarPorArtista(ObjectId artistaId) throws PersistenciaException;

    List<DetallesCancion> buscarPorTitulo(String titulo) throws PersistenciaException;

    void insertarCancion(DetallesCancion cancion) throws PersistenciaException;
    
}
