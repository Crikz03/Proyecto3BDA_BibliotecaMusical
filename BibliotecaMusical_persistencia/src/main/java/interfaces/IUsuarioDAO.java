/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import excepciones.PersistenciaException;
import java.util.List;
import objetos.Favorito;
import objetos.Usuarios;
import org.bson.types.ObjectId;

/**
 *
 * @author Chris
 */
public interface IUsuarioDAO {

    public boolean agregar(Usuarios u) throws PersistenciaException;

    public boolean actualizar(Usuarios u) throws PersistenciaException;

    public Usuarios consultar(Usuarios u) throws PersistenciaException;

    public List<Usuarios> consultarTodos() throws PersistenciaException;

    Usuarios consultarCorreo(String correo) throws PersistenciaException;

    boolean agregarAFavoritos(ObjectId idUsuario, Favorito favorito) throws PersistenciaException;

    boolean agregarAFavoritos(ObjectId idUsuario, List<Favorito> favoritos) throws PersistenciaException;

    void actualizarGenerosNoDeseados(ObjectId usuarioId, List<String> generosBaneados) throws PersistenciaException;

    Usuarios consultarPorId(ObjectId idUsuario) throws PersistenciaException;

    Usuarios consultarPorNombre(String nombreUsuario) throws PersistenciaException;
}
