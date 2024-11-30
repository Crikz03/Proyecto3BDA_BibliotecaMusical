/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package recursos;

import javax.swing.JFrame;

/**
 * Clase utilitaria para manejar la carga de formularios en la interfaz gráfica
 * de usuario.
 *
 * Proporciona métodos para cargar nuevos formularios y regresar a formularios
 * anteriores, facilitando la navegación entre diferentes pantallas en una
 * aplicación de escritorio.
 *
 * @author Cristopher Alberto Elizalde Andrade - 240005
 * @author Paulina Rodríguez Rodríguez Rayos - 117262
 */
public class Forms {

    // Constructor privado para prevenir la instanciación de la clase utilitaria.
    private Forms() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Carga un nuevo formulario, ocultando el formulario actual.
     *
     * @param cargar El formulario a cargar.
     * @param actual El formulario actual que se va a ocultar.
     * @param <T> Tipo del formulario que extiende JFrame.
     */
    public static <T extends JFrame> void cargarForm(
            final T cargar,
            final JFrame actual
    ) {
        if (cargar != null) {
            actual.setVisible(false);
            cargar.setVisible(true);
            return;
        }
        throw new InstantiationError("Form no instanciado");
    }

    /**
     * Carga un formulario que implementa JFormActualizado, ocultando el
     * formulario actual y actualizando el nuevo formulario.
     *
     * @param cargar El formulario a cargar.
     * @param actual El formulario actual que se va a ocultar.
     * @param <T> Tipo del formulario que extiende JFormActualizado.
     */
    public static <T extends JFormActualizado> void cargarForm(T cargar, JFrame actual) {
        if (cargar != null) {
            actual.setVisible(false);
            cargar.actualizaFrame();
            cargar.setVisible(true);
            return;
        }
        throw new InstantiationError("Form no instanciado");
    }

    /**
     * Regresa al formulario anterior, actualizándolo y ocultando el formulario
     * actual.
     *
     * @param frmAnterior El formulario anterior a mostrar.
     * @param actual El formulario actual que se va a ocultar.
     */
    public static void regresar(JFormActualizado frmAnterior, JFrame actual) {
        frmAnterior.actualizaFrame();
        frmAnterior.setVisible(true);
        actual.setVisible(false);
    }
}
