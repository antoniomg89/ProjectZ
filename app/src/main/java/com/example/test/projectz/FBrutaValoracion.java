package com.example.test.projectz;

/**
 * Clase que contiene información referente a las valoraciones del usuario sobre cada ruta y que
 * se utiliza para actualizar su nodo en firebase.
 */

public class FBrutaValoracion {
    private float valoracion;

    /**
     * Constructor de clase.
     */

    public FBrutaValoracion() {}

    /**
     * Constructor de clase con parámetros específicos.
     *
     * @param val Fecha en la que se hace la valoración.
     */

    public FBrutaValoracion(float val) {
        this.valoracion = val;

    }

    /**
     *
     * @return Obtiene la valoración de la ruta.
     */

    public float getValoracion() { return this.valoracion; }

}
