package com.example.test.projectz;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

/**
 * Clase ViewHolder que se usa para crear el RecyclerView de las rutas en favoritos.
 */

public class FavoritosViewHolder extends RecyclerView.ViewHolder{
    private final TextView nombreFavoritos;
    private final TextView precioFavoritos;
    private final ImageView imageViewRecyclerCompeticionFavoritos, imageViewRecyclerParticipacion2Favoritos;
    private final com.makeramen.roundedimageview.RoundedImageView fotoFavoritos;
    private final RatingBar ratingBar;
    private final ConstraintLayout cl;



    /**
     * Constructor de la clase.
     *
     * @param v Vista.
     */

    public FavoritosViewHolder(View v) {
        super(v);

        cl = v.findViewById(R.id.principalRecycler2);
        nombreFavoritos = v.findViewById(R.id.textViewRecyclerNombreFavoritos);
        precioFavoritos = v.findViewById(R.id.textViewRecyclerPrecioRutaFavoritos);
        imageViewRecyclerParticipacion2Favoritos = v.findViewById(R.id.imageViewRecyclerParticipacion2Favoritos);
        imageViewRecyclerCompeticionFavoritos = v.findViewById(R.id.imageViewRecyclerCompeticionFavoritos);
        ratingBar = v.findViewById(R.id.ratingBarFragmentFavoritos);
        fotoFavoritos = v.findViewById(R.id.imageViewFavoritos);
    }

    /**
     * Vincula el nombre, la experiencia y la participación de la ruta a sus respectivos componentes.
     *
     * @param ruta Registros de la ruta.
     */

    public void bind (FBruta ruta) {
        this.nombreFavoritos.setText(ruta.getNombre());
        String st = ruta.getPrecio() + "€";
        this.precioFavoritos.setText(st);
        this.ratingBar.setRating(ruta.getValoracion());

        Picasso.get()
                .load(ruta.getFotorv())
                .fit()
                .into(fotoFavoritos);

        if (ruta.getEstado() == 1) {
            cl.setBackgroundResource(R.drawable.ofertas_shape_estado_1);

        } else if (ruta.getEstado() == 2) {
            cl.setBackgroundResource(R.drawable.ofertas_shape_estado_2);

        } else if (ruta.getEstado() == 3) {
            cl.setBackgroundResource(R.drawable.ofertas_shape_estado_3);


        }

        if (ruta.getCompetitiva() == 1) {
            imageViewRecyclerParticipacion2Favoritos.setVisibility(View.VISIBLE);
            imageViewRecyclerCompeticionFavoritos.setVisibility(View.VISIBLE);
        }


    }

}
