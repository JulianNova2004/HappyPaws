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
                Intent intent = new Intent(ModifyPet.this,Home.class);
                startActivity(intent);
            }
        });
    }
    public void modifyPetInformation(){

        String petNameMStr = petNameM.getText().toString().trim();
        petNameMStr = petNameMStr.isEmpty() ? "" : petNameMStr;

        String breedMStr = breedM.getText().toString().trim();
        breedMStr = breedMStr.isEmpty() ? "" : breedMStr;

        //String ageDouble = !ageM.getText().toString().trim().isEmpty() ? ageM.getText().toString().trim():"";
        Double ageDouble = (!ageM.getText().toString().trim().isEmpty()) ?
                Double.parseDouble(ageM.getText().toString().trim()) : 0;

        int weightMInt = (!weightM.getText().toString().trim().isEmpty()) ?
                Integer.parseInt(weightM.getText().toString().trim()) : 0;

        String foodMStr = foodM.getText().toString().trim();
        foodMStr = foodMStr.isEmpty() ? "" : foodMStr;

        int amountFoodInt = (!amountFoodM.getText().toString().trim().isEmpty()) ?
                Integer.parseInt(amountFoodM.getText().toString().trim()) : 0;

        int amountWalksInt = (!amountWalksM.getText().toString().trim().isEmpty()) ?
                Integer.parseInt(amountWalksM.getText().toString().trim()) : 0;

        String descriptionMStr = descriptionM.getText().toString().trim();
        descriptionMStr = descriptionMStr.isEmpty() ? "" : descriptionMStr;


        Pet petM = new Pet(petNameMStr, breedMStr, amountWalksInt, amountFoodInt, foodMStr, weightMInt, descriptionMStr, ageDouble);
        SharedPreferences sharedPreferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        int petId = sharedPreferences.getInt("Pet_ID", -1);
        Toast.makeText(ModifyPet.this, "id = " + petId + "", Toast.LENGTH_SHORT).show();
        if(petId != -1){
            Call<Void> call = petService.updatePet(petId, petM);
            call.enqueue(new Callback<Void>(){
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {

                        Toast.makeText(ModifyPet.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();

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

        }else{Toast.makeText(ModifyPet.this, "No ha entrado", Toast.LENGTH_SHORT).show();}
    }
}