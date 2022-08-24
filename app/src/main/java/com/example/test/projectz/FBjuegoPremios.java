package com.example.test.projectz;

/**
 * Clase que contiene los premios de cada juego y que se usa para actualizar su nodo en firebase.
 */

public class FBjuegoPremios {
    private String premio1;
    private String premio2;
    private String premio3;
    private String premio4;
    private int experiencia1;
    private int experiencia2;
    private int experiencia3;
    private int experiencia4;

    /**
     * Constructor de clase.
     */

    public FBjuegoPremios() {}

    /**
     * Constructor de clase con parámetros específicos.
     *
     * @param exp1 Experiencia ganador.
     * @param exp2 Experiencia 2º y 3º.
     * @param exp3 Experiencia 4º a 10º.
     * @param exp4 Experiencia resto de participantes.
     * @param prm1 Premio ganador.
     * @param prm2 Premio 2º y 3º.
     * @param prm3 Premio 4º a 10º.
     * @param prm4 Premio resto de participantes.
     */

    public FBjuegoPremios(int exp1, int exp2, int exp3, int exp4, String prm1, String prm2, String prm3, String prm4){
        this.experiencia1 = exp1;
        this.experiencia2 = exp2;
        this.experiencia3 = exp3;
        this.experiencia4 = exp4;
        this.premio1 = prm1;
        this.premio2 = prm2;
        this.premio3 = prm3;
        this.premio4 = prm4;

    }

    /**
     *
     * @return Obtiene la experiencia ganador del juego.
     */

    public int getExperiencia1() { return this.experiencia1; }

    /**
     *
     * @return Obtiene la experiencia del segundo y tercer puesto.
     */

    public int getExperiencia2() { return this.experiencia2; }

    /**
     *
     * @return Obtiene la experiencia del cuarto al décimo puesto.
     */

    public int getExperiencia3() { return this.experiencia3; }

    /**
     *
     * @return Obtiene la experiencia del resto de participantes.
     */

    public int getExperiencia4() { return this.experiencia4; }

    /**
     *
     * @return Obtiene el premio del ganador.
     */

    public String getPremio1() { return this.premio1; }

    /**
     *
     * @return Obtiene el premio del segundo y tercer puesto.
     */

    public String getPremio2() { return this.premio2; }

    /**
     *
     * @return Obtiene el premio del cuarto al décimo puesto.
     */

    public String getPremio3() { return this.premio3; }

    /**
     *
     * @return Obtiene el premio del resto de participantes.
     */

    public String getPremio4() { return this.premio4; }

}