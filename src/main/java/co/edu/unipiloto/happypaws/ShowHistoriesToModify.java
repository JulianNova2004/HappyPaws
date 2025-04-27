package co.edu.unipiloto.happypaws;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import models.Consulta;
import models.History;
import network.ConsultationService;
import network.HistoryService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowHistoriesToModify extends AppCompatActivity {

    private LinearLayout containerM;
    private Button btnSendModifyConsultas, btnBringConsultationInfo;
    private EditText historyId, petId;
    private HistoryService historyService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_histories_to_modify);

        containerM = findViewById(R.id.containerHistoriaM);
        btnSendModifyConsultas = findViewById(R.id.btnSendModifyHistory);
        historyId = findViewById(R.id.historyID);
        petId = findViewById(R.id.petIDH);
        btnBringConsultationInfo = findViewById(R.id.btn_bring_medical_history_info);

        historyService = Retro.getClient().create(HistoryService.class);

        btnBringConsultationInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                reviewPetId();

            }
        });

        btnSendModifyConsultas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                reviewConsultaId();
            }
        });
    }

    public void reviewPetId(){
        String idPetStr = petId.getText().toString().trim();
        int idPet = Integer.parseInt(idPetStr);
        if (idPetStr.isEmpty()) {
            petId.setError("No deje este campo vacio");
            Toast.makeText(ShowHistoriesToModify.this, "Caremonda ponga algo", Toast.LENGTH_SHORT).show();
            return;
        }
        if (idPet == 0) {
            petId.setError("Ingrese un número válido");
            Toast.makeText(ShowHistoriesToModify.this, "Caremonda ponga un número mayor a 0", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(ShowHistoriesToModify.this, "Ingresó un ID válido", Toast.LENGTH_SHORT).show();
        viewConsultas(idPet);

    }

    public void viewConsultas(int idPet){

        Call<List<History>> call = historyService.getHistoryByPetId(idPet);
        call.enqueue(new Callback<List<History>>() {
            @Override
            public void onResponse(Call<List<History>> call, Response<List<History>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    containerM.removeAllViews();
                    List<History> histories = response.body();
                    if(histories.isEmpty()){
                        TextView noConsulta = new TextView(ShowHistoriesToModify.this);
                        noConsulta.setText("No tiene informacion de consulta para esta mascota");
                        noConsulta.setTextSize(18);
                        noConsulta.setGravity(Gravity.CENTER);
                        noConsulta.setPadding(0, 10, 0, 10);
                        containerM.addView(noConsulta);
                        //Toast.makeText(ShowPetsToModify.this, "No tiene ninguna mascota registrada", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        // pets.addAll(response.body());
                        int i = 1;
                        for(History hi: histories){
                            Date fechaFormateada = null;
                            String dateString = hi.getDate();

                            if (dateString != null && !dateString.isEmpty()) {
                                try {

                                    long timestamp = Long.parseLong(dateString);
                                    fechaFormateada = new Date(timestamp);

                                    //Log.i("HappyPaws", "Fecha convertida: " + fechaFormateada);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e("HappyPaws", "Error al convertir el timestamp: " + e.getMessage());
                                }
                            }

                            String fechaStr = (fechaFormateada != null) ? new SimpleDateFormat("yyyy-MM-dd").format(fechaFormateada) : "Fecha no disponible";

                            Log.i("HappyPaws", "Fecha formateada: " + fechaStr);

                            Gson gson = new Gson();
                            String jsonR = gson.toJson(response.body());
                            Log.i("HappyPaws", "JSON GSON " + jsonR);

                            TextView info = new TextView(ShowHistoriesToModify.this);
                            info.setText("CONSULTA NÚMERO " + i);
                            info.setTextSize(20);
                            info.setGravity(Gravity.CENTER);
                            info.setPadding(0, 10, 0, 10);
                            containerM.addView(info);

                            TextView idRecieved = createTextView("Id: " + hi.getId());
                            TextView dateRecieved = createTextView("Fecha: " + fechaStr);
                            TextView vaccineRecieved = createTextView("Vaccine: " + hi.getVaccine());
                            TextView doseRecieved = createTextView("Dosis: " + hi.getDosis());
                            TextView cuantityRecieved = createTextView("Cantidad: " + hi.getCuantity());
                            TextView reasonRecieved = createTextView("Razón: " + hi.getReason());
                            TextView commentsRecieved = createTextView("Comentarios: " + hi.getComments());
                            TextView space = createTextView(" ");
                            space.setTextSize(10);

                            containerM.addView(idRecieved);
                            containerM.addView(dateRecieved);
                            containerM.addView(vaccineRecieved);
                            containerM.addView(doseRecieved);
                            containerM.addView(cuantityRecieved);
                            containerM.addView(reasonRecieved);
                            containerM.addView(commentsRecieved);
                            //containerM.addView(nameRecieved);
                            containerM.addView(space);

                            i++;
                        }
                    }

                } else {
                    Toast.makeText(ShowHistoriesToModify.this, "Error al buscar consultas", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<History>> call, Throwable t) {
                Toast.makeText(ShowHistoriesToModify.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("HappyPaws", "Error al buscar consultas", t);
            }

        });
    }

    private TextView createTextView(String data){
        TextView textView = new TextView(this);

        int lwidth = dpPx(200);
        int lheight = dpPx(50);
        int lmargin = dpPx(16);
        int gravity = Gravity.CENTER;

        LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(lwidth, lheight);
        parameters.gravity = gravity;
        parameters.setMargins(lmargin, lmargin, lmargin, lmargin);

        textView.setLayoutParams(parameters);
        textView.setText(data);
        textView.setGravity(gravity);

        return textView;
    }

    private int dpPx(int dp){
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    public void reviewConsultaId(){
        String idHistoriaStr = historyId.getText().toString().trim();
        int idHistoria = Integer.parseInt(idHistoriaStr);
        if (idHistoriaStr.isEmpty()) {
            petId.setError("No deje este campo vacio");
            Toast.makeText(ShowHistoriesToModify.this, "Caremonda ponga algo", Toast.LENGTH_SHORT).show();
            return;
        }
        if (idHistoria == 0) {
            petId.setError("Ingrese un número válido");
            Toast.makeText(ShowHistoriesToModify.this, "Caremonda ponga un número mayor a 0", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(ShowHistoriesToModify.this, "Ingresó un ID válido", Toast.LENGTH_SHORT).show();
        sendConsultaId(idHistoria);
    }
    public void sendConsultaId(int idConsulta){
        //SharedPreferences preferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        //SharedPreferences.Editor editor = preferences.edit();
        //editor.putInt("Consulta_ID", idConsulta);
        //editor.apply();
        //Actividad --> ModifyHistory
        Intent intent = new Intent(ShowHistoriesToModify.this, ModifyConsulta.class);
        startActivity(intent);
    }
}