package com.example.test.projectz;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

/**
 * Fragmento que gestiona las rutas que el usuario ha agregado a favoritos.
 */

public class FavoritosFragment extends Fragment {
    private TextView textViewFragmentFavoritos;
    private RecyclerView rvFavoritos;
    private FirebaseRecyclerAdapter adapter;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference usuariohistorial;
    private NumberProgressBar progressBarFavoritos;

    /**
     * Crea la vista del fragmento con sus componentes y los inicializa.
     *
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View favoritosView = inflater.inflate(R.layout.fragment_favoritos,container,false);
        // Obtiene el usuario conectado.
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        textViewFragmentFavoritos = favoritosView.findViewById(R.id.textViewFragmentFavoritos);

        rvFavoritos = favoritosView.findViewById(R.id.recyclerViewFragmentFavoritos);
        rvFavoritos.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rvFavoritos.setHasFixedSize(true);

        return favoritosView;
    }

    /**
     * Inicia el adaptador del recyclerview de favoritos.
     *
     */

    private void initAdapter() {
        adapter = new FirebaseRecyclerAdapter<FBruta, FavoritosViewHolder>(getOptions(-1)) {

            @NonNull
            @Override
            public FavoritosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.activity_reciclercardview_favoritos, viewGroup, false);

                return new FavoritosViewHolder(v);
            }


            @Override
            protected void onBindViewHolder(@NonNull final FavoritosViewHolder holder, int position, @NonNull final FBruta model) {
                holder.bind(model);

                usuariohistorial = FirebaseDatabase.getInstance().getReference().child("usuariohistorial").child(mFirebaseUser.getUid()).child(model.getId());
                progressBarFavoritos = holder.itemView.findViewById(R.id.numberprogressBarFavoritos);

                setProgreso(usuariohistorial, progressBarFavoritos);

                holder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(getContext(), Ruta.class);
                    intent.putExtra("nom", model.getNombre());
                    intent.putExtra("dO", model.getDescripcion());
                    intent.putExtra("d2O", model.getDescripcion2());
                    intent.putExtra("d3O", model.getDescripcion3());
                    intent.putExtra("cO", model.getFechaC());
                    intent.putExtra("fO", model.getFechaF());
                    intent.putExtra("loc", model.getLocalidad());
                    intent.putExtra("crd", model.getCoordenadas());
                    intent.putExtra("crd2", model.getCoordenadas2());
                    intent.putExtra("crd3", model.getCoordenadas3());
                    intent.putExtra("est", model.getEstado());
                    intent.putExtra("emp", model.getEmpresa());
                    intent.putExtra("emp2", model.getEmpresa2());
                    intent.putExtra("emp3", model.getEmpresa3());
                    intent.putExtra("ide1", model.getIdemp1());
                    intent.putExtra("ide2", model.getIdemp2());
                    intent.putExtra("ide3", model.getIdemp3());
                    intent.putExtra("ide", model.getId());
                    intent.putExtra("exp", model.getExperiencia());
                    intent.putExtra("pr", model.getParticipacion());
                    intent.putExtra("vl", model.getValoracion());
                    intent.putExtra("foa", model.getFotoact());
                    intent.putExtra("foa2", model.getFotoact2());
                    intent.putExtra("foa3", model.getFotoact3());
                    intent.putExtra("forv", model.getFotorv());
                    intent.putExtra("comp", model.getCompetitiva());
                    intent.putExtra("prec", model.getPrecio());
                    intent.putExtra("prec3", model.getPrecio2());
                    intent.putExtra("prec2", model.getPrecio3());

                    startActivity(intent);
                });

            }

            @Override
            public void onDataChanged() {
                if (adapter.getItemCount() == 0) {
                    textViewFragmentFavoritos.setVisibility(View.VISIBLE);
                } else {
                    textViewFragmentFavoritos.setVisibility(View.GONE);
                }

            }
        };

        rvFavoritos.setAdapter(adapter);
        adapter.startListening();

    }

    /**
     * Cuando se inicia el fragmento, se inicia la escucha del adaptador del recyclerview.
     */

    @Override
    public void onStart() {
        super.onStart();
        DatabaseReference favoritosRef = FirebaseDatabase.getInstance().getReference().child("favoritos").child("usuarios").child(mFirebaseUser.getUid());

        favoritosRef.keepSynced(true);
        initAdapter();
        getBadges();
    }

    /**
     * Cuando se detiene el fragmento, se detiene la escucha del adaptador del recyclerview.
     */

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    /**
     * Obtiene el progreso actual del usuario en la ruta.
     */

    public void setProgreso(DatabaseReference usuariohistorial, NumberProgressBar pbar) {

        usuariohistorial.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String historialFecha1 = Objects.requireNonNull(snapshot.getValue(FBhistorialRutaUsuario.class)).getFechaCompletada();
                    String historialFecha2 = Objects.requireNonNull(snapshot.getValue(FBhistorialRutaUsuario.class)).getFechaCompletada2();
                    String historialFecha3 = Objects.requireNonNull(snapshot.getValue(FBhistorialRutaUsuario.class)).getFechaCompletada3();
                    int progreso = 0;

                    if (!historialFecha1.equals("nv")) {
                        progreso += 33;
                    }

                    if (!historialFecha2.equals("nv")) {
                        progreso += 33;
                    }

                    if (!historialFecha3.equals("nv")) {
                        progreso += 33;
                    }

                    if (progreso == 99) {
                        pbar.setProgress(100);
                    } else {
                        pbar.setProgress(progreso);
                    }

                } else {
                    pbar.setProgress(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /**
     * Borra los badges pendientes del usuario.
     *
     */

    public void getBadges() {
        DatabaseReference referenceBadge = FirebaseDatabase.getInstance().getReference().child("usuariosbadges").child(mFirebaseUser.getUid()).child("favoritos");

        referenceBadge.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int num = Objects.requireNonNull(snapshot.getValue(FBbadgeFavoritos.class)).getTotal();

                    if (num > 0) {
                        FBbadgeFavoritos bf = new FBbadgeFavoritos(0);
                        referenceBadge.setValue(bf);

                        BottomNavigationView navmenu = requireActivity().findViewById(R.id.navigation_menus);
                        BadgeDrawable badge = navmenu.getOrCreateBadge(R.id.navigation_favoritos);
                        badge.clearNumber();
                        badge.setVisible(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Actualiza la lista en función del estado seleccionado.
     *
     * @param estado Estado
     */

    public FirebaseRecyclerOptions<FBruta> getOptions(int estado) {
        FirebaseRecyclerOptions<FBruta> options;
        Query query;

        if (estado != -1) {
            query = FirebaseDatabase.getInstance().getReference()
                    .child("favoritos")
                    .child("usuarios")
                    .child(mFirebaseUser.getUid()).orderByChild("estado").equalTo(estado);

        } else {
            query = FirebaseDatabase.getInstance().getReference()
                    .child("favoritos")
                    .child("usuarios")
                    .child(mFirebaseUser.getUid());
        }

        options = new FirebaseRecyclerOptions.Builder<FBruta>()
                .setQuery(query, FBruta.class)
                .build();

        return options;
    }

}
