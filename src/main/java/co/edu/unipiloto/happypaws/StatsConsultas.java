package co.edu.unipiloto.happypaws;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import models.Consulta;
import models.Pet;
import network.ConsultationService;
import network.PetService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatsConsultas extends AppCompatActivity {

    private TextView statPetMostVisits, amountVisitsPet;
    private EditText petIDS;
    private Button btnBringPetMostVisits, btnBringPetsAcordingState, btnBringAmountOfVisitsOfPets;
    private ConsultationService consultationService;
    private PetService petService;
    private Spinner statePetSpinner;
    private String stateStr;
    private Pet pet;
    private LinearLayout containerPetsDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stats_consultas);

        statPetMostVisits = findViewById(R.id.statPetMostVisits);
        amountVisitsPet = findViewById(R.id.amountVisitsPet);
        petIDS = findViewById(R.id.petIDS);
        btnBringPetMostVisits = findViewById(R.id.btnBringPetMostVisits);
        btnBringPetsAcordingState = findViewById(R.id.btnBringPetsAcordingState);
        btnBringAmountOfVisitsOfPets = findViewById(R.id.btnBringAmountOfVisitsOfPets);
        containerPetsDesc = findViewById(R.id.containerPetsDesc);

        consultationService = Retro.getClient().create(ConsultationService.class);
        petService = Retro.getClient().create(PetService.class);

        statePetSpinner = findViewById(R.id.state_pet_spinnerS);
        String[] opciones = {"Seleccione estado", "Critico", "Estable"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statePetSpinner.setAdapter(adapter);
        statePetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        stateStr = "Critico";
                        break;
                    case 2:
                        stateStr = "Estable";
                        break;
                    default:
                        stateStr = "";
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                stateStr = "";
            }
        });
        
        btnBringPetMostVisits.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                bringPetMostVisit();
            }
        });

        btnBringPetsAcordingState.setOnClickListener(v -> {
            if (statePetSpinner.getSelectedItemPosition() == 0) {
                Toast.makeText(StatsConsultas.this, "Por favor seleccione un estado", Toast.LENGTH_SHORT).show();
                return;
            }
            bringPetsAcordingState();
        });
        
        btnBringAmountOfVisitsOfPets.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                validate();

            }
        });
    }

    public void validate() {
        String idPetStr = petIDS.getText().toString().trim();
        if (idPetStr.isEmpty()) {
            petIDS.setError("No deje este campo vacío");
            return;
        }
        int idPet;
        try {
            idPet = Integer.parseInt(idPetStr);
        } catch (NumberFormatException e) {
            petIDS.setError("Ingrese un número válido");
            return;
        }
        if (idPet <= 0) {
            petIDS.setError("Ingrese un número mayor a 0");
            return;
        }
        amountVisitsPet(idPet);
    }

    public void bringPetMostVisit(){

        Call<Pet> call = consultationService.mostFreq();
        call.enqueue(new Callback<Pet>() {
            @Override
            public void onResponse(Call<Pet> call, Response<Pet> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Pet petR = response.body();

                    // pets.addAll(response.body());
                    String idRecieved = "Id: " + petR.getPetId() + "\n";
                    String nameRecieved = "Name: " + petR.getName() + "\n";
                    String owner = petR.getUser().getFirstname() + " " + petR.getUser().getLastname();
                    String ownerRecieved = "Owner: " + owner + "\n";
                    String braceRecieved = "Breed: " + petR.getRace() + "\n";
                    String ageRecieved = "Age: " + petR.getAge() + "\n";
                    String descriptionRecieved = "Description: " + petR.getDescription();

                    String put = idRecieved + nameRecieved + ownerRecieved + braceRecieved + ageRecieved + descriptionRecieved;
                    //String put = idRecieved + nameRecieved + braceRecieved + ageRecieved + descriptionRecieved;
                    statPetMostVisits.setText(put);
                    statPetMostVisits.setVisibility(View.VISIBLE);
                    /*
                    id
                    name
                    brace
                    age
                    weight
                    food
                    amount_of_food
                    amount_of_walks
                    description
                     */

                }else if(response.body() == null){
                    //TextView noPet = new TextView(StatsConsultas.this);
                    statPetMostVisits.setText("No hay mascotas registradas");
                    statPetMostVisits.setVisibility(View.VISIBLE);
                    //Toast.makeText(ViewPets.this, "No tiene ninguna mascota registrada", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(StatsConsultas.this, "Error al visualizar la mascota", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Pet> call, Throwable t) {
                Toast.makeText(StatsConsultas.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("HappyPaws", "Error al visualizar la mascota", t);
            }

        });
    }

    public void bringPetsAcordingState(){

        Call<List<Pet>> call = consultationService.findByEstado(stateStr);
        call.enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    containerPetsDesc.removeAllViews();
                    List<Pet> pets = response.body();
                        // pets.addAll(response.body());
                        int i = 1;
                        for(Pet pet: pets){

                            TextView petD = createHeaderTextView("PET NUMBER " + i);
                            containerPetsDesc.addView(petD);

                            TextView idRecieved = createTextView("Id: " + pet.getPetId());
                            TextView nameRecieved = createTextView("Name: " + pet.getName());
                            String owner = pet.getUser().getFirstname() + " " + pet.getUser().getLastname();
                            TextView ownerRecieved = createTextView("Owner: " + owner);
                            TextView braceRecieved = createTextView("Breed: " + pet.getRace());
                            TextView ageRecieved = createTextView("Age: " + pet.getAge());
                            TextView descriptionRecieved = createTextView("Description: " + pet.getDescription());
                            /*
                            id
                            name
                            brace
                            age
                            weight
                            food
                            amount_of_food
                            amount_of_walks
                            description
                             */
                            containerPetsDesc.addView(idRecieved);
                            containerPetsDesc.addView(nameRecieved);
                            containerPetsDesc.addView(ownerRecieved);
                            containerPetsDesc.addView(braceRecieved);
                            containerPetsDesc.addView(ageRecieved);
                            containerPetsDesc.addView(descriptionRecieved);

                            i++;
                        }

                } else if(response.body() == null){
                    TextView noPet = new TextView(StatsConsultas.this);
                    noPet.setText("No hay mascotas registradas");
                    noPet.setTextSize(18);
                    noPet.setGravity(Gravity.CENTER);
                    noPet.setPadding(0, 10, 0, 10);
                    containerPetsDesc.addView(noPet);
                }else {
                    Toast.makeText(StatsConsultas.this, "Error al visualizar mascotas", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {
                Toast.makeText(StatsConsultas.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("HappyPaws", "Error al visualizar mascotas", t);
            }

        });

    }

    public void amountVisitsPet(int idPet){
        callConsulta(idPet, () -> {
            Call<Integer> call = consultationService.countVisitsPetId(idPet);
            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.isSuccessful() ) {
                        Integer rta = response.body();
                        if (rta == null) {
                            String put = "Esta mascota no tiene visitas registradas";
                            amountVisitsPet.setText(put);
                            amountVisitsPet.setVisibility(View.VISIBLE);
                            Toast.makeText(StatsConsultas.this, "Esta mascota no tiene visitas", Toast.LENGTH_SHORT).show();
                        } else if(rta == 0){
                            String put = "No se recibió el número de visitas";
                            amountVisitsPet.setText(put);
                            amountVisitsPet.setVisibility(View.VISIBLE);
                        }
                        else {
                            String mascota = "Mascota: " + pet.getName() + " con ID: " + idPet + "\n";
                            String number = "Visitas registradas: " + rta;
                            String put = mascota + number;
                            amountVisitsPet.setText(put);
                            amountVisitsPet.setVisibility(View.VISIBLE);
                            //Toast.makeText(StatsConsultas.this, "La mascota tiene " + rta + " visitas", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(StatsConsultas.this, "TAS MAL", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Toast.makeText(StatsConsultas.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("HappyPaws", "Error al buscar cantidad de consulta", t);
                }
            });

        });
    }

    private void callConsulta(int petId, Runnable onSuccess){
        //boolean called = false;
        if(petId != 0){
            Call<Pet> call = petService.getPet(petId);
            call.enqueue(new Callback<Pet>(){
                @Override
                public void onResponse(Call<Pet> call, Response<Pet> response) {
                    if (response.isSuccessful()) {
                        Log.i("hapi", "callPet: "+response.body().getPetId());
                        pet = response.body();
                        onSuccess.run();
                    } else {
                        Toast.makeText(StatsConsultas.this, "Mascota no encontrada", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Pet> call, Throwable t) {
                    Toast.makeText(StatsConsultas.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("HappyPaws", "Error al encontrar mascota", t);
                }
            });

        }else{Toast.makeText(StatsConsultas.this, "No ha entrado", Toast.LENGTH_SHORT).show();}
        //return called;
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        int pad = dpPx(1);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0, pad, 0, pad);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        textView.setLayoutParams(lp);
        int accent = ContextCompat.getColor(this, R.color.brand_accent);
        textView.setTextColor(accent);
        return textView;
    }

    private TextView createHeaderTextView(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tv.setTextColor(Color.parseColor("#ffaa75"));
        tv.setGravity(Gravity.CENTER);
        int primary = ContextCompat.getColor(this, R.color.brand_primary);
        tv.setTextColor(primary);

        int pad = dpPx(1);
        tv.setPadding(0, pad, 0, pad);
        return tv;
    }

    private int dpPx(int dp){
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
        //return (int) (dp * getResources().getDisplayMetrics().density);
    }

}