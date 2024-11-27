/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import excepciones.PersistenciaException;
import java.util.List;
import objetos.Favorito;
import org.bson.types.ObjectId;
import recursos.tipoFavoritos;

/**
 *
 * @author pauli
 */
public interface IFavoritoDAO {

    boolean agregarFavorito(ObjectId idUsuario, Favorito favorito) throws PersistenciaException;

    boolean desactivarFavorito(ObjectId idUsuario, ObjectId idReferencia) throws PersistenciaException;

    boolean eliminarFavorito(ObjectId idUsuario, ObjectId idReferencia) throws PersistenciaException;

    List<Favorito> obtenerFavoritos(ObjectId idUsuario) throws PersistenciaException;

    List<Favorito> obtenerFavoritosPorTipo(ObjectId idUsuario, tipoFavoritos tipo) throws PersistenciaException;
    
}
