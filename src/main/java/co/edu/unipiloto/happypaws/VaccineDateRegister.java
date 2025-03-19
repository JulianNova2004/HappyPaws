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

import models.History;
import network.HistoryService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VaccineDateRegister extends AppCompatActivity {

    private EditText petId, date, vaccine, dose, reason, cuantity, comments;
    private Button btnRegisterVaccineInformation;
    private HistoryService historyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vaccine_date_register);

        petId = findViewById(R.id.petIdH);
        date = findViewById(R.id.vaccineDate);
        date.setOnClickListener(v -> showCalendar());
        vaccine = findViewById(R.id.vaccine);
        dose = findViewById(R.id.dose);
        reason = findViewById(R.id.reason);
        cuantity = findViewById(R.id.cuantity);
        comments = findViewById(R.id.comments);

        btnRegisterVaccineInformation = findViewById(R.id.registerVaccineInformation);

        historyService = Retro.getClient().create(HistoryService.class);

        btnRegisterVaccineInformation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sendVaccineInfo();
                Intent intent = new Intent(VaccineDateRegister.this,Home.class);
                startActivity(intent);
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

    public void sendVaccineInfo(){

        int petIdInt = (!petId.getText().toString().trim().isEmpty()) ?
                Integer.parseInt(petId.getText().toString().trim()) : 0;

        String dateStr = date.getText().toString().trim();
        dateStr = dateStr.isEmpty() ? "" : dateStr;

        String vaccineStr = vaccine.getText().toString().trim();

        int doseInt = (!dose.getText().toString().trim().isEmpty()) ?
                Integer.parseInt(dose.getText().toString().trim()) : 0;

        double cuantityDouble = (!cuantity.getText().toString().trim().isEmpty()) ?
                Double.parseDouble(cuantity.getText().toString().trim()) : 0;

        String reasonStr = reason.getText().toString().trim();
        String commentsStr = comments.getText().toString().trim();

        History historyA = new History(dateStr, vaccineStr, doseInt, cuantityDouble, reasonStr, commentsStr);
        Call<History> call = historyService.addHistory(historyA, petIdInt);

        call.enqueue(new Callback<History>() {
            @Override
            public void onResponse(Call<History> call, Response<History> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(VaccineDateRegister.this, "Vacuna agregada exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(VaccineDateRegister.this, Home.class);
                    startActivity(intent);}
                else{
                    Toast.makeText(VaccineDateRegister.this, "TAS MAL", Toast.LENGTH_SHORT).show();
                    Toast.makeText(VaccineDateRegister.this, response.body().toString(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<History> call, Throwable t) {
                Toast.makeText(VaccineDateRegister.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("HappyPaws", "Error al agregar consulta", t);
            }
        });
    }


}