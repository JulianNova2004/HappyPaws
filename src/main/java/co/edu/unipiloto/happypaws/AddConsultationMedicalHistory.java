package co.edu.unipiloto.happypaws;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import network.ConsultationService;
import network.PetService;
import network.Retro;

public class AddConsultationMedicalHistory extends AppCompatActivity {

    private EditText date, reason, petState, vet, result;

    private Button btnSendInformationMedicalHistory;

    private ConsultationService consultationService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_consultation_medical_history);

        date = findViewById(R.id.consultationDate);
        reason = findViewById(R.id.consultationReason);
        petState = findViewById(R.id.petState);
        vet = findViewById(R.id.designatedVeterinary);
        result = findViewById(R.id.consultationResults);
        consultationService = Retro.getClient().create(ConsultationService.class);
        btnSendInformationMedicalHistory = findViewById(R.id.registerConsultationInformation);
        btnSendInformationMedicalHistory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showCalendar();
                sendConsultationInfo();
                Intent intent = new Intent(AddConsultationMedicalHistory.this,Home.class);
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
                    String fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;
                    date.setText(fechaSeleccionada);
                },
                año, mes, dia
        );
        datePickerDialog.show();
    }

    public void sendConsultationInfo(){

        String dateStr = date.getText().toString().trim();
        dateStr = dateStr.isEmpty() ? "" : dateStr;

        String reasonStr = reason.getText().toString().trim();
        reasonStr = reasonStr.isEmpty() ? "" : reasonStr;

        String petStateStr = petState.getText().toString().trim();
        petStateStr = petStateStr.isEmpty() ? "" : petStateStr;

        String vetStr = vet.getText().toString().trim();
        vetStr = vetStr.isEmpty() ? "" : vetStr;

        String resultStr = result.getText().toString().trim();
        resultStr = resultStr.isEmpty() ? "" : resultStr;

        SharedPreferences sharedPreferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        int petId = sharedPreferences.getInt("Pet_ID", -1);

    }
}