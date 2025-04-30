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

import models.Paseador;
import models.Pet;
import models.Request;
import network.RequestService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewWalkers extends AppCompatActivity {

    private RequestService requestService;
    private LinearLayout containerAceptedWalkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_walkers);

        containerAceptedWalkers = findViewById(R.id.containerAceptedWalkers);
        requestService = Retro.getClient().create(RequestService.class);

        viewMyWalkers();
    }

    public void viewMyWalkers(){
        SharedPreferences sharedPreferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("User_ID", -10);

        if(userId != -10){
            Call<List<Paseador>> call = requestService.getPasPorUserId(userId);
            call.enqueue(new Callback<List<Paseador>>() {
                @Override
                public void onResponse(Call<List<Paseador>> call, Response<List<Paseador>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        containerAceptedWalkers.removeAllViews();
                        List<Paseador> paseadores = response.body();
                        if(paseadores.isEmpty()){
                            TextView noPaseadores = new TextView(ViewWalkers.this);
                            noPaseadores.setText("En el momento, no hay paseadores\ndisponibles para enviar solicitud.");
                            noPaseadores.setTextSize(18);
                            noPaseadores.setGravity(Gravity.CENTER);
                            noPaseadores.setPadding(0, 10, 0, 10);
                            containerAceptedWalkers.addView(noPaseadores);
                        }
                        else{
                            int i = 1;
                            for(Paseador p: paseadores){

                                TextView number = createHeaderTextView("PASEADOR NÚMERO " + i);

                                TextView idRecieved = createTextView("Id: " + p.getId());
                                TextView nameRecieved = createTextView("Nombre: " + p.getName());
                                TextView emailRecieved = createTextView("Correo: " + p.getEmail());
                                TextView phoneRecieved = createTextView("Número de celular: " + p.getPhoneNum());
                                TextView space = createTextView(" ");
                                space.setTextSize(10);

                                containerAceptedWalkers.addView(number);
                                containerAceptedWalkers.addView(idRecieved);
                                containerAceptedWalkers.addView(nameRecieved);
                                containerAceptedWalkers.addView(emailRecieved);
                                containerAceptedWalkers.addView(phoneRecieved);
                                containerAceptedWalkers.addView(space);

                                i++;
                            }
                        }

                    } else {
                        Toast.makeText(ViewWalkers.this, "No se pudo obtener la lista de paseadores.", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(SendRequest.this, "Error al buscar paseadores disponibles", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<List<Paseador>> call, Throwable t) {
                    Toast.makeText(ViewWalkers.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("HappyPaws", "Error al visualizar paseadores", t);
                }

            });
        }
        else{
            Toast.makeText(ViewWalkers.this, "id -10 guardado, revisar", Toast.LENGTH_SHORT).show();
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