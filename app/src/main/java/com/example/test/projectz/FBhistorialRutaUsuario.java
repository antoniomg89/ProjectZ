package com.example.test.projectz;

/**
 * Clase que contiene el progreso del usuario en las rutas en las que participa.
 */

public class FBhistorialRutaUsuario {
   private String empresa, empresa2, empresa3, fechaCompletada, fechaCompletada2, fechaCompletada3, localidad, nombre;
   private int vecesCompletada;
   private boolean valorada;

   /**
    * Constructor de clase.
    */

   public FBhistorialRutaUsuario() {}

   /**
    * Constructor de clase con parámetros específicos.
    *
    * @param emp Nombre de la empresa 1.
    * @param emp2 Nombre de la empresa 2.
    * @param emp3 Nombre de la empresa 3.
    * @param fc Fecha en la que se completa la ruta en la empresa 1.
    * @param fc2 Fecha en la que se completa la ruta en la empresa 2.
    * @param fc3 Fecha en la que se completa la ruta en la empresa 3.
    * @param loc Localidad de la oferta.
    * @param num Número de veces que el usuario ha completado la oferta.
    * @param vl Estado de la valoración de la oferta.
    * @param nom Nombre de la ruta
    */

   public FBhistorialRutaUsuario(String emp, String emp2, String emp3, String fc, String fc2, String fc3, String loc, int num, boolean vl, String nom){
      this.empresa = emp;
      this.empresa2 = emp2;
      this.empresa3 = emp3;
      this.fechaCompletada = fc;
      this.fechaCompletada2 = fc2;
      this.fechaCompletada3 = fc3;
      this.localidad = loc;
      this.nombre = nom;
      this.vecesCompletada = num;
      this.valorada = vl;
   }

   /**
    *
    * @return Obtiene el nombre de la primera empresa perteneciente a la ruta.
    */

   public String getEmpresa() { return this.empresa; }

   /**
    *
    * @return Obtiene el nombre de la segunda empresa perteneciente a la ruta.
    */

   public String getEmpresa2() { return this.empresa2; }

   /**
    *
    * @return Obtiene el nombre de la tercera empresa perteneciente a la ruta.
    */

   public String getEmpresa3() { return this.empresa3; }

   /**
    *
    * @return Obtiene la fecha en la que se valida el servicio de la primera empresa.
    */

   public String getFechaCompletada() { return this.fechaCompletada; }

   /**
    *
    * @return Obtiene la fecha en la que se valida el servicio de la segunda empresa.
    */

   public String getFechaCompletada2() { return this.fechaCompletada2; }

   /**
    *
    * @return Obtiene la fecha en la que se valida el servicio de la tercera empresa.
    */

   public String getFechaCompletada3() { return this.fechaCompletada3; }

   /**
    *
    * @return Obtiene la localidad de la ruta.
    */

   public String getLocalidad() { return this.localidad; }

   /**
    *
    * @return Obtiene el número de veces que el usuario ha completado la ruta.
    */

   public int getVecesCompletada() {return this.vecesCompletada; }

   /**
    *
    * @return Obtiene el estado de valoración la ruta.
    */

   public boolean getValorada() {return this.valorada; }

   /**
    *
    * @return Obtiene el nombre de la ruta.
    */

   public String getNombre() { return this.nombre; }
}
