package co.edu.unipiloto.happypaws;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import models.Pet;
import retrofit2.Call;
import retrofit2.Callback;
import network.PetService;
import network.Retro;
import retrofit2.Response;

public class ModifyPet extends AppCompatActivity {

    private EditText petNameM, breedM, ageM, weightM, foodM, amountFoodM, amountWalksM, descriptionM;

    private Button btnSendModifyInformation;

    private PetService petService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modify_pet);

        petNameM = findViewById(R.id.petNameM);
        breedM = findViewById(R.id.breedM);
        ageM = findViewById(R.id.ageM);
        weightM = findViewById(R.id.weightM);
        foodM = findViewById(R.id.foodM);
        amountFoodM = findViewById(R.id.amountFoodM);
        amountWalksM = findViewById(R.id.amountWalksM);
        descriptionM = findViewById(R.id.descriptionM);
        btnSendModifyInformation = findViewById(R.id.btnSendModifyInformation);
        petService = Retro.getClient().create(PetService.class);
        btnSendModifyInformation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                modifyPetInformation();
            }
        });
    }
    public void modifyPetInformation(){
        String petNameMStr = petNameM.getText().toString().trim();
        String breedMStr = breedM.getText().toString().trim();
        double ageDouble = Double.parseDouble(ageM.getText().toString().trim());
        int weightMInt = Integer.parseInt(weightM.getText().toString().trim());
        String foodMStr = foodM.getText().toString().trim();
        int amountFoodInt = Integer.parseInt(amountFoodM.getText().toString().trim());
        int amountWalksInt = Integer.parseInt(amountWalksM.getText().toString().trim());
        String descriptionMStr = descriptionM.getText().toString().trim();

        Pet petM = new Pet(petNameMStr, breedMStr, amountWalksInt, amountFoodInt, foodMStr, weightMInt, descriptionMStr, ageDouble);
        SharedPreferences sharedPreferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        int petId = sharedPreferences.getInt("Pet_ID", -1);

        if(petId != -1){
            Call<Void> call = petService.updatePet(petId, petM);
            call.enqueue(new Callback<Void>(){
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        Toast.makeText(ModifyPet.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ModifyPet.this,Home.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(ModifyPet.this, "Error al modificar información de la mascota", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(ModifyPet.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("HappyPaws", "Error al registrar mascota", t);
                }
            });

        }
    }
}