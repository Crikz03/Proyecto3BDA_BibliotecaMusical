/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package recursos;

import javax.swing.JFrame;

/**
 * Clase abstracta que representa un formulario que puede ser actualizado.
 *
 * Esta clase extiende JFrame y requiere que las subclases implementen el método
 * {@code actualizaFrame()} para definir cómo se actualiza el contenido del
 * formulario cuando se carga o se regresa a él.
 *
 * @author Cristopher Alberto Elizalde Andrade - 240005
 * @author Paulina Rodríguez Rodríguez Rayos - 117262
 */
public abstract class JFormActualizado extends JFrame {

    /**
     * Método abstracto que debe ser implementado por las subclases para definir
     * la lógica de actualización del formulario.
     */
    public abstract void actualizaFrame();
}
