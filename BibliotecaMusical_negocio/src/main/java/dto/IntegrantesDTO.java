/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.util.Date;

/**
 *
 * @author pauli
 */
public class IntegrantesDTO {

    private String nombreCompleto;
    private String rol;
    private Date fechaIngreso;
    private Date fechaSalida;
    private boolean activo;

    public IntegrantesDTO() {
    }

    public IntegrantesDTO(String nombreCompleto, String rol, Date fechaIngreso, Date fechaSalida, boolean activo) {
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
        this.fechaIngreso = fechaIngreso;
        this.fechaSalida = fechaSalida;
        this.activo = activo;
    }

    // Getters y Setters
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // Método adicional para obtener una representación legible de las fechas
    public String getPeriodoActivo() {
        if (fechaSalida == null) {
            return "Desde " + fechaIngreso;
        } else {
            return fechaIngreso + " - " + fechaSalida;
        }
    }

    @Override
    public String toString() {
        return "IntegrantesDTO{" +
                "nombreCompleto='" + nombreCompleto + '\'' +
                ", rol='" + rol + '\'' +
                ", fechaIngreso=" + fechaIngreso +
                ", fechaSalida=" + fechaSalida +
                ", activo=" + activo +
                '}';
    }
}
