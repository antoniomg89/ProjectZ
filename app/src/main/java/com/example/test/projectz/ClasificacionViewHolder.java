package com.example.test.projectz;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Clase ViewHolder que se usa para añadir datos al RecyclerView de la clasificación de los juegos.
 */

public class ClasificacionViewHolder extends RecyclerView.ViewHolder{
    private final TextView fechaJuego;

    /**
     * Constructor de la clase.
     *
     * @param v Vista.
     */

    public ClasificacionViewHolder(View v) {
            super(v);

            fechaJuego = v.findViewById(R.id.textViewRecyclerFechaClasificacion);

    }

    /**
     * Vincula la fecha a su respectivo componente.
     *
     * @param clasificacion Registros de la clasificación.
     */

    public void bind (FBjuegoClasificacion clasificacion){
        this.fechaJuego.setText(clasificacion.getFecha());

    }



}
