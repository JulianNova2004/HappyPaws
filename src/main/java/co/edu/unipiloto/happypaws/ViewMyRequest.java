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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import models.Paseador;
import models.Request;
import network.RequestService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewMyRequest extends AppCompatActivity {

    private LinearLayout containerRequestUser;
    private RequestService requestService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_my_request);

        containerRequestUser = findViewById(R.id.containerRequestUser);
        requestService = Retro.getClient().create(RequestService.class);

        viewMyRequest();
    }

    private void viewMyRequest(){
        SharedPreferences sharedPreferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("User_ID", -10);

        if(userId != -10){
            Call<List<Request>> call = requestService.getRequestUser(userId);
            call.enqueue(new Callback<List<Request>>() {
                @Override
                public void onResponse(Call<List<Request>> call, Response<List<Request>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        containerRequestUser.removeAllViews();
                        List<Request> requests = response.body();
                        if(requests.isEmpty()){
                            TextView noRequest = new TextView(ViewMyRequest.this);
                            noRequest.setText("En el momento, no ha registrado solicitudes.");
                            noRequest.setTextSize(18);
                            noRequest.setGravity(Gravity.CENTER);
                            noRequest.setPadding(0, 10, 0, 10);
                            containerRequestUser.addView(noRequest);
                        }
                        else{
                            int i = 1;
                            for(Request r: requests){

                                TextView number = createHeaderTextView("SOLICITUD NÚMERO " + i);

                                //Contenido, paseador y estado
                                TextView idRecieved = createTextView("Id: " + r.getId());
                                TextView nameRecieved = createTextView("Paseador: " + r.getPaseador().getName());
                                TextView contentRecieved = createTextView("Contenido: " + r.getContenido());

                                String dateRecievedF = r.getDate();
                                dateRecievedF = dateRecievedF.substring(0, 10);

                                //Toast.makeText(ViewMyRequest.this, "Date:" + dateRecievedF, Toast.LENGTH_SHORT).show();

                                TextView dateRecieved = createTextView("Fecha de envio: " + dateRecievedF);

                                int state = r.getEstado();
                                String stateStr = "";
                                switch (state){
                                    case -1:
                                        stateStr = "Rechazada";
                                        break;
                                    case 0:
                                        stateStr = "Enviada";
                                        break;
                                    case 1:
                                        stateStr = "Aceptada";
                                        break;
                                }
                                TextView stateRecieved = createTextView("Estado: " + stateStr);

                                TextView space = createTextView(" ");
                                space.setTextSize(10);

                                containerRequestUser.addView(number);
                                containerRequestUser.addView(idRecieved);
                                containerRequestUser.addView(nameRecieved);
                                containerRequestUser.addView(contentRecieved);
                                containerRequestUser.addView(dateRecieved);
                                containerRequestUser.addView(stateRecieved);
                                containerRequestUser.addView(space);

                                i++;
                            }
                        }

                    } else {
                        Toast.makeText(ViewMyRequest.this, "No se pudo obtener la lista de solicitudes.", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(SendRequest.this, "Error al buscar paseadores disponibles", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<List<Request>> call, Throwable t) {
                    Toast.makeText(ViewMyRequest.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("HappyPaws", "Error al visualizar solicitudes", t);
                }

            });
        }
        else{
            Toast.makeText(ViewMyRequest.this, "id -10 guardado, revisar", Toast.LENGTH_SHORT).show();
        }
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