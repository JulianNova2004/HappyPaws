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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import models.Consulta;
import models.Pet;
import network.ConsultationService;
import network.PetService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicalHistory extends AppCompatActivity {

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
            Toast.makeText(MedicalHistory.this, "Caremonda ponga algo", Toast.LENGTH_SHORT).show();
            return;
        }
        /*
        int idPet;

        try {
            idPet = Integer.parseInt(idPetStr);
        } catch (NumberFormatException e) {
            Toast.makeText(MedicalHistory.this, "Caremonda ponga un número válido", Toast.LENGTH_SHORT).show();
            return;
        }
         */

        if (idPet == 0) {
            petId.setError("Ingrese un número válido");
            Toast.makeText(MedicalHistory.this, "Caremonda ponga un número mayor a 0", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(MedicalHistory.this, "Ingresó un ID válido", Toast.LENGTH_SHORT).show();
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
                        TextView noConsulta = new TextView(MedicalHistory.this);
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
                            TextView info = new TextView(MedicalHistory.this);
                            info.setText("CONSULTA NÚMERO " + i);
                            info.setTextSize(20);
                            info.setGravity(Gravity.CENTER);
                            info.setPadding(0, 10, 0, 10);
                            containerC.addView(info);

                            /*
                            TextView idRecieved = createTextView("Id: " + pet.getPetId());
                            TextView nameRecieved = createTextView("Name: " + pet.getName());
                            TextView space = createTextView(" ");
                            space.setTextSize(10);

                            containerC.addView(idRecieved);
                            containerC.addView(nameRecieved);
                            containerC.addView(space);
                            
                             */
                            i++;
                        }
                    }

                } else {
                    Toast.makeText(MedicalHistory.this, "Correo o contraseña incorrectos, revise sus credenciales", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Consulta>> call, Throwable t) {
                Toast.makeText(MedicalHistory.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("HappyPaws", "Error al iniciar sesión", t);
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