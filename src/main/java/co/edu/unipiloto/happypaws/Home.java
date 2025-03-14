package co.edu.unipiloto.happypaws;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        EdgeToEdge.enable(this);



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
        Intent intent = new Intent(this,VaccineDateRegister.class);
        startActivity(intent);
    }


}