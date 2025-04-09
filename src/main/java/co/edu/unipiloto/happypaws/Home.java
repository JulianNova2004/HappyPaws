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
    private Button registerPet, viewPets, viewAllPets, modifyPets, recordatoryVaccine, petVaccine, petConsultation, medicalHistory, deletePet;

    private Button petSection, vaccineSection, medicalHistorySection, recordatorySection, chats, liveLocation;
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
        chats = findViewById(R.id.viewChats);
        liveLocation = findViewById(R.id.send_Map_activity);

//        registerPet = findViewById(R.id.register_pet_Home);
//        viewPets = findViewById(R.id.viewPets);
//        viewAllPets = findViewById(R.id.viewAllPets);
//        modifyPets = findViewById(R.id.modify_pet_Home);
//        recordatoryVaccine = findViewById(R.id.recordatory_vaccine);
//        petVaccine = findViewById(R.id.vaccine_date_register_Home);
//        petConsultation = findViewById(R.id.add_consultation_medical_history_home);
//        medicalHistory = findViewById(R.id.view_medical_history_home);
//        deletePet = findViewById(R.id.delete_pet);



        SharedPreferences preferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        String username = preferences.getString("Username","");
        boolean isUser = preferences.getBoolean("isUser", false);
        int typeUser = preferences.getInt("typeUser", 10);
        title.setText("Welcome " + username + "!");
        //Toast.makeText(Home.this, "Nombre: " + username + " BOOL: " + isUser, Toast.LENGTH_SHORT).show();
        //-1 -> Paseador ; 0 -> Usuario; 1 -> Veterinario ;
        if(typeUser==-1){
            petSection.setVisibility(View.GONE);
            medicalHistorySection.setVisibility(View.GONE);
            vaccineSection.setVisibility(View.GONE);
            recordatorySection.setVisibility(View.GONE);
            liveLocation.setVisibility(View.GONE);
//            registerPet.setVisibility(View.GONE);
//            viewPets.setVisibility(View.GONE);
//            modifyPets.setVisibility(View.GONE);
//            recordatoryVaccine.setVisibility(View.GONE);
//            chats.setVisibility(View.VISIBLE);
//            petVaccine.setVisibility(View.GONE);
//            petConsultation.setVisibility(View.GONE);
//            medicalHistory.setVisibility(View.GONE);
//            liveLocation.setVisibility(View.GONE);
//            deletePet.setVisibility(View.GONE);
        }else if(typeUser==0){
            //Solo no puede ver el ViewAllPets
//            registerPet.setVisibility(View.GONE);
//            modifyPets.setVisibility(View.GONE);
//            chats.setVisibility(View.GONE);
//            liveLocation.setVisibility(View.GONE);
        }else if(typeUser==1){
            chats.setVisibility(View.GONE);
            liveLocation.setVisibility(View.GONE);
//            viewAllPets.setVisibility(View.GONE);
        }else {
            Toast.makeText(Home.this, "Se guardó nullo el typeUser", Toast.LENGTH_SHORT).show();
        }

    }

    public void sendPetSection(View view){
        Intent intent = new Intent(this,PetSection.class);
        startActivity(intent);
    }

    public void sendMedicalHistorySection(View view){
        Intent intent = new Intent(this,MedicalHistorySection.class);
        startActivity(intent);
    }

    public void sendVaccineSection(View view){
        Intent intent = new Intent(this,VaccineSection.class);
        startActivity(intent);
    }

    public void sendRecordatorySection(View view){
        Intent intent = new Intent(this,RecordatorySection.class);
        startActivity(intent);
    }

//    public void sendActivityRegister(View view){
//        Intent intent = new Intent(this,PetRegister.class);
//        startActivity(intent);
//    }
//
//    public void viewPets(View view){
//        Intent intent = new Intent(this,ViewPets.class);
//        startActivity(intent);
//    }
//
//    public void viewAllPets(View view){
//
//        //Activity --> ver todas las mascotas de la db
//        Intent intent = new Intent(this,ViewPets.class);
//        startActivity(intent);
//    }
//
//    public void sendActivityModify(View view){
//        Intent intent = new Intent(this,ShowPetsToModify.class);
//        startActivity(intent);
//    }

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
        Intent intent = new Intent(this, ViewMedicalHistory.class);
        startActivity(intent);
    }

    public void sendActivityMap(View view){

        //view_medical_history_home --> class = ViewMedicalHistory
        Intent intent = new Intent(this,SelectPetUbi.class);
        startActivity(intent);
    }

//    public void deletePet(View view){
//
//        //view_medical_history_home --> class = DeletePet
//        Intent intent = new Intent(this,DeletePet.class);
//        startActivity(intent);
//    }

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