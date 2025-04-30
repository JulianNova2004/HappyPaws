package co.edu.unipiloto.happypaws;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
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

import models.History;
import network.ConsultationService;
import network.HistoryService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAllHistories extends AppCompatActivity {

    private HistoryService historyService;
    private LinearLayout containerH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_all_histories);
        
        containerH = findViewById(R.id.containerAllHistories);
        historyService = Retro.getClient().create(HistoryService.class);
        
        bringInfo();
    }
    
    public void bringInfo(){
        Call<List<History>> call = historyService.getAllHistories();
        call.enqueue(new Callback<List<History>>() {
            @Override
            public void onResponse(Call<List<History>> call, Response<List<History>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    containerH.removeAllViews();
                    List<History> histories = response.body();
                    if(histories.isEmpty()){
                        TextView noConsulta = new TextView(ViewAllHistories.this);
                        noConsulta.setText("No tiene informacion de historia médica");
                        noConsulta.setTextSize(18);
                        noConsulta.setGravity(Gravity.CENTER);
                        noConsulta.setPadding(0, 10, 0, 10);
                        containerH.addView(noConsulta);
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

                            TextView info = createHeaderTextView("CONSULTA NÚMERO " + i);

                            containerH.addView(info);

                            TextView idRecieved = createTextView("Id: " + hi.getId());
                            TextView dateRecieved = createTextView("Fecha: " + fechaStr);
                            TextView vaccineRecieved = createTextView("Vaccine: " + hi.getVaccine());
                            TextView doseRecieved = createTextView("Dosis: " + hi.getDosis());
                            TextView cuantityRecieved = createTextView("Cantidad: " + hi.getCuantity());
                            TextView reasonRecieved = createTextView("Razón: " + hi.getReason());
                            TextView commentsRecieved = createTextView("Comentarios: " + hi.getComments());
                            TextView space = createTextView(" ");
                            space.setTextSize(10);

                            containerH.addView(idRecieved);
                            containerH.addView(dateRecieved);
                            containerH.addView(vaccineRecieved);
                            containerH.addView(doseRecieved);
                            containerH.addView(cuantityRecieved);
                            containerH.addView(reasonRecieved);
                            containerH.addView(commentsRecieved);
                            //containerH.addView(nameRecieved);
                            containerH.addView(space);

                            i++;
                        }
                    }

                } else {
                    Toast.makeText(ViewAllHistories.this, "Error al buscar historias médicas", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<History>> call, Throwable t) {
                Toast.makeText(ViewAllHistories.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("HappyPaws", "Error al buscar historias médicas", t);
            }

        });
    }

    private TextView createHeaderTextView(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tv.setTextColor(Color.parseColor("#ffaa75"));
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
        return textView;
    }

    private int dpPx(int dp){
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}