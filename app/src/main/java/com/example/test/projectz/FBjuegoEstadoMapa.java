package com.example.test.projectz;

/**
 * Clase que contiene el estado del botón del mapa en los juegos y que se usa para actualizar
 * su nodo en firebase.
 */

public class FBjuegoEstadoMapa {

    private String estado;

    /**
     * Constructor de clase.
     */

    public FBjuegoEstadoMapa() {}

    /**
     * Constructor de clase con parámetros específicos.
     *
     * @param est Estado del botón.
     */

    public FBjuegoEstadoMapa(String est){
        this.estado = est;
    }

    /**
     *
     * @return Obtiene el estado del botón del mapa.
     */

    public String getEstado(){ return this.estado; }

}