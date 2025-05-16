package co.edu.unipiloto.happypaws;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import models.Vet;
import network.Retro;
import network.VetService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmergencyContact extends AppCompatActivity {

    private VetService vetService;

    private LinearLayout container;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_emergency_contact);

        container = findViewById(R.id.containerVets);
        vetService = Retro.getClient().create(VetService.class);
        viewVets();

    }

    public void viewVets(){

        Call<List<Vet>> call = vetService.getVets();
        call.enqueue(new Callback<List<Vet>>() {
            @Override
            public void onResponse(Call<List<Vet>> call, Response<List<Vet>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    container.removeAllViews();
                    List<Vet> vets = response.body();
                    if(vets.isEmpty()){
                        TextView noVet = new TextView(EmergencyContact.this);
                        noVet.setText("No hay veterinarios registrados");
                        noVet.setTextSize(18);
                        noVet.setGravity(Gravity.CENTER);
                        noVet.setPadding(0, 10, 0, 10);
                        container.addView(noVet);
                        //Toast.makeText(Viewvets.this, "No tiene ninguna mascota registrada", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        // vets.addAll(response.body());
                        int i = 1;
                        for(Vet vet: vets){
                            TextView vetD = createHeaderTextView("VET NUMBER " + i);

                            container.addView(vetD);

                            //Log.i("HappyPaws", "Objeto JSON recibido" + response.body().toString());
                            //Toast.makeText(EmergencyContact.this, "VET_ID = " + vet.getVetId(), Toast.LENGTH_SHORT).show();
                            TextView idRecieved = createTextView("Id: " + vet.getVetId());
                            TextView nameRecieved = createTextView("Name: " + vet.getName());
                            TextView phoneNumberRecieved = createTextView("Phone: " + vet.getPhoneNumber());
                            TextView specialityRecieved = createTextView("Speciality: " + vet.getSpeciality());
                            TextView emailRecieved = createTextView("Email: " + vet.getEmail());
                            TextView space = createTextView(" ");
                            space.setTextSize(10);

                            container.addView(idRecieved);
                            container.addView(nameRecieved);
                            container.addView(phoneNumberRecieved);
                            container.addView(specialityRecieved);
                            container.addView(emailRecieved);
                            container.addView(space);

                            View.OnClickListener vetClickListener = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Intentar redirigir a la aplicación de teléfono para llamar
                                    phoneNumber = vet.getPhoneNumber();
                                    if (ContextCompat.checkSelfPermission(EmergencyContact.this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                        makeCall(phoneNumber);
                                    } else {
                                        ActivityCompat.requestPermissions(EmergencyContact.this, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
                                    }
                                    //Intent intent = new Intent(Intent.ACTION_DIAL);
                                    //intent.setData(Uri.parse("tel:" + phoneNumber));
                                    //startActivity(intent);
                                }
                            };

                            idRecieved.setOnClickListener(vetClickListener);
                            nameRecieved.setOnClickListener(vetClickListener);
                            phoneNumberRecieved.setOnClickListener(vetClickListener);
                            specialityRecieved.setOnClickListener(vetClickListener);
                            emailRecieved.setOnClickListener(vetClickListener);

                            i++;
                        }
                    }

                } else {
                    Toast.makeText(EmergencyContact.this, "Error al mostrar veterinarios", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Vet>> call, Throwable t) {
                Toast.makeText(EmergencyContact.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("HappyPaws", "Error en la app", t);
            }

        });
        
    }

    private void makeCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall(phoneNumber);
            } else {
                Toast.makeText(this, "El permiso de llamada fue denegado", Toast.LENGTH_SHORT).show();
            }
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