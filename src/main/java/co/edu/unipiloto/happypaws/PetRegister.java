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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class PetRegister extends AppCompatActivity {

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
                            Integer.parseInt(contenido);
                        } catch (NumberFormatException e) {
                            editText.setError("Debe ingresar un numero entero valido");
                            registerIsValid = false;
                            //continue;
                        }
                    }if ((input & InputType.TYPE_CLASS_NUMBER) == InputType.TYPE_CLASS_NUMBER &&
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
        else Toast.makeText(this, "Idiota llene bien todos los campos", Toast.LENGTH_SHORT).show();

    }

    private void sendRegisterPets(){

        String url = "url";
        JSONArray petsArray = new JSONArray();

        for (int i = 0; i<container.getChildCount(); i++){
            View view = container.getChildAt(i);

            if(view instanceof EditText){
                EditText editText = (EditText) view;
                String value = editText.getText().toString().trim();

                if(value.isEmpty()){
                    Toast.makeText(this, "Deben de llenarse todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        for (int i = 0; i<container.getChildCount(); i += 8){
            try {
                JSONObject petObject = new JSONObject();
                petObject.put("name", ((EditText) container.getChildAt(i)).getText().toString());
                petObject.put("age", ((Double.parseDouble((((EditText) container.getChildAt(i+1)).getText()).toString()))));
                petObject.put("race", ((EditText) container.getChildAt(i + 2)).getText().toString());
                petObject.put("amount_of_walks", Integer.parseInt(((EditText) container.getChildAt(i + 3)).getText().toString()));
                petObject.put("food", ((EditText) container.getChildAt(i + 4)).getText().toString());
                petObject.put("amount_of_foods", Integer.parseInt(((EditText) container.getChildAt(i + 5)).getText().toString()));
                petObject.put("weight", Double.parseDouble(((EditText) container.getChildAt(i + 6)).getText().toString()));
                petObject.put("id_owner", ((EditText) container.getChildAt(i + 7)).getText().toString());
                petObject.put("description", ((EditText) container.getChildAt(i + 8)).getText().toString());

                petsArray.put(petObject);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }

        JSONObject requestBody = new JSONObject();
        try{
            requestBody.put("pets", petsArray);
        }catch(JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                response ->{
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                },
                error ->{
                    Toast.makeText(this, "Error al registrar las mascotas", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    private void generateFields(){
        container.removeAllViews();

        String numberOfPetsStr = numberOfPets.getText().toString();

        if (numberOfPetsStr.isEmpty()) return;

        int numberPets = Integer.parseInt(numberOfPetsStr);

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
            EditText foodInput = createEditText("Food", InputType.TYPE_CLASS_TEXT);
            EditText amountMeals = createEditText("Amount of Meals", InputType.TYPE_CLASS_NUMBER);
            EditText weightInput = createEditText("Weight", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            EditText idInput = createEditText("Owner ID", InputType.TYPE_CLASS_TEXT);
            EditText descriptionInput = createEditText("Description", InputType.TYPE_CLASS_TEXT);

            container.addView(nameInput);
            container.addView(age);
            container.addView(breedInput);
            container.addView(dailyWalksInput);
            container.addView(foodInput);
            container.addView(amountMeals);
            container.addView(weightInput);
            container.addView(idInput);
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