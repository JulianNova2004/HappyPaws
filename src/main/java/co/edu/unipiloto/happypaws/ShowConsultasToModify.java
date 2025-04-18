package co.edu.unipiloto.happypaws;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import network.ConsultationService;
import network.RecordatoryService;
import network.Retro;

public class ShowConsultasToModify extends AppCompatActivity {

    private LinearLayout container;
    private Button btnSendModifyConsultas;
    private EditText consultaId;
    private ConsultationService consultationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_consultas_to_modify);

        container = findViewById(R.id.containerConsultasM);
        btnSendModifyConsultas = findViewById(R.id.btnSendModifyConsultas);
        consultaId = findViewById(R.id.consultaID);

        consultationService = Retro.getClient().create(ConsultationService.class);

        btnSendModifyConsultas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sendConsultaId();
            }
        });
        viewConsultas();
    }

    public void viewConsultas(){
        SharedPreferences sharedPreferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("User_ID", -1);


    }

    public void sendConsultaId(){

    }
}