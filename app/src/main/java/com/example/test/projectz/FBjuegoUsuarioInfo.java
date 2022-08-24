package com.example.test.projectz;

/**
 * Clase que contiene información relacionada con los juegos en los que participa el usuario
 * y que se usa para actualizar su nodo en firebase.
 */

public class FBjuegoUsuarioInfo {
    private String fechaInscripcion;
    private String fechaValidacion;
    private String fechaCompletada;
    private int cantidadValidados;
    private int sensor;

    /**
     * Constructor de clase.
     */

    public FBjuegoUsuarioInfo() {}

    /**
     * Constructor de clase con parámetros específicos.
     *
     * @param fc Fecha en la que se completa el juego.
     * @param fi Fecha en la que se inscribe el usuario.
     * @param fv Fechas en las que se han validado los códigos QR.
     * @param cnt Número de códigos QR que se han validado.
     * @param sr Estado del sensor de códigos qr
     */

    public FBjuegoUsuarioInfo(String fc, String fi, String fv, int cnt, int sr){
        this.fechaCompletada = fc;
        this.fechaInscripcion = fi;
        this.fechaValidacion = fv;
        this.cantidadValidados = cnt;
        this.sensor = sr;
    }

    /**
     *
     * @return Fecha en la que se completa la oferta.
     */

    public String getFechaCompletada() { return this.fechaCompletada; }

    /**
     *
     * @return Fecha en la que se se inscribe el usuario.
     */

    public String getFechaInscripcion() { return this.fechaInscripcion; }

    /**
     *
     * @return Fecha en la que se valida cada código QR.
     */

    public String getFechaValidacion() { return this.fechaValidacion; }

    /**
     *
     * @return Cantidad de códigos QR validados.
     */

    public int getCantidadValidados() { return this.cantidadValidados; }

    /**
     *
     * @return Estado del sensor de códigos qr.
     */

    public int getSensor() { return this.sensor; }
}
