package com.example.test.projectz;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.numberprogressbar.NumberProgressBar;

import java.text.Normalizer;

/**
 * Clase que proporciona funcionalidades globales.
 */

public class FuncionesGlobales {
    public FuncionesGlobales() {}

    /**
     *  Elimina los acentos y borra las mayúsculas de una localidad o ciudad.
     *
     * @return Localidad u oferta modificada.
     */

    public String getCiuloc(String cd){
        cd = Normalizer.normalize(cd, Normalizer.Form.NFD);
        cd = cd.replaceAll("[^\\p{ASCII}]", "");
        cd = cd.toLowerCase();

        return cd;
    }

    /**
     * Realiza una animación sobre una vista determinada.
     *
     * @param activity Activity
     * @param view Vista de la activity
     * @param i Animación a realizar
     */

    public void startViewAnimation(Activity activity, View view, int i) {
        if(i == 0) {
            YoYo.with(Techniques.RubberBand)
                    .duration(700)
                    .playOn(activity.findViewById(view.getId()));
        } else if(i == 1) {
            YoYo.with(Techniques.StandUp)
                    .duration(2500)
                    .playOn(activity.findViewById(view.getId()));
        } else if(i == 2) {
            YoYo.with(Techniques.FadeIn)
                    .duration(2000)
                    .playOn(activity.findViewById(view.getId()));
        } else if(i == 3) {
            YoYo.with(Techniques.Pulse)
                    .duration(2000)
                    .playOn(activity.findViewById(view.getId()));
        } else if(i == 4) {
            YoYo.with(Techniques.FadeOut)
                    .duration(500)
                    .playOn(activity.findViewById(view.getId()));
        } else if(i == 5) {
            YoYo.with(Techniques.FadeIn)
                    .duration(500)
                    .playOn(activity.findViewById(view.getId()));
        }

    }


    /**
     * Comprueba el tipo de conexión.
     *
     * @param context Contexto.
     *
     */

    public int getTipoConexion(Context context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return 1;
        }

        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkType();
    }

    /**
     * Comprueba el tipo de conexión móvil.
     *
     * @param tipo Tipo.
     *
     */

    public int getTipoConexionMovil(int tipo) {
        switch (tipo) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return 1;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return 2;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return 3;
            default:
                return 0;
        }
    }

    /**
     * Comprueba el estado de conexión a internet.
     *
     * @param context Contexto.
     *
     */

    public String checkConexionTipo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            String mensaje = "null";

            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                System.out.println("Conexión Tipo WIFI");
                WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                int niveles = 5;
                WifiInfo wi = wm.getConnectionInfo();
                int nivel = WifiManager.calculateSignalLevel(wi.getRssi(), niveles);

                if (nivel == 2) {
                    mensaje =  "WIFI Baja";
                } else if (nivel == 3) {
                    mensaje = "WIFI Media";
                } else if (nivel == 4) {
                    mensaje = "WIFI Buena";
                } else if (nivel == 5) {
                    mensaje = "WIFI Excelente";
                }

                return mensaje;


            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                System.out.println("Conexión Tipo Red móvil");
                int network = getTipoConexionMovil(getTipoConexion(context));

                if (network == 1) {
                    mensaje = "Móvil Baja";
                } else if (network == 2) {
                    mensaje = "Móvil Media";
                } else if (network == 3) {
                    mensaje = "Móvil Buena";
                }

                return mensaje;

            } else {
                return "null";
            }
        } else {
            return "null";
        }

    }

}
