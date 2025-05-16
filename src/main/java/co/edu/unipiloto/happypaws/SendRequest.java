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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import models.Consulta;
import models.Paseador;
import models.Request;
import network.RequestService;
import network.Retro;
import network.WorkerPaws;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendRequest extends AppCompatActivity {

    private LinearLayout containerAvailableWalkers;
    private Button btnSendRequest, btnSearchWalkers;
    private EditText walkerID, contenidoRequest, searchWalkers;
    private RequestService requestService;
    private List<Integer> availableID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_send_request);

        containerAvailableWalkers = findViewById(R.id.containerAvailableWalkers);
        btnSendRequest = findViewById(R.id.btnSendRequest);
        walkerID = findViewById(R.id.walkerID);
        contenidoRequest = findViewById(R.id.contenidoRequest);
        searchWalkers = findViewById(R.id.editTextSearchWalkers);
        btnSearchWalkers = findViewById(R.id.btnSearchWalkers);

        requestService = Retro.getClient().create(RequestService.class);
        SharedPreferences preferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        int userId = preferences.getInt("User_ID", 0);
        bringInfo(userId);

        btnSearchWalkers.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                filterWalkers(userId);
            }
        });

        btnSendRequest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                review(userId);
            }
        });

    }

    private void review(int userId){
        String walkerIDStr = walkerID.getText().toString().trim();
        if (walkerIDStr.isEmpty()) {
            walkerID.setError("No deje este campo vacio");
            Toast.makeText(SendRequest.this, "Caremonda ponga algo", Toast.LENGTH_SHORT).show();
            return;
        }
        int idWalker = Integer.parseInt(walkerIDStr);
        if (idWalker <= 0 || !availableID.contains(Integer.valueOf(idWalker))) {
            walkerID.setError("Ingrese un ID válido, revise la lista de opciones");
            Toast.makeText(SendRequest.this, "Caremonda ponga un número válido", Toast.LENGTH_SHORT).show();
            return;
        }
        String contenidoStr = contenidoRequest.getText().toString().trim();
        if (contenidoStr.isEmpty()) {
            contenidoRequest.setError("No deje este campo vacio");
            Toast.makeText(SendRequest.this, "Caremonda ponga algo", Toast.LENGTH_SHORT).show();
            return;
        }
        //Puso bien el id y no dejó vacio
        sendRequest(userId, idWalker, contenidoStr);

    }

    private void filterWalkers(int userId){

        String searchW = searchWalkers.getText().toString();

        containerAvailableWalkers.removeAllViews();
        TextView loading = createHeaderTextView("Loading");
        containerAvailableWalkers.addView(loading);

        searchWalkers.postDelayed(() ->{
            //No tiene nada de nada, llamar bringInfo();
            if(searchW.isEmpty()){
                bringInfo(userId);
            }

            //Contiene un espacio en blanco, llamar metodo
            else {
                searchWalkersByFilter(searchW);
            }
        }, 1000);

    }

    private void bringInfo(int userId){
        Call<List<Paseador>> call = requestService.getNoReloRech(userId);
        call.enqueue(new Callback<List<Paseador>>() {
            @Override
            public void onResponse(Call<List<Paseador>> call, Response<List<Paseador>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    availableID.clear();
                    containerAvailableWalkers.removeAllViews();
                    List<Paseador> paseadores = response.body();
                    if(paseadores.isEmpty()){
                        TextView noPaseadores = new TextView(SendRequest.this);
                        noPaseadores.setText("En el momento, no hay paseadores disponibles\npara enviar solicitud de paseo");
                        noPaseadores.setTextSize(18);
                        noPaseadores.setGravity(Gravity.CENTER);
                        noPaseadores.setPadding(0, 10, 0, 10);
                        containerAvailableWalkers.addView(noPaseadores);
                    }
                    else{
                        int i = 1;
                        for(Paseador p: paseadores){

                            TextView number = createHeaderTextView("PASEADOR NÚMERO " + i);

                            TextView idRecieved = createTextView("Id del Paseador: " + p.getId());
                            availableID.add(p.getId());
                            TextView nameRecieved = createTextView("Nombre: " + p.getName());
                            TextView emailRecieved = createTextView("Correo: " + p.getEmail());
                            TextView phoneRecieved = createTextView("Número de celular: " + p.getPhoneNum());
                            TextView space = createTextView(" ");
                            space.setTextSize(10);

                            containerAvailableWalkers.addView(number);
                            containerAvailableWalkers.addView(idRecieved);
                            containerAvailableWalkers.addView(nameRecieved);
                            containerAvailableWalkers.addView(emailRecieved);
                            containerAvailableWalkers.addView(phoneRecieved);
                            containerAvailableWalkers.addView(space);

                            i++;
                        }
                    }

                } else {
                    Toast.makeText(SendRequest.this, "No se pudo obtener la lista de paseadores.", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(SendRequest.this, "Error al buscar paseadores disponibles", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Paseador>> call, Throwable t) {
                Toast.makeText(SendRequest.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("HappyPaws", "Error al buscar paseadores", t);
            }

        });
    }

    private void searchWalkersByFilter(String patron){

        Call<List<Paseador>> call = requestService.getPasLike(patron);
        call.enqueue(new Callback<List<Paseador>>() {
            @Override
            public void onResponse(Call<List<Paseador>> call, Response<List<Paseador>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    availableID.clear();
                    containerAvailableWalkers.removeAllViews();
                    List<Paseador> paseadores = response.body();
                    if(paseadores.isEmpty()){
                        TextView noPaseadores = new TextView(SendRequest.this);
                        noPaseadores.setText("En el momento, no hay paseadores disponibles\npara enviar solicitud de paseo");
                        noPaseadores.setTextSize(18);
                        noPaseadores.setGravity(Gravity.CENTER);
                        noPaseadores.setPadding(0, 10, 0, 10);
                        containerAvailableWalkers.addView(noPaseadores);
                    }
                    else{
                        int i = 1;
                        for(Paseador p: paseadores){

                            TextView number = createHeaderTextView("PASEADOR NÚMERO " + i);

                            TextView idRecieved = createTextView("Id del Paseador: " + p.getId());
                            availableID.add(p.getId());
                            TextView nameRecieved = createTextView("Nombre: " + p.getName());
                            TextView emailRecieved = createTextView("Correo: " + p.getEmail());
                            TextView phoneRecieved = createTextView("Número de celular: " + p.getPhoneNum());
                            TextView space = createTextView(" ");
                            space.setTextSize(10);

                            containerAvailableWalkers.addView(number);
                            containerAvailableWalkers.addView(idRecieved);
                            containerAvailableWalkers.addView(nameRecieved);
                            containerAvailableWalkers.addView(emailRecieved);
                            containerAvailableWalkers.addView(phoneRecieved);
                            containerAvailableWalkers.addView(space);

                            i++;
                        }
                    }

                } else {
                    Toast.makeText(SendRequest.this, "No se pudo obtener la lista de paseadores.", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(SendRequest.this, "Error al buscar paseadores disponibles", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Paseador>> call, Throwable t) {
                Toast.makeText(SendRequest.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("HappyPaws", "Error al buscar paseadores", t);
            }

        });
    }
    private void sendRequest(int userId, int walkerId, String contenido){
        //Estado = 0 porque hasta ahora se va a enviar
        int estado = 0;
        Request request = new Request(contenido, estado);
        Call<Request> call = requestService.crearReq(request, userId, walkerId);
        Log.i("Values send request",   "UserId: " + userId + ". PaseadorId: " + walkerId);
        call.enqueue(new Callback<Request>() {
            @Override
            public void onResponse(Call<Request> call, Response<Request> response) {
                if (response.isSuccessful() && response.body()!=null) {
                    Toast.makeText(SendRequest.this, "Solicitud enviada exitosamente", Toast.LENGTH_SHORT).show();

                    int reqCreatedId = response.body().getId();

                    Data input = new Data.Builder()
                            .putInt(WorkerPaws.INPUT_REQ_ID, reqCreatedId)
                            .build();

                    OneTimeWorkRequest makeWork = new OneTimeWorkRequest.Builder(WorkerPaws.class)
                            .setInputData(input)
                            .setInitialDelay(1, TimeUnit.MINUTES)
                            .build();

                    WorkManager.getInstance(SendRequest.this).enqueue(makeWork);

                    //Intent intentS = new Intent(SendRequest.this, NotificationService.class);
                    //startActivity(intentS);

                    Intent intentS = new Intent(SendRequest.this, NotificationService.class);
                    startService(intentS);


                    Intent intent = new Intent(SendRequest.this, Home.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SendRequest.this, "Hubo un error al enviar el la solicitud", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Request> call, Throwable t) {
                Toast.makeText(SendRequest.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("HappyPaws", "Error al enviar solicitud", t);
            }
        });

    }

//    private void searchRequestToDelete(Request request){
//
//        Call<Request> call = requestService.getRequest(request.getId());
//        call.enqueue(new Callback<Request>() {
//            @Override
//            public void onResponse(Call<Request> call, Response<Request> response) {
//                if (response.isSuccessful() && response.body()!=null) {
//                    //Toast.makeText(SendRequest.this, "Solicitud expirada", Toast.LENGTH_SHORT).show();
//                    Request requestRecieved = response.body();
//                    int stateRecieved = requestRecieved.getEstado();
//                    if(stateRecieved == 0){
//                        borrarRequest(requestRecieved);
//                        Toast.makeText(SendRequest.this, "No se aceptó", Toast.LENGTH_SHORT).show();
//                    }
//
//                } else {
//                    Toast.makeText(SendRequest.this, "Hubo un error al buscar la solicitud", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Request> call, Throwable t) {
//                Toast.makeText(SendRequest.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
//                Log.i("HappyPaws", "Error al buscar request", t);
//            }
//        });
//
//    }
//
//    private void borrarRequest(Request request){
//
//        Call<Void> call = requestService.deleteReq((request.getId()));
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(SendRequest.this, "Solicitud expirada", Toast.LENGTH_SHORT).show();
//
//                } else {
//                    Toast.makeText(SendRequest.this, "Hubo un error al borrar la solicitud", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(SendRequest.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
//                Log.i("HappyPaws", "Error al borrar solicitud", t);
//            }
//        });
//    }


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