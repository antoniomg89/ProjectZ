package com.example.test.projectz;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * PagerAdapter de los fragmentos sobre noticias y actividad reciente.
 */

class PagerAdapter extends FragmentStateAdapter {

    /**
     * Constructor del adaptador.
     *
     */

    public PagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    /**
     * Asigna los fragmentos correspondientes a cada posición.
     *
     * @param position Posición.
     * @return Fragmento correspondiente.
     */

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new HistorialFragment();
        }

        return new NoticiasFragment();
    }

    /**
     * Obtiene el número de fragmentos asociados al adaptador.
     *
     * @return 2
     */

    @Override
    public int getItemCount() {
        return 2;
    }
}
