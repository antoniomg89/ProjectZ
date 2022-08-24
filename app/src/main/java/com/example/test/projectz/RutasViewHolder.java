package com.example.test.projectz;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

/**
 * Clase ViewHolder que se usa para añadir los datos al RecyclerView de las rutas.
 */

public class RutasViewHolder extends RecyclerView.ViewHolder{
    private final TextView nombreOferta;
    private final TextView precioRuta;
    private final ImageView imageViewRecyclerCompeticionRuta, imageViewRecyclerParticipacion2Ruta;
    private final RatingBar ratingBar;
    private final com.makeramen.roundedimageview.RoundedImageView fotoOferta;
    private final ConstraintLayout cl;

    /**
     * Constructor de la clase.
     *
     * @param v Vista.
     */

    public RutasViewHolder(View v) {
        super(v);

        cl = v.findViewById(R.id.principalRecycler);
        nombreOferta = v.findViewById(R.id.textViewRecyclerNombreOferta);
        precioRuta = v.findViewById(R.id.textViewRecyclerPrecioRuta);
        ratingBar = v.findViewById(R.id.ratingBarFragmentOfertas);
        imageViewRecyclerCompeticionRuta = v.findViewById(R.id.imageViewRecyclerCompeticionRuta);
        imageViewRecyclerParticipacion2Ruta = v.findViewById(R.id.imageViewRecyclerParticipacion2Ruta);

        fotoOferta = v.findViewById(R.id.imageViewOfertas);

    }

    /**
     * Vincula los datos correspondientes a los componentes del recyclerview de la ruta.
     *
     * @param ruta Datos de la ruta.
     */

    public void bind (FBruta ruta){
        this.nombreOferta.setText(ruta.getNombre());
        String st = ruta.getPrecio() + "€";
        this.precioRuta.setText(st);
        this.ratingBar.setRating(ruta.getValoracion());

        if (!ruta.getFotorv().equals("null")) {
            Picasso.get()
                    .load(ruta.getFotorv())
                    .fit()
                    .into(fotoOferta);
        }

        if (ruta.getEstado() == 1) {
            cl.setBackgroundResource(R.drawable.ofertas_shape_estado_1);

        } else if (ruta.getEstado() == 2) {
            cl.setBackgroundResource(R.drawable.ofertas_shape_estado_2);

        } else if (ruta.getEstado() == 3) {
            cl.setBackgroundResource(R.drawable.ofertas_shape_estado_3);
        }

        if (ruta.getCompetitiva() == 1) {
            imageViewRecyclerParticipacion2Ruta.setVisibility(View.VISIBLE);
            imageViewRecyclerCompeticionRuta.setVisibility(View.VISIBLE);
        } else if (ruta.getCompetitiva() == 0) {
            imageViewRecyclerParticipacion2Ruta.setVisibility(View.GONE);
            imageViewRecyclerCompeticionRuta.setVisibility(View.GONE);
        }

    }

}
