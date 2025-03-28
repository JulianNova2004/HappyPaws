package co.edu.unipiloto.happypaws;

import android.content.SharedPreferences;
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

import models.Pet;
import network.PetService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewPets extends AppCompatActivity {

    private PetService petService;
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_pets);

        container = findViewById(R.id.containerPetsV);
        petService = Retro.getClient().create(PetService.class);
        viewPets();
    }

    public void viewPets(){
        SharedPreferences sharedPreferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("User_ID", -1);

        if(userId != -1){
            Call<List<Pet>> call = petService.getAllPets(userId);
            call.enqueue(new Callback<List<Pet>>() {
                @Override
                public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        container.removeAllViews();
                        List<Pet> pets = response.body();
                        if(pets.isEmpty()){
                            TextView noPet = new TextView(ViewPets.this);
                            noPet.setText("No tiene mascotas registradas");
                            noPet.setTextSize(18);
                            noPet.setGravity(Gravity.CENTER);
                            noPet.setPadding(0, 10, 0, 10);
                            container.addView(noPet);
                            //Toast.makeText(ViewPets.this, "No tiene ninguna mascota registrada", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            // pets.addAll(response.body());
                            int i = 1;
                            for(Pet pet: pets){
                                TextView petD = new TextView(ViewPets.this);
                                petD.setText("PET NUMBER " + i);
                                petD.setTextSize(20);
                                petD.setGravity(Gravity.CENTER);
                                petD.setPadding(0, 10, 0, 10);
                                container.addView(petD);

                                TextView idRecieved = createTextView("Id: " + pet.getPetId());
                                TextView nameRecieved = createTextView("Name: " + pet.getName());
                                TextView braceRecieved = createTextView("Breed: " + pet.getRace());
                                TextView ageRecieved = createTextView("Age: " + pet.getAge());
                                TextView weightRecieved = createTextView("Weight: " + pet.getWeight());
                                TextView foodRecieved = createTextView("Food: " + pet.getFood());
                                TextView amountFoodRecieved = createTextView("Amount of food: " + pet.getAmount_of_food());
                                TextView amountWalksRecieved = createTextView("Amount of walks: " + pet.getAmount_of_walks());
                                TextView descriptionRecieved = createTextView("Description: " + pet.getDescription());
                                TextView space = createTextView(" ");
                                space.setTextSize(10);
                                /*
                                id
                                name
                                brace
                                age
                                weight
                                food
                                amount_of_food
                                amount_of_walks
                                description
                                 */
                                container.addView(idRecieved);
                                container.addView(nameRecieved);
                                container.addView(braceRecieved);
                                container.addView(ageRecieved);
                                container.addView(weightRecieved);
                                container.addView(foodRecieved);
                                container.addView(amountFoodRecieved);
                                container.addView(amountWalksRecieved);
                                container.addView(descriptionRecieved);
                                container.addView(space);
                                i++;
                            }
                        }

                    } else {
                        Toast.makeText(ViewPets.this, "Correo o contraseña incorrectos, revise sus credenciales", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<List<Pet>> call, Throwable t) {
                    Toast.makeText(ViewPets.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("HappyPaws", "Error al iniciar sesión", t);
                }

            });
        }
        else{
            Toast.makeText(ViewPets.this, "id -1 guardado, revisar", Toast.LENGTH_SHORT).show();
        }

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