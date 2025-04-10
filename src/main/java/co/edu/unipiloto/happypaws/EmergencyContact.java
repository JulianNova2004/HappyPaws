package co.edu.unipiloto.happypaws;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import models.Vet;
import network.Retro;
import network.VetService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmergencyContact extends AppCompatActivity {

    private VetService vetService;

    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_emergency_contact);

        container = findViewById(R.id.containerVets);
        vetService = Retro.getClient().create(VetService.class);
        viewVets();

    }

    public void viewVets(){

        Call<List<Vet>> call = vetService.getVets();
        call.enqueue(new Callback<List<Vet>>() {
            @Override
            public void onResponse(Call<List<Vet>> call, Response<List<Vet>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    container.removeAllViews();
                    List<Vet> vets = response.body();
                    if(vets.isEmpty()){
                        TextView noVet = new TextView(EmergencyContact.this);
                        noVet.setText("No hay veterinarios registrados");
                        noVet.setTextSize(18);
                        noVet.setGravity(Gravity.CENTER);
                        noVet.setPadding(0, 10, 0, 10);
                        container.addView(noVet);
                        //Toast.makeText(Viewvets.this, "No tiene ninguna mascota registrada", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        // vets.addAll(response.body());
                        int i = 1;
                        for(Vet vet: vets){
                            TextView vetD = new TextView(EmergencyContact.this);
                            vetD.setText("vet NUMBER " + i);
                            vetD.setTextSize(20);
                            vetD.setGravity(Gravity.CENTER);
                            vetD.setPadding(0, 10, 0, 10);
                            container.addView(vetD);

                            TextView idRecieved = createTextView("Id: " + vet.getId());
                            TextView nameRecieved = createTextView("Name: " + vet.getName());
                            TextView phoneNumberRecieved = createTextView("Phone: " + vet.getPhoneNumber());
                            TextView specialityRecieved = createTextView("Speciality: " + vet.getSpeciality());
                            TextView emailRecieved = createTextView("Email: " + vet.getEmail());
                            //TextView foodRecieved = createTextView("Food: " + vet.getFood());
                            //TextView amountFoodRecieved = createTextView("Amount of food: " + vet.getAmount_of_food());
                            //TextView amountWalksRecieved = createTextView("Amount of walks: " + vet.getAmount_of_walks());
                            //TextView descriptionRecieved = createTextView("Description: " + vet.getDescription());
                            TextView space = createTextView(" ");
                            space.setTextSize(10);

                            container.addView(idRecieved);
                            container.addView(nameRecieved);
                            container.addView(phoneNumberRecieved);
                            container.addView(specialityRecieved);
                            container.addView(emailRecieved);
                            //container.addView(foodRecieved);
                            //container.addView(amountFoodRecieved);
                            //container.addView(amountWalksRecieved);
                            //container.addView(descriptionRecieved);
                            container.addView(space);
                            i++;
                        }
                    }

                } else {
                    Toast.makeText(EmergencyContact.this, "Correo o contraseña incorrectos, revise sus credenciales", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Vet>> call, Throwable t) {
                Toast.makeText(EmergencyContact.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
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