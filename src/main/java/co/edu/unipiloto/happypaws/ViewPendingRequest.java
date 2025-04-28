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

import java.util.ArrayList;
import java.util.List;

import models.Paseador;
import models.Pet;
import models.Request;
import models.User;
import network.RequestService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewPendingRequest extends AppCompatActivity {

    private LinearLayout containerAvailableRequest;
    private EditText insertIDAcept, insertIDDecline;
    private Button btnAcceptRequest, btnDeclineRequest;
    private RequestService requestService;
    private List<Integer> availableID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_pending_request);

        containerAvailableRequest = findViewById(R.id.containerAvailableRequest);
        insertIDAcept = findViewById(R.id.insertIDAcept);
        insertIDDecline = findViewById(R.id.insertIDDecline);
        btnAcceptRequest = findViewById(R.id.btnAcceptRequest);
        btnDeclineRequest = findViewById(R.id.btnDeclineRequest);

        requestService = Retro.getClient().create(RequestService.class);

        SharedPreferences preferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        int paseadorId = preferences.getInt("pasId", 0);

        bringRequest(paseadorId);
        btnAcceptRequest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                reviewA();
            }
        });

        btnDeclineRequest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                reviewD();
            }
        });

    }

    public void bringRequest(int paseadorId){
        Call<List<Request>> call = requestService.pendingRequest(paseadorId);
        call.enqueue(new Callback<List<Request>>() {
            @Override
            public void onResponse(Call<List<Request>> call, Response<List<Request>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    availableID.clear();
                    containerAvailableRequest.removeAllViews();
                    List<Request> requests = response.body();
                    if(requests.isEmpty()){
                        TextView noRequest = new TextView(ViewPendingRequest.this);
                        noRequest.setText("En el momento, no hay solicitudes pendientes");
                        noRequest.setTextSize(18);
                        noRequest.setGravity(Gravity.CENTER);
                        noRequest.setPadding(0, 10, 0, 10);
                        containerAvailableRequest.addView(noRequest);
                    }
                    else{
                        int i = 1;
                        for(Request r: requests) {

                            TextView number = createHeaderTextView("SOLICITUD NÚMERO " + i);

                            TextView idRecieved = createTextView("Id de solicitud: " + r.getId());
                            availableID.add(r.getId());
                            String usuario = "\nUsuario: " +r.getUsuario().getFirstname() + " " + r.getUsuario().getLastname() + "\n";
                            String petListStr = "Información de mascotas: [\n";
                            for(Pet pet: r.getUsuario().getPets()){
                                petListStr += pet.getName() + ": " + pet.getRace() + " -> "+ "\n" + "Cantidad de paseos: " + pet.getAmount_of_walks() + ",\n";
                            }
                            petListStr = petListStr.substring(0, petListStr.length()-2);
                            petListStr += "\n]";
                            String rtaU = usuario + petListStr;
                            TextView userRecieved = createTextView("Información del Usuario: " + rtaU);
                            TextView contentRecieved = createTextView("Contenido: " + r.getContenido());

                            TextView space = createTextView(" ");
                            space.setTextSize(10);

                            containerAvailableRequest.addView(number);
                            containerAvailableRequest.addView(idRecieved);
                            containerAvailableRequest.addView(userRecieved);
                            containerAvailableRequest.addView(contentRecieved);
                            containerAvailableRequest.addView(space);

                            i++;
                        }
                    }
                }else{
                    Toast.makeText(ViewPendingRequest.this, "No se pudo obtener la lista de paseadores.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Request>> call, Throwable t) {
                Toast.makeText(ViewPendingRequest.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("HappyPaws", "Error al buscar paseadores", t);
            }
        });
    }

    public void reviewA(){
        String acceptIdStr = insertIDAcept.getText().toString().trim();
        if (acceptIdStr.isEmpty()) {
            insertIDAcept.setError("No deje este campo vacio");
            Toast.makeText(ViewPendingRequest.this, "Caremonda ponga algo", Toast.LENGTH_SHORT).show();
            return;
        }
        int idAccepted = Integer.parseInt(acceptIdStr);
        if (idAccepted <= 0 || !availableID.contains(Integer.valueOf(idAccepted))) {
            insertIDAcept.setError("Ingrese un ID válido, revise la lista de opciones");
            Toast.makeText(ViewPendingRequest.this, "Caremonda ponga un número válido", Toast.LENGTH_SHORT).show();
            return;
        }
        evaluateRequest(idAccepted, 1);
    }

    public void reviewD(){
        String declineIdStr = insertIDDecline.getText().toString().trim();
        if (declineIdStr.isEmpty()) {
            insertIDDecline.setError("No deje este campo vacio");
            Toast.makeText(ViewPendingRequest.this, "Caremonda ponga algo", Toast.LENGTH_SHORT).show();
            return;
        }
        int idDeclined = Integer.parseInt(declineIdStr);
        if (idDeclined <= 0 || !availableID.contains(Integer.valueOf(idDeclined))) {
            insertIDDecline.setError("Ingrese un ID válido, revise la lista de opciones");
            Toast.makeText(ViewPendingRequest.this, "Caremonda ponga un número válido", Toast.LENGTH_SHORT).show();
            return;
        }
        evaluateRequest(idDeclined, -1);
    }

    private void evaluateRequest(int request_id, int estado){
        Call<Request> call = requestService.edit(request_id, estado);
        call.enqueue(new Callback<Request>() {
            @Override
            public void onResponse(Call<Request> call, Response<Request> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ViewPendingRequest.this, "Solicitud evaluada exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ViewPendingRequest.this, Home.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ViewPendingRequest.this, "Hubo un error al evaluar la solicitud", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Request> call, Throwable t) {
                Toast.makeText(ViewPendingRequest.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("HappyPaws", "Error al evaluar solicitud", t);
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