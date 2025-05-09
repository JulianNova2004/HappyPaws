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

import com.google.gson.Gson;

import java.util.List;

import models.Pet;
import models.User;
import network.RequestService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewUsers extends AppCompatActivity {

    private RequestService requestService;
    private LinearLayout containerAceptedUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_users);

        containerAceptedUsers = findViewById(R.id.containerAceptedUsers);
        requestService = Retro.getClient().create(RequestService.class);

        viewMyUsers();
    }

    private void viewMyUsers(){
        SharedPreferences sharedPreferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        int paseadorId = sharedPreferences.getInt("pasId", 0);
        Call<List<User>> call = requestService.getUserAccepted(paseadorId);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    containerAceptedUsers.removeAllViews();
                    List<User> users = response.body();
                    if(users.isEmpty()){
                        TextView noUsers = new TextView(ViewUsers.this);
                        noUsers.setText("En el momento, no hay solicitudes pendientes");
                        noUsers.setTextSize(18);
                        noUsers.setGravity(Gravity.CENTER);
                        noUsers.setPadding(0, 10, 0, 10);
                        containerAceptedUsers.addView(noUsers);
                    }else{
                        int i = 1;
                        for(User u: users){

                            String petsJson = new Gson().toJson(u.getPets());
                            Log.d("ViewUsers", "Pets JSON for user " + u.getUserId() + ": " + petsJson);

                            TextView number = createHeaderTextView("USUARIO NÚMERO " + i);

                            TextView idRecieved = createTextView("Id: " + u.getUserId());
                            String name = u.getFirstname() + " " + u.getLastname();
                            TextView nameRecieved = createTextView("Nombre: " + name);
                            TextView usernameRecieved = createTextView("Username: " + u.getUsername());
                            TextView phoneRecieved = createTextView("Número de celular: " + u.getPhoneNumber());

                            String petListStr = "[\n";
                            int n = 1;
                            for(Pet pet: u.getPets()){
                                petListStr += n + ": "+ pet.getName() + ": " + pet.getRace() + " -> "+ "\n" + "Cantidad de paseos: " + pet.getAmount_of_walks() + ",\n";
                                n++;
                            }
                            petListStr = petListStr.substring(0, petListStr.length()-2);
                            petListStr += "\n]";

                            TextView petsRecieved = createTextView("Información de mascotas: " + petListStr);
                            TextView space = createTextView(" ");
                            space.setTextSize(10);

                            containerAceptedUsers.addView(number);
                            containerAceptedUsers.addView(idRecieved);
                            containerAceptedUsers.addView(nameRecieved);
                            containerAceptedUsers.addView(usernameRecieved);
                            containerAceptedUsers.addView(phoneRecieved);
                            containerAceptedUsers.addView(petsRecieved);
                            containerAceptedUsers.addView(space);

                            i++;
                        }

                    }
                }else{
                    Toast.makeText(ViewUsers.this, "No se pudo obtener la lista de usuarios.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(ViewUsers.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("HappyPaws", "Error al buscar usuarios", t);
            }
        });
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