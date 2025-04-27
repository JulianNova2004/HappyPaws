package co.edu.unipiloto.happypaws;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//import models.LoginR;
import models.Admin;
import models.Paseador;
import models.User;
import models.Vet;
import network.AdminService;
import network.PaseadorService;
import network.Retro;
import network.UserService;
import network.VetService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogIn extends AppCompatActivity {

    private final String p = "trabajosupc89@gmail.com";
    private EditText username, password;
    private Button btnAccess;
    private int typeUser = 0;
    private UserService userService;
    private PaseadorService paseadorService;
    private VetService vetService;
    private AdminService adminService;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);
        username = findViewById(R.id.usernameLI);
        password = findViewById(R.id.passwordLI);
        btnAccess = findViewById(R.id.logInLI);
        userService = Retro.getClient().create(UserService.class);
        paseadorService = Retro.getClient().create(PaseadorService.class);
        vetService = Retro.getClient().create(VetService.class);
        adminService = Retro.getClient().create(AdminService.class);
        sharedPreferences = getSharedPreferences("SaveSession", MODE_PRIVATE);

        /*
        Usuario = TypeUser 0, credenciales --> username, isUser true sp
        Paseador = TypeUser -1, credenciales --> correo, isUser false sp
        Veterinario = TypeUser 1, credenciales --> cedula, isUser true sp (npi)
        Administrador = TypeUser 10, credenciales --> correo trabajos,
         */
        btnAccess.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                review();

            }
        });

    }

    public void access(){
        String usernameStr = username.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();

        if(usernameStr.equals(p)){
            //Admin
            Admin admin = new Admin(null, usernameStr, null, passwordStr);
            Call<Admin> call = adminService.logIn(admin);
            call.enqueue(new Callback<Admin>() {
                @Override
                public void onResponse(Call<Admin> call, Response<Admin> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        int userId = response.body().getId();
                        typeUser = 10;
                        String usernameStrR = response.body().getName();
                        //Toast.makeText(LogIn.this, "Name = " + usernameStrR + ".", Toast.LENGTH_SHORT).show();
                        //String passwordStrR = response.body().getPassw();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("pasId", userId);
                        editor.putInt("typeUser", typeUser);
                        editor.putString("Username", usernameStrR);
                        //editor.putString("Password", passwordStrR);
                        //Cambiar todos los booleanos por -1 paseador, 0 veterinario, 1 usuario ; revisar bien Chat, viewChats y Mensajes
                        //editor.putBoolean("isUser", false);
                        //editor.putInt("typeUser", typeUser);
                        editor.apply();

                        Toast.makeText(LogIn.this, "Inicio de sesión exitoso :D", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LogIn.this,Home.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(LogIn.this, "Correo o contraseña incorrectos, revise sus credenciales", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Admin> call, Throwable t) {
                    Toast.makeText(LogIn.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("HappyPaws", "Error al iniciar sesión", t);
                    //Log.e("HappyPaws", "Estado call" + call.toString());
                }
            });
        }
        else if(usernameStr.contains("@") &&(usernameStr.contains(".com") || usernameStr.contains(".co"))){
            //Paseador
            Paseador paseador = new Paseador(usernameStr, passwordStr, null, null);
            Call<Paseador> call = paseadorService.login(paseador);
            //Toast.makeText(LogIn.this, "Se instanció call", Toast.LENGTH_SHORT).show();
            call.enqueue(new Callback<Paseador>() {
                @Override
                public void onResponse(Call<Paseador> call, Response<Paseador> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        int userId = response.body().getId();
                        typeUser = -1;
                        String usernameStrR = response.body().getName();
                        //Toast.makeText(LogIn.this, "Name = " + usernameStrR + ".", Toast.LENGTH_SHORT).show();
                        //String passwordStrR = response.body().getPassw();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("pasId", userId);
                        editor.putInt("typeUser", typeUser);
                        editor.putString("Username", usernameStrR);
                        //editor.putString("Password", passwordStrR);
                        //Cambiar todos los booleanos por -1 paseador, 0 veterinario, 1 usuario ; revisar bien Chat, viewChats y Mensajes
                        editor.putBoolean("isUser", false);
                        //editor.putInt("typeUser", typeUser);
                        editor.apply();

                        Toast.makeText(LogIn.this, "Inicio de sesión exitoso :D", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LogIn.this,Home.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(LogIn.this, "Correo o contraseña incorrectos, revise sus credenciales", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Paseador> call, Throwable t) {
                    Toast.makeText(LogIn.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("HappyPaws", "Error al iniciar sesión", t);
                    //Log.e("HappyPaws", "Estado call" + call.toString());
                }
            });
        }
        else if(usernameStr.matches("^[0-9]+$")) {
            //Veterinario
            Vet veterinario = new Vet(null, usernameStr, null, null, passwordStr, null);
            Call<Vet> call = vetService.login(veterinario);
            call.enqueue(new Callback<Vet>() {
                @Override
                public void onResponse(Call<Vet> call, Response<Vet> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        int userId = response.body().getVetId();
                        typeUser = 1;
                        String usernameStrR = response.body().getName();
                        String passwordStrR = response.body().getPassw();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("User_ID", userId);
                        editor.putInt("typeUser", typeUser);
                        editor.putString("Username", usernameStrR);
                        editor.putString("Password", passwordStrR);
                        //editor.putBoolean("isUser", true);
                        editor.apply();

                        Toast.makeText(LogIn.this, "Inicio de sesión exitoso :D", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LogIn.this,Home.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(LogIn.this, "Correo o contraseña incorrectos, revise sus credenciales", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Vet> call, Throwable t) {
                    Toast.makeText(LogIn.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("HappyPaws", "Error al iniciar sesión", t);
                    //Log.e("HappyPaws", "Estado call" + call.toString());
                }
            });
        }
        else {
            //Usuario
            User user = new User(usernameStr, passwordStr, null, null, null, null, null, null);
            Call<User> call = userService.login(user);
            //Toast.makeText(LogIn.this, "Se instanció call", Toast.LENGTH_SHORT).show();
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        int userId = response.body().getUserId();
                        typeUser = 0;
                        String usernameStrR = response.body().getUsername();
                        String passwordStrR = response.body().getPassword();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("User_ID", userId);
                        editor.putInt("typeUser", typeUser);
                        editor.putString("Username", usernameStrR);
                        editor.putString("Password", passwordStrR);
                        editor.putBoolean("isUser", true);
                        editor.apply();

                        Toast.makeText(LogIn.this, "Inicio de sesión exitoso :D", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LogIn.this,Home.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(LogIn.this, "Correo o contraseña incorrectos, revise sus credenciales", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(LogIn.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("HappyPaws", "Error al iniciar sesión", t);
                    //Log.e("HappyPaws", "Estado call" + call.toString());
                }
            });
        }
    }

    public void review(){
        boolean loginIsValid = true;

        if(username.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty()){
            loginIsValid=false;
            Toast.makeText(LogIn.this, "Bueno pero ponga algo", Toast.LENGTH_SHORT).show();
        }

        if(loginIsValid) {
            //Toast.makeText(LogIn.this, "Lleno todos los campos correctamente", Toast.LENGTH_SHORT).show();
            access();
        }
        else Toast.makeText(LogIn.this, "Caremonda llene bien todos los campos", Toast.LENGTH_SHORT).show();

    }

}