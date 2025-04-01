package co.edu.unipiloto.happypaws;

import android.content.Intent;
//import android.content.SharedPreferences;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import models.Paseador;
import models.User;
import network.PaseadorService;
import network.Retro;
import network.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    private Spinner userTypeSpinner;
    private LinearLayout container1U, container2P;
    private EditText username, password, firstname, lastname, ID, address, mail, phoneNumber;
    private EditText mailP, passwordP, firstnameP, lastnameP, phoneNumberP;
    private Button btnSend;
    private UserService userService;
    private PaseadorService paseadorService;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userTypeSpinner = findViewById(R.id.user_type_spinner);
        container1U = findViewById(R.id.tipo1_container);
        container2P = findViewById(R.id.tipo2_container);
        //sharedPreferences = getSharedPreferences("SaveSession", MODE_PRIVATE);

        //Usuario normalito
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        ID = findViewById(R.id.ID);
        address = findViewById(R.id.address);
        mail = findViewById(R.id.mail);
        phoneNumber = findViewById(R.id.phone_number);

        //Paseador
        mailP = findViewById(R.id.mailP);
        passwordP = findViewById(R.id.passwordP);
        firstnameP = findViewById(R.id.firstnameP);
        lastnameP = findViewById(R.id.lastnameP);
        phoneNumberP = findViewById(R.id.phone_numberP);

        btnSend = findViewById(R.id.sign_up);
        userService = Retro.getClient().create(UserService.class);
        paseadorService = Retro.getClient().create(PaseadorService.class);

        String[] opciones = {"Seleccione tipo", "Usuario", "Paseador"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(adapter);
        userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        container1U.setVisibility(View.VISIBLE);
                        container2P.setVisibility(View.GONE);
                        break;
                    case 2:
                        container1U.setVisibility(View.GONE);
                        container2P.setVisibility(View.VISIBLE);
                        break;
                    default:
                        container1U.setVisibility(View.GONE);
                        container2P.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                container1U.setVisibility(View.GONE);
                container2P.setVisibility(View.GONE);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    public void registerUser() {
        String selectedType = userTypeSpinner.getSelectedItem().toString();
        boolean registerIsValid = true;

        if (selectedType.equals("Usuario")) {
            registerIsValid = validateUserFields();
            if (registerIsValid) signUpUser();
        } else if (selectedType.equals("Paseador")) {
            registerIsValid = validateWalkerFields();
            if (registerIsValid) signUpWalker();
        }

        if (!registerIsValid) {
            Toast.makeText(this, "Caremonda llene bien todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateUserFields() {

        String usernameStr = username.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();
        String firstnameStr = firstname.getText().toString().trim();
        String lastnameStr = lastname.getText().toString().trim();
        String IDStr = ID.getText().toString().trim();
        String addressStr = address.getText().toString().trim();
        String mailStr = mail.getText().toString().trim();
        String phoneNumberStr = phoneNumber.getText().toString().trim();

        if(usernameStr.isEmpty() || passwordStr.isEmpty() || firstnameStr.isEmpty() || lastnameStr.isEmpty() ||
                IDStr.isEmpty() || addressStr.isEmpty() || mailStr.isEmpty() || phoneNumberStr.isEmpty()) {
            Toast.makeText(this, "Bot, ningún campo puede quedar vacio", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateWalkerFields() {

        String mailPStr = mailP.getText().toString().trim();
        String passwordPStr = passwordP.getText().toString().trim();
        String firstnamePStr = firstnameP.getText().toString().trim();
        String lastnamePStr = lastnameP.getText().toString().trim();
        String phoneNumberPStr = phoneNumberP.getText().toString().trim();

        if(mailPStr.isEmpty() || passwordPStr.isEmpty() || firstnamePStr.isEmpty() || lastnamePStr.isEmpty() || phoneNumberPStr.isEmpty()) {
            Toast.makeText(this, "Bot, ningún campo puede quedar vacio", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void signUpUser() {

        String usernameStr = username.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();
        String firstnameStr = firstname.getText().toString().trim();
        String lastnameStr = lastname.getText().toString().trim();
        String IDStr = ID.getText().toString().trim();
        String addressStr = address.getText().toString().trim();
        String mailStr = mail.getText().toString().trim();
        String phoneNumberStr = phoneNumber.getText().toString().trim();

        User user = new User(usernameStr,passwordStr,firstnameStr,lastnameStr,IDStr,addressStr,mailStr,phoneNumberStr);

        Call<User> call = userService.saveUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(SignUp.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp.this,FirstPage.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(SignUp.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(SignUp.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void signUpWalker() {
        String mailPStr = mailP.getText().toString().trim();
        String passwordPStr = passwordP.getText().toString().trim();
        String firstnamePStr = firstnameP.getText().toString().trim();
        String lastnamePStr = lastnameP.getText().toString().trim();
        String phoneNumberPStr = phoneNumberP.getText().toString().trim();

        String fullName = firstnamePStr + " " + lastnamePStr;

        Paseador paseador = new Paseador(mailPStr, passwordPStr, fullName, phoneNumberPStr);

        Call<Paseador> call = paseadorService.savePaseador(paseador);
        call.enqueue(new Callback<Paseador>() {
            @Override
            public void onResponse(Call<Paseador> call, Response<Paseador> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(SignUp.this, "Paseador registrado con éxito", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp.this,FirstPage.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(SignUp.this, "Error al registrar paseador", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Paseador> call, Throwable t) {
                Toast.makeText(SignUp.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


//    private EditText username, password, firstname, lastname, ID, address, mail, phoneNumber;
//    private Button btnSend;
//    private UserService userService;
//    //private SharedPreferences sharedPreferences;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up_user);
//        EdgeToEdge.enable(this);
//
//        username = findViewById(R.id.username);
//        password = findViewById(R.id.password);
//        firstname = findViewById(R.id.firstname);
//        lastname = findViewById(R.id.lastname);
//        ID = findViewById(R.id.ID);
//        address = findViewById(R.id.address);
//        mail = findViewById(R.id.mail);
//        phoneNumber = findViewById(R.id.phone_number);
//        btnSend = findViewById(R.id.sign_up);
//
//        userService = Retro.getClient().create(UserService.class);
//
//        btnSend.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                registerUser();
//            }
//        });
//        //sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
//
//    }
//    /*
//    public void onSendMessage(View view){
//        SignUpUser();
//        Intent intent = new Intent(this,FirstPage.class);
//        startActivity(intent);
//    }
//    */
//    public void signUpUser(){
//        String usernameStr = username.getText().toString().trim();
//        String passwordStr = password.getText().toString().trim();
//        String firstnameStr = firstname.getText().toString().trim();
//        String lastnameStr = lastname.getText().toString().trim();
//        String IDStr = ID.getText().toString().trim();
//        String addressStr = address.getText().toString().trim();
//        String mailStr = mail.getText().toString().trim();
//        String phoneNumberStr = phoneNumber.getText().toString().trim();
//
//        User user = new User(usernameStr,passwordStr,firstnameStr,lastnameStr,IDStr,addressStr,mailStr,phoneNumberStr);
//
//
//        Call<User> call = userService.saveUser(user);
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    Toast.makeText(SignUp.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(SignUp.this,FirstPage.class);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(SignUp.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Toast.makeText(SignUp.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
//                Log.i("HappyPaws", "Ocurrió un error", t);
//            }});
//    }
//
//    public void registerUser(){
//        boolean registerIsValid = true;
//
//        String usernameStr = username.getText().toString().trim();
//        String passwordStr = password.getText().toString().trim();
//        String firstnameStr = firstname.getText().toString().trim();
//        String lastnameStr = lastname.getText().toString().trim();
//        String IDStr = ID.getText().toString().trim();
//        String addressStr = address.getText().toString().trim();
//        String mailStr = mail.getText().toString().trim();
//        String phoneNumberStr = phoneNumber.getText().toString().trim();
//
//        if(usernameStr.isEmpty() || passwordStr.isEmpty() || firstnameStr.isEmpty() || lastnameStr.isEmpty() ||
//                IDStr.isEmpty() || addressStr.isEmpty() || mailStr.isEmpty() || phoneNumberStr.isEmpty()){
//            Toast.makeText(this, "Bot, ningún campo puede quedar vacio", Toast.LENGTH_SHORT).show();
//            registerIsValid = false;
//        }
//
//        if (passwordStr.length()<6){
//            password.setError("Papi mínimo una contraseña de al menos 6 caracteres");
//            registerIsValid = false;
//        }
//
//        if(!IDStr.matches("\\d+")){
//            ID.setError("Bueno pero el ID son solo números según yo");
//            registerIsValid = false;
//        }
//
//        if(!Patterns.EMAIL_ADDRESS.matcher(mailStr).matches()){
//            mail.setError("Un correo valido debe contener nombre, dominio y no contener caracteres invalidos");
//            registerIsValid = false;
//        }
//
//        if(registerIsValid){
//            Toast.makeText(SignUp.this, "Lleno todos los campos correctamente", Toast.LENGTH_SHORT).show();
//            signUpUser();
//        }
//        else Toast.makeText(SignUp.this, "Caremonda llene bien todos los campos", Toast.LENGTH_SHORT).show();
//
//    }
//
}
