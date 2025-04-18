package co.edu.unipiloto.happypaws;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
                                TextView recoC = new TextView(ViewVaccineRecordatories.this);
                                recoC.setText("RECORDATORY NUMBER " + i);
                                recoC.setTextSize(20);
                                recoC.setGravity(Gravity.CENTER);
                                recoC.setPadding(0, 10, 0, 10);
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
}