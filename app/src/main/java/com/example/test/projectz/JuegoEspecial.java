package com.example.test.projectz;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Objects;

/**
 * Clase que controla los juegos.
 */

public class JuegoEspecial extends Activity {
    private String comienzoJuego, finalJuego, explicacionJuego, idJuego,
            empresaJuego,  ciudadmin, estadoContador, num_codigo = "qr1",
            fechaV1, fechaV2, fechaV3, swboundbox = null, neboundbox = null;
    private boolean partsuf = false, contadorIniciado = false, sensorAdquirido = false;
    private int numContador, numValidados, estadoJuego, sensor;
    private ValueEventListener estadoListener, contadorListener;
    private TextView textviewDialogTutorial1;
    private TextView textviewObjetivos;
    private DatabaseReference estadoMapa, estadoSensor, estadoValidacion, eventos, eventosContador, progresoUsuario;
    private ImageButton buttonUbicacion;
    private ImageButton buttonSensor;
    private ImageButton buttonValidar;
    private ImageButton buttonPista;
    private LinearLayout layoutSensor;
    private AnimationDrawable animationDrawable;
    private FirebaseUser mFirebaseUser;
    private FusedLocationProviderClient mFusedLocationClient;
    private double latitudActual, longitudActual;
    private RecyclerView rvClasificacion;
    private FirebaseRecyclerAdapter adapter;
    Query query;
    private FuncionesGlobales fg;
    private WebView reloj;

    /**
     * Crea la vista la activity con sus componentes y los inicializa.
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_especial);

        // Obtiene el usuario conectado.
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        fg = new FuncionesGlobales();

        buttonUbicacion = findViewById(R.id.buttonJuegoEspecialMapa);
        buttonSensor = findViewById(R.id.buttonJuegoEspecialSensor);
        buttonValidar = findViewById(R.id.buttonJuegoEspecialValidar);
        ImageButton buttonTutorial = findViewById(R.id.buttonJuegoEspecialTutorial);
        buttonPista = findViewById(R.id.buttonJuegoEspecialPista);
        ImageButton buttonJuegoFiltroQR = findViewById(R.id.imageButtonJuegoFiltroQR);
        TextView textViewJuegoClasificacionM = findViewById(R.id.textViewJuegoClasificacionM);
        textviewObjetivos = findViewById(R.id.textviewJuegoEspecialObjetivos);
        reloj = findViewById(R.id.webviewJuegoEspecialReloj);
        layoutSensor = findViewById(R.id.linear_layout_sensor);
        LinearLayout linearLayoutJuegoObjetivos = findViewById(R.id.linearLayoutJuegoObjetivos);
        LinearLayout linearLayoutJuegoClasificacion = findViewById(R.id.linearLayoutJuegoClasificacion);
        LinearLayout linearLayoutJuegoPistaDetalles = findViewById(R.id.linearLayoutJuegoPistaDetalles);

        rvClasificacion = findViewById(R.id.recyclerViewClasificacion2);
        rvClasificacion.setLayoutManager(new LinearLayoutManager(this));
        rvClasificacion.setHasFixedSize(true);

        Bundle bJuego = getIntent().getExtras();
        comienzoJuego = bJuego.getString("cJ");
        finalJuego = bJuego.getString("fJ");
        explicacionJuego = bJuego.getString("explJ");
        idJuego = bJuego.getString("idJ");
        estadoJuego = bJuego.getInt("estJ");
        empresaJuego = bJuego.getString("empJ");
        String ciudadJuego = bJuego.getString("ciuJ");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        DatabaseReference boundbox = FirebaseDatabase.getInstance().getReference().child("juegosBoxBound").child(idJuego);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ciudadmin = fg.getCiuloc(ciudadJuego);
        eventos = FirebaseDatabase.getInstance().getReference().child("eventos").child(ciudadmin).child(idJuego);
        eventosContador = FirebaseDatabase.getInstance().getReference().child("eventosContador").child(ciudadmin).child(idJuego).child("qr");
        progresoUsuario = FirebaseDatabase.getInstance().getReference().child("usuarioeventos").child(mFirebaseUser.getUid()).child(idJuego);
        estadoMapa = FirebaseDatabase.getInstance().getReference().child("usuarioeventosmapa").child(mFirebaseUser.getUid()).child(idJuego);
        estadoValidacion = FirebaseDatabase.getInstance().getReference().child("usuarioeventosvalidacion").child(mFirebaseUser.getUid()).child(idJuego);
        estadoSensor = FirebaseDatabase.getInstance().getReference().child("usuarioeventossensor").child(mFirebaseUser.getUid()).child(idJuego);

        fg.startViewAnimation(JuegoEspecial.this, linearLayoutJuegoObjetivos, 1);
        fg.startViewAnimation(JuegoEspecial.this, layoutSensor, 2);
        fg.startViewAnimation(JuegoEspecial.this, linearLayoutJuegoPistaDetalles, 2);
        fg.startViewAnimation(JuegoEspecial.this, linearLayoutJuegoClasificacion, 2);

        getProgresoUsuario();

        buttonUbicacion.setOnClickListener(v -> {
            if (!fg.checkConexionTipo(JuegoEspecial.this).equals("null")) {
                fg.startViewAnimation(JuegoEspecial.this, v, 0);

                if(swboundbox == null) {
                    boundbox.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            neboundbox = Objects.requireNonNull(snapshot.getValue(FBjuegosBoxBound.class)).getNe();
                            swboundbox = Objects.requireNonNull(snapshot.getValue(FBjuegosBoxBound.class)).getSw();

                            FBjuegoEstadoMapa jem = new FBjuegoEstadoMapa("true");
                            estadoMapa.setValue(jem).addOnCompleteListener(task -> mFirebaseUser.getIdToken(true)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            String idToken = task1.getResult().getToken();
                                            String rt = "https://przev.herokuapp.com/vgetmdata/" + idJuego + "/" + idToken + "/" + ciudadmin;
                                            //String rt = "http://10.0.2.2:8080/vgetmdata/" + idJuego + "/" + idToken + "/" + ciudadmin;
                                            //String rt = "http://192.168.1.2:8080/vgetmdata/" + idJuego + "/" + idToken + "/" + ciudadmin;
                                            System.out.println(rt);


                                            // Petición de coordenadas del área o localización exacta del código qr actual.
                                            RequestQueue queue = Volley.newRequestQueue(JuegoEspecial.this);
                                            queue.getCache().clear();

                                            StringRequest stringRequest = new StringRequest(Request.Method.GET, rt,
                                                    response -> {
                                                        String coordenadasactuales = response.substring(1, response.length() - 1);
                                                        //System.out.println("Coordenadas obtenidas" + coordenadasactuales);

                                                        Intent intent = new Intent(JuegoEspecial.this, MapsJuegoActivity.class);
                                                        int qr_usuario = numValidados + 1;

                                                        if (qr_usuario == numContador) {
                                                            //System.out.println("El usuario está buscando el código qr mas reciente.");
                                                            intent.putExtra("crdcJ", coordenadasactuales);
                                                            intent.putExtra("ne", neboundbox);
                                                            intent.putExtra("sw", swboundbox);

                                                        } else {
                                                            //System.out.println("El usuario está buscando un código qr anterior al actual.");
                                                            if (estadoContador.equals("false")) {
                                                                intent.putExtra("crdJ", coordenadasactuales);
                                                                intent.putExtra("empJ", empresaJuego);
                                                            } else {
                                                                intent.putExtra("crdcJ", coordenadasactuales);
                                                            }
                                                            intent.putExtra("ne", neboundbox);
                                                            intent.putExtra("sw", swboundbox);
                                                        }

                                                        startActivity(intent);

                                                    }, error -> {
                                                        FBjuegoEstadoMapa jem1 = new FBjuegoEstadoMapa("false");
                                                        estadoMapa.setValue(jem1);
                                                        System.out.println("Error Conexión buttonUbicación (boundbox == null)");
                                                        getDialogConexion();

                                            });

                                            stringRequest.setShouldCache(false);
                                            queue.add(stringRequest);
                                        }
                                    }));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    FBjuegoEstadoMapa jem = new FBjuegoEstadoMapa("true");
                    estadoMapa.setValue(jem).addOnCompleteListener(task -> mFirebaseUser.getIdToken(true)
                            .addOnCompleteListener(task12 -> {
                                if (task12.isSuccessful()) {
                                    String idToken = task12.getResult().getToken();
                                    String rt = "https://przev.herokuapp.com/vgetmdata/" + idJuego + "/" + idToken + "/" + ciudadmin;
                                    //String rt = "http://10.0.2.2:8080/vgetmdata/" + idJuego + "/" + idToken + "/" + ciudadmin;
                                    //String rt = "http://192.168.1.2:8080/vgetmdata/" + idJuego + "/" + idToken + "/" + ciudadmin;

                                    // Petición de coordenadas del área o localización exacta del código qr actual.
                                    RequestQueue queue = Volley.newRequestQueue(JuegoEspecial.this);
                                    queue.getCache().clear();

                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, rt,
                                            response -> {
                                                String coordenadasactuales = response.substring(1, response.length() - 1);
                                                //System.out.println("Coordenadas obtenidas" + coordenadasactuales);

                                                Intent intent = new Intent(JuegoEspecial.this, MapsJuegoActivity.class);
                                                int qr_usuario = numValidados + 1;

                                                if (qr_usuario == numContador) {
                                                    //System.out.println("El usuario está buscando el código qr mas reciente.");
                                                    intent.putExtra("crdcJ", coordenadasactuales);
                                                    intent.putExtra("ne", neboundbox);
                                                    intent.putExtra("sw", swboundbox);

                                                } else {
                                                    //System.out.println("El usuario está buscando un código qr anterior al actual.");
                                                    if (estadoContador.equals("false")) {
                                                        intent.putExtra("crdJ", coordenadasactuales);
                                                        intent.putExtra("empJ", empresaJuego);
                                                    } else {
                                                        intent.putExtra("crdcJ", coordenadasactuales);
                                                    }
                                                    intent.putExtra("ne", neboundbox);
                                                    intent.putExtra("sw", swboundbox);
                                                }

                                                startActivity(intent);

                                            }, error -> {
                                        FBjuegoEstadoMapa jem1 = new FBjuegoEstadoMapa("false");
                                        estadoMapa.setValue(jem1);

                                        System.out.println("Error Conexión buttonUbicación (boundbox != null)");
                                        getDialogConexion();

                                    });

                                    stringRequest.setShouldCache(false);
                                    queue.add(stringRequest);
                                }
                            }));
                }




            } else {
                System.out.println("Error Conexión buttonUbicación (sin conexión)");
                getDialogConexion();
            }

        });

        buttonTutorial.setOnClickListener(view -> {
            fg.startViewAnimation(JuegoEspecial.this, view, 0);

            AlertDialog.Builder builder = new AlertDialog.Builder(JuegoEspecial.this);
            LayoutInflater li = LayoutInflater.from(JuegoEspecial.this);
            final View viewDialog = li.inflate(R.layout.dialog_tutorial, null);
            builder.setView(viewDialog);

            AlertDialog dialog = builder.create();

            textviewDialogTutorial1 = viewDialog.findViewById(R.id.dialog_tutorial_mensaje1);
            textviewDialogTutorial1.setText(explicacionJuego);

            dialog.show();
        });

        buttonSensor.setOnClickListener(view -> {
            if (!fg.checkConexionTipo(JuegoEspecial.this).equals("null")) {
                fg.startViewAnimation(JuegoEspecial.this, view, 0);
                //Si el usuario ya dispone del sensor, no se muestra el diálogo
                if (sensorAdquirido) {
                    //buttonSensor.setEnabled(false);
                    //System.out.println("sensorAdquirido: " + sensorAdquirido);
                    FBjuegoEstadoSensor jes = new FBjuegoEstadoSensor("true");
                    estadoSensor.setValue(jes).addOnCompleteListener(task -> sensorDatos());


                } else {
                    //Obtener las participaciones del usuario.
                    DatabaseReference datos = FirebaseDatabase.getInstance().getReference().child("usuariodatos").child(mFirebaseUser.getUid());

                    datos.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int usuarioParticipaciones = Objects.requireNonNull(snapshot.getValue(FBdatosUsuario.class)).getParticipaciones();
                            int usuarioNivel = Objects.requireNonNull(snapshot.getValue(FBdatosUsuario.class)).getNivel();
                            int usuarioExperiencia = Objects.requireNonNull(snapshot.getValue(FBdatosUsuario.class)).getExperiencia();
                            int usuarioExperienciaNivel = Objects.requireNonNull(snapshot.getValue(FBdatosUsuario.class)).getExpsignivel();
                            int usuarionuevo = Objects.requireNonNull(snapshot.getValue(FBdatosUsuario.class)).getNuevo();

                            partsuf = usuarioParticipaciones >= 1.0;

                            AlertDialog.Builder builder = new AlertDialog.Builder(JuegoEspecial.this);
                            LayoutInflater li = LayoutInflater.from(JuegoEspecial.this);
                            final View viewDialog = li.inflate(R.layout.dialog_juego_sensor, null);
                            builder.setView(viewDialog);
                            AlertDialog dialog = builder.create();
                            Button buttonadquirirSensor = viewDialog.findViewById(R.id.button_dialog_adquirir_sensor);

                            if (partsuf) {
                                buttonadquirirSensor.setEnabled(true);
                            }

                            dialog.show();

                            // En caso de disponer de las participaciones suficientes y adquirir el sensor
                            buttonadquirirSensor.setOnClickListener(view1 -> {

                                progresoUsuario.child("sensor").setValue(1);
                                sensorAdquirido = true;
                                buttonSensor.setBackground(ContextCompat.getDrawable(JuegoEspecial.this, R.drawable.sensor_shape_button));


                                // Restar la participación al usuario.
                                FBdatosUsuario du = new FBdatosUsuario(usuarioNivel, usuarioExperiencia, usuarioExperienciaNivel, usuarioParticipaciones - 1, usuarionuevo);
                                datos.setValue(du);
                                dialog.dismiss();
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            } else {
                System.out.println("Error Conexión buttonSensor");
                getDialogConexion();
            }

        });

        buttonValidar.setOnClickListener(v -> {
            if (!fg.checkConexionTipo(JuegoEspecial.this).equals("null")) {
                fg.startViewAnimation(JuegoEspecial.this, v, 0);
                FBjuegoEstadoValidacion jev = new FBjuegoEstadoValidacion("true");
                estadoValidacion.setValue(jev).addOnCompleteListener(task -> validarDatos());

            } else {
                System.out.println("Error Conexión buttonValidar");
                getDialogConexion();
            }

        });

        buttonJuegoFiltroQR.setOnClickListener(view -> {
            fg.startViewAnimation(JuegoEspecial.this, view, 0);
            AlertDialog.Builder builder = new AlertDialog.Builder(JuegoEspecial.this);
            LayoutInflater li = LayoutInflater.from(JuegoEspecial.this);
            final View viewdialog = li.inflate(R.layout.dialog_filtro_qr, null);
            builder.setView(viewdialog);
            AlertDialog dialog = builder.create();

            Typeface fuente = ResourcesCompat.getFont(JuegoEspecial.this, R.font.poppinsregular);

            RadioButton rbqr1 = viewdialog.findViewById(R.id.radioButtonqr1);
            rbqr1.setTypeface(fuente);
            rbqr1.setTextSize(16);

            RadioButton rbqr2 = viewdialog.findViewById(R.id.radioButtonqr2);
            rbqr2.setTypeface(fuente);
            rbqr2.setTextSize(16);

            RadioButton rbqr3 = viewdialog.findViewById(R.id.radioButtonqr3);
            rbqr3.setTypeface(fuente);
            rbqr3.setTextSize(16);

            switch (num_codigo) {
                case "qr1":
                    rbqr1.setChecked(true);
                    break;
                case "qr2":
                    rbqr2.setChecked(true);
                    break;
                case "qr3":
                    rbqr3.setChecked(true);
                    break;
            }

            rbqr1.setOnClickListener(view12 -> {
                textViewJuegoClasificacionM.setText(getString(R.string.clasificacion_qr1));
                num_codigo = "qr1";
                initAdapter(num_codigo);
            });

            rbqr2.setOnClickListener(view13 -> {
                textViewJuegoClasificacionM.setText(getString(R.string.clasificacion_qr2));
                num_codigo = "qr2";
                initAdapter(num_codigo);
            });

            rbqr3.setOnClickListener(view14 -> {
                textViewJuegoClasificacionM.setText(getString(R.string.clasificacion_qr3));
                num_codigo = "qr3";
                initAdapter(num_codigo);

            });

            dialog.show();
        });

    }

    /**
     * Ejecuta la cámara para escanear códigos QR.
     *
     */

    public void ejecutarCamara() {
        IntentIntegrator escaneadorQR = new IntentIntegrator(JuegoEspecial.this);
        escaneadorQR.setOrientationLocked(false);
        escaneadorQR.setBeepEnabled(false);
        escaneadorQR.initiateScan();
    }

    /**
     * Comprueba que el código QR sea correcto y esté dentro de una distancia determinada.
     *
     */

    public void validarDatos() {
        if (ActivityCompat.checkSelfPermission(JuegoEspecial.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                latitudActual = location.getLatitude();
                longitudActual = location.getLongitude();

                ejecutarCamara();


            } else {
                Toast toast = Toast.makeText(JuegoEspecial.this, "Posición indeterminada", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    /**
     * Obtiene la localización del código qr que está buscando el usuario.
     */

    public void sensorDatos() {
        if (ActivityCompat.checkSelfPermission(JuegoEspecial.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {

                latitudActual = location.getLatitude();
                longitudActual = location.getLongitude();

                mFirebaseUser.getIdToken(true)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();

                                String rt = "https://przev.herokuapp.com/vgetsdata/" + idJuego + "/" + idToken + "/" + ciudadmin + "/" + latitudActual + "/" + longitudActual;
                                //String rt = "http://10.0.2.2:8080/vgetsdata/" + idJuego + "/" + idToken + "/" + ciudadmin + "/" + latitudActual + "/" + longitudActual;
                                //String rt = "http://192.168.1.2:8080/vgetsdata/" + idJuego + "/" + idToken + "/" + ciudadmin + "/" + latitudActual + "/" + longitudActual;

                                // Petición de coordenadas del área o localización exacta del código qr actual.
                                RequestQueue queue = Volley.newRequestQueue(JuegoEspecial.this);
                                queue.getCache().clear();


                                StringRequest stringRequest = new StringRequest(Request.Method.GET, rt,
                                        response -> {
                                            System.out.println("Datos obtenidos del sensor: " + Float.parseFloat(response));
                                            startRotacionSensor(3);

                                            startAnimacionSensor(Float.parseFloat(response));

                                            FBjuegoEstadoSensor jes = new FBjuegoEstadoSensor("false");
                                            estadoSensor.setValue(jes)
                                                    .addOnCompleteListener(task2 -> buttonSensor.setEnabled(true));

                                        }, error -> {
                                            FBjuegoEstadoSensor jes = new FBjuegoEstadoSensor("false");
                                            estadoSensor.setValue(jes)
                                                    .addOnCompleteListener(task2 -> buttonSensor.setEnabled(true));


                                });

                                stringRequest.setShouldCache(false);
                                queue.add(stringRequest);
                            } else {
                                FBjuegoEstadoSensor jes = new FBjuegoEstadoSensor("false");
                                estadoSensor.setValue(jes)
                                        .addOnCompleteListener(task2 -> buttonSensor.setEnabled(true));
                            }
                        });


            } else {
                Toast toast = Toast.makeText(JuegoEspecial.this, "Posición indeterminada", Toast.LENGTH_LONG);
                toast.show();
            }
        });



    }

    /**
     * Cuando se inicia la activity, se inicia la escucha del adaptador.
     */

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("Iniciar Listeners");
        initAdapter(num_codigo);
        setEstadoListener();
        eventos.addValueEventListener(estadoListener);
        setContadorListener();
        eventosContador.addValueEventListener(contadorListener);
        //getProgresoUsuario();
    }

    /**
     * Cuando se detiene la activity, se detiene la escucha del adaptador.
     */

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("Detener Listeners");
        adapter.stopListening();
        eventos.removeEventListener(estadoListener);
        eventosContador.removeEventListener(contadorListener);
        //stopAnimacion();
    }

    @Override
    public void onBackPressed() {
        System.out.println("Detener Listeners");
        adapter.stopListening();
        eventos.removeEventListener(estadoListener);
        eventosContador.removeEventListener(contadorListener);
        Intent intent = new Intent(this, MenusActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Comprueba que la hora de validación de un código QR está dentro del horario del juego.
     *
     * @param fecha_fin Fecha del final del juego.
     * @param fecha_inicio Fecha del inicio del juego.
     */

    public void compararHoras(String fecha_fin, String fecha_inicio) {
        if (!fg.checkConexionTipo(JuegoEspecial.this).equals("null")) {
            String hr = "https://przfe.herokuapp.com/getHora";
            // Petición de la hora real.
            RequestQueue queue = Volley.newRequestQueue(JuegoEspecial.this);
            queue.getCache().clear();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, hr,
                    response -> {
                        System.out.println("Hora obtenida: " + response);

                        boolean cerrado;
                        String[] e = response.split(":");
                        String hora_actual = e[0];
                        String minuto_actual = e[1];
                        int ha = Integer.parseInt(hora_actual);
                        int ma = Integer.parseInt(minuto_actual);

                        String[] ea = fecha_inicio.split(":");
                        String hora_apertura = ea[0].substring(ea[0].length() - 2);
                        String minuto_apertura = ea[1];
                        int hap = Integer.parseInt(hora_apertura);
                        int map = Integer.parseInt(minuto_apertura);

                        String[] ec = fecha_fin.split(":");
                        String hora_cierre = ec[0].substring(ec[0].length() - 2);
                        String minuto_cierre = ec[1];
                        int hc = Integer.parseInt(hora_cierre);
                        int mc = Integer.parseInt(minuto_cierre);

                    /*System.out.println ("Hora Actual: " + ha);
                    System.out.println ("Minuto Actual: " + ma);
                    System.out.println ("Hora Cierre: " + mc);
                    System.out.println ("Minuto Cierre: " + mc);*/

                        // Fin de madrugada
                        if (hc <= 8) {
                            //System.out.println("Fin de madrugada.");
                            if (ha >= 0 && ha <= hc) {
                                if (ha == hc) {
                                    cerrado = ma > mc;
                                } else {
                                    cerrado = false;
                                }

                            } else if (ha > hc) {
                                if (ha >= hap) {
                                    if (ha == hap) {
                                        cerrado = ma < map;
                                    } else {
                                        cerrado = false;
                                    }

                                } else {
                                    cerrado = true;
                                }
                            } else {
                                cerrado = true;
                            }
                            // Fin de día.
                        } else {
                            //System.out.println("Fin de día.");
                            if (ha >= hap && ha <= hc) {
                                if (ha == hap) {
                                    cerrado = ma < map;
                                } else if (ha == hc) {
                                    cerrado = ma > mc;
                                } else {
                                    cerrado = false;
                                }

                            } else {
                                cerrado = true;
                            }
                        }

                        if (cerrado) {
                            /*FBjuegoEstadoValidacion jev = new FBjuegoEstadoValidacion("false");
                            estadoValidacion.setValue(jev);

                            Toast toast = Toast.makeText(JuegoEspecial.this, "Evento finalizado.", Toast.LENGTH_LONG);
                            toast.show();*/
                            validarDatos();

                        } else {
                            //System.out.println("Evento activo.");
                            validarDatos();
                        }

                    }, error -> {
                        FBjuegoEstadoValidacion jev = new FBjuegoEstadoValidacion("false");
                        estadoValidacion.setValue(jev);

                        System.out.println("Error Conexión compararHoras (getFecha)");
                        getDialogConexion();
                    });

            stringRequest.setShouldCache(false);
            queue.add(stringRequest);

        } else {
            System.out.println("Error Conexión compararHoras (sin conexión)");
            getDialogConexion();
        }

    }

    /**
     * Si se completa la validación del código QR con éxito, se actualiza el progreso de usuario en
     * el juego.
     *
     * @param requestCode Código petición.
     * @param resultCode Código resultado.
     * @param data Datos obtenidos.
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null && resultCode == RESULT_OK) {
            mFirebaseUser.getIdToken(true)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            String rt = "https://przev.herokuapp.com/vgetvaldata/" + idJuego + "/" + idToken + "/" + ciudadmin + "/" + latitudActual+ "/" + longitudActual + "/" + result.getContents();
                            //String rt = "http://10.0.2.2:8080/vgetvaldata/" + idJuego + "/" + idToken + "/" + ciudadmin + "/" + latitudActual+ "/" + longitudActual + "/" + result.getContents();
                            //String rt = "http://192.168.1.2:8080/vgetvaldata/" + idJuego + "/" + idToken + "/" + ciudadmin + "/" + latitudActual+ "/" + longitudActual + "/" + result.getContents();

                            // Petición de coordenadas del área o localización exacta del código qr actual.
                            RequestQueue queue = Volley.newRequestQueue(JuegoEspecial.this);
                            queue.getCache().clear();


                            StringRequest stringRequest = new StringRequest(Request.Method.GET, rt,
                                    response -> {
                                        System.out.println("Datos obtenidos del código: " + response);
                                        String resp = response.substring( 1, response.length() - 1);

                                        if (resp.equals("ok")) {
                                            actualizarProgresoUsuario();

                                        } else if (resp.equals(("lejos"))) {
                                            Toast toast = Toast.makeText(JuegoEspecial.this, "Demasiado lejos", Toast.LENGTH_LONG);
                                            toast.show();

                                        } else if (resp.equals(("error"))) {
                                            Toast toast = Toast.makeText(JuegoEspecial.this, "Código incorrecto", Toast.LENGTH_LONG);
                                            toast.show();

                                        } else {
                                            Toast toast = Toast.makeText(JuegoEspecial.this, "Prueba otra vez", Toast.LENGTH_LONG);
                                            toast.show();
                                        }

                                    }, error -> {
                                        FBjuegoEstadoValidacion jev = new FBjuegoEstadoValidacion("false");
                                        estadoValidacion.setValue(jev)
                                                .addOnCompleteListener(task2 -> {
                                                    buttonValidar.setEnabled(true);
                                                    System.out.println("Error Conexión onActivityResult (getDatosQR)");
                                                    getDialogConexion();
                                                });


                            });

                            stringRequest.setShouldCache(false);
                            queue.add(stringRequest);
                        }
                    });

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    /**
     * Actualiza el progreso del usuario en el juego.
     *
     */

    public void actualizarProgresoUsuario() {
        if (!fg.checkConexionTipo(JuegoEspecial.this).equals("null")) {
            String fcr = "https://przfe.herokuapp.com/getFechaCompleta";

            // Petición de la fecha real.
            RequestQueue queue = Volley.newRequestQueue(JuegoEspecial.this);
            queue.getCache().clear();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, fcr,
                    response -> {
                        System.out.println("Fecha obtenida (actualizarProgresoUsuario(): " + response);
                        String[] fecha = response.split(";");

                        //Actualizar interfaz y progreso del usuario.
                        DatabaseReference usuarioEvento = FirebaseDatabase.getInstance().getReference().child("usuarioeventos").child(mFirebaseUser.getUid()).child(idJuego);

                        usuarioEvento.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String fechaI = Objects.requireNonNull(snapshot.getValue(FBjuegoUsuarioInfo.class)).getFechaInscripcion();
                                String fechaV = Objects.requireNonNull(snapshot.getValue(FBjuegoUsuarioInfo.class)).getFechaValidacion();
                                int sensor = Objects.requireNonNull(snapshot.getValue(FBjuegoUsuarioInfo.class)).getSensor();
                                numValidados +=1;
                                FBjuegoUsuarioInfo progreso;

                                if (numValidados == 2) {
                                    progreso = new FBjuegoUsuarioInfo("null", fechaI, fechaV + ";" + fecha[0], numValidados, sensor);
                                    usuarioEvento.setValue(progreso).addOnCompleteListener(task -> {
                                        setHoraClasificacion(fecha,numValidados);
                                        setRegistrosClasificacion(fechaV + ";" + fecha[0]);
                                        getDialogValidacion(numValidados);
                                        actualizaObjectivos();

                                    });


                                } else if (numValidados == 3) {
                                    progreso = new FBjuegoUsuarioInfo(fecha[0], fechaI, fechaV + ";" + fecha[0], numValidados, sensor);
                                    usuarioEvento.setValue(progreso).addOnCompleteListener(task -> {
                                        System.out.println("Actualización con respecto al último código validado");
                                        setHoraClasificacion(fecha,numValidados);
                                        setRegistrosClasificacion(fechaV + ";" + fecha[0]);
                                        getDialogValidacion(numValidados);
                                        actualizaObjectivos();

                                    });

                                } else if (numValidados == 1) {
                                    progreso = new FBjuegoUsuarioInfo("null", fechaI, fecha[0], numValidados, sensor);
                                    usuarioEvento.setValue(progreso).addOnCompleteListener(task -> {
                                        setHoraClasificacion(fecha,numValidados);
                                        setRegistrosClasificacion(fecha[0]);
                                        getDialogValidacion(numValidados);
                                        actualizaObjectivos();

                                    });

                                }

                                FBjuegoEstadoValidacion jev = new FBjuegoEstadoValidacion("false");
                                estadoValidacion.setValue(jev)
                                        .addOnCompleteListener(task2 -> {
                                            buttonValidar.setEnabled(true);
                                        });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        //setHoraClasificacion(fecha);

                    }, error -> getDialogConexion());

            stringRequest.setShouldCache(false);
            queue.add(stringRequest);

        } else {
            System.out.println("Error Conexión actualizarProgresoUsuario (sin conexión)");
            getDialogConexion();
        }

    }

    /**
     * Establece en la clasificación la hora de validación.
     *
     * @param fecha Fecha en la que se realiza la validación.
     * @param nodo Nodo perteneciente al Qr que se ha validado.
     */

    public void setHoraClasificacion(String [] fecha, int nodo) {
        //Obtener la parte de la hora de la fecha previa.
        String hora = fecha[0].substring(fecha[0].length() - 11);

        //Si es la primera vez que se valida el código QR se elimina el campo por defecto de la clasificación.
        DatabaseReference borrarClasificacion = FirebaseDatabase.getInstance().getReference().child("eventosClasificacion").child("granada").child(idJuego).child("qr" + nodo).child("99:99");
        borrarClasificacion.removeValue().addOnCompleteListener(task -> {
            //Actualizar clasificación del código QR correspondiente.
            System.out.println("Existe 99:99 y se borra");
            DatabaseReference usuarioClasificacion = FirebaseDatabase.getInstance().getReference().child("eventosClasificacion").child("granada").child(idJuego).child("qr" + nodo).child(hora);

            FBjuegoClasificacion jc = new FBjuegoClasificacion(hora);
            usuarioClasificacion.setValue(jc);
        }).addOnFailureListener(e -> System.out.println("No existe 99:99 y no se borra"));

    }

    /**
     * Establece la escucha del estado del juego en firebase.
     */

    public void setEstadoListener() {
        estadoListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                estadoJuego = Objects.requireNonNull(snapshot.getValue(FBjuego.class)).getEstado();
                explicacionJuego = Objects.requireNonNull(snapshot.getValue(FBjuego.class)).getExplicacion();
                String mensaje;
                System.out.println("EstadoJuego: " + estadoJuego);
                LinearLayout linear_layout_sensor = findViewById(R.id.linear_layout_sensor);

                // FBjuego no iniciado
                if (estadoJuego == 0) {
                    linear_layout_sensor.setVisibility(View.GONE);
                    buttonUbicacion.setEnabled(false);
                    buttonValidar.setEnabled(false);
                    buttonPista.setEnabled(false);
                    if (sensorAdquirido) {
                        buttonSensor.setEnabled(false);
                    }
                    buttonSensor.setBackground(ContextCompat.getDrawable(JuegoEspecial.this, R.drawable.sensor_shape_button_desactivado));
                    mensaje = "El evento no ha comenzado.";
                    textviewObjetivos.setText(mensaje);

                    // FBjuego activo
                } else if (estadoJuego == 1) {
                    linear_layout_sensor.setVisibility(View.VISIBLE);
                    buttonUbicacion.setEnabled(true);
                    buttonValidar.setEnabled(true);
                    buttonPista.setEnabled(true);
                    if (sensorAdquirido) {
                        buttonSensor.setEnabled(true);
                    }

                    if (sensor == 1) {
                        buttonSensor.setBackground(ContextCompat.getDrawable(JuegoEspecial.this, R.drawable.sensor_shape_button));

                    } else {
                        buttonSensor.setBackground(ContextCompat.getDrawable(JuegoEspecial.this, R.drawable.sensor_shape_button_desactivado));

                    }

                    actualizaObjectivos();

                    // FBjuego pausado
                } else if (estadoJuego == 2) {
                    linear_layout_sensor.setVisibility(View.GONE);
                    buttonUbicacion.setEnabled(false);
                    buttonValidar.setEnabled(false);
                    buttonSensor.setEnabled(false);
                    buttonPista.setEnabled(false);
                    if (sensorAdquirido) {
                        buttonSensor.setEnabled(false);
                    }
                    buttonSensor.setBackground(ContextCompat.getDrawable(JuegoEspecial.this, R.drawable.sensor_shape_button_desactivado));
                    mensaje = "El evento se encuentra en mantenimiento.";
                    textviewObjetivos.setText(mensaje);

                    // FBjuego finalizado
                } else if (estadoJuego == 3) {
                    linear_layout_sensor.setVisibility(View.VISIBLE);
                    buttonUbicacion.setEnabled(false);
                    buttonValidar.setEnabled(false);
                    buttonSensor.setEnabled(false);
                    buttonPista.setEnabled(false);
                    if (sensorAdquirido) {
                        buttonSensor.setEnabled(false);
                    }
                    buttonSensor.setBackground(ContextCompat.getDrawable(JuegoEspecial.this, R.drawable.sensor_shape_button_desactivado));
                    mensaje = "El evento ha finalizado.";
                    textviewObjetivos.setText(mensaje);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        // Definir listener para el progreso de la cuenta atras.
        //setContadorListener();
    }

    /**
     * Establece la escucha de la cuenta atras en firebase.
     */

    public void setContadorListener() {
        contadorListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                estadoContador = Objects.requireNonNull(snapshot.getValue(FBjuegoProgreso.class)).getEstado();
                numContador = Objects.requireNonNull(snapshot.getValue(FBjuegoProgreso.class)).getQr();

                if (estadoContador.equals("true")) {
                    System.out.println("Mostrar la cuenta atras.");
                    if (!contadorIniciado) {
                        iniciarContador();
                        contadorIniciado = true;
                    }
                } else if (estadoContador.equals("false")) {
                    detenerContador();
                    contadorIniciado = false;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    /**
     * Obtiene el progreso actual del usuario en el juego.
     */

    public void getProgresoUsuario() {
        progresoUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String fechaVal = Objects.requireNonNull(snapshot.getValue(FBjuegoUsuarioInfo.class)).getFechaValidacion();
                    numValidados = Objects.requireNonNull(snapshot.getValue(FBjuegoUsuarioInfo.class)).getCantidadValidados();
                    sensor = Objects.requireNonNull(snapshot.getValue(FBjuegoUsuarioInfo.class)).getSensor();

                    if (sensor == 1) {
                        buttonSensor.setBackground(ContextCompat.getDrawable(JuegoEspecial.this, R.drawable.sensor_shape_button));

                    } else {
                        buttonSensor.setBackground(ContextCompat.getDrawable(JuegoEspecial.this, R.drawable.sensor_shape_button_desactivado));

                    }

                    setRegistrosClasificacion(fechaVal);

                    /*qr_actual = numValidados;
                    System.out.println("Qr actual=numvalidados: " + qr_actual);*/

                    if (sensor == 1) {
                        buttonSensor.setEnabled(true);
                        sensorAdquirido = true;
                    }

                    actualizaObjectivos();

                    // Definir listener para el estado del evento.
                    //setEstadoListener();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Obtiene las fechas en las que el usuario ha validado los códigos para resaltarlas en la
     * clasificación.
     *
     * @param fechaVal Fechas en las que se han validado los códigos QR.
     */

    public void setRegistrosClasificacion(String fechaVal) {
        System.out.println("Longitud validación: " + fechaVal.length());
        if (!fechaVal.equals("null")) {
            if (fechaVal.length() < 29) {
                System.out.println("1 validación");
                String[] fechaVaux = fechaVal.split("a las ");

                fechaV1 = fechaVaux[1];

                System.out.println("Fecha validación QR1: " + fechaV1);

            } else if (fechaVal.length() < 58) {
                System.out.println("2 validaciones");
                String[] fechaVaux = fechaVal.split(";");
                String[] fechaVaux1 = fechaVaux[0].split("a las ");
                String[] fechaVaux2 = fechaVaux[1].split("a las ");

                fechaV1 = fechaVaux1[1];
                fechaV2 = fechaVaux2[1];

                System.out.println("Fecha validación QR1: " + fechaV1);
                System.out.println("Fecha validación QR2: " + fechaV2);

            } else if (fechaVal.length() > 58) {
                System.out.println("3 validaciones");
                String[] fechaVaux = fechaVal.split(";");
                String[] fechaVaux1 = fechaVaux[0].split("a las ");
                String[] fechaVaux2 = fechaVaux[1].split("a las ");
                String[] fechaVaux3 = fechaVaux[2].split("a las ");

                fechaV1 = fechaVaux1[1];
                fechaV2 = fechaVaux2[1];
                fechaV3 = fechaVaux3[1];

                System.out.println("Fecha validación QR1: " + fechaV1);
                System.out.println("Fecha validación QR2: " + fechaV2);
                System.out.println("Fecha validación QR3: " + fechaV3);
            }
        }
    }

    /**
     * Muestra el próximo objectivo a cumplir para avanzar en el juego.
     */

    public void actualizaObjectivos() {
        LinearLayout linear_layout_sensor = findViewById(R.id.linear_layout_sensor);
        if (estadoJuego == 1) {
            String mensaje = null;
            System.out.println("numValidados en actualizaObjectivos()" + numValidados);
            if (numValidados == 0) {
                linear_layout_sensor.setVisibility(View.VISIBLE);
                mensaje = "Encuentra el primer código.";
                textviewObjetivos.setText(mensaje);
            } else if (numValidados == 1) {
                linear_layout_sensor.setVisibility(View.VISIBLE);
                mensaje = "Encuentra el segundo código.";
                textviewObjetivos.setText(mensaje);
            } else if (numValidados == 2) {
                linear_layout_sensor.setVisibility(View.VISIBLE);
                mensaje = "Encuentra el último código.";
                textviewObjetivos.setText(mensaje);
            } else if (numValidados == 3){
                linear_layout_sensor.setVisibility(View.GONE);
                mensaje = "Has completado con éxito el evento.";
                textviewObjetivos.setText(mensaje);

                buttonUbicacion.setEnabled(false);
                buttonValidar.setEnabled(false);
                buttonPista.setEnabled(false);
            }

            System.out.println("Mensaje actualizado: " + mensaje);


        }
    }

    /**
     * Inicia la cuenta atras cuando se valida un código qr por primera vez.
     *
     */

    public void iniciarContador() {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            reloj = findViewById(R.id.webviewJuegoEspecialReloj);
            WebSettings wSettings = reloj.getSettings();
            wSettings.setJavaScriptEnabled(true);
            reloj.loadUrl("https://cuenta-atras.herokuapp.com/");
            //reloj.reload();
            reloj.setVisibility(View.VISIBLE);
        }, 2000);
    }

    /**
     * Detiene el webview cuando termina la cuenta atras.
     *
     */

    public void detenerContador() {
        WebSettings wSettings = reloj.getSettings();
        wSettings.setJavaScriptEnabled(false);
        reloj.stopLoading();
        reloj.setVisibility(View.GONE);
    }

    /**
     * Actualiza la clasificación en función del qr seleccionado.
     *
     * @param qr Código QR.
     */

    public FirebaseRecyclerOptions<FBjuegoClasificacion> getOptions(String qr) {
        FirebaseRecyclerOptions<FBjuegoClasificacion> options;
        query = FirebaseDatabase.getInstance().getReference().child("eventosClasificacion").child("granada").child(idJuego).child(qr);
        options = new FirebaseRecyclerOptions.Builder<FBjuegoClasificacion>().setQuery(query, FBjuegoClasificacion.class).build();

        return options;
    }

    /**
     * Inicia el adaptador de la clasificación del juego.
     *
     * @param qr Código QR por el que filtrar la clasificación.
     */

    private void initAdapter(String qr) {
        adapter = new FirebaseRecyclerAdapter<FBjuegoClasificacion, ClasificacionViewHolder>(getOptions(qr)) {

            @Override
            protected void onBindViewHolder(@NonNull ClasificacionViewHolder holder, int position, @NonNull FBjuegoClasificacion model) {
                holder.bind(model);
                TextView registro = holder.itemView.findViewById(R.id.textViewRecyclerFechaClasificacion);
                TextView registro_pos = holder.itemView.findViewById(R.id.textViewRecyclerFechaClasificacionPos);

                if (!registro.getText().equals("")) {
                    String st = holder.getBindingAdapterPosition() + 1 + ". ";
                    registro_pos.setText(st);
                } else {
                    registro_pos.setText("");
                }

                switch (qr) {
                    case "qr1":
                        if (registro.getText().equals(fechaV1)) {
                            registro.setTextColor(Color.GREEN);
                            registro_pos.setTextColor(Color.GREEN);

                        } else {
                            registro.setTextColor(Color.WHITE);
                            registro_pos.setTextColor(Color.WHITE);
                        }

                        break;
                    case "qr2":
                        if (registro.getText().equals(fechaV2)) {
                            registro.setTextColor(Color.GREEN);
                            registro_pos.setTextColor(Color.GREEN);

                        } else {
                            registro.setTextColor(Color.WHITE);
                            registro_pos.setTextColor(Color.WHITE);
                        }

                        break;
                    case "qr3":
                        if (registro.getText().equals(fechaV3)) {
                            registro.setTextColor(Color.GREEN);
                            registro_pos.setTextColor(Color.GREEN);

                        } else {
                            registro.setTextColor(Color.WHITE);
                            registro_pos.setTextColor(Color.WHITE);

                        }
                        break;
                }

            }

            @NonNull
            @Override
            public ClasificacionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.activity_reciclercardview_clasificacion, viewGroup, false);

                return new ClasificacionViewHolder(v);
            }

            @Override
            public void onDataChanged() {

            }

        };

        rvClasificacion.setAdapter(adapter);
        adapter.startListening();

    }

    /**
     * Inicia la animación del sensor.
     *
     * @param distancia Distancia a la que se encuentra el código qr.
     */

    public void startAnimacionSensor(float distancia) {
        if (distancia > 100) {
            layoutSensor.setBackground(ContextCompat.getDrawable(JuegoEspecial.this, R.drawable.lista_gradientes_lejos));

        } else if ((distancia >= 50)) {
            layoutSensor.setBackground(ContextCompat.getDrawable(JuegoEspecial.this, R.drawable.lista_gradientes_medio));

        } else {
            layoutSensor.setBackground(ContextCompat.getDrawable(JuegoEspecial.this, R.drawable.lista_gradientes_cerca));
            Toast toast = Toast.makeText(JuegoEspecial.this, "¡Estás cerca del código!", Toast.LENGTH_LONG);
            toast.show();
        }

        animationDrawable = (AnimationDrawable) layoutSensor.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::stopAnimacionSensor, 16000);


    }

    /**
     * Detiene la animación del sensor.
     *
     */

    public void stopAnimacionSensor() {
        if (animationDrawable != null) {
            animationDrawable.stop();
            layoutSensor.setBackground(ContextCompat.getDrawable(JuegoEspecial.this, R.drawable.lista_gradientes_lejos));
        }

    }

    /**
     * Inicia Rotación visual del sensor.
     * @param c Cantidad de veces que se repite la rotación.
     */

    public void startRotacionSensor(int c) {
        float deg = buttonSensor.getRotation() + 360F;
        buttonSensor.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
        buttonSensor.animate().setDuration(4000);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            if (c > 0) {
                startRotacionSensor(c - 1);
            } else {
                buttonSensor.setEnabled(true);
            }

        }, 4000);

    }

    /**
     * Muestra un diálogo de validación de código.
     *
     * @param cantidad Cantidad de validaciones totales.
     */

    public void getDialogValidacion(int cantidad) {
        AlertDialog.Builder builder = new AlertDialog.Builder(JuegoEspecial.this);
        LayoutInflater li = LayoutInflater.from(JuegoEspecial.this);
        final View view = li.inflate(R.layout.dialog_juego, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        String dialog_m2;

        TextView dialog_juego_m2 = view.findViewById(R.id.dialog_juego_mensaje2);

        if(cantidad < 3) {
            dialog_m2 = "¡Sigue con el siguiente código!";
        } else {
            dialog_m2 = "¡Has completado con éxito el evento!";
        }

        dialog_juego_m2.setText(dialog_m2);

        dialog.show();

    }

    /**
     * Muestra un diálogo de error en la conexión.
     *
     */

    public void getDialogConexion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(JuegoEspecial.this);
        LayoutInflater li = LayoutInflater.from(JuegoEspecial.this);
        final View viewDialog = li.inflate(R.layout.dialog_conexion, null);
        builder.setView(viewDialog);

        AlertDialog dialog = builder.create();

        TextView textViewDialogConexion = viewDialog.findViewById(R.id.textViewDialogConexion);
        String st = "Comprueba tu conexión y vuelve a intentarlo";
        textViewDialogConexion.setText(st);

        dialog.show();

    }

}