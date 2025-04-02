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

public class Home extends AppCompatActivity {

    private TextView txtView;
    private Button registerPet, viewPets, modifyPets, recordatoryVaccine, chats, petVaccine, petConsultation, medicalHistory, liveLocation, deletePet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        EdgeToEdge.enable(this);

        txtView = findViewById(R.id.titleUsr);
        registerPet = findViewById(R.id.register_pet_Home);
        viewPets = findViewById(R.id.viewPets);
        modifyPets = findViewById(R.id.modify_pet_Home);
        recordatoryVaccine = findViewById(R.id.recordatory_vaccine);
        chats = findViewById(R.id.viewChats);
        petVaccine = findViewById(R.id.vaccine_date_register_Home);
        petConsultation = findViewById(R.id.add_consultation_medical_history_home);
        medicalHistory = findViewById(R.id.view_medical_history_home);
        liveLocation = findViewById(R.id.send_Map_activity);
        deletePet = findViewById(R.id.delete_pet);



        SharedPreferences preferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        String username = preferences.getString("Username","");
        boolean isUser = preferences.getBoolean("isUser", false);
        txtView.setText("Welcome " + username + "!");
        //Toast.makeText(Home.this, "Nombre: " + username + " BOOL: " + isUser, Toast.LENGTH_SHORT).show();

        //Paseador
        if(!isUser){
            registerPet.setVisibility(View.GONE);
            viewPets.setVisibility(View.GONE);
            modifyPets.setVisibility(View.GONE);
            recordatoryVaccine.setVisibility(View.GONE);
            //chats.setVisibility(View.VISIBLE);
            petVaccine.setVisibility(View.GONE);
            petConsultation.setVisibility(View.GONE);
            medicalHistory.setVisibility(View.GONE);
            liveLocation.setVisibility(View.GONE);
            deletePet.setVisibility(View.GONE);
        }
        //Usuario normalongo

    }

    public void sendActivityRegister(View view){
        Intent intent = new Intent(this,PetRegister.class);
        startActivity(intent);
    }

    public void viewPets(View view){
        Intent intent = new Intent(this,ViewPets.class);
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

    public void sendActivityMap(View view){

        //view_medical_history_home --> class = ViewMedicalHistory
        Intent intent = new Intent(this,SelectPetUbi.class);
        startActivity(intent);
    }

    public void deletePet(View view){

        //view_medical_history_home --> class = DeletePet
        Intent intent = new Intent(this,DeletePet.class);
        startActivity(intent);
    }

    public void viewChats(View view){

        //view_medical_history_home --> class = DeletePet
        Intent intent = new Intent(this,ViewChats.class);
        startActivity(intent);
    }

    public void setVaccineRecordatory(View view){

        //view_medical_history_home --> class = DeletePet
        Intent intent = new Intent(this,SetRecordatory.class);
        startActivity(intent);
    }

}