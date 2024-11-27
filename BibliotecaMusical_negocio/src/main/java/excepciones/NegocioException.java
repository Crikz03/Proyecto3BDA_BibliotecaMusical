/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

/**
 * Clase que representa una excepción personalizada para la capa de negocio. Se
 * utiliza para manejar errores específicos que pueden ocurrir en el contexto de
 * la lógica de negocio.
 *
 * @author Cristopher Alberto Elizalde Andrade - 240005
 * @author Paulina Rodríguez Rodríguez Rayos - 117262
 */
public class NegocioException extends Exception {

    /**
     * Constructor vacío.
     */
    public NegocioException() {
    }

    /**
     * Constructor que establece el mensaje de la excepción.
     *
     * @param message de la excepción.
     */
    public NegocioException(String message) {
        super(message);
    }

    /**
     * Constructor que establece el mensaje y la causa de la excepción.
     *
     * @param message de la excepción.
     * @param cause de la excepción.
     */
    public NegocioException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor que establece la causa de la excepción.
     *
     * @param cause de la excepción
     */
    public NegocioException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor que establece el mensaje, la causa, y otros atributos de la
     * excepción.
     *
     * @param message El mensaje descriptivo de la excepción.
     * @param cause La causa de la excepción.
     * @param enableSuppression Indica si la supresión de excepciones está
     * habilitada o no.
     * @param writableStackTrace Indica si se debe escribir el stack trace en la
     * salida estándar de error.
     */
    public NegocioException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
