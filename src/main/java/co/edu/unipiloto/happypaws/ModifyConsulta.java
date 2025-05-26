package co.edu.unipiloto.happypaws;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
    //boolean isValid;
    private Consulta consulta;
    private ConsultationService consultationService;
    private Spinner statePetSpinner;
    private String stateStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modify_consulta);

        consultaId = findViewById(R.id.consultationIDM);
        dateM = findViewById(R.id.consultationDateM);
        reasonM = findViewById(R.id.reasonM);
        //stateM = findViewById(R.id.stateM);
        vetM = findViewById(R.id.vetM);
        resultsM = findViewById(R.id.resultsM);
        btnSendModifyInformation = findViewById(R.id.btnSendModifyInformation);
        consultationService = Retro.getClient().create(ConsultationService.class);

        dateM.setOnClickListener(v -> showCalendar());
        btnSendModifyInformation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //validateFields();
                ModifyConsultationInformation();
                //Intent intent = new Intent(ModifyConsulta.this,Home.class);
                //startActivity(intent);
            }
        });

        statePetSpinner = findViewById(R.id.state_pet_spinner);
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


    public void ModifyConsultationInformation(){

        int consultaIdInt = (!consultaId.getText().toString().trim().isEmpty()) ?
                Integer.parseInt(consultaId.getText().toString().trim()) : 0;

        callConsulta(consultaIdInt, () -> {
            //Log.i("state", "STATEbef: " + state);
            String dateStr = dateM.getText().toString().trim();
            dateStr = dateStr.isEmpty() ? "" : dateStr;

            String reasonStr = reasonM.getText().toString().trim();
            reasonStr = reasonStr.isEmpty() ? "" : reasonStr;

            //String stateStr = stateM.getText().toString().trim();
            //stateStr = stateStr.isEmpty() ? "" : stateStr;

            String vetStr = vetM.getText().toString().trim();
            vetStr = vetStr.isEmpty() ? "" : vetStr;

            String resultsStr = resultsM.getText().toString().trim();
            resultsStr = resultsStr.isEmpty() ? "" : resultsStr;

            Consulta Consulta = new Consulta(dateStr, reasonStr, stateStr, vetStr, resultsStr, consulta.getPet());
            //SharedPreferences sharedPreferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
            //int consultaId = sharedPreferences.getInt("Consulta_ID", -1);
            Call<Consulta> call = consultationService.updateConsulta(Consulta, consultaIdInt);
            call.enqueue(new Callback<Consulta>() {
                @Override
                public void onResponse(Call<Consulta> call, Response<Consulta> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ModifyConsulta.this, "Consulta de " + consulta.getPet().getName() +" modificado exitosamente :D", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ModifyConsulta.this, Home.class);
                        startActivity(intent);
                    }
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