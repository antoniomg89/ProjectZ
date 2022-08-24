package com.example.test.projectz;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Fragmento que contiene información asociada al usuario.
 */

public class AjustesFragment extends Fragment implements View.OnClickListener{
    private ImageButton cambiarFoto;
    private ImageView ajustes_nivelusuario2, ajustes_nivelusuario3, ajustes_nivelusuario4, ajustes_nivelusuario5;
    private DatabaseReference databaseReferenceusuariodatos;
    private NumberProgressBar numberprogressBarAjustes;
    private FirebaseUser mFirebaseUser;
    private CircleImageView fotoUsuario;
    private TextView textviewParticipaciones;
    private final String urlAPIFotos = "https://picsum.photos/300";
    private String srcFoto;
    private String srcFecha;
    private ValueEventListener datosListener;
    private android.app.AlertDialog dialog;

    /**
     * Crea la vista del fragmento con sus componentes y los inicializa.
     *
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ajustes,container,false);

        //pref = this.getContext().getSharedPreferences("preferencias", 0);

        // Obtener el usuario actual
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        ajustes_nivelusuario2 = view.findViewById(R.id.ajustes_nivelusuario2);
        ajustes_nivelusuario3 = view.findViewById(R.id.ajustes_nivelusuario3);
        ajustes_nivelusuario4 = view.findViewById(R.id.ajustes_nivelusuario4);
        ajustes_nivelusuario5 = view.findViewById(R.id.ajustes_nivelusuario5);
        TextView nombreusuario = view.findViewById(R.id.ajustes_nombreusuario);
        fotoUsuario = view.findViewById(R.id.imageViewAjustes);
        cambiarFoto = view.findViewById(R.id.cambiarFotoAjustes);
        numberprogressBarAjustes = view.findViewById(R.id.numberprogressBarAjustes);
        textviewParticipaciones = view.findViewById(R.id.ajustes_cantparticipaciones);

        databaseReferenceusuariodatos = FirebaseDatabase.getInstance().getReference().child("usuariodatos").child(mFirebaseUser.getUid());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater li = LayoutInflater.from(getActivity());
        final View viewdialog = li.inflate(R.layout.dialog_foto, null);
        builder.setView(viewdialog);
        dialog = builder.create();

        //salir.setOnClickListener(this);
        cambiarFoto.setOnClickListener(this);
        //historialUsuario.setOnClickListener(this);
        nombreusuario.setText(mFirebaseUser.getDisplayName());

        TabLayout tabLayout = view.findViewById(R.id.fragmentajustes_tab);
        ViewPager2 viewPager2 = view.findViewById(R.id.fragmentajustes_viewpager);

        tabLayout.addTab(tabLayout.newTab().setIcon(ContextCompat.getDrawable(requireContext(),R.drawable.tab_noticias_selector)));
        tabLayout.addTab(tabLayout.newTab().setIcon(ContextCompat.getDrawable(requireContext(),R.drawable.tab_historial_selector)));

        FragmentManager fragmentManager = getChildFragmentManager();
        PagerAdapter pagerAdapter = new PagerAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(pagerAdapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition(),true);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });



        //getDatosUsuario();
        getIntentosFotoUsurio();

        return view;
    }

    /**
     * Cuando se inicia el fragment, se inicia la escucha de datos del usuario.
     */

    @Override
    public void onStart() {
        super.onStart();

        getDatosUsuario();
        databaseReferenceusuariodatos.addValueEventListener(datosListener);
    }

    /**
     * Cuando se detiene el fragment, se detiene la escucha de datos del usuario.
     */

    @Override
    public void onStop() {
        super.onStop();

        databaseReferenceusuariodatos.removeEventListener(datosListener);
    }

    /**
     * Obtiene los intentos de cambio de foto de usuario y actualiza la interfaz.
     *
     */

    public void getIntentosFotoUsurio() {
        DatabaseReference foto = FirebaseDatabase.getInstance().getReference().child("usuariofoto").child(mFirebaseUser.getUid());

        foto.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String url = Objects.requireNonNull(snapshot.getValue(FBfotoUsuario.class)).getFotourl();
                int intentos = Objects.requireNonNull(snapshot.getValue(FBfotoUsuario.class)).getIntentosFoto();

                if (!url.equals("null")) {
                    Picasso.get().load(url).into(fotoUsuario, new Callback() {
                        @Override
                        public void onSuccess() {
                            System.out.println("Imagen cargada con éxito");
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                } else {
                    cambiarFoto.setVisibility(View.VISIBLE);
                    Picasso.get().load(R.mipmap.ic_launcher).into(fotoUsuario);
                }

                if (intentos > 0) {
                    cambiarFoto.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Establece el progreso del nivel actual del usuario.
     *
     * @param act Progreso.
     */

    public void setNumberProgressVar(int act) {
        Timer timer = new Timer();
        Activity activity = getActivity();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(isAdded() && activity != null) {
                    activity.runOnUiThread(() -> {
                        if (numberprogressBarAjustes.getProgress() < act) {
                            numberprogressBarAjustes.incrementProgressBy(1);
                        } else {
                            timer.cancel();
                            timer.purge();
                        }
                    });

                }
            }
        }, 0, 10);
    }

    /**
     * Obtiene los datos del usuario y actualiza la interfaz.
     *
     */

    public void getDatosUsuario() {
        datosListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int usuarioParticipaciones = Objects.requireNonNull(snapshot.getValue(FBdatosUsuario.class)).getParticipaciones();
                int usuarioNivel = Objects.requireNonNull(snapshot.getValue(FBdatosUsuario.class)).getNivel();
                int usuarioExperiencia = Objects.requireNonNull(snapshot.getValue(FBdatosUsuario.class)).getExperiencia();
                int usuarioExperienciaNivel = Objects.requireNonNull(snapshot.getValue(FBdatosUsuario.class)).getExpsignivel();
                int progresoNivel = (usuarioExperiencia*100)/(usuarioExperiencia+usuarioExperienciaNivel);

                if (usuarioNivel >= 2) {
                    ajustes_nivelusuario2.setVisibility(View.VISIBLE);
                }

                if (usuarioNivel >= 3) {
                    ajustes_nivelusuario3.setVisibility(View.VISIBLE);
                }

                if (usuarioNivel >= 4) {
                    ajustes_nivelusuario4.setVisibility(View.VISIBLE);
                }
                if (usuarioNivel >= 5) {
                    ajustes_nivelusuario5.setVisibility(View.VISIBLE);
                }

                textviewParticipaciones.setText(String.valueOf(usuarioParticipaciones));
                String colorNivel = "";

                if (usuarioNivel == 1) {
                    colorNivel = "#00EC76";
                } else if (usuarioNivel == 2) {
                    colorNivel = "#A8A81A";
                }

                setMarcoFoto(colorNivel);

                //while(!estadoprogressbar) {
                    //System.out.println("Intentar iniciar barra");
                    setNumberProgressVar(progresoNivel);
               //}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

    }



    /**
     * Establece las acciones a realizar al hacer click en alguno de los componentes del fragmento.
     *
     * @param v vista.
     */

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cambiarFotoAjustes) {
            fotoUsuario.setVisibility(View.INVISIBLE);
            dialogoFoto(true, false);
            getFotoAPI();
        }
    }

    /**
     * Establece la url de la foto de perfil.
     *
     * @param srcf url de la foto de perfil.
     */

    public void setSrcFoto(String srcf) {
        this.srcFoto = srcf;
    }

    /**
     * Obtiene la url de la foto de perfil.
     *
     * @return url de la foto de perfil.
     */

    public String getSrcFoto() {
        return this.srcFoto;
    }

    /**
     * Establece la fecha actual.
     *
     * @param srcf url de la foto de perfil.
     */

    public void setSrcFecha(String srcf) {
        this.srcFecha = srcf;
    }

    /**
     * Obtiene la fecha actual.
     *
     * @return url de la foto de perfil.
     */

    public String getSrcFecha() {
        return this.srcFecha;
    }

    /**
     * Obtiene la la imagen de la api de fotos
     *
     * @see <a href="https://picsum.photos/">Picsum</a>
     */

    public void getFotoAPI() {
        new Thread(() -> {
            try {
                String foto = Jsoup.connect(urlAPIFotos).ignoreContentType(true).get().baseUri();
                setSrcFoto(foto);
                //srcFoto = foto;
                //System.out.println("Foto obtenida: " + srcFoto);
                actualizarFotoPerfil();

            } catch (IOException e) {
                Toast toast = Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT);
                toast.show();
                dialogoFoto(false, true);
            }
        }).start();
    }

    /**
     * Actualiza la imagen de perfil obtenida de la API de imágenes.
     *
     */

    public void actualizarFotoPerfil() {
        String fcr = "https://przfe.herokuapp.com/getFechaCompleta";

        // Petición de la fecha real.
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, fcr,
                response -> {
                    setSrcFecha(response.substring(0,10));
                    Picasso.get().load(getSrcFoto()).into(fotoUsuario, new Callback() {
                        @Override
                        public void onSuccess() {
                            almacenarFotoPerfil();
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast toast = Toast.makeText(getContext(), "Error cargando imagen", Toast.LENGTH_LONG);
                            toast.show();
                            dialogoFoto(false, true);
                        }
                    });

                }, error -> {
                    Toast toast = Toast.makeText(getContext(), "Error en la conexión", Toast.LENGTH_LONG);
                    toast.show();
                    dialogoFoto(false, true);
                });

        queue.add(stringRequest);
    }

    /**
     * Controla el estado del diálogo que permite cambiar la foto del perfil del usuario.
     *
     * @param iniciar El diálogo se va a iniciar.
     * @param terminar El diálogo se va a terminar.
     */

    public void dialogoFoto(boolean iniciar, boolean terminar) {
        if (iniciar) {
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

        } else if (terminar) {
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    /**
     * Guarda la foto actualizada del usuario en firebase.
     */

    public void almacenarFotoPerfil() {
        System.out.println("Almacenar foto");
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference fotoperfilRef = mStorageRef.child("fotosPerfil" + "/" + mFirebaseUser.getUid());

        fotoperfilRef.listAll()
                .addOnSuccessListener(listResult -> {
                    for(StorageReference id : listResult.getItems()) {
                        System.out.println("Borrar : " + id.getName());

                        id.delete()
                                .addOnSuccessListener(unused -> {
                                    UUID rng = UUID.randomUUID();
                                    String random32 = rng.toString().replaceAll("-","");
                                    String rng1 = random32.substring(0,10);
                                    StorageReference fotoperfilRefNueva = mStorageRef.child("fotosPerfil" + "/" + mFirebaseUser.getUid() + "/" + rng1 + ".jpg");

                                    fotoUsuario.setDrawingCacheEnabled(true);
                                    fotoUsuario.buildDrawingCache();
                                    Bitmap bitmap = ((BitmapDrawable) fotoUsuario.getDrawable()).getBitmap();
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] data = baos.toByteArray();

                                    //UploadTask uploadTask = fotoperfilRef.putBytes(data);
                                    UploadTask uploadTask = fotoperfilRefNueva.putBytes(data);
                                    uploadTask.addOnFailureListener(exception -> {
                                        Toast toast = Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG);
                                        toast.show();
                                        dialogoFoto(false, true);
                                    }).addOnSuccessListener(taskSnapshot -> {
                                        fotoUsuario.setVisibility(View.VISIBLE);
                                        setInfoActualizacionFoto(getSrcFecha(),rng1);
                                        dialogoFoto(false, true);

                                        Toast toast = Toast.makeText(getContext(), "Foto actualizada", Toast.LENGTH_SHORT);
                                        toast.show();
                                    });

                                })
                                .addOnFailureListener(e -> {
                                    Toast toast = Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG);
                                    toast.show();
                                    dialogoFoto(false, true);
                                });


                    }
                })
                .addOnFailureListener(e -> {
                    Toast toast = Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG);
                    toast.show();
                    dialogoFoto(false, true);
                });

    }

    /**
     * Reduce el número de intentos disponible para cambiar la foto de perfil.
     *
     * @param fecha Fecha en la que se produce la actualización de la foto.
     * @param filename Nombre de archivo.
     */

    public void setInfoActualizacionFoto(String fecha, String filename) {
        DatabaseReference referenceFoto = FirebaseDatabase.getInstance().getReference().child("usuariofoto").child(mFirebaseUser.getUid());
        String url = "https://storage.googleapis.com/projectz-d7419.appspot.com/fotosPerfil/" + mFirebaseUser.getUid() + "/" + filename + ".jpg";

        referenceFoto.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int intentos = Objects.requireNonNull(snapshot.getValue(FBfotoUsuario.class)).getIntentosFoto();

                FBfotoUsuario ft = new FBfotoUsuario(fecha, intentos-1, url);

                if (intentos-1 == 0) {
                    cambiarFoto.setVisibility(View.GONE);

                    /*BottomNavigationView navmenu = requireActivity().findViewById(R.id.navigation_menus);
                    BadgeDrawable badge = navmenu.getOrCreateBadge(R.id.navigation_ajustes);
                    badge.setVisible(false);*/
                }

                referenceFoto.setValue(ft);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    /**
     * Establece el marco de la foto de perfil.
     *
     * @param color Nivel del usuario.
     */

    public void setMarcoFoto(String color) {
        fotoUsuario.setBorderColor(Color.parseColor(color));
        fotoUsuario.setBorderWidth(10);
    }

}
