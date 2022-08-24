package com.example.test.projectz;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Clase que gestiona la vista del mapa con la localización del código QR.
 */

public class MapsJuegoActivity extends FragmentActivity implements OnMapReadyCallback {

    private String empresa_map;
    private LatLng coordenadas_conv;
    private Boolean mostrarMarcador;
    private boolean area_presionada = false;
    private LatLngBounds latLngBounds;

    /**
     * Crea la activity e inicializa sus componentes.
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_juego);

        // Obtiene el SupportMapFragment e informa cuando el mapa esta listo para ser usado.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        Bundle coordenadas_bundle = getIntent().getExtras();
        String coordenadasc_map = coordenadas_bundle.getString("crdcJ");
        String coordenadas_map = coordenadas_bundle.getString("crdJ");
        String[] ne = coordenadas_bundle.getString("ne").split(",");
        String[] sw = coordenadas_bundle.getString("sw").split(",");
        empresa_map = coordenadas_bundle.getString("empJ");

        String[] map_lat_long;
        if (coordenadasc_map != null) {
            System.out.println("Maps -> Mostrar area.");
            mostrarMarcador = false;
            map_lat_long = coordenadasc_map.split(",");
        } else {
            System.out.println("Maps -> Mostrar marcador.");
            mostrarMarcador = true;
            map_lat_long = coordenadas_map.split(",");
        }

        double latitud = Double.parseDouble(map_lat_long[0]);
        double longitud = Double.parseDouble(map_lat_long[1]);

        double latne = Double.parseDouble(ne[0]);
        double longne = Double.parseDouble(ne[1]);
        double latsw = Double.parseDouble(sw[0]);
        double longsw = Double.parseDouble(sw[1]);

        latLngBounds = new LatLngBounds(
                new LatLng(latsw, longsw),
                new LatLng(latne, longne)
        );

        coordenadas_conv = new LatLng(latitud, longitud);
    }

    /**
     * Ejecuta la vista del mapa.
     *
     * @param googleMap Mapa.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.stylejsonjuego));

            if (!success) {
                System.out.println("Error aplicando estilo");
            }
        } catch (Resources.NotFoundException e) {
            System.out.println("Excepción: " + e);
        }

        if (mostrarMarcador) {
            Marker marcadorOferta = googleMap.addMarker(new MarkerOptions()
                    .position(coordenadas_conv)
                    .title(empresa_map)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        } else {
            Circle area = googleMap.addCircle(new CircleOptions()
                    .center(coordenadas_conv)
                    .radius(500)
                    .strokeWidth(9)
                    .strokeColor(Color.argb(255, 245, 164, 112))
                    .fillColor(Color.argb(128, 245, 210, 87))
                    .clickable(true));

            googleMap.setOnCircleClickListener(circle -> {
                if (area_presionada) {
                    area_presionada = false;
                    circle.setStrokeColor(Color.argb(255, 245, 164, 112));
                } else {
                    area_presionada = true;
                    circle.setStrokeColor(Color.RED);
                }
            });
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Los permisos no están activados.");
            return;
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordenadas_conv));
        googleMap.setMinZoomPreference(15);
        googleMap.setLatLngBoundsForCameraTarget(latLngBounds);
        googleMap.setMyLocationEnabled(true);

        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(@NonNull Location location) {
                LatLng posicion = new LatLng(location.getLatitude(), location.getLongitude());

                if (latLngBounds.contains(posicion)) {
                    System.out.println("Estás dentro de los márgenes");
                    if (ActivityCompat.checkSelfPermission(MapsJuegoActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsJuegoActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    if(!googleMap.isMyLocationEnabled()) {
                        googleMap.setMyLocationEnabled(true);
                    }

                } else {
                    System.out.println("Estás fuera de los márgenes");

                    if(googleMap.isMyLocationEnabled()) {
                        googleMap.setMyLocationEnabled(false);
                    }

                }

            }
        });

    }
}

