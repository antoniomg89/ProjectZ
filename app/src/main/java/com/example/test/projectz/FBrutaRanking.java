package com.example.test.projectz;

/**
 * Clase que contiene información referente al ranking de las rutas competitivas y que
 * se utiliza para actualizar su nodo en firebase.
 */

public class FBrutaRanking {
    private String idemp1;
    private String idemp2;
    private String idemp3;

    /**
     * Constructor de clase.
     */

    public FBrutaRanking() {}

    /**
     * Constructor de clase con parámetros específicos.
     *
     * @param id1 Id de la empresa en primera posición.
     * @param id2 Id de la empresa en segunda posición.
     * @param id3 Id de la empresa en tercera posición.
     */

    public FBrutaRanking(String id1, String id2, String id3) {
        this.idemp1 = id1;
        this.idemp2 = id2;
        this.idemp3 = id3;

    }

    /**
     *
     * @return Obtiene el id de la empresa en primera posición.
     */

    public String getIdemp1() { return this.idemp1; }

    /**
     *
     * @return Obtiene el id de la empresa en segunda posición.
     */

    public String getIdemp2() { return this.idemp2; }

    /**
     *
     * @return Obtiene el id de la empresa en tercera posición.
     */

    public String getIdemp3() { return this.idemp3; }

}
