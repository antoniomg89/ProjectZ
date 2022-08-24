package com.example.test.projectz;

/**
 * Clase que contiene los badges asociados a las rutas favoritas del usuario y que se usa para
 * actualizar su nodo en firebase.
 */

public class FBbadgeFavoritos {
    private int total;

    /**
     * Constructor de clase.
     */

    public FBbadgeFavoritos() {}

    /**
     * Constructor de clase con parámetros específicos.
     *
     * @param t Total.
     */

    public FBbadgeFavoritos(int t){
        this.total = t;

    }

    /**
     *
     * @return Obtiene los badges pendientes asociados a las rutas favoritas del usuario.
     */

    public int getTotal() { return this.total; }



}