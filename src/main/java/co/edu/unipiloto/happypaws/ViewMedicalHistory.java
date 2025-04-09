package co.edu.unipiloto.happypaws;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import models.Consulta;
import network.ConsultationService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewMedicalHistory extends AppCompatActivity {

    private ConsultationService consultationService;
    private LinearLayout containerC;
    private Button btnBringInfoPets;
    private EditText petId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medical_history);

        containerC = findViewById(R.id.containerInformationC);
        petId = findViewById(R.id.petIDC);
        btnBringInfoPets = findViewById(R.id.btn_bring_consultation_info);
        consultationService = Retro.getClient().create(ConsultationService.class);

        btnBringInfoPets.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                review();

            }
        });
    }

    public void review(){
        String idPetStr = petId.getText().toString().trim();
        int idPet = Integer.parseInt(idPetStr);
        if (idPetStr.isEmpty()) {
            petId.setError("No deje este campo vacio");
            Toast.makeText(ViewMedicalHistory.this, "Caremonda ponga algo", Toast.LENGTH_SHORT).show();
            return;
        }
        /*
        int idPet;

        try {
            idPet = Integer.parseInt(idPetStr);
        } catch (NumberFormatException e) {
            Toast.makeText(ViewMedicalHistory.this, "Caremonda ponga un número válido", Toast.LENGTH_SHORT).show();
            return;
        }
         */

        if (idPet == 0) {
            petId.setError("Ingrese un número válido");
            Toast.makeText(ViewMedicalHistory.this, "Caremonda ponga un número mayor a 0", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(ViewMedicalHistory.this, "Ingresó un ID válido", Toast.LENGTH_SHORT).show();
        bringInfo(idPet);
    }

    public void bringInfo(int idPet){
        Call<List<Consulta>> call = consultationService.getConsultas(idPet);
        call.enqueue(new Callback<List<Consulta>>() {
            @Override
            public void onResponse(Call<List<Consulta>> call, Response<List<Consulta>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    containerC.removeAllViews();
                    List<Consulta> consultas = response.body();
                    if(consultas.isEmpty()){
                        TextView noConsulta = new TextView(ViewMedicalHistory.this);
                        noConsulta.setText("No tiene informacion de consulta para esta mascota");
                        noConsulta.setTextSize(18);
                        noConsulta.setGravity(Gravity.CENTER);
                        noConsulta.setPadding(0, 10, 0, 10);
                        containerC.addView(noConsulta);
                        //Toast.makeText(ShowPetsToModify.this, "No tiene ninguna mascota registrada", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        // pets.addAll(response.body());
                        int i = 1;
                        for(Consulta c: consultas){
                            TextView info = new TextView(ViewMedicalHistory.this);
                            info.setText("CONSULTA NÚMERO " + i);
                            info.setTextSize(20);
                            info.setGravity(Gravity.CENTER);
                            info.setPadding(0, 10, 0, 10);
                            containerC.addView(info);

                            TextView idRecieved = createTextView("Id: " + c.getId());
                            TextView dateRecieved = createTextView("Fecha: " + c.getFecha());
                            TextView reasonRecieved = createTextView("Motivo: " + c.getMotivo());
                            TextView stateRecieved = createTextView("Estado: " + c.getEstado());
                            TextView vetRecieved = createTextView("Veterinario: " + c.getVeterinario());
                            TextView resultsRecieved = createTextView("Veterinario: " + c.getVeterinario());
                            TextView space = createTextView(" ");
                            space.setTextSize(10);

                            containerC.addView(idRecieved);
                            containerC.addView(dateRecieved);
                            containerC.addView(reasonRecieved);
                            containerC.addView(stateRecieved);
                            containerC.addView(vetRecieved);
                            containerC.addView(resultsRecieved);
                            //containerC.addView(nameRecieved);
                            containerC.addView(space);

                            i++;
                        }
                    }

                } else {
                    Toast.makeText(ViewMedicalHistory.this, "TAS MAL", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Consulta>> call, Throwable t) {
                Toast.makeText(ViewMedicalHistory.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("HappyPaws", "Error al buscar consultas", t);
            }

        });
    }

    private TextView createTextView(String data){
        TextView textView = new TextView(this);

        int lwidth = dpPx(200);
        int lheight = dpPx(50);
        int lmargin = dpPx(16);
        int gravity = Gravity.CENTER;

        LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(lwidth, lheight);
        parameters.gravity = gravity;
        parameters.setMargins(lmargin, lmargin, lmargin, lmargin);

        textView.setLayoutParams(parameters);
        textView.setText(data);
        textView.setGravity(gravity);

        return textView;
    }

    private int dpPx(int dp){
        return (int) (dp * getResources().getDisplayMetrics().density);
    }


}