/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import datos.InsercionMasivaDAO;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import interfaces.IinsercionMasivaBO;
import interfaces.IinsercionMasivaDAO;

/**
 *
 * @author Chris
 */
public class InsercionMasivaBO implements IinsercionMasivaBO {

    private IinsercionMasivaDAO inserciondao;

    public InsercionMasivaBO() {
        this.inserciondao = new InsercionMasivaDAO();
    }

    @Override
    public void insertarArtistasMasivos() throws NegocioException {
        try {
            this.inserciondao.insertarArtistasMasivos();
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al insertar los 60 artistas en la base de datos de Mongo.");
        }
    }

    @Override
    public void insertarAlbumesMasivos() throws NegocioException {
        try {
            this.inserciondao.insertarAlbumesMasivos();
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al insertar los albumes a sus artistas en la base de datos de Mongo.");
        }
    }
}
