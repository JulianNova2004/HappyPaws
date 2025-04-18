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

import models.Consulta;
import models.Pet;
import models.Consulta;
import network.ConsultationService;
import network.RecordatoryService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyConsulta extends AppCompatActivity {

    private EditText consultaId, dateM, reasonM, stateM, vetM, resultsM;

    private Button btnSendModifyInformation;
    boolean isValid;
    private Consulta consulta;
    private ConsultationService consultationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modify_consulta);

        consultaId = findViewById(R.id.consultationIDM);
        dateM = findViewById(R.id.consultationDateM);
        reasonM = findViewById(R.id.reasonM);
        stateM = findViewById(R.id.stateM);
        vetM = findViewById(R.id.vetM);
        resultsM = findViewById(R.id.resultsM);
        btnSendModifyInformation = findViewById(R.id.btnSendModifyInformation);
        consultationService = Retro.getClient().create(ConsultationService.class);
        
        dateM.setOnClickListener(v -> showCalendar());
        btnSendModifyInformation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                validateFields();
                ModifyConsultationInformation();
                //Intent intent = new Intent(ModifyConsulta.this,Home.class);
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
                    dateM.setText(fechaSeleccionada);
                },
                año, mes, dia
        );
        datePickerDialog.show();
    }

    public void validateFields(){
        isValid = true;
        String consultaIdStr = consultaId.getText().toString().trim();
        if (consultaIdStr.isEmpty() || !consultaIdStr.matches("\\d+")) {
            consultaId.setError("Debe ingresar un ID de mascota válido");
            isValid = false;
        }

        String dateStr = dateM.getText().toString().trim();
        if (dateStr.isEmpty()) {
            dateM.setError("Debe seleccionar una fecha");
            isValid = false;
        }

        String reasonStr = reasonM.getText().toString().trim();
        if (reasonStr.isEmpty()) {
            reasonM.setError("Este campo es obligatorio");
            isValid = false;
        }

        String stateStr = stateM.getText().toString().trim();
        if (reasonStr.isEmpty()) {
            stateM.setError("Este campo es obligatorio");
            isValid = false;
        }

        String vetStr = vetM.getText().toString().trim();
        if (vetStr.isEmpty()) {
            vetM.setError("Este campo es obligatorio");
            isValid = false;
        }

        String resultsStr = resultsM.getText().toString().trim();
        if (resultsStr.isEmpty()) {
            resultsM.setError("Este campo es obligatorio");
            isValid = false;
        }

        if(isValid) {
            Toast.makeText(this, "Lleno todos los campos correctamente", Toast.LENGTH_SHORT).show();
            ModifyConsultationInformation();
        }
        else Toast.makeText(this, "Caremonda llene bien todos los campos", Toast.LENGTH_SHORT).show();
    }

    public void ModifyConsultationInformation(){

        int consultaIdInt = (!consultaId.getText().toString().trim().isEmpty()) ?
                Integer.parseInt(consultaId.getText().toString().trim()) : 0;

        callConsulta(consultaIdInt, () -> {
            //Log.i("state", "STATEbef: " + state);
            String dateStr = dateM.getText().toString().trim();
            dateStr = dateStr.isEmpty() ? "" : dateStr;

            String reasonStr = reasonM.getText().toString().trim();
            reasonStr = reasonStr.isEmpty() ? "" : reasonStr;

            String stateStr = stateM.getText().toString().trim();
            stateStr = stateStr.isEmpty() ? "" : stateStr;

            String vetStr = vetM.getText().toString().trim();
            vetStr = vetStr.isEmpty() ? "" : vetStr;

            String resultsStr = resultsM.getText().toString().trim();
            resultsStr = resultsStr.isEmpty() ? "" : resultsStr;

            Consulta Consulta = new Consulta(dateStr, reasonStr, stateStr, vetStr, resultsStr, consulta.getPet());
            SharedPreferences sharedPreferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
            int recId = sharedPreferences.getInt("Rec_ID", -1);
            Call<Consulta> call = recordatoryService.updateRec(Consulta, recId);
            call.enqueue(new Callback<Consulta>() {
                @Override
                public void onResponse(Call<Consulta> call, Response<Consulta> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ModifyConsulta.this, "Consulta de " + pet.getName() +" modificado exitosamente :D", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ModifyConsulta.this, Home.class);
                        startActivity(intent);}
                    else{
                        Toast.makeText(ModifyConsulta.this, "TAS MAL", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Consulta> call, Throwable t) {
                    Toast.makeText(ModifyConsulta.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("HappyPaws", "Error al agregar Consulta", t);
                }
            });

            //Toast.makeText(ModifyConsulta.this, "Toast de error", Toast.LENGTH_SHORT).show();
        });
    }

    private void callConsulta(int consultaId, Runnable onSuccess){
        //boolean called = false;
        if(consultaId != 0){
            Call<Consulta> call = consultationService.getConsulta(consultaId);
            call.enqueue(new Callback<Consulta>(){
                @Override
                public void onResponse(Call<Consulta> call, Response<Consulta> response) {
                    if (response.isSuccessful()) {
                        //Toast.makeText(ModifyConsulta.this, "Mascota encontrada", Toast.LENGTH_SHORT).show();
                        consulta = response.body();
                        Log.i("hapi", "callPet: "+response.body().getId());
                        //state = true;
                        //Log.i("stat", "callPet: "+state);
                        onSuccess.run();
                    } else {
                        Toast.makeText(ModifyConsulta.this, "Consulta no encontrada", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Consulta> call, Throwable t) {
                    Toast.makeText(ModifyConsulta.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("HappyPaws", "Error al encontrar consulta", t);
                }
            });

        }else{Toast.makeText(ModifyConsulta.this, "No ha entrado", Toast.LENGTH_SHORT).show();}
        //return called;
    }
}