package co.edu.unipiloto.happypaws;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import models.Pet;
import network.PetService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectPetUbi extends AppCompatActivity {

    private Button btnStartUbication;
    private EditText petIdMap;
    private PetService petService;
    private boolean isValid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_pet_ubi);

        petIdMap = findViewById(R.id.petIdMap);
        btnStartUbication = findViewById(R.id.btnStartUbication);
        petService = Retro.getClient().create(PetService.class);

        btnStartUbication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
            }
        });
    }

    public void validateFields() {
        isValid = true;
        String petIdStr = petIdMap.getText().toString().trim();
        if (petIdStr.isEmpty() || !petIdStr.matches("\\d+")) {
            petIdMap.setError("Debe ingresar un ID de mascota válido");
            isValid = false;
        }

        if (isValid) {
            Toast.makeText(this, "Lleno todos los campos correctamente", Toast.LENGTH_SHORT).show();
            searchPet(Integer.parseInt(petIdStr), () -> {
                Intent intent = new Intent(SelectPetUbi.this, Maps.class);
                startActivity(intent);
            });
        } else
            Toast.makeText(this, "Caremonda llene bien todos los campos", Toast.LENGTH_SHORT).show();
    }

//    public void search(View view) {
//        Intent intent = new Intent(this,Maps.class);
//        startActivity(intent);
//    }

    private void searchPet(int petId, Runnable onSuccess) {
        //boolean called = false;
        if (petId != 0) {
            Call<Pet> call = petService.getPet(petId);
            call.enqueue(new Callback<Pet>() {
                @Override
                public void onResponse(Call<Pet> call, Response<Pet> response) {
                    if (response.isSuccessful()) {
                        //Toast.makeText(ModifyRecordatory.this, "Mascota encontrada", Toast.LENGTH_SHORT).show();
                        //pet = response.body();
                        Log.i("hapi", "callPet: " + response.body().getPetId());
                        //state = true;
                        //Log.i("stat", "callPet: "+state);
                        onSuccess.run();
                    } else {
                        Toast.makeText(SelectPetUbi.this, "Mascota no encontrada", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Pet> call, Throwable t) {
                    Toast.makeText(SelectPetUbi.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("HappyPaws", "Error al encontrar mascota", t);
                }
            });

        } else {
            Toast.makeText(SelectPetUbi.this, "No ha entrado", Toast.LENGTH_SHORT).show();
        }
    }
}