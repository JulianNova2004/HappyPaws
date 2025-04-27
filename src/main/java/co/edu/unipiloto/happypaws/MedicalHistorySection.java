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

public class MedicalHistorySection extends AppCompatActivity {

    private TextView title;
    private Button registerMedicalHistory, viewMedicalHistorys, modifyMedicalHistory, deleteMedicalHistory, viewAllMedicalHistorys;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medical_history_section);

        title = findViewById(R.id.titleUsrM);
        registerMedicalHistory = findViewById(R.id.add_consultation_medical_history);
        viewMedicalHistorys = findViewById(R.id.view_medical_history);
        viewAllMedicalHistorys = findViewById(R.id.view_all_medical_histories);
        modifyMedicalHistory = findViewById(R.id.modify_medical_history);
        deleteMedicalHistory = findViewById(R.id.delete_medical_history);

        SharedPreferences preferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        String username = preferences.getString("Username","");
        //boolean isUser = preferences.getBoolean("isUser", false);
        int typeUser = preferences.getInt("typeUser", -100);
        title.setText("Welcome " + username + "!");

        if(typeUser==0){
            //Usuario
            viewAllMedicalHistorys.setVisibility(View.GONE);
        }else if(typeUser==1){
            //Veterinario
            //registerMedicalHistory.setVisibility(View.GONE);
            viewMedicalHistorys.setVisibility(View.GONE);
            //modifyMedicalHistory.setVisibility(View.GONE);
            deleteMedicalHistory.setVisibility(View.GONE);
        }else{
            if(typeUser != 10) {
                Toast.makeText(MedicalHistorySection.this, "Se guardó nullo el typeUser", Toast.LENGTH_SHORT).show();
            }
        }
        
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

    public void sendActivityViewAllMedicalHistories(View view){
        Intent intent = new Intent(this, ViewAllConsultations.class);
        startActivity(intent);
    }

    public void sendActivityModifyMedicalHistory(View view){
        Intent intent = new Intent(this, ShowConsultasToModify.class);
        startActivity(intent);
    }

    public void sendActivityDeleteMedicalHistory(View view){
        Intent intent = new Intent(this, DeleteConsulta.class);
        startActivity(intent);
    }
}