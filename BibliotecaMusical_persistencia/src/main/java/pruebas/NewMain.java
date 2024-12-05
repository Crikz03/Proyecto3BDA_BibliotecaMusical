/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pruebas;

import datos.InsercionMasivaDAO;
import excepciones.PersistenciaException;

/**
 *
 * @author Chris
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws PersistenciaException {
        InsercionMasivaDAO in = new InsercionMasivaDAO();

        in.insertarArtistasMasivos();
        in.insertarAlbumesMasivos();
    }

}
