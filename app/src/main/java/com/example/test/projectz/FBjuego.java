package com.example.test.projectz;

/**
 * Clase que contiene los datos de los juegos y que se usa para actualizar su nodo en firebase.
 */

public class FBjuego {

    private String id;
    private String descripcion;
    private String explicacion;
    private String ciudad;
    private String fechaC;
    private String fechaF;
    private String empresa;
    private int nivel;
    private int estado;
    private int participacion;


    /**
     * Constructor de clase.
     */

    public FBjuego() {}

    /**
     * Constructor de clase con parámetros específicos.
     *
     * @param desc Descripción.
     * @param fc Fecha de inicio.
     * @param ff Fecha de finalización.
     * @param ciu Ciudad.
     * @param ide ID.
     * @param est Estado del evento
     * @param part Paarticipacines requeridas
     * @param emp Empresa organizadora del evento.
     * @param expl Explicación acerca del funcionamiento del evento.
     * @param niv Nivel requerido.
     */

    public FBjuego(String desc, String fc, String ff, String ciu, String ide, int est, int part, String emp, String expl, int niv){
        this.id = ide;
        this.descripcion = desc;
        this.fechaC = fc;
        this.fechaF = ff;
        this.ciudad = ciu;
        this.estado = est;
        this.participacion = part;
        this.nivel = niv;
        this.empresa = emp;
        this.explicacion = expl;
    }

    /**
     *
     * @return Obtiene el id del juego.
     */

    public String getId(){ return this.id; }

    /**
     *
     * @return Obtiene el código QR del juego.
     */

    //public String getQRdata(){ return this.qrdata; }

    /**
     *
     * @return Obtiene el estado del juego.
     */

    public int getEstado() { return this.estado; }

    /**
     *
     * @return Obtiene la fecha de inicio del juego.
     */

    public String getFechac() { return this.fechaC; }

    /**
     *
     * @return Obtiene la fecha de finalizacion del juego.
     */

    public String getFechaf() { return this.fechaF; }

    /**
     *
     * @return Obtiene la descripción del juego.
     */

    public String getDescripcion() { return this.descripcion; }

    /**
     *
     * @return Obtiene la localidad del juego.
     */

    public String getCiudad() { return this.ciudad; }

    /**
     *
     * @return Obtiene el número de participaciones requeridas.
     */

    public int getParticipacion() { return this.participacion; }

    /**
     *
     * @return Obtiene el nivel requerido.
     */

    public int getNivel() { return this.nivel; }

    /**
     *
     * @return Obtiene la empresa organizadora del evento.
     */

    public String getEmpresa() { return this.empresa; }

    /**
     *
     * @return Obtiene la explicación del funcionamiento del evento.
     */

    public String getExplicacion() { return this.explicacion; }

}