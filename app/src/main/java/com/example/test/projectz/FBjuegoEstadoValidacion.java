package com.example.test.projectz;

/**
 * Clase que contiene el estado del botón de validación en los juegos y que se usa para actualizar
 * su nodo en firebase.
 */

public class FBjuegoEstadoValidacion {

    private String estado;

    /**
     * Constructor de clase.
     */

    public FBjuegoEstadoValidacion() {}

    /**
     * Constructor de clase con parámetros específicos.
     *
     * @param est Estado del botón.
     */

    public FBjuegoEstadoValidacion(String est){
        this.estado = est;
    }

    /**
     *
     * @return Obtiene el estado del botón de validación.
     */

    public String getEstado(){ return this.estado; }

}