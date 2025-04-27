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
import models.History;
import network.ConsultationService;
import network.HistoryService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyHistory extends AppCompatActivity {

    private EditText historyId, date, vaccine, dose, cuantity, reason, comments;
    private Button btnSendModifyInformation;
    private History history;
    private HistoryService historyService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modify_history);

        historyId = findViewById(R.id.historyIDM);
        date = findViewById(R.id.historyDateM);
        vaccine = findViewById(R.id.vaccineM);
        dose = findViewById(R.id.doseM);
        cuantity = findViewById(R.id.cuantityM);
        reason = findViewById(R.id.reasonM);
        comments = findViewById(R.id.commentsM);
        btnSendModifyInformation = findViewById(R.id.btnSendModifyInformation);
        historyService = Retro.getClient().create(HistoryService.class);

        date.setOnClickListener(v -> showCalendar());
        btnSendModifyInformation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                
                ModifyHistoryInformation();
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
    
    public void ModifyHistoryInformation(){
        int historyIdInt = (!historyId.getText().toString().trim().isEmpty()) ?
                Integer.parseInt(historyId.getText().toString().trim()) : 0;

        callHistory(historyIdInt, () -> {
            //Log.i("state", "STATEbef: " + state);
            String dateStr = date.getText().toString().trim();
            dateStr = dateStr.isEmpty() ? "" : dateStr;

            String vaccineStr = vaccine.getText().toString().trim();
            vaccineStr = vaccineStr.isEmpty() ? "" : vaccineStr;

            int dosisInt = Integer.parseInt(dose.getText().toString().trim());

            double cuantityDouble = Double.parseDouble(cuantity.getText().toString().trim());

            String reasonStr = reason.getText().toString().trim();
            reasonStr = reasonStr.isEmpty() ? "" : reasonStr;

            String commentsStr = comments.getText().toString().trim();
            commentsStr = commentsStr.isEmpty() ? "" : commentsStr;

            History historia = new History(dateStr, vaccineStr, dosisInt, cuantityDouble, reasonStr, commentsStr);

            Call<History> call = historyService.updateHistory(historia, historyIdInt);
            call.enqueue(new Callback<History>() {
                @Override
                public void onResponse(Call<History> call, Response<History> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ModifyHistory.this, "Consulta de " + history.getPet().getName() +" modificado exitosamente :D", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ModifyHistory.this, Home.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(ModifyHistory.this, "TAS MAL", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<History> call, Throwable t) {
                    Toast.makeText(ModifyHistory.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("HappyPaws", "Error al modificar Consulta", t);
                }
            });

            //Toast.makeText(ModifyHistory.this, "Toast de error", Toast.LENGTH_SHORT).show();
        });
    }

    private void callHistory(int historyId, Runnable onSuccess){
        //boolean called = false;
        if(historyId != 0){
            Call<History> call = historyService.getHistory(historyId);
            call.enqueue(new Callback<History>(){
                @Override
                public void onResponse(Call<History> call, Response<History> response) {
                    if (response.isSuccessful()) {
                        //Toast.makeText(ModifyHistory.this, "Mascota encontrada", Toast.LENGTH_SHORT).show();
                        history = response.body();
                        Log.i("hapi", "call: "+response.body().getId());
                        //state = true;
                        //Log.i("stat", "callPet: "+state);
                        onSuccess.run();
                    } else {
                        Toast.makeText(ModifyHistory.this, "Consulta no encontrada", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<History> call, Throwable t) {
                    Toast.makeText(ModifyHistory.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("HappyPaws", "Error al encontrar consulta", t);
                }
            });

        }else{Toast.makeText(ModifyHistory.this, "No ha entrado", Toast.LENGTH_SHORT).show();}
        //return called;
    }
}