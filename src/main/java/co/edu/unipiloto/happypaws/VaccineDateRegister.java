package co.edu.unipiloto.happypaws;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        Call<Void> call = historyService.addHistory(historyA, petIdInt);
    }


}