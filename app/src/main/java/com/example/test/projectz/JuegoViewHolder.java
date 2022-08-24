package com.example.test.projectz;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Clase ViewHolder que se usa para crear el RecyclerView de los juegos.
 */

public class JuegoViewHolder extends RecyclerView.ViewHolder{
    private final TextView textViewFragmentoJuegoDescripcion;
    private final TextView textViewFragmentJuegoTicket;

    /**
     * Constructor de la clase.
     *
     * @param v Vista.
     */

    public JuegoViewHolder(View v) {
        super(v);
        textViewFragmentoJuegoDescripcion = v.findViewById(R.id.textViewFragmentoJuegoDescripcion);
        textViewFragmentJuegoTicket = v.findViewById(R.id.textViewFragmentJuegoTicket);

    }

    /**
     * Vincula la descripción y las participaciones del juego a su respectivos componentes.
     *
     * @param juego Datos del juego.
     */

    public void bind (FBjuego juego){
        this.textViewFragmentoJuegoDescripcion.setText(juego.getDescripcion());
        this.textViewFragmentJuegoTicket.setText(String.valueOf(juego.getNivel()));

    }



}
