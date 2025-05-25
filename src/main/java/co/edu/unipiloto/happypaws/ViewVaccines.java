package co.edu.unipiloto.happypaws;

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
import models.History;
import network.ConsultationService;
import network.HistoryService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewVaccines extends AppCompatActivity {

    private HistoryService historyService;
    private LinearLayout containerV;
    private Button btnBringConsultationInfo;
    private EditText petId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_vaccines);

        containerV = findViewById(R.id.containerVaccinesC);
        petId = findViewById(R.id.petIDV);
        btnBringConsultationInfo = findViewById(R.id.btn_bring_vaccines_info);
        historyService = Retro.getClient().create(HistoryService.class);

        btnBringConsultationInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                review();
            }
        });
    }

    public void review(){
        String idPetStr = petId.getText().toString().trim();
        int idPet = Integer.parseInt(idPetStr);
        if (idPetStr.isEmpty()) {
            petId.setError("No deje este campo vacio");
            Toast.makeText(ViewVaccines.this, "Caremonda ponga algo", Toast.LENGTH_SHORT).show();
            return;
        }

        if (idPet == 0) {
            petId.setError("Ingrese un número válido");
            Toast.makeText(ViewVaccines.this, "Caremonda ponga un número mayor a 0", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(ViewVaccines.this, "Ingresó un ID válido", Toast.LENGTH_SHORT).show();
        bringInfo(idPet);
    }

    public void bringInfo(int idPet){
        Call<List<History>> call = historyService.getHistoryByPetId(idPet);
        call.enqueue(new Callback<List<History>>() {
            @Override
            public void onResponse(Call<List<History>> call, Response<List<History>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    containerV.removeAllViews();
                    List<History> histories = response.body();
                    if(histories.isEmpty()){
                        TextView noConsulta = new TextView(ViewVaccines.this);
                        noConsulta.setText("No tiene informacion de consulta para esta mascota");
                        noConsulta.setTextSize(18);
                        noConsulta.setGravity(Gravity.CENTER);
                        noConsulta.setPadding(0, 10, 0, 10);
                        containerV.addView(noConsulta);
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

                            TextView info = createHeaderTextView("VACUNA NÚMERO " + i);

                            containerV.addView(info);

                            TextView idRecieved = createTextView("Id: " + hi.getId());
                            TextView dateRecieved = createTextView("Fecha: " + fechaStr);
                            TextView vaccineRecieved = createTextView("Vaccine: " + hi.getVaccine());
                            TextView doseRecieved = createTextView("Dosis: " + hi.getDosis());
                            TextView cuantityRecieved = createTextView("Cantidad: " + hi.getCuantity());
                            TextView reasonRecieved = createTextView("Razón: " + hi.getReason());
                            TextView commentsRecieved = createTextView("Comentarios: " + hi.getComments());
                            TextView space = createTextView(" ");
                            space.setTextSize(10);

                            containerV.addView(idRecieved);
                            containerV.addView(dateRecieved);
                            containerV.addView(vaccineRecieved);
                            containerV.addView(doseRecieved);
                            containerV.addView(cuantityRecieved);
                            containerV.addView(reasonRecieved);
                            containerV.addView(commentsRecieved);
                            //containerV.addView(nameRecieved);
                            containerV.addView(space);

                            i++;
                        }
                    }

                } else {
                    Toast.makeText(ViewVaccines.this, "Error al buscar consultas", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<History>> call, Throwable t) {
                Toast.makeText(ViewVaccines.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
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
        textView.setLayoutParams(lp);
        int accent = ContextCompat.getColor(this, R.color.brand_accent);
        textView.setTextColor(accent);
        return textView;
    }

    private int dpPx(int dp){
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}