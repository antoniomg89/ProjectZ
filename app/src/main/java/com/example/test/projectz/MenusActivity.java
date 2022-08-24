package com.example.test.projectz;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

/**
 * Clase que gestiona el menú de navegación de la aplicación.
 */

public class MenusActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private ValueEventListener badgesListener;
    private DatabaseReference referenceRecompensas;
    /**
     * Crea el menú e inicializa sus componentes.
     *
     */

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus);

        BottomNavigationView navigation = findViewById(R.id.navigation_menus);
        navigation.setItemIconTintList(null);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        DatabaseReference referenceDatos = FirebaseDatabase.getInstance().getReference().child("usuariodatos").child(user.getUid());
        DatabaseReference referenceFoto = FirebaseDatabase.getInstance().getReference().child("usuariofoto").child(user.getUid());
        DatabaseReference referenceBadgeF = FirebaseDatabase.getInstance().getReference().child("usuariosbadges").child(user.getUid());
        referenceRecompensas = FirebaseDatabase.getInstance().getReference().child("usuariorecompensas").child(user.getUid());

        // Comprobar si es la primera vez que entra un usuario para inicializar sus datos (nivel, experiencia, etc)
        referenceDatos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // El usuario no es nuevo
                if (snapshot.hasChild("participaciones")) {
                    referenceBadgeF.child("favoritos").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild("favoritos")) {
                                int num = Objects.requireNonNull(snapshot.getValue(FBbadgeFavoritos.class)).getTotal();

                                if (num > 0) {
                                    BottomNavigationView navmenu = findViewById(R.id.navigation_menus);
                                    BadgeDrawable badge = navmenu.getOrCreateBadge(R.id.navigation_favoritos);
                                    badge.setNumber(num);
                                    badge.setBadgeTextColor(Color.parseColor("#000000"));
                                    badge.setBackgroundColor(Color.parseColor("#6ff9ff"));
                                    badge.setVisible(true);
                                }

                            }

                            /*referenceFoto.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int intentos = Objects.requireNonNull(snapshot.getValue(FBfotoUsuario.class)).getIntentosFoto();

                                    if (intentos > 0) {
                                        BottomNavigationView navmenu = findViewById(R.id.navigation_menus);
                                        BadgeDrawable badge = navmenu.getOrCreateBadge(R.id.navigation_ajustes);
                                        badge.setBackgroundColor(Color.parseColor("#6ff9ff"));
                                        badge.setVisible(true);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });*/

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                } else {
                    // El usuario es nuevo
                    int nivel = 1;
                    int experiencia = 0;
                    int expsignivel = 100;
                    int participaciones = 0;
                    int intentos = 2;
                    String foto = "no actualizada";

                    FBdatosUsuario datos = new FBdatosUsuario(nivel, experiencia, expsignivel, participaciones,1);
                    FBfotoUsuario fotoU = new FBfotoUsuario(foto, intentos, "null");
                    FBbadgeFavoritos bf = new FBbadgeFavoritos(0);
                    FBusuarioRecompensas rec = new FBusuarioRecompensas("00000", "Regalo de bienvenida",0,50,false);

                    referenceBadgeF.child("favoritos").setValue(bf);
                    referenceDatos.setValue(datos).addOnCompleteListener(task -> referenceFoto.setValue(fotoU).addOnCompleteListener(task12 -> referenceRecompensas.child("00000").setValue(rec).addOnCompleteListener(task1 -> {
                        BadgeDrawable badge = navigation.getOrCreateBadge(R.id.navigation_ajustes);
                        badge.setNumber(1);
                        badge.setBadgeTextColor(Color.parseColor("#000000"));
                        badge.setBackgroundColor(Color.parseColor("#6ff9ff"));
                        badge.setVisible(true);
                    })));

                }

                // Petición de los permisos necesarios (localización y cámara).
                ActivityCompat.requestPermissions(MenusActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA},
                        1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setSelectedItemId(R.id.navigation_ofertas);

        /*if (savedInstanceState == null) {
            navigation.setSelectedItemId(R.id.navigation_especial);
            //Fragment fragment = new JuegoFragment();
            //cargarFragment(fragment);
        }*/

    }

    /**
     * Cuando se inicia la activity, se inicia la escucha de recompensas del usuario.
     */

    @Override
    public void onStart() {
        super.onStart();

        getRecompensas();
        referenceRecompensas.addValueEventListener(badgesListener);
    }

    /**
     * Cuando se detiene la activity, se detiene la escucha de recompensas del usuario.
     */

    @Override
    public void onStop() {
        super.onStop();

        referenceRecompensas.removeEventListener(badgesListener);
    }

    /**
     * Salir de la aplicación al presionar el botón retroceder.
     *
     */

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    /**
     * Obtiene los datos del usuario y actualiza la interfaz.
     *
     */

    public void getRecompensas() {
        badgesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    int n = 0;
                    BottomNavigationView navmenu = findViewById(R.id.navigation_menus);
                    BadgeDrawable badge = navmenu.getOrCreateBadge(R.id.navigation_ajustes);

                    for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        boolean reclamada = Objects.requireNonNull(dataSnapshot.getValue(FBusuarioRecompensas.class)).getReclamada();

                        if (!reclamada) {
                            ++n;

                        }
                    }

                    if (n>0) {
                        badge.setNumber(n);
                        badge.setBadgeTextColor(Color.parseColor("#000000"));
                        badge.setBackgroundColor(Color.parseColor("#6ff9ff"));
                        badge.setVisible(true);
                    } else {
                        badge.setVisible(false);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

    }

    /**
     * Carga el fragmento que se va a visualizar.
     *
     * @param fragment Fragmento.
     * @return Fragmento.
     */

    private boolean cargarFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layout_menus, fragment)
                    .commit();

            return true;
        }
        return false;
    }

    /**
     * Obtiene la opción que se ha presionado en el menú de navegación
     *
     * @param menuItem Opción seleccionada del menú.
     * @return Fragmento seleccionado.
     */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        if (menuItem.getItemId() == R.id.navigation_ofertas) {
            fragment = new RutasFragment();

        } else if (menuItem.getItemId() == R.id.navigation_favoritos) {
            fragment = new FavoritosFragment();

        } else if (menuItem.getItemId() == R.id.navigation_especial) {
            fragment = new JuegoFragment();

        } else if (menuItem.getItemId() == R.id.navigation_ajustes) {
            fragment = new AjustesFragment();

        }

        return cargarFragment(fragment);
    }

}
