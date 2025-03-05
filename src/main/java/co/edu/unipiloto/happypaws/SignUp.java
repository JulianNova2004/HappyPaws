package co.edu.unipiloto.happypaws;

import android.content.Intent;
//import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import models.User;
import network.Retro;
import network.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    private EditText username, password, firstname, lastname, ID, address, mail, phoneNumber;
    private Button btnSend;
    private UserService userService;
    //private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_user);
        //EdgeToEdge.enable(this);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        ID = findViewById(R.id.ID);
        address = findViewById(R.id.address);
        mail = findViewById(R.id.mail);
        phoneNumber = findViewById(R.id.phone_number);
        btnSend = findViewById(R.id.sign_up);

        userService = Retro.getClient().create(UserService.class);

        //sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

    }
    public void onSendMessage(View view){
        SignUpUser();
        Intent intent = new Intent(this,FirstPage.class);
        startActivity(intent);
    }
    public void SignUpUser(){
          String usernameStr = username.getText().toString().trim();
          String passwordStr = password.getText().toString().trim();
          String firstnameStr = firstname.getText().toString().trim();
          String lastnameStr = lastname.getText().toString().trim();
          String IDStr = ID.getText().toString().trim();
          String addressStr = address.getText().toString().trim();
          String mailStr = mail.getText().toString().trim();
          String phoneNumberStr = phoneNumber.getText().toString().trim();

        User user = new User(usernameStr,passwordStr,firstnameStr,lastnameStr,IDStr,addressStr,mailStr,phoneNumberStr);

        //Logica Shared Preference
        /*
        Call<User> call = userService.saveUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    //String correo = response.body().getUserId().toString();
                    //SharedPreferences.Editor editor = sharedPreferences.edit();
                    //editor.putString("DataUser", correo);
                    //editor.apply();

                    Toast.makeText(SignUp.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                    finish(); // Cerrar la actividad después de registrar
                } else {
                    Toast.makeText(SignUp.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(SignUp.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("MiApp", "Ocurrió un error", t);
            }});

        */
    }
}