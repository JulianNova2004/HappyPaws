package co.edu.unipiloto.happypaws;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import models.Consulta;
import network.ConsultationService;
import network.RecordatoryService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowConsultasToModify extends AppCompatActivity {

    private LinearLayout containerC;
    private Button btnSendModifyConsultas, btnBringConsultationInfo;
    private EditText consultaId, petId;
    private ConsultationService consultationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_consultas_to_modify);

        containerC = findViewById(R.id.containerConsultasM);
        btnSendModifyConsultas = findViewById(R.id.btnSendModifyConsultas);
        consultaId = findViewById(R.id.consultaID);
        petId = findViewById(R.id.petIDC);
        btnBringConsultationInfo = findViewById(R.id.btn_bring_consultation_info);

        consultationService = Retro.getClient().create(ConsultationService.class);

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
            Toast.makeText(ShowConsultasToModify.this, "Caremonda ponga algo", Toast.LENGTH_SHORT).show();
            return;
        }
        if (idPet == 0) {
            petId.setError("Ingrese un número válido");
            Toast.makeText(ShowConsultasToModify.this, "Caremonda ponga un número mayor a 0", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(ShowConsultasToModify.this, "Ingresó un ID válido", Toast.LENGTH_SHORT).show();
        viewConsultas(idPet);

    }

    public void viewConsultas(int idPet){

        Call<List<Consulta>> call = consultationService.getConsultasByPetId(idPet);
        call.enqueue(new Callback<List<Consulta>>() {
            @Override
            public void onResponse(Call<List<Consulta>> call, Response<List<Consulta>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    containerC.removeAllViews();
                    List<Consulta> consultas = response.body();
                    if(consultas.isEmpty()){
                        TextView noConsulta = new TextView(ShowConsultasToModify.this);
                        noConsulta.setText("No tiene informacion de consulta para esta mascota");
                        noConsulta.setTextSize(18);
                        noConsulta.setGravity(Gravity.CENTER);
                        noConsulta.setPadding(0, 10, 0, 10);
                        containerC.addView(noConsulta);
                        //Toast.makeText(ShowPetsToModify.this, "No tiene ninguna mascota registrada", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        // pets.addAll(response.body());
                        int i = 1;
                        for(Consulta c: consultas){
                            Date fechaFormateada = null;
                            String dateString = c.getFecha();

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

                            TextView info = createHeaderTextView("CONSULTA NÚMERO " + i);

                            containerC.addView(info);

                            TextView idRecieved = createTextView("Id: " + c.getId());
                            TextView dateRecieved = createTextView("Fecha: " + fechaStr);
                            TextView reasonRecieved = createTextView("Motivo: " + c.getMotivo());
                            TextView stateRecieved = createTextView("Estado: " + c.getEstado());
                            TextView vetRecieved = createTextView("Veterinario: " + c.getVeterinario());
                            TextView resultsRecieved = createTextView("Resultados: " + c.getVeterinario());
                            TextView space = createTextView(" ");
                            space.setTextSize(10);

                            containerC.addView(idRecieved);
                            containerC.addView(dateRecieved);
                            containerC.addView(reasonRecieved);
                            containerC.addView(stateRecieved);
                            containerC.addView(vetRecieved);
                            containerC.addView(resultsRecieved);
                            //containerC.addView(nameRecieved);
                            containerC.addView(space);

                            i++;
                        }
                    }

                } else {
                    Toast.makeText(ShowConsultasToModify.this, "Error al buscar consultas", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Consulta>> call, Throwable t) {
                Toast.makeText(ShowConsultasToModify.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("HappyPaws", "Error al buscar consultas", t);
            }

        });

    }

    private TextView createHeaderTextView(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        //tv.setTextColor(Color.parseColor("#ffaa75"));
        int primary = ContextCompat.getColor(this, R.color.brand_primary);
        tv.setTextColor(primary);
        tv.setGravity(Gravity.CENTER);
        int pad = dpPx(1);
        tv.setPadding(0, pad, 0, pad);
        return tv;
    }

    private TextView createTextView(String data){

        TextView textView = new TextView(this);
        textView.setText(data);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        int pad = dpPx(1);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0, pad, 0, pad);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        int accent = ContextCompat.getColor(this, R.color.brand_accent);
        textView.setTextColor(accent);
        textView.setLayoutParams(lp);
        return textView;
    }

    private int dpPx(int dp){
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    public void reviewConsultaId(){
        String idConsultaStr = consultaId.getText().toString().trim();
        int idConsulta = Integer.parseInt(idConsultaStr);
        if (idConsultaStr.isEmpty()) {
            petId.setError("No deje este campo vacio");
            Toast.makeText(ShowConsultasToModify.this, "Caremonda ponga algo", Toast.LENGTH_SHORT).show();
            return;
        }
        if (idConsulta == 0) {
            petId.setError("Ingrese un número válido");
            Toast.makeText(ShowConsultasToModify.this, "Caremonda ponga un número mayor a 0", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(ShowConsultasToModify.this, "Ingresó un ID válido", Toast.LENGTH_SHORT).show();
        sendConsultaId(idConsulta);
    }
    public void sendConsultaId(int idConsulta){
        //SharedPreferences preferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        //SharedPreferences.Editor editor = preferences.edit();
        //editor.putInt("Consulta_ID", idConsulta);
        //editor.apply();
        Intent intent = new Intent(ShowConsultasToModify.this, ModifyConsulta.class);
        startActivity(intent);
    }
}