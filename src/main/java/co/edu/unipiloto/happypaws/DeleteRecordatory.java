package co.edu.unipiloto.happypaws;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import models.Pet;
import models.Recordatorio;
import network.RecordatoryService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteRecordatory extends AppCompatActivity {

    private Button btnNext, btnConfirm;
    private LinearLayout contenedorDelete;
    private EditText recId, confirmation;
    private TextView txtView1, txtView2;
    private boolean isValid;
    private RecordatoryService recordatoryService;
    //private Pet pet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delete_recordatory);

        recId = findViewById(R.id.recIdD);
        btnNext = findViewById(R.id.send_comprobation);
        recordatoryService = Retro.getClient().create(RecordatoryService.class);
        contenedorDelete = findViewById(R.id.contenedorDelete);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
            }
        });
    }

    public void validateFields(){
        isValid = true;
        String recIdStr = recId.getText().toString().trim();
        if (recIdStr.isEmpty() || !recIdStr.matches("\\d+")) {
            recId.setError("Debe ingresar un ID de recordatorio válido");
            isValid = false;
        }

        if(isValid) {
            Toast.makeText(this, "Lleno todos los campos correctamente", Toast.LENGTH_SHORT).show();
            show();
        }
        else Toast.makeText(this, "Caremonda llene bien todos los campos", Toast.LENGTH_SHORT).show();
    }
    /*
    public void search(){
        //buscar mascota y llamar show()
        show();
    }
     */



    public void show(){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        layoutParams.setMargins(0, 30, 0, 30);

        txtView1 = new TextView(DeleteRecordatory.this);
        txtView1.setText("Llene el siguiente campo para verificar su identidad");
        txtView1.setLayoutParams(layoutParams);
        //txtView1.setGravity(1);

        confirmation = new EditText(DeleteRecordatory.this);
        confirmation.setHint("Ingrese su contraseña");
        confirmation.setLayoutParams(layoutParams);

        //btnConfirm = new Button(DeleteRecordatory.this);
        //btnConfirm = new Button(new ContextThemeWrapper(DeleteRecordatory.this, com.google.android.material.R.style.Widget_Material3_Button), null, 0);
        //btnConfirm = new Button(new ContextThemeWrapper(DeleteRecordatory.this, androidx.appcompat.R.style.Widget_AppCompat_Button), null, 0);

        btnConfirm = new MaterialButton(new ContextThemeWrapper(DeleteRecordatory.this, com.google.android.material.R.style.Widget_Material3_Button), null, 0);
        btnConfirm.setText("Eliminar recordatorio");
        btnConfirm.setLayoutParams(layoutParams);

        txtView2 = new TextView(DeleteRecordatory.this);
        txtView2.setText("Si su contraseña es correcta, se borrará su recordatorio");
        txtView2.setLayoutParams(layoutParams);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review();
            }
        });

        contenedorDelete.addView(txtView1);
        //contenedorDelete.setPadding(0, 30, 0, 60);
        contenedorDelete.addView(confirmation);
        //contenedorDelete.setPadding(0, 30, 0, 60);
        contenedorDelete.addView(btnConfirm);
        //contenedorDelete.setPadding(0, 30, 0, 60);
        contenedorDelete.addView(txtView2);
        contenedorDelete.setVisibility(View.VISIBLE);
    }

    public void review(){
        boolean borrar = true;
        SharedPreferences sharedPreferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        String password = sharedPreferences.getString("Password", "");
        String passwordInput = confirmation.getText().toString();
        if(password.isEmpty()){
            Toast.makeText(this, "Revisar shared preference", Toast.LENGTH_SHORT).show();
            borrar = false;
        }
        if(passwordInput.isEmpty()){
            Toast.makeText(this, "No ha ingresado una contraseña", Toast.LENGTH_SHORT).show();
            borrar = false;
        }
        if(password.equals(passwordInput) && borrar){
            Toast.makeText(this, "Lleno todos los campos correctamente", Toast.LENGTH_SHORT).show();
            deleteRecordatory();
        }
        else Toast.makeText(this, "La contraseña ingresada es incorrecta", Toast.LENGTH_SHORT).show();
    }

    public void deleteRecordatory(){
        int recIdInt = (!recId.getText().toString().trim().isEmpty()) ?
                Integer.parseInt(recId.getText().toString().trim()) : 0;

        Call<Recordatorio> call = recordatoryService.deleteRec(recIdInt);

        call.enqueue(new Callback<Recordatorio>() {
            @Override
            public void onResponse(Call<Recordatorio> call, Response<Recordatorio> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DeleteRecordatory.this, "Recordatorio eliminado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DeleteRecordatory.this, Home.class);
                    startActivity(intent);}
                else{
                    Toast.makeText(DeleteRecordatory.this, "Error al eliminar mascota", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Recordatorio> call, Throwable t) {
                Toast.makeText(DeleteRecordatory.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("HappyPaws", "Error al eliminar mascota", t);
            }
        });
        //else {
        //    Toast.makeText(DeleteRecordatory.this, "Toast de error", Toast.LENGTH_SHORT).show();
        //}

    }
}