package com.example.test.projectz;

/**
 * Clase que contiene información referente a los límites de desplazamientos en mapas de rutas
 * y que se usa para actualizar su nodo en firebase.
 */

public class FBlocalidadesBoundBox {
    private String ne;
    private String sw;


    /**
     * Constructor de clase.
     */

    public FBlocalidadesBoundBox() {
    }

    /**
     * Constructor de clase con parámetros específicos.
     *
     * @param n Noreste.
     * @param s Suroeste.
     */

    public FBlocalidadesBoundBox(String n, String s) {
        this.ne = n;
        this.sw = s;
    }

    /**
     * @return Obtiene las coordenadas referentes al noreste de la localidad.
     */

    public String getNe() {
        return this.ne;
    }

    /**
     * @return Obtiene las coordenadas referentes al suroeste de la localidad.
     */

    public String getSw() {
        return this.sw;
    }

}
