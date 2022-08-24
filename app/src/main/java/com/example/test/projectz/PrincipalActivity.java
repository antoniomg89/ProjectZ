package com.example.test.projectz;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Clase principal que gestiona el registro e inicio de sesión del usuario.
 */

public class PrincipalActivity extends AppCompatActivity {
    //private static final int RC_SIGN_IN = 123;

    /**
     * Crea la activity e inicia sus respectivos componentes.
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createSignInIntent();
    }

    /**
     * Al iniciar la activity se comprueba si el usuario está o no conectado.
     */

    @Override
    public void onStart() {
        super.onStart();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            //Establecer persistencia de datos
            //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            Menus();
        }
    }

    /**
     * Al continuar la activity se comprueba si el usuario está o no conectado.
     */

    @Override
    public void onResume() {
        super.onResume();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Menus();
        }
    }

    /**
     * Comprueba si se ha realizado el inicio/registro de sesión de forma correcta.
     *
     * @param requestCode Código petición.
     * @param resultCode Código resultado.
     * @param data Datos obtenidos.
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("RequestCode: " + requestCode);

        if (requestCode == 123) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                Menus();

            } else {
                if (response == null) {
                    // Botón retroceder presionado
                    return;
                }

                Objects.requireNonNull(response.getError());

            }
        }
    }

    /**
     * Crea el intent de registro o inicio de sesión.
     */

    public void createSignInIntent() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
                //new AuthUI.IdpConfig.FacebookBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder().setIsSmartLockEnabled(false)
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.loginimage)
                        .setTheme(R.style.TemaGris)
                        .build(), 123);
    }

    /**
     * Muestra el menú de navegación.
     */

    public void Menus(){
        //FirebaseAuth auth = FirebaseAuth.getInstance();
        Intent intent = new Intent(this, MenusActivity.class);
        startActivity(intent);
        finish();
    }
}
