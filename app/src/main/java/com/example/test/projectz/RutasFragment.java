package com.example.test.projectz;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
 * Clase que gestiona las rutas disponibles en las que el usuario puede participar.
 */

public class RutasFragment extends Fragment{
    private RecyclerView rvRutas;
    private TextView textViewFragmentRutas;
    private FirebaseRecyclerAdapter adapter;
    private ProgressBar progressBarFragmentRutas;
    private FirebaseUser mFirebaseUser;
    private String localidad = "granada";
    private int filtro = 0;
    private final FuncionesGlobales fg = new FuncionesGlobales();
    private SharedPreferences pref;

    /**
     * Crea la vista del fragmento con sus componentes y los inicializa.
     *
     * @return Vista.
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View menusView = inflater.inflate(R.layout.fragment_rutas,container,false);

        pref = this.requireContext().getSharedPreferences("preferencias", 0);
        //String loc = getStringCache("oferlocalidad");
        //String filt = getStringCache("oferfiltro");

        //System.out.println("localidad cache: " + loc);
        //System.out.println("filtro cache: " + filt);

        // Detectar ciudad (trabajo futuro)
        /*Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = gcd.getFromLocation(lat, lng, 1);
        if (addresses.size() > 0) {
            System.out.println(addresses.get(0).getLocality());
        }
        else {

        }*/

        /*if (loc != null) {
            localidad = se.getCiuloc(loc);
        }

        if (filt  != null) {
            filtro = Integer.parseInt(filt);

        }*/

        ImageButton imageButtonLocalizacion = menusView.findViewById(R.id.imageButtonLocalizacion);
        ImageButton imageButtonFiltro = menusView.findViewById(R.id.imageButtonFiltro);
        progressBarFragmentRutas = menusView.findViewById(R.id.progressBarFragmentRutas);
        textViewFragmentRutas = menusView.findViewById(R.id.textViewFragmentRutas);

        rvRutas = menusView.findViewById(R.id.recyclerViewFragmentOfertas);
        rvRutas.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rvRutas.setHasFixedSize(true);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        assert mFirebaseUser != null;
        System.out.println("Usuario: " + mFirebaseUser.getUid());

        imageButtonLocalizacion.setOnClickListener(view -> {
            fg.startViewAnimation(getActivity(),imageButtonLocalizacion,0);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater li = LayoutInflater.from(getContext());
            final View viewdialog = li.inflate(R.layout.dialog_localizacion, null);
            builder.setView(viewdialog);
            AlertDialog dialog = builder.create();

            Typeface fuente = ResourcesCompat.getFont(requireContext(),R.font.poppinsregular);

            RadioButton rbgranada = viewdialog.findViewById(R.id.radioButtonGranada);
            rbgranada.setTypeface(fuente);
            rbgranada.setTextSize(16);

            RadioButton rbzaidin = viewdialog.findViewById(R.id.radioButtonZaidin);
            rbzaidin.setTypeface(fuente);
            rbzaidin.setTextSize(16);

            RadioButton rblachana = viewdialog.findViewById(R.id.radioButtonLaChana);
            rblachana.setTypeface(fuente);
            rblachana.setTextSize(16);

            if(localidad.equals("zaidin")) {
                rbzaidin.setChecked(true);
            } else if(localidad.equals("la chana")) {
                rblachana.setChecked(true);
            }

            rbgranada.setOnClickListener(view1 -> {
                localidad = "granada";
                setStringCache("oferlocalidad",localidad);

                initAdapter(localidad,filtro);
            });

            rbzaidin.setOnClickListener(view12 -> {
                localidad = "zaidin";
                setStringCache("oferlocalidad",localidad);

                initAdapter(localidad,filtro);

            });

            rblachana.setOnClickListener(view13 -> {
                localidad = "la chana";
                setStringCache("oferlocalidad",localidad);

                initAdapter(localidad,filtro);

            });

            dialog.show();


        });

        imageButtonFiltro.setOnClickListener(view -> {
            fg.startViewAnimation(getActivity(),imageButtonFiltro,0);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater li = LayoutInflater.from(getContext());
            final View viewdialog = li.inflate(R.layout.dialog_filtro, null);
            builder.setView(viewdialog);
            AlertDialog dialog = builder.create();

            Typeface fuente = ResourcesCompat.getFont(requireContext(),R.font.poppinsregular);

            RadioButton rbninguno = viewdialog.findViewById(R.id.radioButtonNinguno);
            rbninguno.setTypeface(fuente);
            rbninguno.setTextSize(15);

            RadioButton rbprecio = viewdialog.findViewById(R.id.radioButtonPrecio);
            rbprecio.setTypeface(fuente);
            rbprecio.setTextSize(15);

            RadioButton rbvaloracion = viewdialog.findViewById(R.id.radioButtonValoracion);
            rbvaloracion.setTypeface(fuente);
            rbvaloracion.setTextSize(15);

            RadioButton rbactivas = viewdialog.findViewById(R.id.radioButtonActivas);
            rbvaloracion.setTypeface(fuente);
            rbvaloracion.setTextSize(15);

            RadioButton rbporfinalizar = viewdialog.findViewById(R.id.radioButtonPorFinalizar);
            rbvaloracion.setTypeface(fuente);
            rbvaloracion.setTextSize(15);

            if(filtro == 1) {
                rbactivas.setChecked(true);
            } else if(filtro == 2) {
                rbporfinalizar.setChecked(true);
            } else if(filtro == 3) {
                rbprecio.setChecked(true);
            } else if(filtro == 4) {
                rbvaloracion.setChecked(true);
            }

            rbninguno.setOnClickListener(view14 -> {
                filtro = 0;
                setStringCache("oferfiltro",String.valueOf(filtro));
                initAdapter(localidad,filtro);
            });

            rbactivas.setOnClickListener(view15 -> {
                filtro = 1;
                setStringCache("oferfiltro",String.valueOf(filtro));
                initAdapter(localidad,filtro);
            });

            rbporfinalizar.setOnClickListener(view16 -> {
                filtro = 2;
                setStringCache("oferfiltro",String.valueOf(filtro));
                initAdapter(localidad,filtro);

            });

            rbprecio.setOnClickListener(view17 -> {
                filtro = 3;
                setStringCache("oferfiltro",String.valueOf(filtro));
                initAdapter(localidad,filtro);
            });

            rbvaloracion.setOnClickListener(view18 -> {
                filtro = 4;
                setStringCache("oferfiltro",String.valueOf(filtro));
                initAdapter(localidad,filtro);

            });

            dialog.show();
        });

        return menusView;
    }

    /**
     * Obtiene los badges correspondientes del usuario.
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
                        BottomNavigationView navmenu = requireActivity().findViewById(R.id.navigation_menus);
                        BadgeDrawable badge = navmenu.getOrCreateBadge(R.id.navigation_favoritos);
                        badge.setNumber(num);
                        badge.setBadgeTextColor(Color.parseColor("#000000"));
                        badge.setBackgroundColor(Color.parseColor("#6ff9ff"));
                        badge.setVisible(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Inicia el adaptador del recyclerview.
     *
     * @param loc Localidad por la que filtrar rutas.
     * @param fs Selección del filtro.
     */

    private void initAdapter(String loc, int fs) {
        progressBarFragmentRutas.setVisibility(View.VISIBLE);

        adapter = new FirebaseRecyclerAdapter<FBruta, RutasViewHolder>(getOptions(loc,fs)) {

            @NonNull
            @Override
            public RutasViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.activity_reciclercardview_rutas, viewGroup, false);

                return new RutasViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull final RutasViewHolder holder, int position, @NonNull final FBruta model) {
                holder.bind(model);

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
                    intent.putExtra("foapr", model.getFotoactpr());
                    intent.putExtra("foapr2", model.getFotoactpr2());
                    intent.putExtra("foapr3", model.getFotoactpr3());

                    startActivity(intent);

                });

            }

            @Override
            public void onDataChanged() {
                rvRutas.setVisibility(View.VISIBLE);
                progressBarFragmentRutas.setVisibility(View.GONE);

                if (adapter.getItemCount() == 0) {
                    textViewFragmentRutas.setVisibility(View.VISIBLE);
                } else {
                    textViewFragmentRutas.setVisibility(View.GONE);
                }



            }

        };

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        if (fs != 3) {
            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);

        } else {
            layoutManager.setReverseLayout(false);
            layoutManager.setStackFromEnd(false);
        }

        rvRutas.setLayoutManager(layoutManager);


        rvRutas.setAdapter(adapter);
        adapter.startListening();

    }

    /**
     * Cuando se inicia el fragmento, se inicia la escucha del adaptador.
     */

    @Override
    public void onStart() {
        super.onStart();
        DatabaseReference rutasRef = FirebaseDatabase.getInstance().getReference().child("ofertas").child(localidad);
        rutasRef.keepSynced(true);
        String loc = getStringCache("oferlocalidad");
        String filt = getStringCache("oferfiltro");

        //System.out.println("localidad cache: " + loc);
        //System.out.println("filtro cache: " + filt);

        if (loc != null) {
            localidad = fg.getCiuloc(loc);
        }

        if (filt  != null) {
            filtro = Integer.parseInt(filt);

        }

        initAdapter(fg.getCiuloc(localidad),filtro);
        //getBadges();
    }

    /**
     * Cuando se detiene el fragmento, se detiene la escucha del adaptador.
     */

    @Override
    public void onStop() {
        super.onStop();

        adapter.stopListening();
    }

    /**
     * Actualiza la lista en función del filtro  y localidad seleccionados.
     *
     * @param loc Localidad del filtro.
     * @param fs Selección del filtro.
     */

    public FirebaseRecyclerOptions<FBruta> getOptions(String loc, int fs) {
        Query query = null;
        FirebaseRecyclerOptions<FBruta> options;
        boolean f = false;

        if (fs != 0) {
            if (filtro == 1) {
                query = FirebaseDatabase.getInstance().getReference()
                        .child("ofertas")
                        .child("granada")
                        .child(loc).orderByChild("estado").equalTo(3);

                f = true;

            } else if (fs == 2) {
                query = FirebaseDatabase.getInstance().getReference()
                        .child("ofertas")
                        .child("granada")
                        .child(loc).orderByChild("estado").equalTo(2);

                f = true;
            } else if (fs == 3) {
                query = FirebaseDatabase.getInstance().getReference()
                        .child("ofertas")
                        .child("granada")
                        .child(loc).orderByChild("precio");

                f = true;
            } else if (filtro == 4) {
                query = FirebaseDatabase.getInstance().getReference()
                        .child("ofertas")
                        .child("granada")
                        .child(loc).orderByChild("valoracion");

                f = true;

            }

        }

        if (!f) {
            System.out.println("Filtro ciudad: " + loc);
            query = FirebaseDatabase.getInstance().getReference()
                    .child("ofertas")
                    .child("granada")
                    .child(loc);
        }

        options = new FirebaseRecyclerOptions.Builder<FBruta>()
                .setQuery(query, FBruta.class)
                .build();

        return options;
    }

    /**
     * Guarda los filtros seleccionados en la caché.
     *
     * @param st Elemento a guardar
     */

    public void setStringCache(String key, String st) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, st);
        editor.apply();

    }

    /**
     * Carga los filtros aplicados de la caché.
     *
     */

    public String getStringCache(String key) {
        return pref.getString(key, null);
    }

}
