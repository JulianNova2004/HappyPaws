package co.edu.unipiloto.happypaws;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LocationSection extends AppCompatActivity {

    private TextView titleUsrL;

    private Button send_select_pet, send_ubication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_location_section);

        titleUsrL = findViewById(R.id.titleUsrL);
        send_select_pet = findViewById(R.id.send_select_pet);
        send_ubication = findViewById(R.id.send_ubication);

        SharedPreferences preferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        String username = preferences.getString("Username","");
        titleUsrL.setText("Welcome " + username + "!");

        int typeUser = preferences.getInt("typeUser", -100);
        if(typeUser==-1){
            //Paseador
            send_select_pet.setVisibility(View.GONE);

        }else if(typeUser==0){
            //Usuario
            send_ubication.setVisibility(View.GONE);

        } else {
            //O se guardo nulo con el sharedPreferences o por algún motivo se guardo veterinario/admin
            Log.i("HappyPaws: SP TypeUser", "Este es el typeUser guardado: " + typeUser + ".\n1 -> Veterinario; 10 -> Admin; 100 -> Nulo");
            Toast.makeText(LocationSection.this, "Se guardó nulo el typeUser", Toast.LENGTH_SHORT).show();
        }

    }


    public void sendSelectPet(View view){

        //SelectPetUbi
        Intent intent = new Intent(this,SelectPetUbi.class);
        startActivity(intent);
    }

    public void sendUbicationRealTime(View view){

        //startSendingUbication
        Intent intent = new Intent(this,StatsSection.class);
        startActivity(intent);
    }

}