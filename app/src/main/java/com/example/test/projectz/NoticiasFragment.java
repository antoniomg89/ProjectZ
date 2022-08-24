package com.example.test.projectz;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

/**
 * Fragmento de las noticias relacionadas con la aplicación (en construcción).
 */
public class NoticiasFragment extends Fragment {
    private FirebaseUser mFirebaseUser;
    private RecyclerView rvRecompensas;
    private FirebaseRecyclerAdapter adapter;
    private FuncionesGlobales fg;

    /**
     * Constructor por defecto.
     */

    public NoticiasFragment() {
        // Required empty public constructor
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_noticias, container, false);
        System.out.println("Parent: " + view.getParent());
        rvRecompensas = view.findViewById(R.id.recyclerViewFragmentRecompensas);
        rvRecompensas.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rvRecompensas.setHasFixedSize(true);
        rvRecompensas.setBackgroundColor(Color.parseColor("#EDEDED"));

        ImageButton imageButtonRecompensasPapelera = view.findViewById(R.id.imageButtonRecompensasPapelera);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fg = new FuncionesGlobales();

        imageButtonRecompensasPapelera.setOnClickListener(view1 -> {
            fg.startViewAnimation(getActivity(), view1,0);
            //Borrar recompensas reclamadas.

            DatabaseReference recompensasUsuarios = FirebaseDatabase.getInstance().getReference().child("usuariorecompensas").child(mFirebaseUser.getUid());

            recompensasUsuarios.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            String id = Objects.requireNonNull(dataSnapshot.getValue(FBusuarioRecompensas.class)).getId();
                            boolean reclamada = Objects.requireNonNull(dataSnapshot.getValue(FBusuarioRecompensas.class)).getReclamada();

                            if (reclamada) {
                                recompensasUsuarios.child(id).removeValue();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

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


    private void initAdapter() {
        adapter = new FirebaseRecyclerAdapter<FBusuarioRecompensas, RecompensasViewHolder>(getOptions()) {

            @NonNull
            @Override
            public RecompensasViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.fragment_reciclercardview_recompensas, viewGroup, false);

                return new RecompensasViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull final RecompensasViewHolder holder, int position, @NonNull final FBusuarioRecompensas model) {
                holder.bind(model);

                holder.itemView.setOnClickListener(view -> {
                    if(!model.getReclamada()) {
                        ImageView imageViewActividadRecompensaEstadoFalse = holder.itemView.findViewById(R.id.imageViewActividadRecompensaEstadoFalse);
                        ImageView imageViewActividadRecompensaEstadoTrue = holder.itemView.findViewById(R.id.imageViewActividadRecompensaEstadoTrue);
                        fg.startViewAnimation(getActivity(),imageViewActividadRecompensaEstadoFalse,0);
                        int experiencia = 0;
                        int participaciones = 0;

                        if(model.getExperiencia() > 0) {
                            experiencia = model.getExperiencia();
                        }

                        if(model.getParticipaciones() > 0) {
                            participaciones = model.getParticipaciones();
                        }

                        FBusuarioRecompensas fbusuarioRecompensas = new FBusuarioRecompensas(model.getId(),model.getNombre(),
                                participaciones,experiencia,true);

                        DatabaseReference databaseReferenceusuariorecompensas = FirebaseDatabase.getInstance().getReference().child("usuariorecompensas").child(mFirebaseUser.getUid()).child(model.getId());

                        Handler handler = new Handler();

                        int finalExperiencia = experiencia;
                        int finalParticipaciones = participaciones;
                        handler.postDelayed(() -> {
                            fg.startViewAnimation(getActivity(),imageViewActividadRecompensaEstadoFalse,4);

                            handler.postDelayed(() -> databaseReferenceusuariorecompensas.setValue(fbusuarioRecompensas).addOnCompleteListener(task -> {
                                fg.startViewAnimation(getActivity(),imageViewActividadRecompensaEstadoTrue,5);

                                DatabaseReference databaseReferenceusuariodatos = FirebaseDatabase.getInstance().getReference()
                                        .child("usuariodatos").child(mFirebaseUser.getUid());

                                databaseReferenceusuariodatos.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int usuarioexp = Objects.requireNonNull(snapshot.getValue(FBdatosUsuario.class)).getExperiencia();
                                        int usuarionivel = Objects.requireNonNull(snapshot.getValue(FBdatosUsuario.class)).getNivel();
                                        int usuarioexpsignivel = Objects.requireNonNull(snapshot.getValue(FBdatosUsuario.class)).getExpsignivel();
                                        int usuarioparticipaciones = Objects.requireNonNull(snapshot.getValue(FBdatosUsuario.class)).getParticipaciones();
                                        int usuarionuevo = Objects.requireNonNull(snapshot.getValue(FBdatosUsuario.class)).getNuevo();
                                        int exp = usuarioexp;
                                        int expsignivel = usuarioexpsignivel;

                                        if(finalExperiencia > 0) {
                                            exp = (usuarioexp + finalExperiencia)%250;
                                            expsignivel = (usuarioexpsignivel - finalExperiencia)%250;

                                            if(exp < usuarioexp) {
                                                usuarionivel += 1;
                                            }
                                        }

                                        if(finalParticipaciones > 0) {
                                            usuarioparticipaciones += finalParticipaciones;
                                        }

                                        FBdatosUsuario fbdatosUsuario = new FBdatosUsuario(usuarionivel,exp,expsignivel,usuarioparticipaciones,usuarionuevo);

                                        databaseReferenceusuariodatos.setValue(fbdatosUsuario).addOnCompleteListener(task1 -> {
                                            System.out.println("ok");
                                            boolean ctrlparticipaciones = false;

                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            LayoutInflater li = LayoutInflater.from(getActivity());
                                            final View view2 = li.inflate(R.layout.dialog_recompensa_reclamada, null);
                                            builder.setView(view2);

                                            AlertDialog dialog = builder.create();

                                            if(finalParticipaciones>0) {
                                                ImageView imageViewDialogRecompensaReclamada = view2.findViewById(R.id.imageViewDialogRecompensaReclamada);
                                                ImageView imageViewDialogRecompensaReclamadaMult = view2.findViewById(R.id.imageViewDialogRecompensaReclamadaMult);
                                                TextView textViewDialogRecompensaReclamadaParticipaciones = view2.findViewById(R.id.textViewDialogRecompensaReclamadaParticipaciones);

                                                imageViewDialogRecompensaReclamada.setVisibility(View.VISIBLE);
                                                imageViewDialogRecompensaReclamadaMult.setVisibility(View.VISIBLE);
                                                textViewDialogRecompensaReclamadaParticipaciones.setVisibility(View.VISIBLE);

                                                textViewDialogRecompensaReclamadaParticipaciones.setText(String.valueOf(finalParticipaciones));
                                                ctrlparticipaciones = true;

                                            }

                                            if(finalExperiencia>0) {
                                                TextView textViewDialogRecompensaReclamadaExp = view2.findViewById(R.id.textViewDialogRecompensaReclamadaExp);

                                                if(ctrlparticipaciones) {
                                                    ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) textViewDialogRecompensaReclamadaExp.getLayoutParams();
                                                    newLayoutParams.topMargin = 100;
                                                    newLayoutParams.leftMargin = 0;
                                                    newLayoutParams.rightMargin = 0;
                                                    textViewDialogRecompensaReclamadaExp.setLayoutParams(newLayoutParams);

                                                }

                                                textViewDialogRecompensaReclamadaExp.setVisibility(View.VISIBLE);
                                                String st = "Exp: " + finalExperiencia;

                                                textViewDialogRecompensaReclamadaExp.setText(st);
                                            }

                                            dialog.show();

                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });



                            }),500);

                        },700);

                    }

                });

            }

            @Override
            public void onDataChanged() {

            }

        };

        rvRecompensas.setAdapter(adapter);
        adapter.startListening();
    }

    public FirebaseRecyclerOptions<FBusuarioRecompensas> getOptions() {
        Query query = FirebaseDatabase.getInstance().getReference()
                .child("usuariorecompensas")
                .child(mFirebaseUser.getUid()).orderByChild("reclamada");

        return new FirebaseRecyclerOptions.Builder<FBusuarioRecompensas>()
                .setQuery(query, FBusuarioRecompensas.class)
                .build();
    }


}