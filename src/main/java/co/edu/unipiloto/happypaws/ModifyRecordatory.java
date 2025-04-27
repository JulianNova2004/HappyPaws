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

import java.util.Calendar;

import models.Pet;
import models.Recordatorio;
import network.PetService;
import network.RecordatoryService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyRecordatory extends AppCompatActivity {

    private EditText petId, dateR, vaccineM;

    private Button btnSendModifyInformation;
    boolean isValid;
    private Pet pet;
    private RecordatoryService recordatoryService;

    private PetService petService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modify_recordatory);

        petId = findViewById(R.id.petIDM);
        dateR = findViewById(R.id.recordatoryDateM);
        vaccineM = findViewById(R.id.vaccineM);
        
        btnSendModifyInformation = findViewById(R.id.btnSendModifyInformation);
        petService = Retro.getClient().create(PetService.class);
        recordatoryService = Retro.getClient().create(RecordatoryService.class);
        dateR.setOnClickListener(v -> showCalendar());
        btnSendModifyInformation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                validateFields();
                modifyRecordatoryInformation();
                //Intent intent = new Intent(ModifyRecordatory.this,Home.class);
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
                    dateR.setText(fechaSeleccionada);
                },
                año, mes, dia
        );
        datePickerDialog.show();
    }

    public void validateFields(){
        isValid = true;
        String petIdStr = petId.getText().toString().trim();
        if (petIdStr.isEmpty() || !petIdStr.matches("\\d+")) {
            petId.setError("Debe ingresar un ID de mascota válido");
            isValid = false;
        }

        String dateStr = dateR.getText().toString().trim();
        if (dateStr.isEmpty()) {
            dateR.setError("Debe seleccionar una fecha");
            isValid = false;
        }

        String vaccineStr = vaccineM.getText().toString().trim();
        if (vaccineStr.isEmpty()) {
            vaccineM.setError("Este campo es obligatorio");
            isValid = false;
        }

        if(isValid) {
            Toast.makeText(this, "Lleno todos los campos correctamente", Toast.LENGTH_SHORT).show();
            modifyRecordatoryInformation();
        }
        else Toast.makeText(this, "Caremonda llene bien todos los campos", Toast.LENGTH_SHORT).show();
    }
    
    public void modifyRecordatoryInformation(){

        int petIdInt = (!petId.getText().toString().trim().isEmpty()) ?
                Integer.parseInt(petId.getText().toString().trim()) : 0;

        callPet(petIdInt, () -> {
            //Log.i("state", "STATEbef: " + state);
                String dateStr = dateR.getText().toString().trim();
                dateStr = dateStr.isEmpty() ? "" : dateStr;

                String vaccineStr = vaccineM.getText().toString().trim();
                //String petStateStr = petState.getText().toString().trim();
                //String vetStr = vet.getText().toString().trim();
                //String resultStr = result.getText().toString().trim();

                Recordatorio recordatorio = new Recordatorio(dateStr, vaccineStr);
                SharedPreferences sharedPreferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
                int recId = sharedPreferences.getInt("Rec_ID", -1);
                Call<Recordatorio> call = recordatoryService.updateRec(recordatorio, recId);
                call.enqueue(new Callback<Recordatorio>() {
                    @Override
                    public void onResponse(Call<Recordatorio> call, Response<Recordatorio> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ModifyRecordatory.this, "Recordatorio de " + pet.getName() +" modificado exitosamente :D", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ModifyRecordatory.this, Home.class);
                            startActivity(intent);}
                        else{
                            Toast.makeText(ModifyRecordatory.this, "TAS MAL", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Recordatorio> call, Throwable t) {
                        Toast.makeText(ModifyRecordatory.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.i("HappyPaws", "Error al agregar recordatorio", t);
                    }
                });

                //Toast.makeText(ModifyRecordatory.this, "Toast de error", Toast.LENGTH_SHORT).show();
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
                        //Toast.makeText(ModifyRecordatory.this, "Mascota encontrada", Toast.LENGTH_SHORT).show();
                        pet = response.body();
                        Log.i("hapi", "callPet: "+response.body().getPetId());
                        //state = true;
                        //Log.i("stat", "callPet: "+state);
                        onSuccess.run();
                    } else {
                        Toast.makeText(ModifyRecordatory.this, "Mascota no encontrada", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Pet> call, Throwable t) {
                    Toast.makeText(ModifyRecordatory.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("HappyPaws", "Error al encontrar mascota", t);
                }
            });

        }else{Toast.makeText(ModifyRecordatory.this, "No ha entrado", Toast.LENGTH_SHORT).show();}
        //return called;
    }
}