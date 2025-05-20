package co.edu.unipiloto.happypaws;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import java.util.ArrayList;

import models.Pet;
import network.PetService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetRegister extends AppCompatActivity {

    private PetService petService;
    private LinearLayout container;
    private EditText numberOfPets;
    private Button generateFieldsBtn, registerPetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_pet);

        numberOfPets = findViewById(R.id.number_pets);
        generateFieldsBtn = findViewById(R.id.generate_fields);
        registerPetBtn = findViewById(R.id.register_pet);
        container = findViewById(R.id.dynamic_container);
        petService = Retro.getClient().create(PetService.class);

        generateFieldsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                generateFields();

            }
        });

        registerPetBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                registerPets();
                //sendRegisterPets();
            }
        });

    }

    private void registerPets(){
        boolean registerIsValid = true;

        if(container.getChildCount()==0) {
            registerIsValid=false;
            Toast.makeText(PetRegister.this, "Ingrese el numero de mascotas que tenga", Toast.LENGTH_SHORT).show();
        }
        //JSONArray petsArray = new JSONArray();

        else{
            for (int i = 0; i<container.getChildCount(); i++) {
                //JSONObject petObject = new JSONObject();

                View view = container.getChildAt(i);
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    String contenido = editText.getText().toString().trim();


                    if (contenido.isEmpty()) {
                        editText.setError("Debe llenar este campo");
                        registerIsValid = false;
                        continue;
                    }
                    int input = editText.getInputType();
                    String tag = (String) editText.getTag();

                    if (tag != null && (tag.equals("dailyWalks") || tag.equals("amountOfMeals"))) {
                        if (!contenido.matches("\\d+")) {
                            editText.setError("Debe ingresar un número entero válido");
                            registerIsValid = false;
                            continue;
                        }
                        int numero = Integer.parseInt(contenido);
                        if (numero <= 0 || numero > 5) {
                            editText.setError("Debe ingresar un número entre 1 y 5");
                            registerIsValid = false;
                        }
                    }


                    if ((input & InputType.TYPE_NUMBER_FLAG_DECIMAL) == InputType.TYPE_NUMBER_FLAG_DECIMAL) {
                        if (!contenido.matches("\\d+(\\.\\d+)?")) {
                            editText.setError("Debe ingresar un número decimal válido");
                            registerIsValid = false;
                        }
                    }

                    if (tag != null && tag.equals("petName")) {
                        if (!contenido.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
                            editText.setError("El nombre de la mascota solo debe contener letras");
                            registerIsValid = false;
                        }
                    }
                    /*
                    if ((input & InputType.TYPE_CLASS_NUMBER) == InputType.TYPE_CLASS_NUMBER &&
                            (input & InputType.TYPE_NUMBER_FLAG_DECIMAL) == InputType.TYPE_NUMBER_FLAG_DECIMAL) {
                        try {
                            Float.parseFloat(contenido);
                        } catch (NumberFormatException e) {
                            editText.setError("Debe ser un número decimal válido");
                            registerIsValid = false;
                        }
                    }
                    */
                }


            }
        }
        if(registerIsValid) {
            Toast.makeText(PetRegister.this, "Lleno todos los campos correctamente", Toast.LENGTH_SHORT).show();
            sendRegisterPets();
        }
        else Toast.makeText(PetRegister.this, "Caremonda llene bien todos los campos", Toast.LENGTH_SHORT).show();

    }

    private void sendRegisterPets(){
        ArrayList<Pet> pets = new ArrayList<Pet>();

        for (int i = 1; i<container.getChildCount(); i += 8) {
            if(i!=1) i++;
            String name = ((EditText) container.getChildAt(i)).getText().toString();
            Double age = Double.parseDouble(((EditText) container.getChildAt(i + 1)).getText().toString());
            String race = ((EditText) container.getChildAt(i + 2)).getText().toString();
            int amount_of_walks = Integer.parseInt(((EditText) container.getChildAt(i + 3)).getText().toString());
            String food = ((EditText) container.getChildAt(i + 4)).getText().toString();
            int amount_of_food = Integer.parseInt(((EditText) container.getChildAt(i + 5)).getText().toString());
            int weight = Integer.parseInt(((EditText) container.getChildAt(i + 6)).getText().toString());
            String description = ((EditText) container.getChildAt(i + 7)).getText().toString();

            Pet pet = new Pet(name,race,amount_of_walks,amount_of_food,food,weight,description,age);
            pets.add(pet);
            //Toast.makeText(PetRegister.this, "PET_age= " + pet.getAge() + ".", Toast.LENGTH_SHORT).show();
        }
        Gson gson = new Gson();
        String json = gson.toJson(pets);
        Log.d("HappyPaws", "JSON enviado: " + json);

        SharedPreferences preferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        int userId = preferences.getInt("User_ID",-1);

        Toast.makeText(PetRegister.this, "User_ID = " + userId + ".", Toast.LENGTH_SHORT).show();
        Call<Void> call = petService.savePets(pets,userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PetRegister.this, "Mascotas registradas con éxito :D", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PetRegister.this, Home.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(PetRegister.this, "Hubo un error al registrar las mascotas :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(PetRegister.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("HappyPaws", "Error al registrar mascotas", t);
            }
        });

    }

    private void generateFields(){
        container.removeAllViews();

        String numberOfPetsStr = numberOfPets.getText().toString().trim();

        if (numberOfPetsStr.isEmpty()) {
            Toast.makeText(PetRegister.this, "Caremonda ponga algo", Toast.LENGTH_SHORT).show();
            return;
        }
        int numberPets;

        try {
            numberPets = Integer.parseInt(numberOfPetsStr);
        } catch (NumberFormatException e) { 
            Toast.makeText(PetRegister.this, "Caremonda ponga un número válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (numberPets == 0) {
            Toast.makeText(PetRegister.this, "Caremonda ponga un número mayor a 0", Toast.LENGTH_SHORT).show();
            return;
        }


        for (int i = 1; i<= numberPets; i++){
            TextView pet = createHeaderTextView("Pet number " + i);

            container.addView(pet);

            //EditText nameInput = createEditText("Name", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            EditText nameInput = createEditText("Name", InputType.TYPE_CLASS_TEXT);
            EditText age = createEditText("Age", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            EditText breedInput = createEditText("Breed", InputType.TYPE_CLASS_TEXT);
            EditText dailyWalksInput = createEditText("Daily Walks", InputType.TYPE_CLASS_NUMBER);
            dailyWalksInput.setTag("dailyWalks");
            EditText foodInput = createEditText("Food", InputType.TYPE_CLASS_TEXT);
            EditText amountMeals = createEditText("Amount of Meals", InputType.TYPE_CLASS_NUMBER);
            amountMeals.setTag("amountOfMeals");
            EditText weightInput = createEditText("Weight", InputType.TYPE_CLASS_NUMBER);
            //EditText idInput = createEditText("Owner ID", InputType.TYPE_CLASS_TEXT);
            EditText descriptionInput = createEditText("Description", InputType.TYPE_CLASS_TEXT);

            container.addView(nameInput);
            container.addView(age);
            container.addView(breedInput);
            container.addView(dailyWalksInput);
            container.addView(foodInput);
            container.addView(amountMeals);
            container.addView(weightInput);
            //container.addView(idInput);
            container.addView(descriptionInput);
        }

        //container.addView(registerPetBtn);

    }

    private EditText createEditText(String hint, int type){
        EditText editText = new EditText(this);

        int lwidth = dpPx(200);
        int lheight = dpPx(50);
        int lmargin = dpPx(16);
        int gravity = Gravity.CENTER;

        LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(lwidth, lheight);
        parameters.gravity = gravity;
        parameters.setMargins(lmargin, lmargin, lmargin, lmargin);

        int accent = ContextCompat.getColor(this, R.color.brand_accent);
        editText.setTextColor(accent);
        editText.setHintTextColor(accent);
        ColorStateList tint = ContextCompat.getColorStateList(this, R.color.brand_secondary);
        editText.setBackgroundTintList(tint);

        editText.setLayoutParams(parameters);
        editText.setHint(hint);
        editText.setInputType(type);
        editText.setGravity(gravity);

        //editText.setPadding(lmargin, lmargin, lmargin, lmargin);

        return editText;
        /*
        1. textCapWords
        2. text
        3. number
        4. numberDecimal
         */
    }

    private TextView createHeaderTextView(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        int primary = ContextCompat.getColor(this, R.color.brand_primary);

        //tv.setTextColor(Color.parseColor("#ffaa75"));
        tv.setTextColor(primary);
        tv.setGravity(Gravity.CENTER);
        int pad = dpPx(1);
        tv.setPadding(0, pad, 0, pad);
        return tv;
    }
    private int dpPx(int dp){
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}