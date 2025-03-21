package co.edu.unipiloto.happypaws;

import android.os.Bundle;
import android.text.InputType;
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

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import models.Pet;
import network.PetService;
import network.Retro;
import network.UserService;
import retrofit2.Call;

public class PetRegister extends AppCompatActivity {

    private PetService petService;
    private LinearLayout container;
    private EditText numberOfPets;
    private Button generateFieldsBtn, registerPetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.register_pet);

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
            Toast.makeText(this, "Ingrese el numero de mascotas que tenga", Toast.LENGTH_SHORT).show();
        }
        //JSONArray petsArray = new JSONArray();

        else{
            for (int i = 0; i<container.getChildCount(); i++) {
                //JSONObject petObject = new JSONObject();

                View view = container.getChildAt(i);
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    String contenido = editText.getText().toString().trim();
                    int input = editText.getInputType();


                    if (contenido.isEmpty()) {
                        editText.setError("Debe llenar este campo");
                        registerIsValid = false;
                        //continue;
                    }

                    if ((input & InputType.TYPE_CLASS_NUMBER) == InputType.TYPE_CLASS_NUMBER) {
                        try {
                            int numero = Integer.parseInt(contenido);

                            if ("dailyWalks".equals(editText.getTag())) {
                                if (numero <= 0 || numero > 5) {
                                    editText.setError("Debe ingresar un número entre 1 y 5 para los paseos diarios");
                                    registerIsValid = false;
                                }
                            }

                            if ("amountOfMeals".equals(editText.getTag())) {
                                if (numero <= 0 || numero > 5) {
                                    editText.setError("Debe ingresar un número entre 1 y 5 para las comidas diarias");
                                    registerIsValid = false;
                                }
                            }

                        } catch (NumberFormatException e) {
                            editText.setError("Debe ingresar un número entero válido");
                            registerIsValid = false;
                        }
                    }

                    if ((input & InputType.TYPE_CLASS_NUMBER) == InputType.TYPE_CLASS_NUMBER &&
                            (input & InputType.TYPE_NUMBER_FLAG_DECIMAL) == InputType.TYPE_NUMBER_FLAG_DECIMAL) {
                        try {
                            Float.parseFloat(contenido);
                        } catch (NumberFormatException e) {
                            editText.setError("Debe ser un número decimal válido");
                            registerIsValid = false;
                        }
                    }
                }


            }
        }
        if(registerIsValid) {
            Toast.makeText(this, "Lleno todos los campos correctamente", Toast.LENGTH_SHORT).show();
            sendRegisterPets();
        }
        else Toast.makeText(this, "Caremonda llene bien todos los campos", Toast.LENGTH_SHORT).show();

    }

    private void sendRegisterPets(){
        ArrayList<Pet> pets = new ArrayList<Pet>();

        for (int i = 0; i<container.getChildCount(); i += 8) {
            String name = ((EditText) container.getChildAt(i)).getText().toString();
            double age = ((Double.parseDouble((((EditText) container.getChildAt(i + 1)).getText()).toString())));
            String race = ((EditText) container.getChildAt(i + 2)).getText().toString();
            int amount_of_walks = Integer.parseInt(((EditText) container.getChildAt(i + 3)).getText().toString());
            String food = ((EditText) container.getChildAt(i + 4)).getText().toString();
            int amount_of_food = Integer.parseInt(((EditText) container.getChildAt(i + 5)).getText().toString());
            int weight = Integer.parseInt(((EditText) container.getChildAt(i + 6)).getText().toString());
            String description = ((EditText) container.getChildAt(i + 8)).getText().toString();

            Pet pet = new Pet(name,race,amount_of_walks,amount_of_food,food,weight,description,age);
            pets.add(pet);
        }

        //Call<Pet> call = petService.savePets(pets,id_owner);





//        String url = "url";
//        JSONArray petsArray = new JSONArray();
//
//        for (int i = 0; i<container.getChildCount(); i++){
//            View view = container.getChildAt(i);
//
//            if(view instanceof EditText){
//                EditText editText = (EditText) view;
//                String value = editText.getText().toString().trim();
//
//                if(value.isEmpty()){
//                    Toast.makeText(this, "Deben de llenarse todos los campos", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//            }
//        }
//
//        for (int i = 0; i<container.getChildCount(); i += 8){
//            try {
//                JSONObject petObject = new JSONObject();
//                petObject.put("name", ((EditText) container.getChildAt(i)).getText().toString());
//                petObject.put("age", ((Double.parseDouble((((EditText) container.getChildAt(i+1)).getText()).toString()))));
//                petObject.put("race", ((EditText) container.getChildAt(i + 2)).getText().toString());
//                petObject.put("amount_of_walks", Integer.parseInt(((EditText) container.getChildAt(i + 3)).getText().toString()));
//                petObject.put("food", ((EditText) container.getChildAt(i + 4)).getText().toString());
//                petObject.put("amount_of_foods", Integer.parseInt(((EditText) container.getChildAt(i + 5)).getText().toString()));
//                petObject.put("weight", Double.parseDouble(((EditText) container.getChildAt(i + 6)).getText().toString()));
//                petObject.put("id_owner", ((EditText) container.getChildAt(i + 7)).getText().toString());
//                petObject.put("description", ((EditText) container.getChildAt(i + 8)).getText().toString());
//
//                petsArray.put(petObject);
//            }catch(JSONException e){
//                e.printStackTrace();
//            }
//        }
//
//        JSONObject requestBody = new JSONObject();
//        try{
//            requestBody.put("pets", petsArray);
//        }catch(JSONException e){
//            e.printStackTrace();
//        }
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
//                response ->{
//                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
//                },
//                error ->{
//                    Toast.makeText(this, "Error al registrar las mascotas", Toast.LENGTH_SHORT).show();
//                    error.printStackTrace();
//                }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError{
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json");
//                return headers;
//            }
//        };
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(request);

    }

    private void generateFields(){
        container.removeAllViews();

        String numberOfPetsStr = numberOfPets.getText().toString().trim();

        if (numberOfPetsStr.isEmpty()) {
            Toast.makeText(this, "Caremonda ponga algo", Toast.LENGTH_SHORT).show();
            return;
        }
        int numberPets;

        try {
            numberPets = Integer.parseInt(numberOfPetsStr);
        } catch (NumberFormatException e) { 
            Toast.makeText(this, "Caremonda ponga un número válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (numberPets == 0) {
            Toast.makeText(this, "Caremonda ponga un número mayor a 0", Toast.LENGTH_SHORT).show();
            return;
        }


        for (int i = 1; i<= numberPets; i++){
            TextView pet = new TextView(this);
            pet.setText("Pet number " + i);
            pet.setTextSize(18);
            pet.setGravity(Gravity.CENTER);
            pet.setPadding(0, 10, 0, 10);
            container.addView(pet);

            EditText nameInput = createEditText("Name", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
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
    private int dpPx(int dp){
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}