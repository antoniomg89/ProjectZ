package com.example.test.projectz;

/**
 * Clase que contiene información referente a la foto de perfil y que se usa para actualizar
 * su nodo en firebase.
 */

public class FBfotoUsuario {
    private String actualizacionFoto;
    private int intentosFoto;
    private String fotourl;


    /**
     * Constructor de clase.
     */

    public FBfotoUsuario() {}

    /**
     * Constructor de clase con parámetros específicos.
     *
     * @param fecha Fecha en la que se hace el cambio de foto.
     * @param num Números de intentos de cambio de foto actuales.
     * @param fo Enlace de la foto de perfil.
     */

    public FBfotoUsuario(String fecha, int num, String fo) {
        this.actualizacionFoto = fecha;
        this.intentosFoto = num;
        this.fotourl = fo;
    }

    /**
     *
     * @return Obtiene la fecha en la que se hizo el último cambio de foto.
     */

    public String getActualizacionFoto() { return this.actualizacionFoto; }

    /**
     *
     * @return Obtiene los intentos de cambio de foto restantes.
     */

    public int getIntentosFoto() { return this.intentosFoto; }

    /**
     *
     * @return Obtiene la url de la foto.
     */

    public String getFotourl() { return this.fotourl; }
}
