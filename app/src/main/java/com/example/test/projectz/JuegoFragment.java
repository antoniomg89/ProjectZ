package com.example.test.projectz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;

/**
 * Fragmento que contiene la información relacionada a los juegos.
 */

public class JuegoFragment extends Fragment {

    private RecyclerView rvJuego;
    private FirebaseRecyclerAdapter adapter;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference datos;
    private ImageButton imageButtonFragmentJuegoTrofeo, imageButtonFragmentJuegoDetalles;
    private Button buttonFragmentJuegoEntrar;
    private NumberProgressBar numberprogressBarFragmentJuego;
    private ConstraintLayout constraintLayout;
    private ImageView imageViewFragmentJuegoNivel2, imageViewFragmentJuegoNivel3,imageViewFragmentJuegoNivel4, imageViewFragmentJuegoNivel5;
    private DatabaseReference premios;
    private AnimationDrawable animationDrawable;
    private int usuarioParticipaciones;
    private String juegoId;
    private FuncionesGlobales fg;
    private final FuncionesGlobales se = new FuncionesGlobales();

    /**
     * Crea el fragmento e inicializa sus componentes.
     *
     * @return Vista.
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View juegoView = inflater.inflate(R.layout.fragment_juego,container,false);

        // Obtener el usuario actual
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        fg = new FuncionesGlobales();

        //layout_fragmento = (RelativeLayout) juegoView.findViewById(R.id.layout_fragmento_evento);

        datos = FirebaseDatabase.getInstance().getReference().child("usuariodatos").child(mFirebaseUser.getUid());

        // Comprobar las participaciones que tiene el usuario para permitir o no su participación en el juego.
        datos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuarioParticipaciones = Objects.requireNonNull(snapshot.getValue(FBdatosUsuario.class)).getParticipaciones();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rvJuego = juegoView.findViewById(R.id.recyclerViewFragmentJuego);
        rvJuego.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rvJuego.setHasFixedSize(true);
        //rvJuego.setBackgroundColor(getResources().getColor(R.color.gris_claro3));

        return juegoView;
    }

    /**
     * Inicia el adaptador del recyclerview de juegos.
     *
     */

    private void initAdapter() {
        adapter = new FirebaseRecyclerAdapter<FBjuego, JuegoViewHolder>(getOptions()) {

            @NonNull
            @Override
            public JuegoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.activity_reciclercardview_juego, viewGroup, false);

                return new JuegoViewHolder(v);
            }


            @Override
            protected void onBindViewHolder(@NonNull final JuegoViewHolder holder, int position, @NonNull final FBjuego model) {
                holder.bind(model);

                int juegoParticipacion = model.getParticipacion();
                int juegoNivel = model.getNivel();
                juegoId = model.getId();
                String entrar = "Entrar";
                String inscribirse = "Inscribirme";

                constraintLayout = holder.itemView.findViewById(R.id.layout_constraint_juego);

                imageButtonFragmentJuegoTrofeo = holder.itemView.findViewById(R.id.imageButtonFragmentJuegoTrofeo);
                imageButtonFragmentJuegoDetalles = holder.itemView.findViewById(R.id.imageButtonFragmentJuegoDetalles);

                buttonFragmentJuegoEntrar = holder.itemView.findViewById(R.id.buttonFragmentJuegoEntrar);
                numberprogressBarFragmentJuego = holder.itemView.findViewById(R.id.numberprogressBarFragmentJuego);

                Typeface fuente = ResourcesCompat.getFont(requireContext(),R.font.poppinssemibold);
                buttonFragmentJuegoEntrar.setTypeface(fuente);

                if(juegoNivel == 2) {
                    imageViewFragmentJuegoNivel2 = holder.itemView.findViewById(R.id.imageViewFragmentJuegoNivel2);
                    imageViewFragmentJuegoNivel2.setVisibility(View.VISIBLE);
                } else if(juegoNivel == 3) {
                    imageViewFragmentJuegoNivel3 = holder.itemView.findViewById(R.id.imageViewFragmentJuegoNivel3);
                    imageViewFragmentJuegoNivel3.setVisibility(View.VISIBLE);
                } else if(juegoNivel == 4) {
                    imageViewFragmentJuegoNivel4 = holder.itemView.findViewById(R.id.imageViewFragmentJuegoNivel4);
                    imageViewFragmentJuegoNivel4.setVisibility(View.VISIBLE);
                } else if(juegoNivel == 5) {
                    imageViewFragmentJuegoNivel5 = holder.itemView.findViewById(R.id.imageViewFragmentJuegoNivel5);
                    imageViewFragmentJuegoNivel5.setVisibility(View.VISIBLE);
                }

                datos.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        float usuarioParticipacion = Objects.requireNonNull(snapshot.getValue(FBdatosUsuario.class)).getParticipaciones();
                        int usuarioNivel = Objects.requireNonNull(snapshot.getValue(FBdatosUsuario.class)).getNivel();
                        int progreso = 0;

                        if (usuarioNivel-juegoNivel >= 0) {
                            progreso += 50;

                        }

                        if (usuarioParticipacion-juegoParticipacion >= 0) {
                            progreso +=50;
                        }

                        Timer timer = new Timer();
                        Activity activity = getActivity();

                        int finalProgreso1 = progreso;
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if(isAdded() && activity != null) {
                                    activity.runOnUiThread(() -> {
                                        if (numberprogressBarFragmentJuego.getProgress() < finalProgreso1) {
                                            numberprogressBarFragmentJuego.incrementProgressBy(1);
                                        } else {
                                            timer.cancel();
                                            timer.purge();
                                        }
                                    });

                                    //estadoprogressbar = true;
                                }
                            }
                        }, 1000, 10);

                        numberprogressBarFragmentJuego.setProgress(progreso);


                        DatabaseReference usuarioEventos = FirebaseDatabase.getInstance().getReference().child("usuarioeventos").child(mFirebaseUser.getUid());
                        //Comprobar si el usuario está inscrito en el juego
                        int finalProgreso = progreso;
                        usuarioEventos.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(juegoId)) {
                                    System.out.println();

                                    Timer timer = new Timer();
                                    Activity activity = getActivity();

                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            if(isAdded() && activity != null) {
                                                activity.runOnUiThread(() -> {
                                                    if (numberprogressBarFragmentJuego.getProgress() < 100) {
                                                        numberprogressBarFragmentJuego.incrementProgressBy(1);

                                                    } else {
                                                        timer.cancel();
                                                        timer.purge();
                                                        initAnimacion(constraintLayout);
                                                        numberprogressBarFragmentJuego.setProgressTextSize(60);
                                                        numberprogressBarFragmentJuego.setReachedBarColor(Color.parseColor("#3EEC95"));
                                                        numberprogressBarFragmentJuego.setProgressTextColor(Color.parseColor("#3EEC95"));
                                                        buttonFragmentJuegoEntrar.setText(entrar);
                                                        buttonFragmentJuegoEntrar.setVisibility(View.VISIBLE);
                                                    }
                                                });

                                            }
                                        }
                                    }, 0, 1);

                                } else {
                                    System.out.println("No existe el hijo " + numberprogressBarFragmentJuego.getProgress());

                                    if(numberprogressBarFragmentJuego.getProgress() == 100) {
                                        System.out.println("Mostrar el botón de inscribirse");
                                        numberprogressBarFragmentJuego.setProgressTextSize(60);
                                        numberprogressBarFragmentJuego.setReachedBarColor(Color.parseColor("#3EEC95"));
                                        numberprogressBarFragmentJuego.setProgressTextColor(Color.parseColor("#3EEC95"));
                                        buttonFragmentJuegoEntrar.setText(inscribirse);
                                        buttonFragmentJuegoEntrar.setVisibility(View.VISIBLE);
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                imageButtonFragmentJuegoDetalles.setOnClickListener(view -> {
                    String inicio = model.getFechac();
                    String fin = model.getFechaf();

                    fg.startViewAnimation(getActivity(),view,0);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    LayoutInflater li = LayoutInflater.from(getContext());
                    final View viewDialog = li.inflate(R.layout.dialog_detalles_juego, null);
                    builder.setView(viewDialog);

                    AlertDialog dialog = builder.create();

                    TextView textviewInicioJuego = viewDialog.findViewById(R.id.textviewDetallesInicio);
                    TextView textviewFinJuego = viewDialog.findViewById(R.id.textviewDetallesFin);

                    textviewInicioJuego.setText(inicio);
                    textviewFinJuego.setText(fin);

                    dialog.show();

                });

                imageButtonFragmentJuegoTrofeo.setOnClickListener(view -> {
                    String ciu = model.getCiudad();
                    ciu = se.getCiuloc(ciu);

                    fg.startViewAnimation(getActivity(),view,0);
                    premios = FirebaseDatabase.getInstance().getReference().child("eventosPremios").child(ciu).child(model.getId());

                    premios.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String premio1 = Objects.requireNonNull(snapshot.getValue(FBjuegoPremios.class)).getPremio1() + " y " + Objects.requireNonNull(snapshot.getValue(FBjuegoPremios.class)).getExperiencia1() + " puntos de experiencia";
                            String premio2 = Objects.requireNonNull(snapshot.getValue(FBjuegoPremios.class)).getPremio2() + " y " + Objects.requireNonNull(snapshot.getValue(FBjuegoPremios.class)).getExperiencia2() + " puntos de experiencia";
                            String premio3 = Objects.requireNonNull(snapshot.getValue(FBjuegoPremios.class)).getPremio3() + " y " + Objects.requireNonNull(snapshot.getValue(FBjuegoPremios.class)).getExperiencia3() + " puntos de experiencia";
                            String premio4 = Objects.requireNonNull(snapshot.getValue(FBjuegoPremios.class)).getPremio4();

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            LayoutInflater li = LayoutInflater.from(getContext());
                            final View viewDialog = li.inflate(R.layout.dialog_premios_juego, null);
                            builder.setView(viewDialog);

                            AlertDialog dialog = builder.create();

                            TextView textviewPremio1 = viewDialog.findViewById(R.id.textviewPremios1);
                            TextView textviewPremio2 = viewDialog.findViewById(R.id.textviewPremios2);
                            TextView textviewPremio3 = viewDialog.findViewById(R.id.textviewPremios3);
                            TextView textviewPremio4 = viewDialog.findViewById(R.id.textviewPremios4);

                            textviewPremio1.setText(premio1);
                            textviewPremio2.setText(premio2);
                            textviewPremio3.setText(premio3);
                            textviewPremio4.setText(premio4);

                            dialog.show();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                });

                buttonFragmentJuegoEntrar.setOnClickListener(view -> {
                    boolean activarButtonInscribirse = false;
                    String participacion1 = "";

                    if (usuarioParticipaciones >= juegoParticipacion) {
                        participacion1 = "Tienes " + usuarioParticipaciones + " participaciones. Pulsa en 'Inscribirme' en caso de que quieras participar";
                        activarButtonInscribirse = true;
                    }

                    if (buttonFragmentJuegoEntrar.getText() == entrar) {
                        Intent intent = new Intent(getContext(), JuegoEspecial.class);

                        intent.putExtra("cJ",model.getFechac());
                        intent.putExtra("fJ",model.getFechaf());
                        intent.putExtra("estJ", model.getEstado());
                        intent.putExtra("idJ", model.getId());
                        intent.putExtra("explJ", model.getExplicacion());
                        intent.putExtra("empJ", model.getEmpresa());
                        intent.putExtra("ciuJ", model.getCiudad());

                        startActivity(intent);

                    } else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        LayoutInflater li = LayoutInflater.from(getContext());
                        final View viewDialog = li.inflate(R.layout.dialog_juego_participar, null);
                        builder.setView(viewDialog);

                        AlertDialog dialog = builder.create();

                        TextView textViewJuegoParticiparCantidad = viewDialog.findViewById(R.id.textViewJuegoParticiparCantidad);
                        TextView textViewJuegoParticiparCantidad2 = viewDialog.findViewById(R.id.textViewJuegoParticiparCantidad2);

                        Button buttonJuegoParticipar = viewDialog.findViewById(R.id.buttonJuegoParticipar);

                        Typeface fuente1 = ResourcesCompat.getFont(requireContext(),R.font.poppinsregular);
                        buttonJuegoParticipar.setTypeface(fuente1);

                        textViewJuegoParticiparCantidad.setText(String.valueOf(usuarioParticipaciones));
                        textViewJuegoParticiparCantidad2.setText(String.valueOf(juegoParticipacion));

                        if (activarButtonInscribirse) {
                            buttonJuegoParticipar.setEnabled(true);
                        } else {
                            buttonJuegoParticipar.setVisibility(View.GONE);
                        }

                        dialog.show();

                        buttonJuegoParticipar.setOnClickListener(view1 -> {
                            //Se crea el nodo de usuarioeventos con la fecha de inscripión

                            DatabaseReference usuarioEvento = FirebaseDatabase.getInstance().getReference().child("usuarioeventos").child(mFirebaseUser.getUid()).child(juegoId);
                            getURLFecha(usuarioEvento, juegoParticipacion, model);

                        });
                    }
                });
            }
        };

        rvJuego.setAdapter(adapter);
        adapter.startListening();

    }

    /**
     * Actualiza la lista con filtro por defecto (Granada).
     *
     */

    public FirebaseRecyclerOptions<FBjuego> getOptions() {
        FirebaseRecyclerOptions<FBjuego> options;
        Query query;
        query = FirebaseDatabase.getInstance().getReference()
                .child("eventos")
                .child("granada");


        options = new FirebaseRecyclerOptions.Builder<FBjuego>()
                        .setQuery(query, FBjuego.class)
                        .build();

        return options;
    }

    /**
     * Cuando se inicia el fragmento, se inicia la escucha del adaptador.
     */


    @Override
    public void onStart() {
        super.onStart();
        DatabaseReference eventosRef = FirebaseDatabase.getInstance().getReference().child("eventos").child("granada");
        //eventosRef.keepSynced(true);

        initAdapter();
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
     * Obtiene la fecha actual cuando se va a participar en el juego.
     *
     * @param eventosU Referencia al progreso actual del usuario en el juego.
     * @param participaciones Participaciones de las que dispone el usuario.
     */

    public void getURLFecha(DatabaseReference eventosU, int participaciones, FBjuego m) {
        String fcr = "https://przfe.herokuapp.com/getFechaCompleta";

        // Petición de la fecha real.
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, fcr,
                response -> {
                    System.out.println("Fecha obtenida: " + response);
                    String [] fecha = response.split(";");

                    // Crear el nodo con la fecha de inscripción
                    FBjuegoUsuarioInfo eventos = new FBjuegoUsuarioInfo("null",fecha[0],"null",0,0);
                    eventosU.setValue(eventos);

                    //Restar las participaciones usadas al usuario
                    DatabaseReference usuarioDatos = FirebaseDatabase.getInstance().getReference().child("usuariodatos").child(mFirebaseUser.getUid());

                    usuarioDatos.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int exp = Objects.requireNonNull(snapshot.getValue(FBdatosUsuario.class)).getExperiencia();
                            int signivel = Objects.requireNonNull(snapshot.getValue(FBdatosUsuario.class)).getExpsignivel();
                            int nivel = Objects.requireNonNull(snapshot.getValue(FBdatosUsuario.class)).getNivel();
                            int participacion = Objects.requireNonNull(snapshot.getValue(FBdatosUsuario.class)).getParticipaciones();
                            int nuevo = Objects.requireNonNull(snapshot.getValue(FBdatosUsuario.class)).getNuevo();

                            int participacion_act = participacion-participaciones;

                            FBdatosUsuario datosU = new FBdatosUsuario(nivel,exp,signivel,participacion_act,nuevo);
                            usuarioDatos.setValue(datosU);

                            Intent intent = new Intent(getContext(), JuegoEspecial.class);
                            intent.putExtra("cJ",m.getFechac());
                            intent.putExtra("fJ",m.getFechaf());
                            intent.putExtra("estJ", m.getEstado());
                            intent.putExtra("idJ", m.getId());
                            intent.putExtra("explJ", m.getExplicacion());
                            intent.putExtra("empJ", m.getEmpresa());
                            intent.putExtra("ciuJ", m.getCiudad());

                            //Crear los nodos con los estados de los botones de mapa y validación.
                            DatabaseReference estadoMapa = FirebaseDatabase.getInstance().getReference().child("usuarioeventosmapa").child(mFirebaseUser.getUid()).child(m.getId());
                            DatabaseReference estadoValidacion = FirebaseDatabase.getInstance().getReference().child("usuarioeventosvalidacion").child(mFirebaseUser.getUid()).child(m.getId());

                            FBjuegoEstadoMapa jem = new FBjuegoEstadoMapa("false");
                            FBjuegoEstadoValidacion jev = new FBjuegoEstadoValidacion(("false"));
                            estadoMapa.setValue(jem);
                            estadoValidacion.setValue(jev);

                            startActivity(intent);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }, error -> {
                    Toast toast = Toast.makeText(getContext(), "Error en la conexión.", Toast.LENGTH_LONG);
                    toast.show();
                });

        queue.add(stringRequest);
    }

    /**
     * Inicia la animación del fragmento.
     *
     */

    public void initAnimacion(ConstraintLayout cl) {
        if(isAdded()) {
            cl.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.lista_gradientes_fragmento));
            animationDrawable = (AnimationDrawable) cl.getBackground();
            animationDrawable.setEnterFadeDuration(2000);
            animationDrawable.setExitFadeDuration(3000);
            animationDrawable.start();
        }
    }

    /**
     * Detiene la animación del fragmento.
     *
     */

    public void stopAnimacion() {
        if (animationDrawable != null) {
            animationDrawable.stop();
        }

    }

}
