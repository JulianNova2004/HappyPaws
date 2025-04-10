package co.edu.unipiloto.happypaws;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;
import java.util.Locale;


import models.Consulta;
import models.Pet;
import network.ConsultationService;
import network.PetService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddConsultationMedicalHistory extends AppCompatActivity {

    private EditText date, reason, petState, vet, result, petId;

    private Button btnSendInformationMedicalHistory;
    boolean state = false, isValid;

    private Pet pet;
    private ConsultationService consultationService;

    private PetService petService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_consultation_medical_history);

        petId = findViewById(R.id.petIdC);
        date = findViewById(R.id.consultationDate);
        reason = findViewById(R.id.consultationReason);
        petState = findViewById(R.id.petState);
        vet = findViewById(R.id.designatedVeterinary);
        result = findViewById(R.id.consultationResults);
        consultationService = Retro.getClient().create(ConsultationService.class);
        petService = Retro.getClient().create(PetService.class);
        btnSendInformationMedicalHistory = findViewById(R.id.registerConsultationInformation);
        date.setOnClickListener(v -> showCalendar());

        btnSendInformationMedicalHistory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                validateFields();
                //Intent intent = new Intent(AddConsultationMedicalHistory.this,Home.class);
                //startActivity(intent);
            }
        });
    }

    public void validateFields(){
        isValid = true;
        String petIdStr = petId.getText().toString().trim();
        if (petIdStr.isEmpty() || !petIdStr.matches("\\d+")) {
            petId.setError("Debe ingresar un ID de mascota válido");
            isValid = false;
        }

        String dateStr = date.getText().toString().trim();
        if (dateStr.isEmpty()) {
            date.setError("Debe seleccionar una fecha");
            isValid = false;
        }

        String reasonStr = reason.getText().toString().trim();
        if (reasonStr.isEmpty()) {
            reason.setError("Este campo es obligatorio");
            isValid = false;
        }

        String petStateStr = petState.getText().toString().trim();
        if (petStateStr.isEmpty()) {
            petState.setError("Este campo es obligatorio");
            isValid = false;
        }

        String vetStr = vet.getText().toString().trim();
        if (vetStr.isEmpty()) {
            vet.setError("Este campo es obligatorio");
            isValid = false;
        }

        String resultStr = result.getText().toString().trim();
        if (resultStr.isEmpty()) {
            result.setError("Este campo es obligatorio");
            isValid = false;
        }

        if(isValid) {
            Toast.makeText(this, "Lleno todos los campos correctamente", Toast.LENGTH_SHORT).show();
            sendConsultationInfo();
        }
        else Toast.makeText(this, "Caremonda llene bien todos los campos", Toast.LENGTH_SHORT).show();
    }


    public void showCalendar(){
        final Calendar calendar = Calendar.getInstance();
        int año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String fechaSeleccionada = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    date.setText(fechaSeleccionada);
                },
                año, mes, dia
        );
        datePickerDialog.show();
    }

    public void sendConsultationInfo(){

        int petIdInt = (!petId.getText().toString().trim().isEmpty()) ?
                Integer.parseInt(petId.getText().toString().trim()) : 0;

        callPet(petIdInt, () -> {
            Log.i("state", "STATEbef: " + state);
            if (state) {
                String dateStr = date.getText().toString().trim();
                dateStr = dateStr.isEmpty() ? "" : dateStr;

                String reasonStr = reason.getText().toString().trim();
                String petStateStr = petState.getText().toString().trim();
                String vetStr = vet.getText().toString().trim();
                String resultStr = result.getText().toString().trim();

                Consulta consultaA = new Consulta(dateStr, reasonStr, petStateStr, vetStr, resultStr, pet);
                Call<Consulta> call = consultationService.addConsulta(consultaA, petIdInt);

                call.enqueue(new Callback<Consulta>() {
                    @Override
                    public void onResponse(Call<Consulta> call, Response<Consulta> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(AddConsultationMedicalHistory.this, "Consulta agregada exitosamente", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddConsultationMedicalHistory.this, Home.class);
                            startActivity(intent);}
                        else{
                            Toast.makeText(AddConsultationMedicalHistory.this, "Error al agregar la consulta", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Consulta> call, Throwable t) {
                        Toast.makeText(AddConsultationMedicalHistory.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.i("HappyPaws", "Error al agregar consulta", t);
                    }
                });
            } else {
                Toast.makeText(AddConsultationMedicalHistory.this, "Toast de error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void callPet(int petId, Runnable onSuccess){
        //boolean called = false;
        if(petId != 0){
            Call<Pet> call = petService.getPet(petId);
            call.enqueue(new Callback<Pet>(){
                @Override
                public void onResponse(Call<Pet> call, Response<Pet> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(AddConsultationMedicalHistory.this, "Mascota encontrada", Toast.LENGTH_SHORT).show();
                        pet = response.body();
                        Log.i("hapi", "callPet: "+response.body().getPetId());
                        state = true;
                        Log.i("stat", "callPet: "+state);
                        onSuccess.run();
                    } else {
                        Toast.makeText(AddConsultationMedicalHistory.this, "Mascota no encontrada", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Pet> call, Throwable t) {
                    Toast.makeText(AddConsultationMedicalHistory.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("HappyPaws", "Error al encontrar mascota", t);
                }
            });

        }else{Toast.makeText(AddConsultationMedicalHistory.this, "Toast ->nNo ha entrado", Toast.LENGTH_SHORT).show();}
        //return called;
    }

}