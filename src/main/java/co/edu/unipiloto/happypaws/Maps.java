package co.edu.unipiloto.happypaws;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import models.Recorrido;
import network.RecorridoService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker mascotaMarker;
    private Polyline rutaPolyline;
    private Handler handler = new Handler();
    private RecorridoService recorridoService;
    private boolean primeraVez = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_maps);

        recorridoService = Retro.getClient().create(RecorridoService.class);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private final Runnable simulationRunnable = new Runnable() {
        @Override
        public void run() {

            Call<Recorrido> call = recorridoService.getLast();
            call.enqueue(new Callback<Recorrido>() {
                @Override
                public void onResponse(Call<Recorrido> call, Response<Recorrido> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Recorrido rec = response.body();
                        LatLng nuevaPos = new LatLng(rec.getLat(), rec.getLon());

                        if (primeraVez) {

                            mascotaMarker = mMap.addMarker(new MarkerOptions()
                                    .position(nuevaPos)
                                    .title("Mascota")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                            rutaPolyline = mMap.addPolyline(new PolylineOptions()
                                    .color(Color.BLUE)
                                    .width(10));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nuevaPos, 16));
                            primeraVez = false;
                        } else {

                            mascotaMarker.setPosition(nuevaPos);
                            List<LatLng> pts = new ArrayList<>(rutaPolyline.getPoints());
                            pts.add(nuevaPos);
                            rutaPolyline.setPoints(pts);
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(nuevaPos));
                        }

                        //mascotaMarker.setPosition(nuevaPos);
                        //List<LatLng> puntos = new ArrayList<>(rutaPolyline.getPoints());
                        //puntos.add(nuevaPos);
                        //rutaPolyline.setPoints(puntos);
                        //mMap.animateCamera(CameraUpdateFactory.newLatLng(nuevaPos));
                    }
                }

                @Override
                public void onFailure(Call<Recorrido> call, Throwable t) {
                    Log.e("MapsActivity", "Error al obtener ubicación", t);
                }
            });
            //Acá esta el delay de 3s para volver a ejecutar el metodo
            handler.postDelayed(this, 3000);
        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        handler.post(simulationRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(simulationRunnable);
    }

    //private int index = 0;

    //private List<LatLng> rutaSimulada;

    /*
    private List<List<LatLng>> rutasBogota = Arrays.asList(
            Arrays.asList( // Ruta 1: Chapiyork
                    new LatLng(4.648620, -74.064710),
                    new LatLng(4.650320, -74.062810),
                    new LatLng(4.652000, -74.061500),
                    new LatLng(4.653600, -74.060200),
                    new LatLng(4.655200, -74.059000),
                    new LatLng(4.657000, -74.058000),
                    new LatLng(4.658500, -74.056800),
                    new LatLng(4.660000, -74.055600),
                    new LatLng(4.661500, -74.054400),
                    new LatLng(4.663000, -74.053200),
                    new LatLng(4.664500, -74.052000),
                    new LatLng(4.666000, -74.051000),
                    new LatLng(4.668000, -74.050000)
            ),
            Arrays.asList( // Ruta 2: La Candelaria
                    new LatLng(4.598080, -74.076043),
                    new LatLng(4.599500, -74.075100),
                    new LatLng(4.600800, -74.074200),
                    new LatLng(4.602100, -74.073300),
                    new LatLng(4.603400, -74.072400),
                    new LatLng(4.605000, -74.071500),
                    new LatLng(4.606500, -74.070400),
                    new LatLng(4.608000, -74.069300),
                    new LatLng(4.609500, -74.068200),
                    new LatLng(4.611000, -74.067100),
                    new LatLng(4.612500, -74.066000),
                    new LatLng(4.614000, -74.065000),
                    new LatLng(4.615500, -74.064000)
            ),
            Arrays.asList( // Ruta 3: Usaquén
                    new LatLng(4.693000, -74.030800),
                    new LatLng(4.694500, -74.029900),
                    new LatLng(4.696000, -74.029000),
                    new LatLng(4.697500, -74.028100),
                    new LatLng(4.698800, -74.027500),
                    new LatLng(4.700200, -74.027000),
                    new LatLng(4.701800, -74.026500),
                    new LatLng(4.703400, -74.026000),
                    new LatLng(4.705000, -74.025500),
                    new LatLng(4.706500, -74.025000),
                    new LatLng(4.708000, -74.024500),
                    new LatLng(4.709500, -74.024000),
                    new LatLng(4.711000, -74.023500)
            ),
            Arrays.asList( // Ruta 4: Suba
                    new LatLng(4.742500, -74.098000),
                    new LatLng(4.744000, -74.096800),
                    new LatLng(4.745500, -74.095600),
                    new LatLng(4.747000, -74.094400),
                    new LatLng(4.748500, -74.093200),
                    new LatLng(4.750000, -74.092000),
                    new LatLng(4.751500, -74.090800),
                    new LatLng(4.753000, -74.089600),
                    new LatLng(4.754500, -74.088400),
                    new LatLng(4.756000, -74.087200),
                    new LatLng(4.757500, -74.086000),
                    new LatLng(4.759000, -74.085000),
                    new LatLng(4.760500, -74.084000)  
            )
    );
     */

//    private List<List<LatLng>> rutasBogota = Arrays.asList(
//            Arrays.asList( // Ruta 1: Chapiyork
//                    new LatLng(4.658572, -74.058123),
//                    new LatLng(4.658400, -74.058000),
//                    new LatLng(4.658250, -74.057850),
//                    new LatLng(4.658100, -74.057700),
//                    new LatLng(4.657950, -74.057550),
//                    new LatLng(4.657800, -74.057400),
//                    new LatLng(4.657650, -74.057250),
//                    new LatLng(4.657500, -74.057100)
//            ),
//            Arrays.asList( // Ruta 2: Candelaria
//                    new LatLng(4.598056, -74.075833),
//                    new LatLng(4.598300, -74.075600),
//                    new LatLng(4.598550, -74.075370),
//                    new LatLng(4.598800, -74.075140),
//                    new LatLng(4.599050, -74.074910),
//                    new LatLng(4.599300, -74.074680),
//                    new LatLng(4.599550, -74.074450),
//                    new LatLng(4.599800, -74.074220)
//            ),
//            Arrays.asList( // Ruta 3: Usaquen
//                    new LatLng(4.699567, -74.044321),
//                    new LatLng(4.699450, -74.044200),
//                    new LatLng(4.699333, -74.044079),
//                    new LatLng(4.699216, -74.043958),
//                    new LatLng(4.699099, -74.043837),
//                    new LatLng(4.698982, -74.043716),
//                    new LatLng(4.698865, -74.043595),
//                    new LatLng(4.698748, -74.043474)
//            ),
//            Arrays.asList( // Ruta 4: Suba
//                    new LatLng(4.710678, -74.091234),
//                    new LatLng(4.710500, -74.091100),
//                    new LatLng(4.710322, -74.090966),
//                    new LatLng(4.710144, -74.090832),
//                    new LatLng(4.709966, -74.090698),
//                    new LatLng(4.709788, -74.090564),
//                    new LatLng(4.709610, -74.090430),
//                    new LatLng(4.709432, -74.090296)
//            )
//    );
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        seleccionarRutaAleatoria();
//
//        mascotaMarker = mMap.addMarker(new MarkerOptions()
//                .position(rutaSimulada.get(0))
//                .title("Mascota")
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//
//        rutaPolyline = mMap.addPolyline(new PolylineOptions()
//                .color(Color.BLUE)
//                .width(10));
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rutaSimulada.get(0), 16));
//
//        iniciarSimulacion();
//    }
//
//    private void seleccionarRutaAleatoria() {
//        Random random = new Random();
//        int rutaIndex = random.nextInt(rutasBogota.size());
//        rutaSimulada = rutasBogota.get(rutaIndex);
//    }
//    private void iniciarSimulacion() {
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (index < rutaSimulada.size()) {
//                    LatLng nuevaPosicion = rutaSimulada.get(index);
//
//                    mascotaMarker.setPosition(nuevaPosicion);
//
//                    List<LatLng> puntos = new ArrayList<>(rutaPolyline.getPoints());
//                    puntos.add(nuevaPosicion);
//                    rutaPolyline.setPoints(puntos);
//
//                    mMap.animateCamera(CameraUpdateFactory.newLatLng(nuevaPosicion));
//
//                    index++;
//                    handler.postDelayed(this, 2000); // Intervalo de 2 segundos
//                }
//            }
//        }, 2000);
//    }
}