package co.edu.unipiloto.happypaws;

import android.content.SharedPreferences;
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
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import models.Pet;
import models.Recordatorio;
import network.RecordatoryService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewVaccineRecordatories extends AppCompatActivity {

    private RecordatoryService recordatoryService;
    private LinearLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_vaccine_recordatories);

        container = findViewById(R.id.containerRecordatoriesV);
        recordatoryService = Retro.getClient().create(RecordatoryService.class);
        viewRecordatories();

    }

    public void viewRecordatories(){
        SharedPreferences sharedPreferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("User_ID", -1);

        if(userId != -1){
            Call<List<Recordatorio>> call = recordatoryService.verRecs(userId);
            call.enqueue(new Callback<List<Recordatorio>>() {
                @Override
                public void onResponse(Call<List<Recordatorio>> call, Response<List<Recordatorio>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        container.removeAllViews();
                        List<Recordatorio> recs = response.body();
                        if(recs.isEmpty()){
                            TextView noRec = new TextView(ViewVaccineRecordatories.this);
                            noRec.setText("No tiene recordatorios registradas");
                            noRec.setTextSize(18);
                            noRec.setGravity(Gravity.CENTER);
                            noRec.setPadding(0, 10, 0, 10);
                            container.addView(noRec);
                            //Toast.makeText(Viewrecs.this, "No tiene ninguna mascota registrada", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            // recs.addAll(response.body());
                            int i = 1;
                            for(Recordatorio reco: recs){
                                Date fechaFormateada = null;
                                String dateString = reco.getDate();

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

                                TextView recoC = createHeaderTextView("RECORDATORY NUMBER " + i);

                                container.addView(recoC);

                                TextView idRecieved = createTextView("Id: " + reco.getId());
                                TextView dateRecieved = createTextView("Date: " + fechaStr);
                                TextView vaccineRecieved = createTextView("Vaccine: " + reco.getVaccine());
                                TextView petRecieved = createTextView("Pet: " + reco.getPet().getName());
                                TextView stateRecieved = createTextView("State: " + reco.getEstado());
                                TextView space = createTextView(" ");
                                space.setTextSize(10);

                                container.addView(idRecieved);
                                container.addView(petRecieved);
                                container.addView(dateRecieved);
                                container.addView(vaccineRecieved);
                                container.addView(stateRecieved);
                                container.addView(space);
                                i++;
                            }
                        }

                    } else {
                        Toast.makeText(ViewVaccineRecordatories.this, "Error al visualizar recordatorios", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<List<Recordatorio>> call, Throwable t) {
                    Toast.makeText(ViewVaccineRecordatories.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("HappyPaws", "Error al visualizar mascotas", t);
                }

            });
        }
        else{
            Toast.makeText(ViewVaccineRecordatories.this, "id -1 guardado, revisar", Toast.LENGTH_SHORT).show();
        }
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