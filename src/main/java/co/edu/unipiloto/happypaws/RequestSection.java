package co.edu.unipiloto.happypaws;

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

public class RequestSection extends AppCompatActivity {

    private TextView title;
    private Button sendRequest, viewRequestU, viewPendingRequest, viewU, viewP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_request_section);

        title = findViewById(R.id.titleUsrRe);
        sendRequest = findViewById(R.id.send_request);
        viewRequestU = findViewById(R.id.viewRequestU);
        viewPendingRequest = findViewById(R.id.viewPendingRequest);
        viewU = findViewById(R.id.viewU);
        viewP = findViewById(R.id.viewP);

        SharedPreferences preferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        String username = preferences.getString("Username","");
        //boolean isUser = preferences.getBoolean("isUser", false);
        int typeUser = preferences.getInt("typeUser", -100);
        title.setText("Welcome " + username + "!");

        if(typeUser==0){
            //Usuario
            viewPendingRequest.setVisibility(View.GONE);
            viewU.setVisibility(View.GONE);
        }else if(typeUser==-1){
            //Paseador
            sendRequest.setVisibility(View.GONE);
            viewRequestU.setVisibility(View.GONE);
            viewP.setVisibility(View.GONE);
        }else{
            if(typeUser != 10) {
                Toast.makeText(RequestSection.this, "Se guardó nullo el typeUser", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void sendRequest(View view){
        //Enviar solicitud, solo mostrar las posibles-->Actividad

    }

    public void viewRequestUser(View view){
        //Ver las solicitues de un usuario en especifico-->Actividad

    }

    public void viewPendingRequest(View view){
        //Ver las solicitudes pendientes de un paseador-->Actividad

    }

    public void viewUsers(View view){
        //Ver los usuarios aceptados de un paseador

    }

    public void viewWalkers(View view){
        //Ver los paseadores de un usuario

    }
}