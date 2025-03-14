package co.edu.unipiloto.happypaws;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;


import models.Consultation;
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
    boolean state = false;

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
        btnSendInformationMedicalHistory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showCalendar();
                sendConsultationInfo();
                //Intent intent = new Intent(AddConsultationMedicalHistory.this,Home.class);
                //startActivity(intent);
            }
        });
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

        callPet(petIdInt);

        String dateStr = date.getText().toString().trim();
        dateStr = dateStr.isEmpty() ? "" : dateStr;

        Date dateC = null;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = sdf.parse(dateStr);

            dateC = new Date(utilDate.getTime());

        }catch (ParseException e) {
            e.printStackTrace();
        }

        String reasonStr = reason.getText().toString().trim();
        reasonStr = reasonStr.isEmpty() ? "" : reasonStr;

        String petStateStr = petState.getText().toString().trim();
        petStateStr = petStateStr.isEmpty() ? "" : petStateStr;

        String vetStr = vet.getText().toString().trim();
        vetStr = vetStr.isEmpty() ? "" : vetStr;

        String resultStr = result.getText().toString().trim();
        resultStr = resultStr.isEmpty() ? "" : resultStr;

        if(state){
            Consultation consultationA = new Consultation(dateC, reasonStr, petStateStr, vetStr, resultStr,pet);
            Call<Void> call = consultationService.addConsultation(consultationA, petIdInt);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(AddConsultationMedicalHistory.this, "Consulta agregada exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddConsultationMedicalHistory.this,Home.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(AddConsultationMedicalHistory.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("HappyPaws", "Error al agregar consulta", t);
                }
            });
        }
        else{
            Toast.makeText(AddConsultationMedicalHistory.this, "Toast de error", Toast.LENGTH_SHORT).show();
        }
    }

    private void callPet(int petId){
        //boolean called = false;
        if(petId != 0){
            Call<Pet> call = petService.getPet(petId);
            call.enqueue(new Callback<Pet>(){
                @Override
                public void onResponse(Call<Pet> call, Response<Pet> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(AddConsultationMedicalHistory.this, "Mascota encontrada", Toast.LENGTH_SHORT).show();
                        pet = response.body();
                        state = true;
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

        }else{Toast.makeText(AddConsultationMedicalHistory.this, "No ha entrado", Toast.LENGTH_SHORT).show();}
        //return called;
    }

}