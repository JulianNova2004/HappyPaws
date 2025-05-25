package co.edu.unipiloto.happypaws;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import models.Pet;
import models.User;
import network.PetService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowPetsToModify extends AppCompatActivity {

    private PetService petService;
    private LinearLayout container;
    private Button btnSendModifyPets;
    private EditText petId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_pets_to_modify);

        container = findViewById(R.id.containerPets);
        btnSendModifyPets = findViewById(R.id.send_modify_pet);
        petId = findViewById(R.id.petID);
        petService = Retro.getClient().create(PetService.class);
        btnSendModifyPets.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sendPetId();

            }
        });
        sendPets();
    }

    public void sendPetId(){

        String idPetStr = petId.getText().toString().trim();

        if (idPetStr.isEmpty()) {
            Toast.makeText(ShowPetsToModify.this, "Caremonda ponga algo", Toast.LENGTH_SHORT).show();
            return;
        }
        int idPet;

        try {
            idPet = Integer.parseInt(idPetStr);
        } catch (NumberFormatException e) {
            Toast.makeText(ShowPetsToModify.this, "Caremonda ponga un número válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (idPet == 0) {
            Toast.makeText(ShowPetsToModify.this, "Caremonda ponga un número mayor a 0", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(ShowPetsToModify.this, "Ingresó un ID válido", Toast.LENGTH_SHORT).show();
        SharedPreferences preferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("Pet_ID", idPet);
        editor.apply();
        Intent intent = new Intent(ShowPetsToModify.this, ModifyPet.class);
        startActivity(intent);
    }

    public void sendPets(){
        SharedPreferences sharedPreferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        int typeUser = sharedPreferences.getInt("typeUser", -100);
        if(typeUser==0){
            //Usuario
            int userId = sharedPreferences.getInt("User_ID", -1);
            sendPetsUser(userId);
        }else if(typeUser==1 || typeUser ==10){
            //Veterinario o admin
            //viewAllRecs();
        }else{
            Toast.makeText(ShowPetsToModify.this, "Error al con sharedPreferences", Toast.LENGTH_SHORT).show();
        }

    }

    private void sendPetsUser(int userId){
        //SharedPreferences sharedPreferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        //int userId = sharedPreferences.getInt("User_ID", -1);

        if(userId != -1){
            Call<List<Pet>> call = petService.getAllPets(userId);
            call.enqueue(new Callback<List<Pet>>() {
                @Override
                public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        container.removeAllViews();
                        List<Pet> pets = response.body();
                        if(pets.isEmpty()){
                            TextView noPet = new TextView(ShowPetsToModify.this);
                            noPet.setText("No tiene mascotas registradas");
                            noPet.setTextSize(18);
                            noPet.setGravity(Gravity.CENTER);
                            noPet.setPadding(0, 10, 0, 10);
                            container.addView(noPet);
                            //Toast.makeText(ShowPetsToModify.this, "No tiene ninguna mascota registrada", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            // pets.addAll(response.body());
                            int i = 1;
                            for(Pet pet: pets){
                                TextView petD = createHeaderTextView("PET NUMBER " + i);

                                container.addView(petD);

                                TextView idRecieved = createTextView("Id: " + pet.getPetId());
                                TextView nameRecieved = createTextView("Name: " + pet.getName());
                                TextView space = createTextView(" ");
                                space.setTextSize(10);

                                container.addView(idRecieved);
                                container.addView(nameRecieved);
                                container.addView(space);
                                i++;
                            }
                        }

                    } else {
                        Toast.makeText(ShowPetsToModify.this, "Error al buscar mascotas", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<List<Pet>> call, Throwable t) {
                    Toast.makeText(ShowPetsToModify.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("HappyPaws", "Error al buscar mascotas", t);
                }

            });
        }
        else{
            Toast.makeText(ShowPetsToModify.this, "id -1 guardado, revisar", Toast.LENGTH_SHORT).show();
        }

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
                        Toast.makeText(ShowPetsToModify.this, "Mascota no encontrada", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Pet> call, Throwable t) {
                    Toast.makeText(ShowPetsToModify.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("HappyPaws", "Error al encontrar mascota", t);
                }
            });

        } else {
            Toast.makeText(ShowPetsToModify.this, "No ha entrado", Toast.LENGTH_SHORT).show();
        }
    }

    private TextView createHeaderTextView(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        //tv.setTextColor(Color.parseColor("#ffaa75"));
        int primary = ContextCompat.getColor(this, R.color.brand_primary);
        tv.setTextColor(primary);

        tv.setGravity(Gravity.CENTER);
        int pad = dpPx(1);
        tv.setPadding(0, pad, 0, pad);
        return tv;
    }

    private TextView createTextView(String data){

        TextView textView = new TextView(this);
        textView.setText(data);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        int pad = dpPx(1);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0, pad, 0, pad);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        int accent = ContextCompat.getColor(this, R.color.brand_accent);
        textView.setTextColor(accent);
        textView.setLayoutParams(lp);
        return textView;
    }

    private int dpPx(int dp){
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}