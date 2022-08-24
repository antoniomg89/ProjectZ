package com.example.test.projectz;

/**
 * Clase que contiene información referente a los límites de desplazamientos en mapas de juegos
 * y que se usa para actualizar su nodo en firebase.
 */

public class FBjuegosBoxBound {
    private String ne;
    private String sw;


    /**
     * Constructor de clase.
     */

    public FBjuegosBoxBound() {
    }

    /**
     * Constructor de clase con parámetros específicos.
     *
     * @param n Noreste.
     * @param s Suroeste.
     */

    public FBjuegosBoxBound(String n, String s) {
        this.ne = n;
        this.sw = s;
    }

    /**
     * @return Obtiene las coordenadas referentes al noreste del área del juego.
     */

    public String getNe() {
        return this.ne;
    }

    /**
     * @return Obtiene las coordenadas referentes al suroeste del área del juego.
     */

    public String getSw() {
        return this.sw;
    }

}
