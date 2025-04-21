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

public class Home extends AppCompatActivity {

    private TextView title;

    private Button petSection, vaccineSection, medicalHistorySection, recordatorySection, statsSection, chats, liveLocation, emergency;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        EdgeToEdge.enable(this);

        title = findViewById(R.id.titleUsr);
        petSection = findViewById(R.id.send_pet_section);
        vaccineSection = findViewById(R.id.send_vaccine_section);
        medicalHistorySection = findViewById(R.id.send_medicalHistory_section);
        recordatorySection = findViewById(R.id.send_recordatory_section);
        statsSection = findViewById(R.id.send_stats_section);
        chats = findViewById(R.id.viewChats);
        liveLocation = findViewById(R.id.send_Map_activity);
        emergency = findViewById(R.id.emergency);

        SharedPreferences preferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        String username = preferences.getString("Username","");
        boolean isUser = preferences.getBoolean("isUser", false);
        int typeUser = preferences.getInt("typeUser", -10);
        title.setText("Welcome " + username + "!");
        //Toast.makeText(Home.this, "Nombre: " + username + " BOOL: " + isUser, Toast.LENGTH_SHORT).show();
        //-1 -> Paseador ; 0 -> Usuario; 1 -> Veterinario: 10 -> Admin;
        if(typeUser==-1){
            //Paseador
            petSection.setVisibility(View.GONE);
            medicalHistorySection.setVisibility(View.GONE);
            vaccineSection.setVisibility(View.GONE);
            recordatorySection.setVisibility(View.GONE);
            statsSection.setVisibility(View.GONE);
            liveLocation.setVisibility(View.GONE);
            emergency.setVisibility(View.GONE);

        }else if(typeUser==0){
            //Usuario
            //Solo no puede ver el ViewAll de todas las secciones
            statsSection.setVisibility(View.GONE);

        }else if(typeUser==1){
            //Veterinario
            statsSection.setVisibility(View.GONE);
            chats.setVisibility(View.GONE);
            liveLocation.setVisibility(View.GONE);
            emergency.setVisibility(View.GONE);

        }else if(typeUser==10){
            //Admin
            chats.setVisibility(View.GONE);
            liveLocation.setVisibility(View.GONE);
            emergency.setVisibility(View.GONE);
        } else {
            Toast.makeText(Home.this, "Se guardó nullo el typeUser", Toast.LENGTH_SHORT).show();
        }

    }

    public void sendPetSection(View view){
        Intent intent = new Intent(this,PetSection.class);
        startActivity(intent);
    }

    public void sendMedicalHistorySection(View view){
        //Medical History = Consulta --> Cita veterinario
        Intent intent = new Intent(this,MedicalHistorySection.class);
        startActivity(intent);
    }

    public void sendVaccineSection(View view){
        //History --> vacunas
        Intent intent = new Intent(this,VaccineSection.class);
        startActivity(intent);
    }

    public void sendRecordatorySection(View view){
        //Recordatorio
        Intent intent = new Intent(this,RecordatorySection.class);
        startActivity(intent);
    }

    public void sendStatsSection(View view){
        Intent intent = new Intent(this,StatsSection.class);
        startActivity(intent);
    }
    public void sendActivityMap(View view){

        //view_medical_history_home --> class = ViewMedicalHistory
        Intent intent = new Intent(this,SelectPetUbi.class);
        startActivity(intent);
    }

    public void viewChats(View view){

        //view_medical_history_home --> class = DeletePet
        Intent intent = new Intent(this,ViewChats.class);
        startActivity(intent);
    }

    public void emergencyContact(View view){
        Intent intent = new Intent(this,EmergencyContact.class);
        startActivity(intent);
    }

}