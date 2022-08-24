package com.example.test.projectz;

/**
 * Clase que contiene los datos del usuario y que se usa para actualizar su nodo en firebase.
 */

public class FBdatosUsuario {
    private int experiencia;
    private int nivel;
    private int expsignivel;
    private int participaciones;
    private int nuevo;

    /**
     * Constructor de clase.
     */

    public FBdatosUsuario() {}

    /**
     * Constructor de clase con parámetros específicos.
     *
     * @param nvl Nivel.
     * @param exp Experiencia.
     * @param exps Experiencia para el próximo nivel.
     * @param part Participaciones actuales.
     * @param nu El usuario es recién creado.
     */

    public FBdatosUsuario(int nvl, int exp, int exps, int part, int nu) {
        this.nivel = nvl;
        this.experiencia = exp;
        this.expsignivel = exps;
        this.participaciones = part;
        this.nuevo = nu;
    }

    /**
     *
     * @return Obtiene el nivel del usuario.
     */

    public int getNivel(){ return this.nivel; }

    /**
     *
     * @return Obtiene la experiencia del usuario.
     */

    public int getExperiencia(){ return this.experiencia; }

    /**
     *
     * @return Obtiene la experiencia para el próximo nivel.
     */

    public int getExpsignivel(){ return this.expsignivel; }

    /**
     *
     * @return Obtiene las participaciones del usuario.
     */

    public int getParticipaciones(){ return this.participaciones; }

    /**
     *
     * @return Obtiene si el usuario es o no nuevo.
     */

    public int getNuevo(){ return this.nuevo; }

}
