package com.example.test.projectz;

/**
 * Clase que contiene las recompensas asociadas a usuarios por completar rutas y juegos, y que se
 * usa para actualizar su nodo en firebase.
 */

public class FBusuarioRecompensas {
    private String id;
    private String nombre;
    private boolean reclamada;
    private int participaciones;
    private int experiencia;

    /**
     * Constructor de clase.
     */

    public FBusuarioRecompensas() {}

    /**
     * Constructor de clase con parámetros específicos.
     *
     * @param id Identificador de la actividad.
     * @param nom Nombre de actividad.
     * @param part Participaciones ganadas.
     * @param exp Experiencia ganada.
     * @param rec Estado de la reclamación.
     */

    public FBusuarioRecompensas(String id, String nom, int part, int exp, boolean rec){
        this.id = id;
        this.nombre = nom;
        this.participaciones = part;
        this.experiencia = exp;
        this.reclamada = rec;
    }

    /**
     *
     * @return Obtiene el identificador de actividad.
     */

    public String getId() { return this.id; }

    /**
     *
     * @return Obtiene el tipo de actividad.
     */

    public String getNombre() { return this.nombre; }

    /**
     *
     * @return Obtiene las participaciones de la actividad.
     */

    public int getParticipaciones() { return this.participaciones; }

    /**
     *
     * @return Obtiene la experiencia de la actividad.
     */

    public int getExperiencia() { return this.experiencia; }

    /**
     *
     * @return Obtiene el estado de la reclamación de la actividad.
     */

    public boolean getReclamada() { return this.reclamada; }

}