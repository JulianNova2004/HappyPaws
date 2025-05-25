

package co.edu.unipiloto.happypaws;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

import models.Pet;
import models.RitmoCardiaco;
import network.PetService;
import network.Retro;
import network.RitmoCardiacoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewRitmoCardiaco extends AppCompatActivity {

    private EditText petIdRythm, dateToGetAverage, petIdRythmAverage;

    private Button btnStartGetingRythm, btnStopGetingRythm, btnGetAverageRythm;

    private TextView rithmSended, averageSended;

    private RitmoCardiacoService ritmoCardiacoService;

    private PetService petService;

    private Runnable rythmRunnable;

    private boolean isValid, isBringingRythm = false;

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_ritmo_cardiaco);

        petIdRythm = findViewById(R.id.petIdRythm);
        dateToGetAverage = findViewById(R.id.dateToGetAverage);
        petIdRythmAverage = findViewById(R.id.petIdRythmAverage);
        btnStartGetingRythm = findViewById(R.id.btnStartGetingRythm);
        btnStopGetingRythm = findViewById(R.id.btnStopGetingRythm);
        btnStopGetingRythm.setEnabled(false);
        btnGetAverageRythm = findViewById(R.id.btnGetAverageRythm);
        rithmSended = findViewById(R.id.rithmSended);
        rithmSended.setText("No data yet.");
        averageSended = findViewById(R.id.averageSended);
        averageSended.setText("No data yet.");

        ritmoCardiacoService = Retro.getClient().create(RitmoCardiacoService.class);
        petService = Retro.getClient().create(PetService.class);

        dateToGetAverage.setOnClickListener(v -> showCalendar());
        btnStartGetingRythm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                review();
            }
        });

        btnStopGetingRythm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                stopBringingRythm();
            }
        });

        btnGetAverageRythm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                reviewToAverage();
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
                    dateToGetAverage.setText(fechaSeleccionada);
                },
                año, mes, dia
        );

        //datePickerDialog.getDatePicker().setMinDate(Long.MIN_VALUE);
        //datePickerDialog.getDatePicker().setMaxDate(Long.MAX_VALUE);

        datePickerDialog.show();
    }

    private void review(){
        isValid = true;
        String petIdStr = petIdRythm.getText().toString().trim();
        if (petIdStr.isEmpty() || !petIdStr.matches("\\d+")) {
            petIdRythm.setError("Debe ingresar un ID de mascota válido");
            isValid = false;
        }

        if (isValid) {
            //Toast.makeText(this, "Lleno todos los campos correctamente", Toast.LENGTH_SHORT).show();
            searchPet(Integer.parseInt(petIdStr), () -> {
                //Empezar a ejecutar metodo para traer el ritmo
                //bringRythm(petIdStr);
                startBringingRythm(petIdStr);

            });
        } else Toast.makeText(this, "Caremonda llene bien todos los campos", Toast.LENGTH_SHORT).show();

    }

    private void searchPet(int petId, Runnable onSuccess) {
        //boolean called = false;
        if (petId != 0) {
            Call<Pet> call = petService.getPet(petId);
            call.enqueue(new Callback<Pet>() {
                @Override
                public void onResponse(Call<Pet> call, Response<Pet> response) {
                    if (response.isSuccessful()) {
                        Log.i("hapi", "callPet: " + response.body().getPetId());
                        //Log.i("stat", "callPet: "+state);
                        onSuccess.run();
                    } else {
                        Toast.makeText(ViewRitmoCardiaco.this, "Mascota no encontrada", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Pet> call, Throwable t) {
                    Toast.makeText(ViewRitmoCardiaco.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("HappyPaws", "Error al encontrar mascota", t);
                }
            });

        } else {
            Toast.makeText(ViewRitmoCardiaco.this, "No ha entrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void startBringingRythm(String petIdStr){
        if (isBringingRythm) return;
        isBringingRythm = true;

        rythmRunnable = new Runnable() {
            @Override
            public void run() {
                bringRythm(petIdStr);
                handler.postDelayed(this, 2_000);
            }
        };
        // arranca inmediatamente
        handler.post(rythmRunnable);

        btnStartGetingRythm.setEnabled(false);
        btnStopGetingRythm.setEnabled(true);


    }

    private void bringRythm(String petIdStr){

        rithmSended.setText("");
        int petIdInt = Integer.parseInt(petIdStr);
        Call<RitmoCardiaco> call = ritmoCardiacoService.getHeartRate(petIdInt);
        call.enqueue(new Callback<RitmoCardiaco>() {
            @Override
            public void onResponse(Call<RitmoCardiaco> call, Response<RitmoCardiaco> response) {
                if (response.isSuccessful()) {
                    RitmoCardiaco ritmo = response.body();
                    if(ritmo!=null){

                        int valor = ritmo.getValor();
                        String dateRecieved = ritmo.getDate();
                        String arrDate[] = dateRecieved.split("T");
                        String fecha = arrDate[0];
                        String hora = arrDate[1];

                        String rta = "Valor recibido: " + valor + " ppm.\n" +
                                "Fecha: " + fecha + ".\n" +
                                "Hora: " + hora + ".";

                        rithmSended.setText(rta);
                    }else{
                        String rta = "Hubo un error, intente nuevamente";
                        rithmSended.setText(rta);
                    }
                } else {
                    Toast.makeText(ViewRitmoCardiaco.this, "Hubo un error, intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RitmoCardiaco> call, Throwable t) {
                Toast.makeText(ViewRitmoCardiaco.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("HappyPaws", "Error al traer el ritmo cardiaco", t);
            }
        });

        btnStopGetingRythm.setEnabled(true);
    }

    private void stopBringingRythm(){

        if (!isBringingRythm) return;
        isBringingRythm = false;
        handler.removeCallbacks(rythmRunnable);

        btnStartGetingRythm.setEnabled(true);
        btnStopGetingRythm.setEnabled(false);

        //Toast.makeText(this, "Boton de parar", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private void reviewToAverage(){

        boolean isBothGood = true;
        String petIdStr = petIdRythmAverage.getText().toString().trim();
        String dateStr = dateToGetAverage.getText().toString().trim();
        if (petIdStr.isEmpty() || dateStr.isEmpty() || !petIdStr.matches("\\d+")) {
            if(petIdStr.isEmpty()){
                petIdRythmAverage.setError("Debe ingresar un ID de mascota válido");
            }
            if(dateStr.isEmpty()){
                dateToGetAverage.setError("Debe ingresar una fecha válida");
            }
            isBothGood = false;
        }

        if (isBothGood) {
            //Toast.makeText(this, "Lleno todos los campos correctamente", Toast.LENGTH_SHORT).show();
            searchPet(Integer.parseInt(petIdStr), () -> {
                //Empezar a ejecutar metodo para traer el ritmo
                bringAverage(petIdStr, dateStr);

            });
        } else Toast.makeText(this, "Caremonda llene bien todos los campos", Toast.LENGTH_SHORT).show();

    }

    private void bringAverage(String petId, String dateToFilter){

        //Toast.makeText(this, "Entro 1", Toast.LENGTH_SHORT).show();
        int petIdInt = Integer.parseInt(petId);
        Call<Double> call = ritmoCardiacoService.getByFecha(dateToFilter, petIdInt);
        averageSended.setText("");
        call.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                //Toast.makeText(ViewRitmoCardiaco.this, "Entro 2 ", Toast.LENGTH_LONG).show();
                if (response.code() == 204) {
                    averageSended.setText("No hay valor para esta fecha");
                }
                else if (response.isSuccessful()) {
                    //Toast.makeText(ViewRitmoCardiaco.this, "Entro 3 ", Toast.LENGTH_LONG).show();
                    Double promedio = response.body();
//                    if(promedio == null){
//                        averageSended.setText("No hay valor para esta fecha");
//                    }else{
                    String rta = "Valor obtenido: " + promedio + ".";
                    averageSended.setText(rta);
                    //}

                }
                else {
                    Toast.makeText(ViewRitmoCardiaco.this, "Valor no encontrada", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                Toast.makeText(ViewRitmoCardiaco.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("HappyPaws", "Error al buscar promedio de ritmo cardiaco", t);
            }
        });

    }
}