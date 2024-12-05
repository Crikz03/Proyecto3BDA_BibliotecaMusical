/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import excepciones.PersistenciaException;

/**
 *
 * @author Chris
 */
public interface IinsercionMasivaDAO {

    void insertarArtistasMasivos() throws PersistenciaException;

    void insertarAlbumesMasivos() throws PersistenciaException;
}
