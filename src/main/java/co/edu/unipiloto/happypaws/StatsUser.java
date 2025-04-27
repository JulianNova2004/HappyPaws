package co.edu.unipiloto.happypaws;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import models.User;
import network.Retro;
import network.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatsUser extends AppCompatActivity {

    private LinearLayout containerUsersDesc;
    private Button btnBringUsersWithMostPets;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stats_user);

        containerUsersDesc = findViewById(R.id.containerUsersDesc);
        btnBringUsersWithMostPets = findViewById(R.id.btnBringUsersWithMostPets);
        userService = Retro.getClient().create(UserService.class);

        btnBringUsersWithMostPets.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                bringUsersWithMostPets();
            }
        });

    }

    public void bringUsersWithMostPets(){
        Call<List<User>> call = userService.most();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                containerUsersDesc.removeAllViews();
                if (response.isSuccessful() && response.body() != null) {
                    List<User> usuarios = response.body();
                    int i = 1;
                    for(User users: usuarios){
                        TextView userdD = createHeaderTextView("USER NUMBER " + i);

                        TextView idRecieved = createTextView("Id: " + users.getUserId());
                        String fn = users.getFirstname();
                        String ln = users.getLastname();
                        String nameStr = fn + " " + ln;
                        TextView nameRecieved = createTextView("Name: " + nameStr);
                        TextView usernameRecieved = createTextView("Username: " + users.getEmail());
                        TextView amountPetsRecieved = createTextView("Amount of Pets: " + users.getPets().size());

                        String petListStr = "[\n";
                        for(Pet pet: users.getPets()){
                            petListStr += pet.getName() + ": " + pet.getRace() + ",\n";
                        }
                        petListStr = petListStr.substring(0, petListStr.length()-2);
                        petListStr += "\n]";

                        TextView petsRecieved = createTextView("Pets: " + petListStr);
                        TextView identificationRecieved = createTextView("Identification: " + users.getIdentification());
                        TextView emailRecieved = createTextView("Email: " + users.getEmail());
                        TextView phoneNumberRecieved = createTextView("Phone Number: " + users.getPhoneNumber());

                        containerUsersDesc.addView(userdD);
                        containerUsersDesc.addView(idRecieved);
                        containerUsersDesc.addView(nameRecieved);
                        containerUsersDesc.addView(usernameRecieved);
                        containerUsersDesc.addView(amountPetsRecieved);
                        containerUsersDesc.addView(petsRecieved);
                        containerUsersDesc.addView(identificationRecieved);
                        containerUsersDesc.addView(emailRecieved);
                        containerUsersDesc.addView(phoneNumberRecieved);
                        i++;
                    }

                }else if(response.body() == null){
                    TextView noUser = new TextView(StatsUser.this);
                    noUser.setText("No hay usuarios con mascotas registradas");
                    noUser.setTextSize(18);
                    noUser.setGravity(Gravity.CENTER);
                    noUser.setPadding(0, 10, 0, 10);
                    containerUsersDesc.addView(noUser);
                }
                else {
                    Toast.makeText(StatsUser.this, "Error al visualizar usuarios", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(StatsUser.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("HappyPaws", "Error al visualizar los usuarios", t);
            }

        });
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextColor(Color.BLACK);
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

    private int dpPx(int dp){
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
        //return (int) (dp * getResources().getDisplayMetrics().density);
    }
}