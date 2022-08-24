package com.example.test.projectz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Clase que gestiona la información de cada ruta.
 */

public class Ruta extends Activity {
    private FirebaseUser mFirebaseUser;
    private DatabaseReference favoritosRef;
    private NumberProgressBar progressBar;
    private RatingBar ratingBarRutaActivity, ratingBarValoracionUsuario;
    private ImageView imageViewRvRuta, imageViewRutaLocal1, imageViewRutaLocal2, imageViewRutaLocal3, imageViewDialogQR,
            imageViewRutaFavoritos, imageViewProducto;
    private CircleImageView imageButtonLocal1Tick, imageButtonLocal1Tick2, imageButtonLocal1Tick3;
    private TextView textViewRutaNombre, textViewLocal1Nombre, textViewLocal2Nombre, textViewLocal3Nombre, textViewValoracionRuta, textViewRankingRuta,
            textviewdialogrutaempresa, textviewdialoginfodescripcion, textviewdialoginfoprecio;
    private ImageButton imageButtonEnviarValoracion, imageButtonTicketRuta, imageButtonSeleccionRanking,imageButtonRutaRepeat;
    private Button buttonDialogEnviar;
    private int estado_ruta, participacion_ruta, competitiva_ruta, experiencia_ruta, medallas = 0, controlcompletada;
    private float valoracion_ruta, precio_ruta, precio1_ruta, precio2_ruta, precio3_ruta, valoracionUsuario;
    private String id_ruta, empresa_ruta, empresa2_ruta, empresa3_ruta, id_empresa1, id_empresa2, id_empresa3, nombre_ruta, fotoact_ruta,
            fotoact2_ruta, fotoact3_ruta, fotorv_ruta, localidad_ruta, descripcion_ruta, descripcion2_ruta, descripcion3_ruta, comienzo_ruta,
            fotoactpr_ruta, fotoactpr2_ruta, fotoactpr3_ruta, final_ruta, coordenadas_ruta, coordenadas2_ruta, coordenadas3_ruta,
            swboundbox = null, neboundbox = null;

    AlertDialog qrdialog;
    private FuncionesGlobales fg;
    private boolean added_badge = false, estado_favorito, semcompletada = false;
    private final Map<Integer,String> mapranking = new HashMap<>();

    private SharedPreferences pref;

    /**
     * Crea la activity e inicializa sus componentes.
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta);
        pref = this.getSharedPreferences("preferencias", 0);
        fg = new FuncionesGlobales();

        // Obtiene el usuario conectado.
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // Obtener la referencia de firebase storage.
        //mStorageRef = FirebaseStorage.getInstance().getReference();

        progressBar = findViewById(R.id.numberprogressBarRuta);
        ratingBarRutaActivity = findViewById(R.id.ratingBarRuta);
        ratingBarValoracionUsuario = findViewById(R.id.ratingBarValoracionUsuario);
        imageViewRvRuta = findViewById(R.id.imageViewRuta);
        textViewRutaNombre = findViewById(R.id.textViewRutaNombre);
        textViewLocal1Nombre = findViewById(R.id.textViewLocal1Nombre);
        textViewLocal2Nombre = findViewById(R.id.textViewLocal2Nombre);
        textViewLocal3Nombre = findViewById(R.id.textViewLocal3Nombre);
        imageViewRutaLocal1 = findViewById(R.id.imageViewRutaLocal1);
        imageViewRutaLocal2 = findViewById(R.id.imageViewRutaLocal2);
        imageViewRutaLocal3 = findViewById(R.id.imageViewRutaLocal3);
        ImageButton imageButtonRutaLocalizacion1 = findViewById(R.id.imageButtonRutaLocalizacion1);
        ImageButton imageButtonRutaLocalizacion2 = findViewById(R.id.imageButtonRutaLocalizacion2);
        ImageButton imageButtonRutaLocalizacion3 = findViewById(R.id.imageButtonRutaLocalizacion3);
        ImageButton imageButtonRutaInfo1 = findViewById(R.id.imageButtonRutaInfo1);
        ImageButton imageButtonRutaInfo2 = findViewById(R.id.imageButtonRutaInfo2);
        ImageButton imageButtonRutaInfo3 = findViewById(R.id.imageButtonRutaInfo3);
        ImageButton imageButtonRutaQR = findViewById(R.id.imageButtonRutaQR);
        ImageButton imageButtonRutaCalendar = findViewById(R.id.imageButtonRutaCalendar);
        imageViewRutaFavoritos = findViewById(R.id.imageViewRutaFavoritos);
        imageButtonLocal1Tick = findViewById(R.id.imageButtonLocal1Tick);
        imageButtonLocal1Tick2 = findViewById(R.id.imageButtonLocal1Tick2);
        imageButtonLocal1Tick3 = findViewById(R.id.imageButtonLocal1Tick3);
        imageButtonTicketRuta = findViewById(R.id.imageButtonTicketRuta);
        imageButtonRutaRepeat = findViewById(R.id.imageButtonRutaRepeat);
        imageButtonEnviarValoracion = findViewById(R.id.imageButtonEnviarValoracion);
        imageButtonEnviarValoracion.setEnabled(false);
        textViewValoracionRuta = findViewById(R.id.textViewValoracionRuta);
        textViewRankingRuta = findViewById(R.id.textViewRankingRuta);
        imageButtonSeleccionRanking = findViewById(R.id.imageButtonSeleccionRanking);

        Bundle bundle_ruta = getIntent().getExtras();
        descripcion_ruta = bundle_ruta.getString("dO");
        descripcion2_ruta = bundle_ruta.getString("d2O");
        descripcion3_ruta = bundle_ruta.getString("d3O");
        comienzo_ruta = bundle_ruta.getString("cO");
        final_ruta = bundle_ruta.getString("fO");
        localidad_ruta = bundle_ruta.getString("loc");
        coordenadas_ruta = bundle_ruta.getString("crd");
        coordenadas2_ruta = bundle_ruta.getString("crd2");
        coordenadas3_ruta = bundle_ruta.getString("crd3");
        estado_ruta = bundle_ruta.getInt("est");
        empresa_ruta = bundle_ruta.getString("emp");
        empresa2_ruta = bundle_ruta.getString("emp2");
        empresa3_ruta = bundle_ruta.getString("emp3");
        id_empresa1 = bundle_ruta.getString("ide1");
        id_empresa2 = bundle_ruta.getString("ide2");
        id_empresa3 = bundle_ruta.getString("ide3");
        id_ruta = bundle_ruta.getString("ide");
        nombre_ruta = bundle_ruta.getString("nom");
        experiencia_ruta = bundle_ruta.getInt("exp");
        participacion_ruta = bundle_ruta.getInt("pr");
        valoracion_ruta = bundle_ruta.getFloat("vl");
        fotoact_ruta = bundle_ruta.getString("foa");
        fotoact2_ruta = bundle_ruta.getString("foa2");
        fotoact3_ruta = bundle_ruta.getString("foa3");
        fotorv_ruta = bundle_ruta.getString("forv");
        competitiva_ruta = bundle_ruta.getInt("pat");
        precio_ruta = bundle_ruta.getFloat("prec");
        precio1_ruta = bundle_ruta.getFloat("prec1");
        precio2_ruta = bundle_ruta.getFloat("prec2");
        precio3_ruta = bundle_ruta.getFloat("prec3");
        fotoactpr_ruta = bundle_ruta.getString("foapr");
        fotoactpr2_ruta = bundle_ruta.getString("foapr2");
        fotoactpr3_ruta = bundle_ruta.getString("foapr3");

        if (!final_ruta.equals("Na")) {
            imageButtonRutaCalendar.setVisibility(View.VISIBLE);
            imageButtonRutaCalendar.setEnabled(true);
        }

        favoritosRef = FirebaseDatabase.getInstance().getReference().child("favoritos").child("usuarios").child(mFirebaseUser.getUid()).child(id_ruta);
        DatabaseReference o = FirebaseDatabase.getInstance().getReference().child("ofertas").child("granada").child(fg.getCiuloc(localidad_ruta)).child(id_ruta).getRef();
        DatabaseReference usuariohistorial = FirebaseDatabase.getInstance().getReference().child("usuariohistorial").child(mFirebaseUser.getUid()).child(id_ruta);
        DatabaseReference valoracionUsuarioRuta = FirebaseDatabase.getInstance().getReference().child("ofertasValoracion").child("granada").child(id_ruta).child(mFirebaseUser.getUid());
        DatabaseReference rankingUsuarioRuta = FirebaseDatabase.getInstance().getReference().child("rutaRanking").child("granada").child(id_ruta).child(mFirebaseUser.getUid());
        DatabaseReference rankingRuta = FirebaseDatabase.getInstance().getReference().child("rutaRanking").child("granada").child(id_ruta);
        DatabaseReference boundbox = FirebaseDatabase.getInstance().getReference().child("localidadesBoundBox").child("granada").child(fg.getCiuloc(localidad_ruta));

        o.keepSynced(true);
        getEstadoFavorito();

        o.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    nombre_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getNombre();
                    descripcion_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getDescripcion();
                    descripcion2_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getDescripcion2();
                    descripcion3_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getDescripcion3();
                    comienzo_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getFechaC();
                    final_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getFechaF();
                    localidad_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getLocalidad();
                    coordenadas_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getCoordenadas();
                    coordenadas2_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getCoordenadas2();
                    coordenadas3_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getCoordenadas3();
                    estado_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getEstado();
                    empresa_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getEmpresa();
                    empresa2_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getEmpresa2();
                    empresa3_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getEmpresa3();
                    id_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getId();
                    id_empresa1 = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getIdemp1();
                    id_empresa2 = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getIdemp2();
                    id_empresa3 = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getIdemp3();
                    experiencia_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getExperiencia();
                    valoracion_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getValoracion();
                    fotoact_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getFotoact();
                    fotoact2_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getFotoact2();
                    fotoact3_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getFotoact3();
                    fotorv_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getFotorv();
                    competitiva_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getCompetitiva();
                    precio_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getPrecio();
                    precio1_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getPrecio1();
                    precio2_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getPrecio2();
                    precio3_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getPrecio3();
                    fotoactpr_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getFotoactpr();
                    fotoactpr2_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getFotoactpr2();
                    fotoactpr3_ruta = Objects.requireNonNull(snapshot.getValue(FBruta.class)).getFotoactpr3();

                    iniciarInterfazRuta(usuariohistorial,valoracionUsuarioRuta,rankingUsuarioRuta,rankingRuta);

                } else {
                    Intent intent = new Intent(Ruta.this, MenusActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imageButtonRutaLocalizacion1.setOnClickListener(v -> {
            fg.startViewAnimation(Ruta.this,v,0);

            if(swboundbox == null) {
                boundbox.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        swboundbox = Objects.requireNonNull(snapshot.getValue(FBlocalidadesBoundBox.class)).getSw();
                        neboundbox = Objects.requireNonNull(snapshot.getValue(FBlocalidadesBoundBox.class)).getNe();

                        Intent intent = new Intent(Ruta.this, MapsRutaActivity.class);
                        intent.putExtra("crd", coordenadas_ruta);
                        intent.putExtra("emp", empresa_ruta);
                        intent.putExtra("ne", neboundbox);
                        intent.putExtra("sw", swboundbox);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                Intent intent = new Intent(Ruta.this, MapsRutaActivity.class);
                intent.putExtra("crd", coordenadas_ruta);
                intent.putExtra("emp", empresa_ruta);
                intent.putExtra("ne", neboundbox);
                intent.putExtra("sw", swboundbox);
                startActivity(intent);
            }


        });

        imageButtonRutaLocalizacion2.setOnClickListener(v -> {
            fg.startViewAnimation(Ruta.this,v,0);

            if(swboundbox == null) {
                boundbox.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        swboundbox = Objects.requireNonNull(snapshot.getValue(FBlocalidadesBoundBox.class)).getSw();
                        neboundbox = Objects.requireNonNull(snapshot.getValue(FBlocalidadesBoundBox.class)).getNe();

                        Intent intent = new Intent(Ruta.this, MapsRutaActivity.class);
                        intent.putExtra("crd", coordenadas2_ruta);
                        intent.putExtra("emp", empresa2_ruta);
                        intent.putExtra("ne", neboundbox);
                        intent.putExtra("sw", swboundbox);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                Intent intent = new Intent(Ruta.this, MapsRutaActivity.class);
                intent.putExtra("crd", coordenadas2_ruta);
                intent.putExtra("emp", empresa2_ruta);
                intent.putExtra("ne", neboundbox);
                intent.putExtra("sw", swboundbox);
                startActivity(intent);
            }

        });

        imageButtonRutaLocalizacion3.setOnClickListener(v -> {
            fg.startViewAnimation(Ruta.this,v,0);

            if(swboundbox == null) {
                boundbox.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        swboundbox = Objects.requireNonNull(snapshot.getValue(FBlocalidadesBoundBox.class)).getSw();
                        neboundbox = Objects.requireNonNull(snapshot.getValue(FBlocalidadesBoundBox.class)).getNe();

                        Intent intent = new Intent(Ruta.this, MapsRutaActivity.class);
                        intent.putExtra("crd", coordenadas3_ruta);
                        intent.putExtra("emp", empresa3_ruta);
                        intent.putExtra("ne", neboundbox);
                        intent.putExtra("sw", swboundbox);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                Intent intent = new Intent(Ruta.this, MapsRutaActivity.class);
                intent.putExtra("crd", coordenadas3_ruta);
                intent.putExtra("emp", empresa3_ruta);
                intent.putExtra("ne", neboundbox);
                intent.putExtra("sw", swboundbox);
                startActivity(intent);
            }

        });

        imageButtonRutaInfo1.setOnClickListener(view -> {
            fg.startViewAnimation(Ruta.this,view,0);
            AlertDialog.Builder builder = new AlertDialog.Builder(Ruta.this);
            LayoutInflater li = LayoutInflater.from(Ruta.this);
            final View view2 = li.inflate(R.layout.dialog_info_producto_ruta, null);
            builder.setView(view2);

            AlertDialog dialog = builder.create();

            textviewdialogrutaempresa = view2.findViewById(R.id.textviewdialogrutaempresa);
            textviewdialoginfodescripcion = view2.findViewById(R.id.textviewdialoginfodescripcion);
            textviewdialoginfoprecio = view2.findViewById(R.id.textviewdialoginfoprecio);
            imageViewProducto = view2.findViewById(R.id.imageViewProducto);

            Picasso.get()
                    .load(fotoactpr_ruta)
                    .fit()
                    .into(imageViewProducto);

            textviewdialogrutaempresa.setText(empresa_ruta);
            textviewdialoginfodescripcion.setText(descripcion_ruta);
            String st = precio1_ruta + getString(R.string.euro);
            textviewdialoginfoprecio.setText(st);

            dialog.show();

        });

        imageButtonRutaInfo2.setOnClickListener(view -> {
            fg.startViewAnimation(Ruta.this,view,0);
            AlertDialog.Builder builder = new AlertDialog.Builder(Ruta.this);
            LayoutInflater li = LayoutInflater.from(Ruta.this);
            final View view2 = li.inflate(R.layout.dialog_info_producto_ruta, null);
            builder.setView(view2);

            AlertDialog dialog = builder.create();

            textviewdialogrutaempresa = view2.findViewById(R.id.textviewdialogrutaempresa);
            textviewdialoginfodescripcion = view2.findViewById(R.id.textviewdialoginfodescripcion);
            textviewdialoginfoprecio = view2.findViewById(R.id.textviewdialoginfoprecio);
            imageViewProducto = view2.findViewById(R.id.imageViewProducto);

            Picasso.get()
                    .load(fotoactpr2_ruta)
                    .fit()
                    .into(imageViewProducto);

            textviewdialogrutaempresa.setText(empresa2_ruta);
            textviewdialoginfodescripcion.setText(descripcion2_ruta);
            String st = precio2_ruta + getString(R.string.euro);
            textviewdialoginfoprecio.setText(st);

            dialog.show();

        });

        imageButtonRutaInfo3.setOnClickListener(view -> {
            fg.startViewAnimation(Ruta.this,view,0);
            AlertDialog.Builder builder = new AlertDialog.Builder(Ruta.this);
            LayoutInflater li = LayoutInflater.from(Ruta.this);
            final View view2 = li.inflate(R.layout.dialog_info_producto_ruta, null);
            builder.setView(view2);

            AlertDialog dialog = builder.create();

            textviewdialogrutaempresa = view2.findViewById(R.id.textviewdialogrutaempresa);
            textviewdialoginfodescripcion = view2.findViewById(R.id.textviewdialoginfodescripcion);
            textviewdialoginfoprecio = view2.findViewById(R.id.textviewdialoginfoprecio);
            imageViewProducto = view2.findViewById(R.id.imageViewProducto);

            Picasso.get()
                    .load(fotoactpr3_ruta)
                    .fit()
                    .into(imageViewProducto);

            textviewdialogrutaempresa.setText(empresa3_ruta);
            textviewdialoginfodescripcion.setText(descripcion3_ruta);
            String st = precio3_ruta + getString(R.string.euro);
            textviewdialoginfoprecio.setText(st);

            dialog.show();

        });

        imageButtonRutaQR.setOnClickListener(view -> {
            fg.startViewAnimation(Ruta.this,view,0);
            AlertDialog.Builder builder = new AlertDialog.Builder(Ruta.this);
            LayoutInflater li = LayoutInflater.from(Ruta.this);
            final View view2 = li.inflate(R.layout.dialog_qr, null);
            builder.setView(view2);

            qrdialog = builder.create();

            imageViewDialogQR = view2.findViewById(R.id.imageViewDialogQR);

            try {
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.encodeBitmap(id_ruta + ";" + fg.getCiuloc(localidad_ruta) + ";" + mFirebaseUser.getUid(), BarcodeFormat.QR_CODE, 300, 300);
                imageViewDialogQR.setImageBitmap(bitmap);

            } catch(Exception ignored) {

            }

            qrdialog.show();

        });

        imageViewRutaFavoritos.setOnClickListener(view -> {
            fg.startViewAnimation(Ruta.this,view,0);
            if (estado_favorito) {
                //Eliminar de favoritos
                estado_favorito = false;
                imageViewRutaFavoritos.setBackground(ContextCompat.getDrawable(Ruta.this, R.drawable.estrella_36v));
                setStringCache(id_ruta+"-favorito","no");

                FirebaseDatabase.getInstance().getReference().child("favoritos").child("usuarios").child(mFirebaseUser.getUid()).child(id_ruta).removeValue();

                if (added_badge) {
                    DatabaseReference referenceBadge = FirebaseDatabase.getInstance().getReference().child("usuariosbadges").child(mFirebaseUser.getUid()).child("favoritos");

                    referenceBadge.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                int num = Objects.requireNonNull(snapshot.getValue(FBbadgeFavoritos.class)).getTotal();
                                int nuevo = num-1;

                                FBbadgeFavoritos bf = new FBbadgeFavoritos(nuevo);
                                referenceBadge.setValue(bf);
                                added_badge = false;


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            } else {
                //Transferir a favoritos
                estado_favorito = true;
                imageViewRutaFavoritos.setBackground(ContextCompat.getDrawable(Ruta.this, R.drawable.estrella_36r));

                FBruta oferta = new FBruta(descripcion_ruta,descripcion2_ruta,descripcion3_ruta,nombre_ruta,
                        empresa_ruta,empresa2_ruta,empresa3_ruta,id_empresa1,id_empresa2,id_empresa3,comienzo_ruta,final_ruta,
                        localidad_ruta,id_ruta,estado_ruta,coordenadas_ruta,coordenadas2_ruta,coordenadas3_ruta,
                        experiencia_ruta,participacion_ruta, valoracion_ruta,fotoact_ruta,fotoact2_ruta,
                        fotoact3_ruta,fotorv_ruta,competitiva_ruta,precio_ruta,
                        precio1_ruta,precio2_ruta,precio3_ruta,fotoactpr_ruta,fotoactpr2_ruta,fotoactpr3_ruta);

                DatabaseReference destino = FirebaseDatabase.getInstance().getReference().child("favoritos").child("usuarios").child(mFirebaseUser.getUid()).child(id_ruta);
                transferirFavoritos(destino,oferta);

                setStringCache(id_ruta+"-favorito","si");

                Toast toast = Toast.makeText(Ruta.this, "Oferta añadida a favoritos", Toast.LENGTH_SHORT);
                toast.show();



            }

        });


        ratingBarValoracionUsuario.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            valoracionUsuario = v;

            imageButtonEnviarValoracion.setImageDrawable(ContextCompat.getDrawable(Ruta.this,R.drawable.iconenviargreen24));
            imageButtonEnviarValoracion.setEnabled(true);

        });

        imageButtonEnviarValoracion.setOnClickListener(view -> {
            fg.startViewAnimation(Ruta.this,view,0);
            AlertDialog.Builder builder = new AlertDialog.Builder(Ruta.this);
            LayoutInflater li = LayoutInflater.from(Ruta.this);
            final View view2 = li.inflate(R.layout.dialog_valoracion_ruta, null);
            builder.setView(view2);

            AlertDialog dialog = builder.create();

            buttonDialogEnviar = view2.findViewById(R.id.buttonDialogEnviar);

            buttonDialogEnviar.setOnClickListener(view1 -> {
                FBrutaValoracion vo = new FBrutaValoracion(valoracionUsuario);
                valoracionUsuarioRuta.setValue(vo).addOnCompleteListener(task -> usuariohistorial.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String historialFecha1 = Objects.requireNonNull(snapshot.getValue(FBhistorialRutaUsuario.class)).getFechaCompletada();
                        String historialFecha2 = Objects.requireNonNull(snapshot.getValue(FBhistorialRutaUsuario.class)).getFechaCompletada2();
                        String historialFecha3 = Objects.requireNonNull(snapshot.getValue(FBhistorialRutaUsuario.class)).getFechaCompletada3();
                        String historialEmpresa = Objects.requireNonNull(snapshot.getValue(FBhistorialRutaUsuario.class)).getEmpresa();
                        String historialEmpresa2 = Objects.requireNonNull(snapshot.getValue(FBhistorialRutaUsuario.class)).getEmpresa2();
                        String historialEmpresa3 = Objects.requireNonNull(snapshot.getValue(FBhistorialRutaUsuario.class)).getEmpresa3();
                        String historialLocalidad = Objects.requireNonNull(snapshot.getValue(FBhistorialRutaUsuario.class)).getLocalidad();
                        int historialNumCompletada = Objects.requireNonNull(snapshot.getValue(FBhistorialRutaUsuario.class)).getVecesCompletada();

                        FBhistorialRutaUsuario hru = new FBhistorialRutaUsuario(historialEmpresa,historialEmpresa2,historialEmpresa3,
                                historialFecha1,historialFecha2,historialFecha3,historialLocalidad,historialNumCompletada,true,nombre_ruta);

                        usuariohistorial.setValue(hru).addOnCompleteListener(task1 -> {
                            imageButtonEnviarValoracion.setClickable(false);
                            ratingBarValoracionUsuario.setClickable(false);
                            textViewValoracionRuta.setText(getString(R.string.ruta_valorada));
                            imageButtonEnviarValoracion.setVisibility(View.GONE);
                            dialog.dismiss();
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }));



            });

            dialog.show();

        });

        imageButtonSeleccionRanking.setOnClickListener(view -> {
            fg.startViewAnimation(Ruta.this,view,0);
            AlertDialog.Builder builder = new AlertDialog.Builder(Ruta.this);
            LayoutInflater li = LayoutInflater.from(Ruta.this);
            final View view2 = li.inflate(R.layout.dialog_ranking, null);
            builder.setView(view2);


            AlertDialog dialog = builder.create();

            LinearLayout linearLayoutDialogRankingP = view2.findViewById(R.id.linearLayoutDialogRankingP);
            TextView textViewDialogRankingP1 = view2.findViewById(R.id.textViewDialogRankingP1);
            TextView textViewDialogRankingP2 = view2.findViewById(R.id.textViewDialogRankingP2);
            TextView textViewDialogRankingP3 = view2.findViewById(R.id.textViewDialogRankingP3);

            TextView textViewDialogRankingMensaje1 = view2.findViewById(R.id.textViewDialogRankingMensaje1);
            TextView textViewDialogRankingEmp = view2.findViewById(R.id.textViewDialogRankingEmp);
            LinearLayout linearLayoutDialogRankingMedallas = view2.findViewById(R.id.linearLayoutDialogRankingMedallas);

            textViewDialogRankingEmp.setText(empresa_ruta);
            Button buttonDialogRankingEnviar = view2.findViewById(R.id.buttonDialogRankingEnviar);
            TextView textViewDialogRankingResultado = view2.findViewById(R.id.textViewDialogRankingResultado);
            ImageButton imageButtonDialogRankingPlata = view2.findViewById(R.id.imageButtonDialogRankingPlata);
            ImageButton imageButtonDialogRankingOro = view2.findViewById(R.id.imageButtonDialogRankingOro);
            ImageButton imageButtonDialogRankingBronce = view2.findViewById(R.id.imageButtonDialogRankingBronce);
            Typeface fuente = ResourcesCompat.getFont(view2.getContext(),R.font.poppinssemibold);
            buttonDialogRankingEnviar.setTypeface(fuente);

            rankingUsuarioRuta.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        String idemp1 = Objects.requireNonNull(snapshot.getValue(FBrutaRanking.class)).getIdemp1();
                        String idemp2 = Objects.requireNonNull(snapshot.getValue(FBrutaRanking.class)).getIdemp2();
                        String idemp3 = Objects.requireNonNull(snapshot.getValue(FBrutaRanking.class)).getIdemp3();


                        if(idemp1.equals(id_empresa1)) {
                            textViewDialogRankingP1.setText(empresa_ruta);
                        } else if(idemp1.equals(id_empresa2)) {
                            textViewDialogRankingP1.setText(empresa2_ruta);
                        } else if(idemp1.equals(id_empresa3)) {
                            textViewDialogRankingP1.setText(empresa3_ruta);
                        }

                        if(idemp2.equals(id_empresa1)) {
                            textViewDialogRankingP2.setText(empresa_ruta);
                        } else if(idemp2.equals(id_empresa2)) {
                            textViewDialogRankingP2.setText(empresa2_ruta);
                        } else if(idemp2.equals(id_empresa3)) {
                            textViewDialogRankingP2.setText(empresa3_ruta);
                        }

                        if(idemp3.equals(id_empresa1)) {
                            textViewDialogRankingP3.setText(empresa_ruta);
                        } else if(idemp3.equals(id_empresa2)) {
                            textViewDialogRankingP3.setText(empresa2_ruta);
                        } else if(idemp3.equals(id_empresa3)) {
                            textViewDialogRankingP3.setText(empresa3_ruta);
                        }

                        linearLayoutDialogRankingP.setVisibility(View.VISIBLE);

                    } else {
                        textViewDialogRankingMensaje1.setVisibility(View.VISIBLE);
                        textViewDialogRankingEmp.setVisibility(View.VISIBLE);
                        linearLayoutDialogRankingMedallas.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            imageButtonDialogRankingPlata.setOnClickListener(view12 -> {
                ++medallas;

                YoYo.with(Techniques.RubberBand)
                        .duration(700)
                        .playOn(view12);

                YoYo.with(Techniques.FadeOut)
                        .duration(700)
                        .playOn(view12);

                YoYo.with(Techniques.FadeOutRight)
                        .duration(500)
                        .playOn(textViewDialogRankingEmp);

                YoYo.with(Techniques.FadeInLeft)
                        .duration(500)
                        .playOn(textViewDialogRankingEmp);

                if(medallas == 1) {
                    textViewDialogRankingP2.setText(empresa_ruta);
                    mapranking.put(2,id_empresa1);
                    textViewDialogRankingEmp.setText(empresa2_ruta);
                } else if(medallas == 2) {
                    textViewDialogRankingP2.setText(empresa2_ruta);
                    mapranking.put(2,id_empresa2);
                    textViewDialogRankingEmp.setText(empresa3_ruta);
                } else if(medallas == 3) {
                    textViewDialogRankingP2.setText(empresa3_ruta);
                    mapranking.put(2,id_empresa3);

                    linearLayoutDialogRankingMedallas.setVisibility(View.GONE);
                    textViewDialogRankingMensaje1.setVisibility(View.GONE);
                    textViewDialogRankingEmp.setVisibility(View.GONE);

                    linearLayoutDialogRankingP.setVisibility(View.VISIBLE);
                    buttonDialogRankingEnviar.setVisibility(View.VISIBLE);

                    buttonDialogRankingEnviar.setOnClickListener(view121 -> {
                        FBrutaRanking fBrutaRanking = new FBrutaRanking(mapranking.get(1),mapranking.get(2),mapranking.get(3));
                        rankingUsuarioRuta.setValue(fBrutaRanking)
                                .addOnCompleteListener(task -> {
                                    buttonDialogRankingEnviar.setVisibility(View.GONE);
                                    textViewDialogRankingResultado.setVisibility(View.VISIBLE);
                                    iniciarInterfazRuta(usuariohistorial,valoracionUsuarioRuta,rankingUsuarioRuta,rankingRuta);

                                })
                                .addOnFailureListener(e -> System.out.println("Error"));
                    });
                }


            });

            imageButtonDialogRankingOro.setOnClickListener(view13 -> {
                ++medallas;

                YoYo.with(Techniques.RubberBand)
                        .duration(700)
                        .playOn(view13);

                YoYo.with(Techniques.FadeOut)
                        .duration(700)
                        .playOn(view13);

                YoYo.with(Techniques.FadeOut)
                        .duration(700)
                        .playOn(view13);

                YoYo.with(Techniques.FadeOutRight)
                        .duration(500)
                        .playOn(textViewDialogRankingEmp);

                YoYo.with(Techniques.FadeInLeft)
                        .duration(500)
                        .playOn(textViewDialogRankingEmp);

                if(medallas == 1) {
                    textViewDialogRankingP1.setText(empresa_ruta);
                    mapranking.put(1,id_empresa1);
                    textViewDialogRankingEmp.setText(empresa2_ruta);
                } else if(medallas == 2) {
                    textViewDialogRankingP1.setText(empresa2_ruta);
                    mapranking.put(1,id_empresa2);
                    textViewDialogRankingEmp.setText(empresa3_ruta);
                } else if(medallas == 3) {
                    textViewDialogRankingP1.setText(empresa3_ruta);
                    mapranking.put(1,id_empresa3);

                    linearLayoutDialogRankingMedallas.setVisibility(View.GONE);
                    textViewDialogRankingMensaje1.setVisibility(View.GONE);
                    textViewDialogRankingEmp.setVisibility(View.GONE);

                    linearLayoutDialogRankingP.setVisibility(View.VISIBLE);
                    buttonDialogRankingEnviar.setVisibility(View.VISIBLE);

                    buttonDialogRankingEnviar.setOnClickListener(view131 -> {
                        FBrutaRanking fBrutaRanking = new FBrutaRanking(mapranking.get(1),mapranking.get(2),mapranking.get(3));
                        rankingUsuarioRuta.setValue(fBrutaRanking)
                                .addOnCompleteListener(task -> {
                                    buttonDialogRankingEnviar.setVisibility(View.GONE);
                                    textViewDialogRankingResultado.setVisibility(View.VISIBLE);
                                    iniciarInterfazRuta(usuariohistorial,valoracionUsuarioRuta,rankingUsuarioRuta,rankingRuta);
                                })
                                .addOnFailureListener(e -> System.out.println("Error"));
                    });
                }
            });

            imageButtonDialogRankingBronce.setOnClickListener(view14 -> {
                ++medallas;

                YoYo.with(Techniques.RubberBand)
                        .duration(700)
                        .playOn(view14);

                YoYo.with(Techniques.FadeOut)
                        .duration(700)
                        .playOn(view14);

                YoYo.with(Techniques.FadeOut)
                        .duration(700)
                        .playOn(view14);

                YoYo.with(Techniques.FadeOutRight)
                        .duration(500)
                        .playOn(textViewDialogRankingEmp);

                YoYo.with(Techniques.FadeInLeft)
                        .duration(500)
                        .playOn(textViewDialogRankingEmp);

                if(medallas == 1) {
                    textViewDialogRankingP3.setText(empresa_ruta);
                    mapranking.put(3,id_empresa1);
                    textViewDialogRankingEmp.setText(empresa2_ruta);
                } else if(medallas == 2) {
                    textViewDialogRankingP3.setText(empresa2_ruta);
                    mapranking.put(3,id_empresa2);
                    textViewDialogRankingEmp.setText(empresa3_ruta);
                } else if(medallas == 3) {
                    textViewDialogRankingP3.setText(empresa3_ruta);
                    mapranking.put(3,id_empresa3);

                    linearLayoutDialogRankingMedallas.setVisibility(View.GONE);
                    textViewDialogRankingMensaje1.setVisibility(View.GONE);
                    textViewDialogRankingEmp.setVisibility(View.GONE);

                    linearLayoutDialogRankingP.setVisibility(View.VISIBLE);
                    buttonDialogRankingEnviar.setVisibility(View.VISIBLE);

                    buttonDialogRankingEnviar.setOnClickListener(view141 -> {
                        FBrutaRanking fBrutaRanking = new FBrutaRanking(mapranking.get(1),mapranking.get(2),mapranking.get(3));
                        rankingUsuarioRuta.setValue(fBrutaRanking)
                                .addOnCompleteListener(task -> {
                                    buttonDialogRankingEnviar.setVisibility(View.GONE);
                                    textViewDialogRankingResultado.setVisibility(View.VISIBLE);
                                    iniciarInterfazRuta(usuariohistorial,valoracionUsuarioRuta,rankingUsuarioRuta,rankingRuta);
                                })
                                .addOnFailureListener(e -> System.out.println("Error"));
                    });
                }

            });

            dialog.show();
        });

        imageButtonRutaRepeat.setOnClickListener(view -> {
            fg.startViewAnimation(Ruta.this,imageButtonRutaRepeat,0);

            AlertDialog.Builder builder = new AlertDialog.Builder(Ruta.this);
            LayoutInflater li = LayoutInflater.from(Ruta.this);
            final View view2 = li.inflate(R.layout.dialog_ruta_repetir, null);
            builder.setView(view2);

            AlertDialog dialog = builder.create();

            Button buttonDialogrepetir = view2.findViewById(R.id.buttonDialogRepetir);
            TextView textViewDialogRepetir = view2.findViewById(R.id.textViewDialogRepetir);

            buttonDialogrepetir.setOnClickListener(view15 -> usuariohistorial.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String emp1 = Objects.requireNonNull(snapshot.getValue(FBhistorialRutaUsuario.class)).getEmpresa();
                    String emp2 = Objects.requireNonNull(snapshot.getValue(FBhistorialRutaUsuario.class)).getEmpresa2();
                    String emp3 = Objects.requireNonNull(snapshot.getValue(FBhistorialRutaUsuario.class)).getEmpresa3();
                    String loc = Objects.requireNonNull(snapshot.getValue(FBhistorialRutaUsuario.class)).getLocalidad();
                    String nom = Objects.requireNonNull(snapshot.getValue(FBhistorialRutaUsuario.class)).getNombre();
                    boolean val = Objects.requireNonNull(snapshot.getValue(FBhistorialRutaUsuario.class)).getValorada();
                    int comp = Objects.requireNonNull(snapshot.getValue(FBhistorialRutaUsuario.class)).getVecesCompletada();

                    FBhistorialRutaUsuario fBhistorialRutaUsuario = new FBhistorialRutaUsuario(emp1,emp2,emp3,"nv","nv","nv",loc,comp,val,nom);

                    usuariohistorial.setValue(fBhistorialRutaUsuario).addOnCompleteListener(task -> {
                        buttonDialogrepetir.setVisibility(View.GONE);
                        textViewDialogRepetir.setText(getText(R.string.dialog_repetir_ruta));
                        progressBar.setProgress(0);
                        imageButtonRutaRepeat.setVisibility(View.GONE);
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            }));

            dialog.show();
        });

        imageButtonRutaCalendar.setOnClickListener(view -> {
            fg.startViewAnimation(Ruta.this,imageButtonRutaCalendar,0);

            AlertDialog.Builder builder = new AlertDialog.Builder(Ruta.this);
            LayoutInflater li = LayoutInflater.from(Ruta.this);
            final View view2 = li.inflate(R.layout.dialog_fecha_fin, null);
            builder.setView(view2);

            AlertDialog dialog = builder.create();

            TextView textViewDialogFechaFinMensaje1 = view2.findViewById(R.id.textViewDialogFechaFinMensaje1);
            TextView textViewDialogFechaFinMensaje2 = view2.findViewById(R.id.textViewDialogFechaFinMensaje2);
            textViewDialogFechaFinMensaje2.setText(final_ruta);

            if (competitiva_ruta == 1) {
                textViewDialogFechaFinMensaje1.setText(getString(R.string.dialog_fecha_fin_competicion));
            } else {
                textViewDialogFechaFinMensaje1.setText(getString(R.string.dialog_fecha_fin_ruta));
            }

            dialog.show();
        });

    }

    /**
     * Actualiza la interfaz de la ruta.
     */

    public void iniciarInterfazRuta(DatabaseReference uh, DatabaseReference uvr, DatabaseReference rur, DatabaseReference rr) {
        ratingBarRutaActivity.setRating(valoracion_ruta);
        //int part = getparticipacionTotalOferta();

        Picasso.get()
                .load(fotorv_ruta)
                .fit()
                .into(imageViewRvRuta);

        Picasso.get()
                .load(fotoact_ruta)
                .fit()
                .into(imageViewRutaLocal1);

        Picasso.get()
                .load(fotoact2_ruta)
                .fit()
                .into(imageViewRutaLocal2);

        Picasso.get()
                .load(fotoact3_ruta)
                .fit()
                .into(imageViewRutaLocal3);

        textViewRutaNombre.setText(nombre_ruta);
        textViewLocal1Nombre.setText(empresa_ruta);
        textViewLocal2Nombre.setText(empresa2_ruta);
        textViewLocal3Nombre.setText(empresa3_ruta);

        if(competitiva_ruta == 1) {
            rr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        int id1 = 0, id2 = 0, id3 = 0;
                        ImageView imageViewRutaMedalla1 = findViewById(R.id.imageViewRutaMedalla1);
                        ImageView imageViewRutaMedalla12 = findViewById(R.id.imageViewRutaMedalla12);
                        ImageView imageViewRutaMedalla13 = findViewById(R.id.imageViewRutaMedalla13);

                        ImageView imageViewRutaMedalla2 = findViewById(R.id.imageViewRutaMedalla2);
                        ImageView imageViewRutaMedalla22 = findViewById(R.id.imageViewRutaMedalla22);
                        ImageView imageViewRutaMedalla23 = findViewById(R.id.imageViewRutaMedalla23);

                        ImageView imageViewRutaMedalla3 = findViewById(R.id.imageViewRutaMedalla3);
                        ImageView imageViewRutaMedalla32 = findViewById(R.id.imageViewRutaMedalla32);
                        ImageView imageViewRutaMedalla33 = findViewById(R.id.imageViewRutaMedalla33);

                        for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            String idemp1 = Objects.requireNonNull(dataSnapshot.getValue(FBrutaRanking.class)).getIdemp1();
                            String idemp2 = Objects.requireNonNull(dataSnapshot.getValue(FBrutaRanking.class)).getIdemp2();
                            String idemp3 = Objects.requireNonNull(dataSnapshot.getValue(FBrutaRanking.class)).getIdemp3();

                            if(idemp1.equals(id_empresa1)) {
                                id1+=5;
                            } else if(idemp1.equals(id_empresa2)) {
                                id2+=5;
                            } else if(idemp1.equals(id_empresa3)) {
                                id3+=5;
                            }

                            if(idemp2.equals(id_empresa1)) {
                                id1+=3;
                            } else if(idemp2.equals(id_empresa2)) {
                                id2+=3;
                            } else if(idemp2.equals(id_empresa3)) {
                                id3+=3;
                            }

                            if(idemp3.equals(id_empresa1)) {
                                id1+=1;
                            } else if(idemp3.equals(id_empresa2)) {
                                id2+=1;
                            } else if(idemp3.equals(id_empresa3)) {
                                id3+=1;
                            }
                        }

                        //Establecer medallas
                        if((id1>id2) && (id1>id3)) {
                            imageViewRutaMedalla1.setVisibility(View.VISIBLE);
                            if(id2>id3) {
                                imageViewRutaMedalla22.setVisibility(View.VISIBLE);
                                imageViewRutaMedalla33.setVisibility(View.VISIBLE);
                            } else {
                                imageViewRutaMedalla32.setVisibility(View.VISIBLE);
                                imageViewRutaMedalla23.setVisibility(View.VISIBLE);
                            }
                        } else if((id2>id1) && (id2>id3)) {
                            imageViewRutaMedalla2.setVisibility(View.VISIBLE);
                            if(id1>id3) {
                                imageViewRutaMedalla12.setVisibility(View.VISIBLE);
                                imageViewRutaMedalla33.setVisibility(View.VISIBLE);
                            } else {
                                imageViewRutaMedalla32.setVisibility(View.VISIBLE);
                                imageViewRutaMedalla13.setVisibility(View.VISIBLE);
                            }
                        } else if((id3>id2) && (id3>id1)) {
                            imageViewRutaMedalla3.setVisibility(View.VISIBLE);
                            if(id1>id2) {
                                imageViewRutaMedalla12.setVisibility(View.VISIBLE);
                                imageViewRutaMedalla23.setVisibility(View.VISIBLE);

                            } else {
                                imageViewRutaMedalla22.setVisibility(View.VISIBLE);
                                imageViewRutaMedalla13.setVisibility(View.VISIBLE);
                            }
                        } else if(id1==id2 && id1==id3) {
                            imageViewRutaMedalla1.setVisibility(View.VISIBLE);
                            imageViewRutaMedalla2.setVisibility(View.VISIBLE);
                            imageViewRutaMedalla3.setVisibility(View.VISIBLE);
                        } else if(id1==id2) {
                            imageViewRutaMedalla1.setVisibility(View.VISIBLE);
                            imageViewRutaMedalla2.setVisibility(View.VISIBLE);
                            imageViewRutaMedalla33.setVisibility(View.VISIBLE);
                        } else if(id1==id3) {
                            imageViewRutaMedalla1.setVisibility(View.VISIBLE);
                            imageViewRutaMedalla3.setVisibility(View.VISIBLE);
                            imageViewRutaMedalla23.setVisibility(View.VISIBLE);
                        } else {
                            imageViewRutaMedalla2.setVisibility(View.VISIBLE);
                            imageViewRutaMedalla3.setVisibility(View.VISIBLE);
                            imageViewRutaMedalla13.setVisibility(View.VISIBLE);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        actualizarRuta(uh,uvr,rur);

    }

    /**
     * Actualiza la ruta en función del progreso del usuario.
     *
     * @param uh Referencia al nodo de Firebase.
     */

    public void actualizarRuta(DatabaseReference uh, DatabaseReference vur, DatabaseReference rur) {
        System.out.println("Ruta actualizada");
        uh.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String historialFecha1 = Objects.requireNonNull(snapshot.getValue(FBhistorialRutaUsuario.class)).getFechaCompletada();
                    String historialFecha2 = Objects.requireNonNull(snapshot.getValue(FBhistorialRutaUsuario.class)).getFechaCompletada2();
                    String historialFecha3 = Objects.requireNonNull(snapshot.getValue(FBhistorialRutaUsuario.class)).getFechaCompletada3();
                    boolean historialValorada = Objects.requireNonNull(snapshot.getValue(FBhistorialRutaUsuario.class)).getValorada();
                    int vecesCompletada = Objects.requireNonNull(snapshot.getValue(FBhistorialRutaUsuario.class)).getVecesCompletada();
                    boolean val1, val2, val3;
                    val1 = val2 = val3 = false;
                    int progreso = 0;

                    if (!semcompletada) {
                        controlcompletada = vecesCompletada;
                        //System.out.println("controlcompletada: " + controlcompletada);
                        semcompletada = true;
                    } else if (vecesCompletada > controlcompletada){
                        controlcompletada = vecesCompletada;
                        //System.out.println("Se ha completado la oferta: " + vecesCompletada);
                        if (qrdialog.isShowing()) {
                            qrdialog.dismiss();

                        }

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Ruta.this);
                                LayoutInflater li = LayoutInflater.from(Ruta.this);
                                final View view2 = li.inflate(R.layout.dialog_ruta_completada, null);
                                builder.setView(view2);

                                AlertDialog dialog = builder.create();

                                dialog.show();
                            }
                        },500);


                    }

                    if (historialFecha1.equals("nv")) {
                        imageButtonLocal1Tick.setBorderColor(Color.parseColor("#A1A1A1"));
                    } else {
                        imageButtonLocal1Tick.setBorderColor(Color.parseColor("#3EEC95"));
                        val1 = true;
                        progreso += 33;
                    }

                    if (historialFecha2.equals("nv")) {
                        imageButtonLocal1Tick2.setBorderColor(Color.parseColor("#A1A1A1"));
                    } else {
                        imageButtonLocal1Tick2.setBorderColor(Color.parseColor("#3EEC95"));
                        val2 = true;
                        progreso += 33;
                    }

                    if (historialFecha3.equals("nv")) {
                        imageButtonLocal1Tick3.setBorderColor(Color.parseColor("#A1A1A1"));
                    } else {
                        imageButtonLocal1Tick3.setBorderColor(Color.parseColor("#3EEC95"));
                        val3 = true;
                        progreso += 33;
                    }

                    initProgressBarAnimation(progreso);

                    if (val1 && val2 && val3) {
                        textViewValoracionRuta.setText(getString(R.string.ruta_valorar));
                        textViewValoracionRuta.setVisibility(View.VISIBLE);
                        imageButtonTicketRuta.setImageDrawable(ContextCompat.getDrawable(Ruta.this,R.drawable.iconticket35));

                        if(competitiva_ruta == 1) {
                            imageButtonSeleccionRanking.setVisibility(View.VISIBLE);
                            rur.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()) {
                                        textViewRankingRuta.setText(getString(R.string.ranking_hecho));

                                    } else {
                                        textViewRankingRuta.setText(getString(R.string.ranking_por_hacer));

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        if (!historialValorada) {
                            imageButtonTicketRuta.setImageDrawable(ContextCompat.getDrawable(Ruta.this,R.drawable.iconticket35));
                            ratingBarValoracionUsuario.setVisibility(View.VISIBLE);
                            ratingBarValoracionUsuario.setVisibility(View.VISIBLE);
                            ratingBarValoracionUsuario.setIsIndicator(false);
                            imageButtonEnviarValoracion.setVisibility(View.VISIBLE);
                            imageButtonEnviarValoracion.setImageDrawable(ContextCompat.getDrawable(Ruta.this,R.drawable.iconenviargray24));

                        } else {
                            textViewValoracionRuta.setText(getString(R.string.ruta_valorada));
                            imageButtonEnviarValoracion.setVisibility(View.GONE);
                            ratingBarValoracionUsuario.setVisibility(View.VISIBLE);

                            vur.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ratingBarValoracionUsuario.setRating(Objects.requireNonNull(snapshot.getValue(FBrutaValoracion.class)).getValoracion());
                                    ratingBarValoracionUsuario.setIsIndicator(true);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                    } else {
                        imageButtonTicketRuta.setImageDrawable(ContextCompat.getDrawable(Ruta.this,R.drawable.iconticketdisasbled35));
                    }

                } else {
                    imageButtonLocal1Tick.setBorderColor(Color.parseColor("#A1A1A1"));
                    imageButtonLocal1Tick2.setBorderColor(Color.parseColor("#A1A1A1"));
                    imageButtonLocal1Tick3.setBorderColor(Color.parseColor("#A1A1A1"));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Animación de la barra de progreso del usuario en la ruta.
     */

    public void initProgressBarAnimation(int p) {
        Timer timer = new Timer();
        int progreso;

        if(p != 99) {
            progreso = p;
        } else {
            progreso = 100;
            fg.startViewAnimation(Ruta.this,imageButtonTicketRuta,3);

            if (competitiva_ruta == 0) {
                imageButtonRutaRepeat.setVisibility(View.VISIBLE);
            }
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if(progressBar.getProgress() < progreso) {
                        progressBar.incrementProgressBy(1);
                    } else {
                        timer.cancel();
                        timer.purge();
                    }
                });
            }
        }, 0, 10);
    }

    /**
     * Comprueba si el usuario tiene la ruta en favoritos.
     */

    public void getEstadoFavorito() {
        String resFavCache = getStringCache(id_ruta+"-favorito");

        if (resFavCache == null ) {
            //System.out.println("No existe la caché de favoritos y se crea ");
            setStringCache(id_ruta+"-favorito","no");

            favoritosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        imageViewRutaFavoritos.setBackground(ContextCompat.getDrawable(Ruta.this, R.drawable.estrella_36r));
                        estado_favorito = true;
                        setStringCache(id_ruta+"-favorito","si");

                    } else {
                        imageViewRutaFavoritos.setBackground(ContextCompat.getDrawable(Ruta.this, R.drawable.estrella_36v));
                        estado_favorito = false;
                        setStringCache(id_ruta+"-favorito","no");

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (resFavCache.equals("si")){
            System.out.println("La ruta se encuentra en caché de favoritos (sí)");
            estado_favorito = true;
            imageViewRutaFavoritos.setBackground(ContextCompat.getDrawable(Ruta.this, R.drawable.estrella_36r));

        } else if (resFavCache.equals("no")) {
            System.out.println("La ruta se encuentra en caché de favoritos (no)");
            estado_favorito = false;
            imageViewRutaFavoritos.setBackground(ContextCompat.getDrawable(Ruta.this, R.drawable.estrella_36v));
        }



    }

    /**
     * Agrega la ruta a favoritos.
     *
     * @param destino Ruta de destino en Firebase.
     */

    public void transferirFavoritos(final DatabaseReference destino, FBruta oferta) {
        destino.setValue(oferta);

        if (!added_badge) {
            DatabaseReference referenceBadge = FirebaseDatabase.getInstance().getReference().child("usuariosbadges").child(mFirebaseUser.getUid()).child("favoritos");

            referenceBadge.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int num = Objects.requireNonNull(snapshot.getValue(FBbadgeFavoritos.class)).getTotal();

                    FBbadgeFavoritos bf = new FBbadgeFavoritos(num+1);

                    referenceBadge.setValue(bf);
                    added_badge = true;

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    /**
     * Guarda el estado de favorito de la ruta en la caché.
     *
     * @param st Elemento a guardar
     */

    public void setStringCache(String key, String st) {
        //pref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, st);
        editor.apply();

    }

    /**
     * Carga el estado de favorito de la ruta de la caché.
     *
     */

    public String getStringCache(String key) {
        return pref.getString(key, null);

    }

}
