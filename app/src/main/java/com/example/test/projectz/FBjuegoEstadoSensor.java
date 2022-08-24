package com.example.test.projectz;

/**
 * Clase que contiene el estado del botón del sensor en los juegos y que se usa para actualizar
 * su nodo en firebase.
 */

public class FBjuegoEstadoSensor {

    private String estado;

    /**
     * Constructor de clase.
     */

    public FBjuegoEstadoSensor() {}

    /**
     * Constructor de clase con parámetros específicos.
     *
     * @param est Estado del botón.
     */

    public FBjuegoEstadoSensor(String est){
        this.estado = est;
    }

    /**
     *
     * @return Obtiene el estado del botón del sensor.
     */

    public String getEstado(){ return this.estado; }

}