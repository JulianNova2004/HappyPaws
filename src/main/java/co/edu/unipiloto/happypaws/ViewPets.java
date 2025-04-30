package co.edu.unipiloto.happypaws;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
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
                                TextView petD = createHeaderTextView("PET NUMBER " + i);

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
                        Toast.makeText(ViewPets.this, "Error al visualizar mascotas", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<List<Pet>> call, Throwable t) {
                    Toast.makeText(ViewPets.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("HappyPaws", "Error al visualizar mascotas", t);
                }

            });
        }
        else{
            Toast.makeText(ViewPets.this, "id -1 guardado, revisar", Toast.LENGTH_SHORT).show();
        }

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