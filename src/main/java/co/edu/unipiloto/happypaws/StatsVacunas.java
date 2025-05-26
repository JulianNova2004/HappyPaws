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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.History;
import models.Pet;
import network.ConsultationService;
import network.HistoryService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatsVacunas extends AppCompatActivity {

    private EditText firstDate, secondDate;
    private LinearLayout containerVaccinesBetweenDates;
    private Spinner vaccine_spinnerS;
    private Button btnSearchVaccinesBetweenTwoDates, btnBringAmountOfVaccines;
    private String vaccineStr;
    private TextView amountVaccines;
    private HistoryService historyService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stats_vacunas);

        firstDate = findViewById(R.id.firstDate);
        secondDate = findViewById(R.id.secondDate);
        containerVaccinesBetweenDates = findViewById(R.id.containerVaccinesBetweenDates);
        vaccine_spinnerS = findViewById(R.id.vaccine_spinnerS);
        btnSearchVaccinesBetweenTwoDates = findViewById(R.id.btnSearchVaccinesBetweenTwoDates);
        btnBringAmountOfVaccines = findViewById(R.id.btnBringAmountOfVaccines);
        amountVaccines = findViewById(R.id.amountVaccines);

        historyService = Retro.getClient().create(HistoryService.class);

        firstDate.setOnClickListener(v -> showCalendarF());
        secondDate.setOnClickListener(v -> showCalendarS());
        loadVaccineTypes();

        btnSearchVaccinesBetweenTwoDates.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                searchVaccinesBetweenDates();
            }
        });
        btnBringAmountOfVaccines.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                review();

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

    public void searchVaccinesBetweenDates(){
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
            Call<List<History>> call = historyService.getVaccinesBetween(firstDateStr, secondDateStr);
            call.enqueue(new Callback<List<History>>() {
                @Override
                public void onResponse(Call<List<History>> call, Response<List<History>> response) {
                    //Log.i("HappyPaws", "Revisar " + response.body());
                    //Log.i("HappyPaws", "Booleano 1 " + (response.body() == null));
                    //Log.i("HappyPaws", "Booleano 2 " + (response.body().isEmpty()));
                    //Log.i("HappyPaws", "IsSuccesfull? " + (response.isSuccessful()));
                    if (response.isSuccessful()) {
                        containerVaccinesBetweenDates.removeAllViews();
                        List<History> histories = response.body();
                        if(histories.isEmpty()){
                            TextView noVaccines = new TextView(StatsVacunas.this);
                            noVaccines.setText("No hay vacunas registradas");
                            noVaccines.setTextSize(18);
                            noVaccines.setGravity(Gravity.CENTER);
                            noVaccines.setPadding(0, 10, 0, 10);
                            containerVaccinesBetweenDates.addView(noVaccines);
                            Log.i("HappyPaws", "Revisar" + noVaccines.getText().toString());
                        }else{
                            int i = 1;
                            for(History history: histories){
                                Date fechaFormateada = null;
                                String dateString = history.getDate();

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

                                TextView petD = createHeaderTextView("VACCINE NUMBER " + i);
                                containerVaccinesBetweenDates.addView(petD);
                                TextView idRecieved = createTextView("Id: " + history.getId());
                                TextView vaccineRecieved = createTextView("Vaccine applied: " + history.getVaccine());
                                TextView dateRecieved = createTextView("Date applied: " + fechaStr);
                                TextView reasonRecieved = createTextView("Reason: " + history.getReason());
                                TextView petRecieved = createTextView("Vaccine applied: " + history.getPet().getName());
                                TextView doseRecieved = createTextView("Dose applied: " + history.getDosis());
                                TextView cuantityRecieved = createTextView("Cuantity applied: " + history.getCuantity() + " ml");
                                TextView commentsRecieved = createTextView("Comments: " + history.getComments());

                                containerVaccinesBetweenDates.addView(idRecieved);
                                containerVaccinesBetweenDates.addView(vaccineRecieved);
                                containerVaccinesBetweenDates.addView(dateRecieved);
                                containerVaccinesBetweenDates.addView(reasonRecieved);
                                containerVaccinesBetweenDates.addView(petRecieved);
                                containerVaccinesBetweenDates.addView(doseRecieved);
                                containerVaccinesBetweenDates.addView(cuantityRecieved);
                                containerVaccinesBetweenDates.addView(commentsRecieved);

                                i++;

                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<History>> call, Throwable t) {
                    Toast.makeText(StatsVacunas.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
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
        int accent = ContextCompat.getColor(this, R.color.brand_accent);
        textView.setTextColor(accent);
        textView.setLayoutParams(lp);
        return textView;
    }

    private int dpPx(int dp){
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
        //return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private void loadVaccineTypes(){
        Call<List<String>> call = historyService.getVaccineTypes();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> tipos = response.body();
                    tipos.add(0, "Seleccione vacuna");

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(StatsVacunas.this, android.R.layout.simple_spinner_item, tipos
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    vaccine_spinnerS.setAdapter(adapter);
                } else {
                    Toast.makeText(StatsVacunas.this, "Error al cargar tipos de vacuna", Toast.LENGTH_SHORT).show();
                    Log.e("HappyPaws", "ResponseBody: " + response.body());
                    Log.e("HappyPaws", "ResponseMessage: " + response.message());

                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(StatsVacunas.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void review(){
        int pos = vaccine_spinnerS.getSelectedItemPosition();
        if (pos == 0) {
            vaccine_spinnerS.requestFocus();
            vaccine_spinnerS.performClick();

            View selectedView = vaccine_spinnerS.getSelectedView();
            if (selectedView instanceof TextView) {
                TextView errorText = (TextView) selectedView;
                errorText.setError("Seleccione una vacuna");
                errorText.setTextColor(Color.RED);
            }

            Toast.makeText(this, "Por favor seleccione un tipo de vacuna", Toast.LENGTH_SHORT).show();
            return;
        }
        vaccineStr = (String) vaccine_spinnerS.getSelectedItem();
        //Toast.makeText(this, "Seleccionaste: " + tipoVacuna, Toast.LENGTH_SHORT).show();
        bringAmountOfVaccines(vaccineStr);
    }

    private void bringAmountOfVaccines(String vaccineStr) {
        Call<Integer> call = historyService.getNumVaccines(vaccineStr);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int number = response.body();
                    String l1 = "Vacuna seleccionada: " + vaccineStr + "\n";
                    if(number == 0){
                        String l2 = "No se han aplicado dosis de esa vacuna";
                        String put = l1 + l2;
                        amountVaccines.setText(put);
                        amountVaccines.setVisibility(View.VISIBLE);
                    }else{
                        if(number == 1){
                            String l2 = "Se ha aplicado " + number + " vez";
                            String put = l1 + l2;
                            amountVaccines.setText(put);
                            amountVaccines.setVisibility(View.VISIBLE);
                        }else{
                            String l2 = "Se ha aplicado " + number + " veces";
                            String put = l1 + l2;
                            amountVaccines.setText(put);
                            amountVaccines.setVisibility(View.VISIBLE);
                        }
                    }

                } else {
                    Toast.makeText(StatsVacunas.this, "Error al cargar tipos de vacuna", Toast.LENGTH_SHORT).show();
                    //Log.e("HappyPaws", "ResponseBody: " + response.body());
                    //Log.e("HappyPaws", "ResponseMessage: " + response.message());

                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(StatsVacunas.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}