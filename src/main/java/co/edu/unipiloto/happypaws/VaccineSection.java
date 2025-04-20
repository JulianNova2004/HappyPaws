package co.edu.unipiloto.happypaws;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VaccineSection extends AppCompatActivity {

    private TextView title;
    private Button registerVaccine, viewVaccines, modifyVaccine, deleteVaccine, viewAllVaccines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vaccine_section);

        title = findViewById(R.id.titleUsrV);
        registerVaccine = findViewById(R.id.vaccine_date_register);
        viewVaccines = findViewById(R.id.view_vaccine);
        viewAllVaccines = findViewById(R.id.view_all_vaccine);
        modifyVaccine = findViewById(R.id.modify_vaccine);
        deleteVaccine = findViewById(R.id.delete_vaccine);

        SharedPreferences preferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        String username = preferences.getString("Username","");
        //boolean isUser = preferences.getBoolean("isUser", false);
        int typeUser = preferences.getInt("typeUser", 10);
        title.setText("Welcome " + username + "!");

        if(typeUser==0){
            //Usuario
            viewAllVaccines.setVisibility(View.GONE);
        }else if(typeUser==1){
            //Veterinario
            //registerVaccine.setVisibility(View.GONE);
            viewVaccines.setVisibility(View.GONE);
            //modifyVaccine.setVisibility(View.GONE);
            deleteVaccine.setVisibility(View.GONE);
        }else{
            Toast.makeText(VaccineSection.this, "Se guardó nullo el typeUser", Toast.LENGTH_SHORT).show();
        }
        

    }

    //HISTORY
    public void sendActivityVaccineDateRegister(View view){
        Intent intent = new Intent(this,VaccineDateRegister.class);
        startActivity(intent);
    }

    public void sendActivityViewVaccine(View view){
        Intent intent = new Intent(this,ViewVaccines.class);
        startActivity(intent);
    }

    public void sendActivityViewAllVaccines(View view){
        Intent intent = new Intent(this,ViewAllHistories.class);
        startActivity(intent);
    }

    public void sendActivityModifyVaccine(View view){
        Intent intent = new Intent(this,ShowHistoriesToModify.class);
        startActivity(intent);
    }

    public void sendActivityDeleteVaccine(View view){
        Intent intent = new Intent(this,DeleteVaccine.class);
        startActivity(intent);
    }


}