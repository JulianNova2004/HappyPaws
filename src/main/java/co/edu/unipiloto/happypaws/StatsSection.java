package co.edu.unipiloto.happypaws;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StatsSection extends AppCompatActivity {

    private TextView title;
    private Button allPetsS, allVaccinesS, allMedicalHistoriesS, allRecordatoriesS, medicalHistoryStats, vaccineStats, recordatoryStats, userStats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stats_section);
        title = findViewById(R.id.titleStatsS);
        allPetsS = findViewById(R.id.allPetsS);
        allVaccinesS = findViewById(R.id.allVaccinesS);
        allMedicalHistoriesS = findViewById(R.id.allMedicalHistoriesS);
        allRecordatoriesS = findViewById(R.id.allRecordatoriesS);
        medicalHistoryStats = findViewById(R.id.medicalHistoryStats);
        vaccineStats = findViewById(R.id.vaccineStats);
        recordatoryStats = findViewById(R.id.recordatoryStats);
        userStats = findViewById(R.id.userStats);

        SharedPreferences preferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        String username = preferences.getString("Username","");
        title.setText("Welcome " + username + "!");
    }

    public void allPets(View view){
        Intent intent = new Intent(this,ViewAllPets.class);
        startActivity(intent);
    }

    public void allVaccines(View view){
        Intent intent = new Intent(this,ViewAllHistories.class);
        startActivity(intent);
    }

    public void allMedicalHistories(View view){
        Intent intent = new Intent(this,ViewAllConsultations.class);
        startActivity(intent);
    }

    public void allRecordatories(View view){
        Intent intent = new Intent(this,ViewAllRecordatories.class);
        startActivity(intent);
    }

    public void medicalHistoryStats(View view){
        //Crear actividad de stats de medicalHistories
        Intent intent = new Intent(this,PetSection.class);
        startActivity(intent);
    }

    public void vaccineStats(View view){
        //Crear actividad de stats de vacunas
        Intent intent = new Intent(this,PetSection.class);
        startActivity(intent);
    }

    public void recordatoryStats(View view){
        //Crear actividad de stats de recordatorios
        Intent intent = new Intent(this,PetSection.class);
        startActivity(intent);
    }

    public void userStats(View view){
        //Crear actividad de stats de users
        Intent intent = new Intent(this,PetSection.class);
        startActivity(intent);
    }

}