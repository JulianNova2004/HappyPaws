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
import models.Paseador;
import models.User;
import network.PaseadorService;
import network.Retro;
import network.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogIn extends AppCompatActivity {


    private EditText username, password;
    private Button btnAccess;
    private UserService userService;
    private PaseadorService paseadorService;
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
        sharedPreferences = getSharedPreferences("SaveSession", MODE_PRIVATE);

        btnAccess.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                review();

            }
        });

    }

    /*
    public void startLogIn(View view){
        access();
        Intent intent = new Intent(this,Home.class);
        startActivity(intent);
    }
     */

    public void access(){
        String usernameStr = username.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();

        if(usernameStr.contains("@")){
            Paseador paseador = new Paseador(usernameStr, passwordStr, null, null);
            Call<Paseador> call = paseadorService.login(paseador);
            //Toast.makeText(LogIn.this, "Se instanció call", Toast.LENGTH_SHORT).show();
            call.enqueue(new Callback<Paseador>() {
                @Override
                public void onResponse(Call<Paseador> call, Response<Paseador> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        int userId = response.body().getId();
                        String usernameStrR = response.body().getName();
                        //String passwordStrR = response.body().getPassw();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("User_ID", userId);
                        editor.putString("Username", usernameStrR);
                        //editor.putString("Password", passwordStrR);
                        editor.putBoolean("isUser", false);
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
        else {
            User user = new User(usernameStr, passwordStr, null, null, null, null, null, null);
            Call<User> call = userService.login(user);
            //Toast.makeText(LogIn.this, "Se instanció call", Toast.LENGTH_SHORT).show();
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        int userId = response.body().getUserId();
                        String usernameStrR = response.body().getUsername();
                        String passwordStrR = response.body().getPassword();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("User_ID", userId);
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
//        Call<User> call = userService.login(user);
//        //Toast.makeText(LogIn.this, "Se instanció call", Toast.LENGTH_SHORT).show();
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (response.isSuccessful() && response.body() != null) {
//
//                    int userId = response.body().getUserId();
//                    String usernameStrR = response.body().getUsername();
//                    String passwordStrR = response.body().getPassword();
//
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putInt("User_ID", userId);
//                    editor.putString("Username", usernameStrR);
//                    editor.putString("Password", passwordStrR);
//                    editor.apply();
//
//                    Toast.makeText(LogIn.this, "Inicio de sesión exitoso :D", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(LogIn.this,Home.class);
//                    startActivity(intent);
//
//                } else {
//                    Toast.makeText(LogIn.this, "Correo o contraseña incorrectos, revise sus credenciales", Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Toast.makeText(LogIn.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
//                Log.e("HappyPaws", "Error al iniciar sesión", t);
//                //Log.e("HappyPaws", "Estado call" + call.toString());
//            }
//        });
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