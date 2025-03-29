package co.edu.unipiloto.happypaws;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

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

import models.Pet;
import network.PetService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private EditText petId;
    private Button btnSearchPet;
    private FrameLayout mapContainer;

    private PetService petService;
    private GoogleMap mMap;
    private Marker mascotaMarker;
    private Polyline rutaPolyline;
    private Handler handler = new Handler();
    private int index = 0;

    private List<LatLng> rutaSimulada;

    private List<List<LatLng>> rutasBogota = Arrays.asList(
            Arrays.asList( // Ruta 1: Chapiyork
                    new LatLng(4.658572, -74.058123),
                    new LatLng(4.658400, -74.058000),
                    new LatLng(4.658250, -74.057850),
                    new LatLng(4.658100, -74.057700),
                    new LatLng(4.657950, -74.057550),
                    new LatLng(4.657800, -74.057400),
                    new LatLng(4.657650, -74.057250),
                    new LatLng(4.657500, -74.057100)
            ),
            Arrays.asList( // Ruta 2: Candelaria
                    new LatLng(4.598056, -74.075833),
                    new LatLng(4.598300, -74.075600),
                    new LatLng(4.598550, -74.075370),
                    new LatLng(4.598800, -74.075140),
                    new LatLng(4.599050, -74.074910),
                    new LatLng(4.599300, -74.074680),
                    new LatLng(4.599550, -74.074450),
                    new LatLng(4.599800, -74.074220)
            ),
            Arrays.asList( // Ruta 3: Usaquen
                    new LatLng(4.699567, -74.044321),
                    new LatLng(4.699450, -74.044200),
                    new LatLng(4.699333, -74.044079),
                    new LatLng(4.699216, -74.043958),
                    new LatLng(4.699099, -74.043837),
                    new LatLng(4.698982, -74.043716),
                    new LatLng(4.698865, -74.043595),
                    new LatLng(4.698748, -74.043474)
            ),
            Arrays.asList( // Ruta 4: Suba
                    new LatLng(4.710678, -74.091234),
                    new LatLng(4.710500, -74.091100),
                    new LatLng(4.710322, -74.090966),
                    new LatLng(4.710144, -74.090832),
                    new LatLng(4.709966, -74.090698),
                    new LatLng(4.709788, -74.090564),
                    new LatLng(4.709610, -74.090430),
                    new LatLng(4.709432, -74.090296)
            )
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        petId = findViewById(R.id.petIdM);
        btnSearchPet = findViewById(R.id.search_pet);
        mapContainer = findViewById(R.id.map_container);
        petService = Retro.getClient().create(PetService.class);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(this, "Error al cargar el mapa", Toast.LENGTH_SHORT).show();
        }

        //mapFragment.getMapAsync(this);
        btnSearchPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String petIdStr = petId.getText().toString().trim();
                if (!petIdStr.isEmpty()) {
                    trackPet(Integer.parseInt(petIdStr));
                } else {
                    Toast.makeText(Maps.this, "Ingrese un ID de mascota", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        seleccionarRutaAleatoria();

        mascotaMarker = mMap.addMarker(new MarkerOptions()
                .position(rutaSimulada.get(0))
                .title("Mascota")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        rutaPolyline = mMap.addPolyline(new PolylineOptions()
                .color(Color.BLUE)
                .width(10));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rutaSimulada.get(0), 16));

        //iniciarSimulacion();
    }

    private void trackPet(int petIdInt){

        Call<Pet> call = petService.getPet(petIdInt);
        call.enqueue(new Callback<Pet>(){
            @Override
            public void onResponse(Call<Pet> call, Response<Pet> response) {
                if (response.isSuccessful() && response.body() != null) {
                    //Pet pet = response.body();
                    //showPetOnMap(new LatLng(pet.getLatitude(), pet.getLongitude()));
                    mapContainer.setVisibility(View.VISIBLE);
                    iniciarSimulacion();

                } else {
                    Toast.makeText(Maps.this, "Mascota no encontrada", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Pet> call, Throwable t) {
                Toast.makeText(Maps.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void seleccionarRutaAleatoria() {
        Random random = new Random();
        int rutaIndex = random.nextInt(rutasBogota.size());
        rutaSimulada = rutasBogota.get(rutaIndex);
    }
    private void iniciarSimulacion() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (index < rutaSimulada.size()) {
                    LatLng nuevaPosicion = rutaSimulada.get(index);

                    mascotaMarker.setPosition(nuevaPosicion);

                    List<LatLng> puntos = new ArrayList<>(rutaPolyline.getPoints());
                    puntos.add(nuevaPosicion);
                    rutaPolyline.setPoints(puntos);

                    mMap.animateCamera(CameraUpdateFactory.newLatLng(nuevaPosicion));

                    index++;
                    handler.postDelayed(this, 2000); // Intervalo de 2 segundos
                }
            }
        }, 2000);
    }
}