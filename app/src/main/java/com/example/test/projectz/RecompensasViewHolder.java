package com.example.test.projectz;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Clase ViewHolder que se usa para crear el RecyclerView de las recompensas.
 */

public class RecompensasViewHolder extends RecyclerView.ViewHolder{
    private final TextView textViewNombreActividadRecompensa;
    private final TextView textViewNombreActividadRecompensaEstado;
    private final ImageView imageViewActividadRecompensaEstadoFalse;
    private final ImageView imageViewActividadRecompensaEstadoTrue;

    /**
     * Constructor de la clase.
     *
     * @param v Vista.
     */

    public RecompensasViewHolder(View v) {
        super(v);
        textViewNombreActividadRecompensa = v.findViewById(R.id.textViewNombreActividadRecompensa);
        textViewNombreActividadRecompensaEstado = v.findViewById(R.id.textViewNombreActividadRecompensaEstado);
        imageViewActividadRecompensaEstadoFalse = v.findViewById(R.id.imageViewActividadRecompensaEstadoFalse);
        imageViewActividadRecompensaEstadoTrue = v.findViewById(R.id.imageViewActividadRecompensaEstadoTrue);

    }

    /**
     * Vincula el nombre de la actividad correspondiente a la recompensa.
     *
     * @param recompensa Datos de la recompensa.
     */

    public void bind (FBusuarioRecompensas recompensa){
        this.textViewNombreActividadRecompensa.setText(recompensa.getNombre());

        if(recompensa.getReclamada()) {
            imageViewActividadRecompensaEstadoFalse.setVisibility(View.GONE);
            textViewNombreActividadRecompensaEstado.setVisibility(View.VISIBLE);
            imageViewActividadRecompensaEstadoTrue.setVisibility(View.VISIBLE);
        } else {
            textViewNombreActividadRecompensaEstado.setVisibility(View.GONE);
            imageViewActividadRecompensaEstadoTrue.setVisibility(View.GONE);
            imageViewActividadRecompensaEstadoFalse.setVisibility(View.VISIBLE);
        }

    }



}
