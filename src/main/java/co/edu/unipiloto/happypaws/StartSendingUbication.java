package co.edu.unipiloto.happypaws;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import models.Pet;
import models.Recorrido;
import network.PetService;
import network.RecorridoService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;


public class StartSendingUbication extends AppCompatActivity {

    private Button btnStartSendUbication, btnStopSendUbication;
    private TextView dataSended;
    private EditText petIdUbication;
    private PetService petService;
    private RecorridoService recorridoService;
    private boolean isValid;
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL_MS = 4_000L;
    //private static final int REQ_LOCATION_PERMS = 1000;
    private FusedLocationProviderClient fusedClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private double actualLat;
    private double actualLong;
    private long currentTimestamp;
    private Recorrido recorrido;
    private boolean enviandoUbicacion;

    private final ActivityResultLauncher<String[]> locationPermissionLauncher =
        registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            Boolean fineGranted = result.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false);
            Boolean coarseGranted = result.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false);
            if (Boolean.TRUE.equals(fineGranted) || Boolean.TRUE.equals(coarseGranted)) {
                setButtons();
            } else {
                Toast.makeText(this, "Permisos de ubicación denegados", Toast.LENGTH_SHORT).show();
                btnStartSendUbication.setEnabled(false);
            }
        });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start_sending_ubication);

        petIdUbication = findViewById(R.id.petIdUbication);
        btnStartSendUbication = findViewById(R.id.btnStartSendUbication);
        btnStopSendUbication = findViewById(R.id.btnStopSendUbication);
        dataSended = findViewById(R.id.dataSended);
        petService = Retro.getClient().create(PetService.class);
        recorridoService = Retro.getClient().create(RecorridoService.class);
        fusedClient = LocationServices.getFusedLocationProviderClient(this);


        locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                INTERVAL_MS).build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult result) {
                Location loc = result.getLastLocation();
                if (loc != null) {
                    actualLat  = loc.getLatitude();
                    actualLong = loc.getLongitude();
                    currentTimestamp = loc.getTime();

                    Recorrido newReco = new Recorrido();
                    newReco.setId(recorrido.getId());
                    newReco.setPet_id(recorrido.getPet_id());
                    newReco.setLat(actualLat);
                    newReco.setLon(actualLong);

                    Log.i(TAG, "Ubicación: lat=" + actualLat +
                            ", lon=" + actualLong +
                            " @ " + currentTimestamp);


                    Call<Void> call = recorridoService.actualizarUbicacion(newReco);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {

                            Toast.makeText(StartSendingUbication.this, "Ubicación enviada", Toast.LENGTH_SHORT).show();
                            Log.i("StartSendingUbication", "Reco enviado: " + recorrido.getId() + ", LAT: " + recorrido.getLat() + ", LON: " + recorrido.getLon());
                            //Actualizar textview con datos

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(StartSendingUbication.this, "Error al enviar ubicación", Toast.LENGTH_SHORT).show();
                            Log.i("StartSendingUbication", "Reco enviado: " + recorrido.getId() + ", LAT: " + recorrido.getLat() + ", LON: " + recorrido.getLon());
                        }
                    });

                }else{
                    return;
                }
            }
        };

        requestLocationPermissions();

        btnStartSendUbication.setEnabled(false);
        btnStopSendUbication.setEnabled(false);
    }

    public void validateFields() {
        isValid = true;
        String petIdStr = petIdUbication.getText().toString().trim();
        if (petIdStr.isEmpty() || !petIdStr.matches("\\d+")) {
            petIdUbication.setError("Debe ingresar un ID de mascota válido");
            isValid = false;
        }

        if (isValid) {
            Toast.makeText(this, "Lleno todos los campos correctamente", Toast.LENGTH_SHORT).show();
            searchPet(Integer.parseInt(petIdStr), () -> {
                //Empezar a ejecutar metodo para enviar ubicacion
                searchReco();

            });
        } else
            Toast.makeText(this, "Caremonda llene bien todos los campos", Toast.LENGTH_SHORT).show();
    }

    private void searchPet(int petId, Runnable onSuccess) {
        //boolean called = false;
        if (petId != 0) {
            Call<Pet> call = petService.getPet(petId);
            call.enqueue(new Callback<Pet>() {
                @Override
                public void onResponse(Call<Pet> call, Response<Pet> response) {
                    if (response.isSuccessful()) {
                        //pet = response.body();
                        Log.i("hapi", "callPet: " + response.body().getPetId());
                        //Log.i("stat", "callPet: "+state);
                        onSuccess.run();
                    } else {
                        Toast.makeText(StartSendingUbication.this, "Mascota no encontrada", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Pet> call, Throwable t) {
                    Toast.makeText(StartSendingUbication.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("HappyPaws", "Error al encontrar mascota", t);
                }
            });

        } else {
            Toast.makeText(StartSendingUbication.this, "No ha entrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void setButtons(){
        btnStartSendUbication.setEnabled(true);
        btnStopSendUbication.setEnabled(false);

        btnStartSendUbication.setOnClickListener(v -> validateFields());
        btnStopSendUbication.setOnClickListener(v -> stopSendingUbication());

//        btnStartSendUbication.setOnClickListener(v -> {
//            //Primero comprueba permisos de ubicación
//            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                    || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//                // Si ya está el permiso, setea los botones
//                fusedClient.requestLocationUpdates(
//                        locationRequest,
//                        locationCallback,
//                        Looper.getMainLooper()
//                );
//                btnStartSendUbication.setEnabled(false);
//                btnStopSendUbication.setEnabled(true);
//
//            } else {
//                //Si no hay permiso, se le pide al paseador
//                ActivityCompat.requestPermissions(
//                        this,
//                        new String[]{
//                                android.Manifest.permission.ACCESS_FINE_LOCATION,
//                                android.Manifest.permission.ACCESS_COARSE_LOCATION
//                        }, REQ_LOCATION_PERMS
//                );
//            }
//        });
//
//        btnStopSendUbication.setOnClickListener(v -> {
//            fusedClient.removeLocationUpdates(locationCallback);
//            btnStartSendUbication.setEnabled(true);
//            btnStopSendUbication.setEnabled(false);
//        });
//
//        // Estado inicial
//        btnStartSendUbication.setEnabled(true);
//        btnStopSendUbication.setEnabled(false);

    }

    private void requestLocationPermissions() {
        String[] perms = {
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        };
        boolean fineGranted = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean coarseGranted = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (fineGranted || coarseGranted) {
            setButtons();
        } else {
            locationPermissionLauncher.launch(perms);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fusedClient.removeLocationUpdates(locationCallback);
    }

    private void searchReco(){

        Call<Recorrido> call = recorridoService.getLast();
        call.enqueue(new Callback<Recorrido>() {
            @Override
            public void onResponse(Call<Recorrido> call, Response<Recorrido> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recorrido = response.body();
                    startSendingUbication();
                    //onSuccess2.run();
                }
            }

            @Override
            public void onFailure(Call<Recorrido> call, Throwable t) {
                Log.e("StartSendingUbication", "Error al obtener ubicación", t);
            }
        });

    }

    private void startSendingUbication(){
        if (enviandoUbicacion) return;
        enviandoUbicacion = true;

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Si ya está el permiso, setea los botones
            fusedClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
            );
            btnStartSendUbication.setEnabled(false);
            btnStopSendUbication.setEnabled(true);

        } else {
            //Si no hay permiso, se le pide al paseador
            locationPermissionLauncher.launch(new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }

    }

    private void stopSendingUbication(){
        if (!enviandoUbicacion) return;
        enviandoUbicacion = false;
        fusedClient.removeLocationUpdates(locationCallback);
        btnStartSendUbication.setEnabled(true);
        btnStopSendUbication.setEnabled(false);
    }

}