package co.edu.unipiloto.happypaws;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home extends AppCompatActivity {

    private TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        EdgeToEdge.enable(this);

        txtView = findViewById(R.id.titleUsr);
        SharedPreferences preferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        String username = preferences.getString("Username","");
        txtView.setText("Welcome " + username + "!");

    }

    public void sendActivityRegister(View view){
        Intent intent = new Intent(this,PetRegister.class);
        startActivity(intent);
    }

    public void sendActivityModify(View view){
        Intent intent = new Intent(this,ShowPetsToModify.class);
        startActivity(intent);
    }

    public void sendActivityVaccineDateRegister(View view){
        Intent intent = new Intent(this,VaccineDateRegister.class);
        startActivity(intent);
    }

    public void sendActivityAddConsultationMedicalHistory(View view){
        Intent intent = new Intent(this,AddConsultationMedicalHistory.class);
        startActivity(intent);
    }

    public void sendActivityViewMedicalHistory(View view){

        //view_medical_history_home --> class = ViewMedicalHistory
        Intent intent = new Intent(this,MedicalHistory.class);
        startActivity(intent);
    }

    public void deletePet(View view){

        //view_medical_history_home --> class = DeletePet
        Intent intent = new Intent(this,DeletePet.class);
        startActivity(intent);
    }


}