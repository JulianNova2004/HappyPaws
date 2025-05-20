package co.edu.unipiloto.happypaws;

import android.app.DatePickerDialog;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.History;
import models.Recordatorio;
import network.HistoryService;
import network.RecordatoryService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatsRecordatory extends AppCompatActivity {

    private EditText firstDate, secondDate;
    private LinearLayout containerRecordatoriesBetweenDates;
    private Button btnSearchRecordatoriesBetweenTwoDates;
    private RecordatoryService recordatoryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stats_recordatory);

        firstDate = findViewById(R.id.firstDate);
        secondDate = findViewById(R.id.secondDate);
        btnSearchRecordatoriesBetweenTwoDates = findViewById(R.id.btnSearchRecordatoriesBetweenTwoDates);
        containerRecordatoriesBetweenDates = findViewById(R.id.containerRecordatoriesBetweenDates);

        recordatoryService = Retro.getClient().create(RecordatoryService.class);

        firstDate.setOnClickListener(v -> showCalendarF());
        secondDate.setOnClickListener(v -> showCalendarS());

        btnSearchRecordatoriesBetweenTwoDates.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                searchRecordatoriesBetweenDates();
            }
        });
    }

    public void showCalendarF(){
        final Calendar calendar = Calendar.getInstance();
        int año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String fechaSeleccionada = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    firstDate.setText(fechaSeleccionada);
                },
                año, mes, dia
        );
        datePickerDialog.show();
    }

    public void showCalendarS(){
        final Calendar calendar = Calendar.getInstance();
        int año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String fechaSeleccionada = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    secondDate.setText(fechaSeleccionada);
                },
                año, mes, dia
        );
        datePickerDialog.show();
    }

    private void searchRecordatoriesBetweenDates(){
        String firstDateStr = firstDate.getText().toString().trim();
        String secondDateStr = secondDate.getText().toString().trim();
        if(firstDateStr.isEmpty() || secondDateStr.isEmpty()){
            if(firstDateStr.isEmpty()){
                firstDate.setError("Este campo no puede quedar vacio");
            }
            if(secondDateStr.isEmpty()){
                secondDate.setError("Este campo no puede quedar vacio");
            }
            return;
        }
        else{
            Call<List<Recordatorio>> call = recordatoryService.getBetween(firstDateStr, secondDateStr);
            call.enqueue(new Callback<List<Recordatorio>>() {
                @Override
                public void onResponse(Call<List<Recordatorio>> call, Response<List<Recordatorio>> response) {
                    //Log.i("HappyPaws", "Revisar " + response.body());
                    //Log.i("HappyPaws", "Booleano 1 " + (response.body() == null));
                    //Log.i("HappyPaws", "Booleano 2 " + (response.body().isEmpty()));
                    //Log.i("HappyPaws", "IsSuccesfull? " + (response.isSuccessful()));
                    if (response.isSuccessful()) {
                        containerRecordatoriesBetweenDates.removeAllViews();
                        List<Recordatorio> recordatories = response.body();
                        if(recordatories.isEmpty()){
                            TextView noVaccines = new TextView(StatsRecordatory.this);
                            noVaccines.setText("No hay recordatorios programados");
                            noVaccines.setTextSize(18);
                            noVaccines.setGravity(Gravity.CENTER);
                            noVaccines.setPadding(0, 10, 0, 10);
                            containerRecordatoriesBetweenDates.addView(noVaccines);
                            Log.i("HappyPaws", "Revisar" + noVaccines.getText().toString());
                        }else{
                            int i = 1;
                            for(Recordatorio recordatorio: recordatories){
                                Date fechaFormateada = null;
                                String dateString = recordatorio.getDate();

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

                                TextView recD = createHeaderTextView("RECORDATORY NUMBER " + i);

                                TextView idRecieved = createTextView("Id: " + recordatorio.getId());
                                TextView dateRecieved = createTextView("Date programmed: " + fechaStr);
                                TextView vaccineRecieved = createTextView("Vaccine to apply: " + recordatorio.getVaccine());
                                TextView petRecieved = createTextView("Vaccine applied: " + recordatorio.getPet().getName());
                                boolean estado = recordatorio.getEstado();
                                String estadoStr;
                                if(estado){
                                    estadoStr = "Enviado";
                                }else{
                                    estadoStr = "No enviado";
                                }
                                TextView stateRecieved = createTextView("State of recordatory: " + estadoStr);
                                //fecha, vacuna, mascota, estado

                                containerRecordatoriesBetweenDates.addView(recD);
                                containerRecordatoriesBetweenDates.addView(idRecieved);
                                containerRecordatoriesBetweenDates.addView(dateRecieved);
                                containerRecordatoriesBetweenDates.addView(vaccineRecieved);
                                containerRecordatoriesBetweenDates.addView(petRecieved);
                                containerRecordatoriesBetweenDates.addView(stateRecieved);

                                i++;

                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Recordatorio>> call, Throwable t) {
                    Toast.makeText(StatsRecordatory.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("HappyPaws", "Error al visualizar vacunas", t);
                }
            });
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

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextColor(Color.BLACK);
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
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
        //return (int) (dp * getResources().getDisplayMetrics().density);
    }

}