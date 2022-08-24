package com.example.test.projectz;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Clase que gestiona la vista del mapa con la localización de cada empresa de la ruta.
 */

public class MapsRutaActivity extends FragmentActivity implements OnMapReadyCallback {

    private String empresa_map;
    private LatLng coordenadas_conv;
    private LatLngBounds latLngBounds;

    /**
     * Crea la activity e inicializa sus componentes.
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtiene el SupportMapFragment e informa cuando el mapa esta listo para ser usado.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        Bundle coordenadas_bundle = getIntent().getExtras();
        String coordenadas_map = coordenadas_bundle.getString("crd");
        empresa_map = coordenadas_bundle.getString("emp");
        String [] ne = coordenadas_bundle.getString("ne").split(",");
        String [] sw = coordenadas_bundle.getString("sw").split(",");

        String[] map_lat_long = coordenadas_map.split(",");
        double latitud = Double.parseDouble(map_lat_long[0]);
        double longitud = Double.parseDouble(map_lat_long[1]);

        double latne = Double.parseDouble(ne[0]);
        double longne = Double.parseDouble(ne[1]);
        double latsw = Double.parseDouble(sw[0]);
        double longsw = Double.parseDouble(sw[1]);

        coordenadas_conv = new LatLng(latitud, longitud);

        latLngBounds = new LatLngBounds(
                new LatLng(latsw,longsw),
                new LatLng(latne,longne)
        );
    }

    /**
     * Ejecuta la vista del mapa.
     *
     * @param googleMap Mapa.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.stylejsonrutas));

            if (!success) {
                System.out.println("Error aplicando estilo");
            }
        } catch (Resources.NotFoundException e) {
            //Error
            System.out.println("Error: " + e);
        }

        Marker marcadorOferta = googleMap.addMarker(new MarkerOptions()
                .position(coordenadas_conv)
                .title(empresa_map)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordenadas_conv));
        googleMap.setLatLngBoundsForCameraTarget(latLngBounds);
        googleMap.setMinZoomPreference(15);

    }
}

