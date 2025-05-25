package co.edu.unipiloto.happypaws;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import network.RequestService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestSection extends AppCompatActivity {

    private TextView title;
    private Button sendRequest, viewRequestU, viewPendingRequest, viewU, viewP;
    private RequestService requestService;
    private int pasIdSP, userIdSP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_request_section);

        title = findViewById(R.id.titleUsrRe);
        sendRequest = findViewById(R.id.send_request);
        viewRequestU = findViewById(R.id.viewRequestU);
        viewPendingRequest = findViewById(R.id.viewPendingRequest);
        viewPendingRequest.setEnabled(false);
        viewU = findViewById(R.id.viewU);
        viewP = findViewById(R.id.viewP);
        requestService = Retro.getClient().create(RequestService.class);

        SharedPreferences preferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        String username = preferences.getString("Username","");
        //boolean isUser = preferences.getBoolean("isUser", false);
        int typeUser = preferences.getInt("typeUser", -100);
        title.setText("Welcome " + username + "!");

        if(typeUser==0){
            //Usuario
            viewPendingRequest.setVisibility(View.GONE);
            viewU.setVisibility(View.GONE);
            int userId = preferences.getInt("User_ID", 0);
            if(userId!=0){
                userIdSP = userId;
            }
            else{
                Toast.makeText(RequestSection.this, "Se guardó mal el userId", Toast.LENGTH_SHORT).show();
            }

        }else if(typeUser==-1){
            //Paseador
            int paseadorId = preferences.getInt("pasId", 0);
            if(paseadorId!=0){
                pasIdSP = paseadorId;
                bringCount();
            }
            else{
                Toast.makeText(RequestSection.this, "Se guardó mal el pasId", Toast.LENGTH_SHORT).show();
            }
            sendRequest.setVisibility(View.GONE);
            viewRequestU.setVisibility(View.GONE);
            viewP.setVisibility(View.GONE);
        }else{
            if(typeUser != 10) {
                Toast.makeText(RequestSection.this, "Se guardó nullo el typeUser", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void bringCount(){
        Call<Integer> call = requestService.pendingmsg(pasIdSP);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                final int count = (response.isSuccessful() && response.body() != null)
                        ? response.body()
                        : 0;
                //Enabled ya permite accionar boton, antes no deja hacer nada
                runOnUiThread(() -> {
                    viewPendingRequest.setText("Ver solicitudes (" + count + ")");
                    viewPendingRequest.setEnabled(true);
                });

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                runOnUiThread(() -> {
                    viewPendingRequest.setText("Ver solicitudes (0)");
                    viewPendingRequest.setEnabled(true);
                });
            }
        });
    }

    public void sendRequest(View view){
        //Enviar solicitud, solo mostrar las posibles-->Actividad
        Intent intent = new Intent(this,SendRequest.class);
        startActivity(intent);

    }

    public void viewRequestUser(View view){
        //Ver las solicitues de un usuario en especifico-->Actividad (hacer de ultimo)
        Intent intent = new Intent(this,ViewMyRequest.class);
        startActivity(intent);
    }

    public void viewPendingRequest(View view){
        //Ver las solicitudes pendientes de un paseador-->Actividad
        Intent intent = new Intent(this,ViewPendingRequest.class);
        startActivity(intent);
    }

    public void viewUsers(View view){
        //Ver los usuarios aceptados de un paseador
        Intent intent = new Intent(this,ViewUsers.class);
        startActivity(intent);
    }

    public void viewWalkers(View view){
        //Ver los paseadores de un usuario
        Intent intent = new Intent(this,ViewWalkers.class);
        startActivity(intent);

    }
}