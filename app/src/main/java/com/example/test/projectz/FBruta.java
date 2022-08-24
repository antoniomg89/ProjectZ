package com.example.test.projectz;

/**
 * Clase que contiene los datos de cada ruta y que se usa para actualizar su nodo en firebase.
 */

public class FBruta {
    private String id;
    private String descripcion;
    private String descripcion2;
    private String descripcion3;
    private String nombre;
    private String empresa;
    private String empresa2;
    private String empresa3;
    private int estado;
    private String fechaC;
    private String fechaF;
    private String localidad;
    private String coordenadas;
    private String coordenadas2;
    private String coordenadas3;
    private int experiencia;
    private int participacion;
    private int competitiva;
    private float precio;
    private float precio1;
    private float precio2;
    private float precio3;
    private float valoracion;
    private String fotoact;
    private String fotoact2;
    private String fotoact3;
    private String fotoactpr;
    private String fotoactpr2;
    private String fotoactpr3;
    private String fotorv;
    private String idemp1;
    private String idemp2;
    private String idemp3;

    /**
     * Constructor de clase.
     */

    public FBruta() {}

    /**
     * Constructor de clase con parámetros específicos.
     *
     * @param desc Primer producto.
     * @param desc2 Segundo producto.
     * @param desc3 Tercer producto
     * @param nom Nombre.
     * @param emp Empresa 1.
     * @param emp2 Empresa 2.
     * @param emp3 Empresa 3.
     * @param ide1 Id empresa 1.
     * @param ide2 Id empresa 2.
     * @param ide3 Id empresa 3.
     * @param fc Fecha de inicio.
     * @param ff Fecha de finalización.
     * @param loc Localidad.
     * @param ide ID.
     * @param est Estado.
     * @param crd Coordenadas de la empresa 1.
     * @param crd2 Coordenadas de la empresa 2.
     * @param crd3 Coordenadas de la empresa 3.
     * @param exp Experiencia.
     * @param pr Número de veces que es necesario completar la oferta para obtener una participación.
     * @param comp Ruta normal o competitiva.
     * @param prec Precio total.
     * @param prec1 Precio del producto 1.
     * @param prec2 Precio del producto 2.
     * @param prec3 Precio del producto 3.
     * @param vl Valoración media de la oferta.
     * @param foa Imagen de la empresa 1.
     * @param foa2 Imagen de la empresa 2.
     * @param foa3 Imagen de la empresa 3.
     * @param foapr Imagen del producto de la empresa 1.
     * @param foapr2 Imagen del producto de la empresa 2.
     * @param foapr3 Imagen del producto de la empresa 3.
     * @param forv Imagen mostrada en la lista de ofertas y favoritos.
     */

    public FBruta(String desc, String desc2, String desc3, String nom, String emp, String emp2, String emp3,
                  String ide1, String ide2, String ide3, String fc, String ff, String loc, String ide, int est, String crd,
                  String crd2, String crd3, int exp, int pr, float vl, String foa, String foa2, String foa3,
                  String forv, int comp, float prec, float prec1, float prec2, float prec3, String foapr, String foapr2, String foapr3){
        this.id = ide;
        this.descripcion = desc;
        this.descripcion2 = desc2;
        this.descripcion3 = desc3;
        this.nombre = nom;
        this.empresa = emp;
        this.empresa2 = emp2;
        this.empresa3 = emp3;
        this.fechaC = fc;
        this.fechaF = ff;
        this.localidad = loc;
        this.estado = est;
        this.coordenadas = crd;
        this.coordenadas2 = crd2;
        this.coordenadas3 = crd3;
        this.experiencia = exp;
        this.participacion = pr;
        this.competitiva = comp;
        this.precio = prec;
        this.precio1 = prec1;
        this.precio2 = prec2;
        this.precio3 = prec3;
        this.valoracion = vl;
        this.fotoact = foa;
        this.fotoact2 = foa2;
        this.fotoact3 = foa3;
        this.fotoactpr = foapr;
        this.fotoactpr2 = foapr2;
        this.fotoactpr3 = foapr3;
        this.fotorv = forv;
        this.idemp1 = ide1;
        this.idemp2 = ide2;
        this.idemp3 = ide3;
    }

    /**
     *
     * @return Obtiene el id de la ruta.
     */

    public String getId(){ return this.id; }

    /**
     *
     * @return Obtiene el nombre de la ruta.
     */

    public String getNombre(){ return this.nombre; }

    /**
     *
     * @return Obtiene el nombrela primera empresa de la ruta.
     */

    public String getEmpresa(){ return this.empresa; }

    /**
     *
     * @return Obtiene el nombre la segunda empresa de la ruta.
     */

    public String getEmpresa2(){ return this.empresa2; }

    /**
     *
     * @return Obtiene el nombre la tercera empresa de la ruta.
     */

    public String getEmpresa3(){ return this.empresa3; }

    /**
     *
     * @return Obtiene el id de la primera empresa de la ruta.
     */

    public String getIdemp1(){ return this.idemp1; }

    /**
     *
     * @return Obtiene el id de la segunda empresa de la ruta.
     */

    public String getIdemp2(){ return this.idemp2; }

    /**
     *
     * @return Obtiene el id de la tercera empresa de la ruta.
     */

    public String getIdemp3(){ return this.idemp3; }

    /**
     *
     * @return Obtiene el estado de la ruta.
     */

    public int getEstado() { return this.estado; }

    /**
     *
     * @return Obtiene la fecha de inicio de la ruta.
     */

    public String getFechaC() { return this.fechaC; }

    /**
     *
     * @return Obtiene la fecha de fin de la ruta.
     */

    public String getFechaF() { return this.fechaF; }

    /**
     *
     * @return Obtiene la descripción del primer producto de la ruta.
     */

    public String getDescripcion() { return this.descripcion; }

    /**
     *
     * @return Obtiene la descripción del segundo producto de la ruta.
     */

    public String getDescripcion2() { return this.descripcion2; }

    /**
     *
     * @return Obtiene la descripción del tercer producto de la ruta.
     */

    public String getDescripcion3() { return this.descripcion3; }

    /**
     *
     * @return Obtiene la localidad de la ruta.
     */

    public String getLocalidad() { return this.localidad; }

    /**
     *
     * @return Obtiene las coordenadas de la primera empresa la ruta.
     */

    public String getCoordenadas() { return this.coordenadas; }

    /**
     *
     * @return Obtiene las coordenadas de la segunda empresa la ruta.
     */

    public String getCoordenadas2() { return this.coordenadas2; }

    /**
     *
     * @return Obtiene las coordenadas de la tercera empresa la ruta.
     */

    public String getCoordenadas3() { return this.coordenadas3; }

    /**
     *
     * @return Obtiene la experiencia de la ruta.
     */

    public int getExperiencia() { return this.experiencia; }

    /**
     *
     * @return Obtiene el número de veces que es necesario completar la ruta para obtener una participación.
     */

    public int getParticipacion() { return this.participacion; }

    /**
     *
     * @return Obtiene si es una ruta competitiva o normal.
     */

    public int getCompetitiva() { return this.competitiva; }

    /**
     *
     * @return Obtiene el precio total de la ruta.
     */

    public float getPrecio() { return this.precio; }

    /**
     *
     * @return Obtiene el precio de la primera empresa.
     */

    public float getPrecio1() { return this.precio1; }

    /**
     *
     * @return Obtiene el precio de la segunda empresa.
     */

    public float getPrecio2() { return this.precio2; }

    /**
     *
     * @return Obtiene el precio de la tercera empresa.
     */

    public float getPrecio3() { return this.precio3; }

    /**
     *
     * @return Obtiene la valoración media de la ruta.
     */

    public float getValoracion() { return this.valoracion; }

    /**
     *
     * @return Obtiene la imagen de la primera empresa.
     */

    public String getFotoact() { return this.fotoact; }

    /**
     *
     * @return Obtiene la imagen de la segunda empresa.
     */

    public String getFotoact2() { return this.fotoact2; }

    /**
     *
     * @return Obtiene la imagen de la tercera empresa.
     */

    public String getFotoact3() { return this.fotoact3; }

    /**
     *
     * @return Obtiene la imagen del producto de la empresa 1.
     */

    public String getFotoactpr() { return this.fotoactpr; }

    /**
     *
     * @return Obtiene la imagen del producto de la empresa 2.
     */

    public String getFotoactpr2() { return this.fotoactpr2; }

    /**
     *
     * @return Obtiene la imagen del producto de la empresa 3.
     */

    public String getFotoactpr3() { return this.fotoactpr3; }

    /**
     *
     * @return Obtiene la imagen de la ruta.
     */

    public String getFotorv() { return this.fotorv; }

}
