package com.example.test.projectz;

/**
 * Clase que contiene los datos de la clasificación actual del juego y que se usa para actualizar su nodo en firebase.
 */

public class FBjuegoClasificacion {

    private String fecha;

    /**
     * Constructor de clase.
     */

    public FBjuegoClasificacion() {}

    /**
     * Constructor de clase con parámetros específicos.
     *
     * @param fv Fecha de validación del código QR.
     */

    public FBjuegoClasificacion(String fv){
        this.fecha = fv;
    }

    /**
     *
     * @return Obtiene la fecha de validación del código QR.
     */

    public String getFecha(){ return this.fecha; }

}