/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pruebas;

import datos.InsecionMasiva;
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
        InsecionMasiva in = new InsecionMasiva();

        in.insertarArtistasMasivos();
        in.insertarAlbumesMasivos();
    }

}
