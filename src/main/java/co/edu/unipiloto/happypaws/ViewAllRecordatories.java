package co.edu.unipiloto.happypaws;

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
import network.PetService;
import network.RecordatoryService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAllRecordatories extends AppCompatActivity {

    private RecordatoryService recordatoryService;
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_all_recordatories);

        container = findViewById(R.id.containerAllRecs);
        recordatoryService = Retro.getClient().create(RecordatoryService.class);
        viewAllRecs();

    }

    public void viewAllRecs(){
        Call<List<Recordatorio>> call = recordatoryService.allRecs();
        call.enqueue(new Callback<List<Recordatorio>>() {
            @Override
            public void onResponse(Call<List<Recordatorio>> call, Response<List<Recordatorio>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    container.removeAllViews();
                    List<Recordatorio> recs = response.body();
                    if(recs.isEmpty()){
                        TextView noRec = new TextView(ViewAllRecordatories.this);
                        noRec.setText("No hay recordatorios registrados");
                        noRec.setTextSize(18);
                        noRec.setGravity(Gravity.CENTER);
                        noRec.setPadding(0, 10, 0, 10);
                        container.addView(noRec);
                        //Toast.makeText(ViewPets.this, "No tiene ninguna mascota registrada", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        int i = 1;
                        for(Recordatorio rec: recs){
                            Date fechaFormateada = null;
                            String dateString = rec.getDate();

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

                            TextView recD = new TextView(ViewAllRecordatories.this);
                            recD.setText("RECORDATORY NUMBER " + i);
                            recD.setTextSize(20);
                            recD.setGravity(Gravity.CENTER);
                            recD.setPadding(0, 10, 0, 10);
                            container.addView(recD);

                            TextView idRecieved = createTextView("Id: " + rec.getId());
                            TextView petRecieved = createTextView("Pet: " + rec.getPet().getName());
                            TextView dateRecieved = createTextView("Date: " + fechaStr);
                            TextView vaccineRecieved = createTextView("Vaccine: " + rec.getVaccine());
                            TextView stateRecieved = createTextView("State: " + rec.getEstado());
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
                    Toast.makeText(ViewAllRecordatories.this, "Error al visualizar recordatorios", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Recordatorio>> call, Throwable t) {
                Toast.makeText(ViewAllRecordatories.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("HappyPaws", "Error al visualizar recordatorios", t);
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

}