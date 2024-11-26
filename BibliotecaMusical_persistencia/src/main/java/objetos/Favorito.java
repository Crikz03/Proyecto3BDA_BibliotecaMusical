/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetos;

import java.util.Date;
import org.bson.types.ObjectId;
import recursos.tipoFavoritos;

/**
 *
 * @author Chris
 */
public class Favorito {

    private ObjectId IdReferecnia;
    private tipoFavoritos tipo;
    private String titulo;
    private Date fechaAgregado;
    private boolean activo;

    public Favorito(ObjectId IdReferecnia, tipoFavoritos tipo) {
        this.IdReferecnia = IdReferecnia;
        this.tipo = tipo;
        this.fechaAgregado = new Date();
        this.activo = true;
    }

    public ObjectId getId() {
        return IdReferecnia;
    }

    public void setId(ObjectId IdReferecnia) {
        this.IdReferecnia = IdReferecnia;
    }

    public tipoFavoritos getTipo() {
        return tipo;
    }

    public void setTipo(tipoFavoritos tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFechaAgregado() {
        return fechaAgregado;
    }

    public void setFechaAgregado(Date fechaAgregado) {
        this.fechaAgregado = fechaAgregado;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

}
