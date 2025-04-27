package co.edu.unipiloto.happypaws;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import models.Consulta;
import models.History;
import network.ConsultationService;
import network.HistoryService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteVaccine extends AppCompatActivity {

    private Button btnNext, btnConfirm;

    private EditText hisId, confirmation;
    private TextView continue1, warning;
    private boolean isValid;
    private HistoryService historyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delete_vaccine);

        hisId = findViewById(R.id.hisIdD);
        btnNext = findViewById(R.id.send_comprobation);
        btnConfirm = findViewById(R.id.btnConfirm);
        confirmation = findViewById(R.id.confirmation);
        continue1 = findViewById(R.id.continue1);
        warning = findViewById(R.id.warning);

        historyService = Retro.getClient().create(HistoryService.class);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review();
            }
        });

    }

    public void validateFields(){
        isValid = true;
        String hisIdStr = hisId.getText().toString().trim();
        if (hisIdStr.isEmpty() || !hisIdStr.matches("\\d+")) {
            hisId.setError("Debe ingresar un ID de consulta válido");
            isValid = false;
        }

        if(isValid) {
            Toast.makeText(this, "Lleno todos los campos correctamente", Toast.LENGTH_SHORT).show();
            show();
        }
        else Toast.makeText(this, "Caremonda llene bien todos los campos", Toast.LENGTH_SHORT).show();
    }

    public void show(){

        continue1.setVisibility(View.VISIBLE);
        confirmation.setVisibility(View.VISIBLE);
        btnConfirm.setVisibility(View.VISIBLE);
        warning.setVisibility(View.VISIBLE);

    }

    public void review(){
        boolean borrar = true;
        SharedPreferences sharedPreferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        String password = sharedPreferences.getString("Password", "");
        String passwordInput = confirmation.getText().toString();
        if(password.isEmpty()){
            Toast.makeText(this, "Revisar shared preference", Toast.LENGTH_SHORT).show();
            borrar = false;
        }
        if(passwordInput.isEmpty()){
            Toast.makeText(this, "No ha ingresado una contraseña", Toast.LENGTH_SHORT).show();
            borrar = false;
        }
        if(password.equals(passwordInput) && borrar){
            Toast.makeText(this, "Lleno todos los campos correctamente", Toast.LENGTH_SHORT).show();
            deleteVaccine();
        }
        else Toast.makeText(this, "La contraseña ingresada es incorrecta", Toast.LENGTH_SHORT).show();
    }

    public void deleteVaccine(){
        int hisIdInt = (!hisId.getText().toString().trim().isEmpty()) ?
                Integer.parseInt(hisId.getText().toString().trim()) : 0;

        Call<History> call = historyService.deleteHistory(hisIdInt);

        call.enqueue(new Callback<History>() {
            @Override
            public void onResponse(Call<History> call, Response<History> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DeleteVaccine.this, "Consulta eliminada", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DeleteVaccine.this, Home.class);
                    startActivity(intent);}
                else{
                    Toast.makeText(DeleteVaccine.this, "Error al eliminar consulta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<History> call, Throwable t) {
                Toast.makeText(DeleteVaccine.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("HappyPaws", "Error al eliminar consulta", t);
            }
        });
        //else {
        //    Toast.makeText(DeleteConsulta.this, "Toast de error", Toast.LENGTH_SHORT).show();
        //}

    }
}