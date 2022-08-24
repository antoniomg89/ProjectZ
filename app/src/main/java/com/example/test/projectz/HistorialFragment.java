package com.example.test.projectz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Fragmento del historial/actividad reciente del usuario.
 */
public class HistorialFragment extends Fragment {
    private final FirebaseUser mFirebaseUser;
    private View view;

    private ListView historialEmp;
    private ArrayAdapter<String> arrayAdapterEmp;
    //private TextView textViewID;
    //private EditText editTextID;
    //private Button buttonEnviar;

    // TODO: Rename and change types of parameters
   /* private String mParam1;
    private String mParam2;*/

    public HistorialFragment() {
        // Required empty public constructor
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    /*public static HistorialFragment newInstance(String param1, String param2) {
        HistorialFragment fragment = new HistorialFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }*/


    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_historial, container, false);
        init();

        return view;

    }

    /**
     * Carga los datos del historial.
     *
     */

    public void init() {
        ArrayList<String> arrayListEmp = new ArrayList<>();
        DatabaseReference fbHistorial = FirebaseDatabase.getInstance().getReference().child("usuariohistorial").child(mFirebaseUser.getUid());
        historialEmp = view.findViewById(R.id.listviewHistorialEmp);

        fbHistorial.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot hist: snapshot.getChildren()) {
                    int veces_completada = Objects.requireNonNull(hist.getValue(FBhistorialRutaUsuario.class)).getVecesCompletada();
                    String fechacomp3 = Objects.requireNonNull(hist.getValue(FBhistorialRutaUsuario.class)).getFechaCompletada3();
                    String nombre = Objects.requireNonNull(hist.getValue(FBhistorialRutaUsuario.class)).getNombre();

                    if (veces_completada > 0) {
                        String [] fechacomp = fechacomp3.split(" a las ");
                        arrayListEmp.add(nombre + " " + fechacomp[0]);
                    } else {
                        arrayListEmp.add("Aun no has completado ninguna ruta");
                    }

                    if (getContext()!= null) {
                        arrayAdapterEmp = new ArrayAdapter<>(getContext(), R.layout.historial_ruta_nombre, arrayListEmp);
                        historialEmp.setAdapter(arrayAdapterEmp);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

}