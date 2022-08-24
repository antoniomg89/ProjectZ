package com.example.test.projectz;

/**
 * Clase que contiene información relacionada con el progreso general del juego
 * y que se usa para actualizar su nodo en firebase.
 */

public class FBjuegoProgreso {
    private String estado;
    private int qr;

    /**
     * Constructor de clase.
     */

    public FBjuegoProgreso() {}

    /**
     * Constructor de clase con parámetros específicos.
     *
     * @param ej Estado de la cuenta atras.
     * @param qrj Qr que se está buscando actualmente.
     *
     */

    public FBjuegoProgreso(String ej, int qrj){
        this.estado = ej;
        this.qr = qrj;
    }

    /**
     *
     * @return Obtiene el estado de la cuenta atras.
     */

    public String getEstado() { return this.estado; }

    /**
     *
     * @return Obtiene el código QR que se está buscando actualmente.
     */

    public int getQr() { return this.qr; }
}
