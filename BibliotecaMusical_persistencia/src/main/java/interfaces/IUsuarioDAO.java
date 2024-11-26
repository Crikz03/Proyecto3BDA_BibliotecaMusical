/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import excepciones.PersistenciaException;
import java.util.List;
import objetos.Usuarios;

/**
 *
 * @author Chris
 */
public interface IUsuarioDAO {

    public boolean agregar(Usuarios u) throws PersistenciaException;

    public boolean actualizar(Usuarios u) throws PersistenciaException;

    public Usuarios consultar(Usuarios u) throws PersistenciaException;

    public List<Usuarios> consultarTodos() throws PersistenciaException;
}
