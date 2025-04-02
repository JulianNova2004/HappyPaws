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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

import models.Consulta;
import models.Pet;
import models.Recordatorio;
import network.ConsultationService;
import network.PetService;
import network.RecordatoryService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetRecordatory extends AppCompatActivity {

    private EditText date, vaccine, petId;

    private Button btnSendRecordatory;
    boolean state = false, isValid;
    private Pet pet;
    private RecordatoryService recordatoryService;

    private PetService petService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_recordatory);

        petId = findViewById(R.id.petIdR);
        date = findViewById(R.id.recordatoryDate);
        vaccine = findViewById(R.id.vaccineR);

        recordatoryService = Retro.getClient().create(RecordatoryService.class);
        petService = Retro.getClient().create(PetService.class);
        btnSendRecordatory = findViewById(R.id.registerRecordatory);
        date.setOnClickListener(v -> showCalendar());

        btnSendRecordatory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                validateFields();
                //Intent intent = new Intent(SetRecordatory.this,Home.class);
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

        String vaccineStr = vaccine.getText().toString().trim();
        if (vaccineStr.isEmpty()) {
            vaccine.setError("Este campo es obligatorio");
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

                String vaccineStr = vaccine.getText().toString().trim();
                //String petStateStr = petState.getText().toString().trim();
                //String vetStr = vet.getText().toString().trim();
                //String resultStr = result.getText().toString().trim();

                Recordatorio recordatorio = new Recordatorio(dateStr, vaccineStr);

                Call<Recordatorio> call = recordatoryService.add(recordatorio, petIdInt);

                call.enqueue(new Callback<Recordatorio>() {
                    @Override
                    public void onResponse(Call<Recordatorio> call, Response<Recordatorio> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(SetRecordatory.this, "Recordatorio de " + pet.getName() +" programado exitosamente :D", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SetRecordatory.this, Home.class);
                            startActivity(intent);}
                        else{
                            Toast.makeText(SetRecordatory.this, "TAS MAL", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Recordatorio> call, Throwable t) {
                        Toast.makeText(SetRecordatory.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.i("HappyPaws", "Error al agregar recordatorio", t);
                    }
                });
            } else {
                Toast.makeText(SetRecordatory.this, "Toast de error", Toast.LENGTH_SHORT).show();
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
                        //Toast.makeText(SetRecordatory.this, "Mascota encontrada", Toast.LENGTH_SHORT).show();
                        pet = response.body();
                        Log.i("hapi", "callPet: "+response.body().getPetId());
                        state = true;
                        Log.i("stat", "callPet: "+state);
                        onSuccess.run();
                    } else {
                        Toast.makeText(SetRecordatory.this, "Mascota no encontrada", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Pet> call, Throwable t) {
                    Toast.makeText(SetRecordatory.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("HappyPaws", "Error al encontrar mascota", t);
                }
            });

        }else{Toast.makeText(SetRecordatory.this, "No ha entrado", Toast.LENGTH_SHORT).show();}
        //return called;
    }

}