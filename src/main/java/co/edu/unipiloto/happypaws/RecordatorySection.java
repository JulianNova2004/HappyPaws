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

public class RecordatorySection extends AppCompatActivity {

    private TextView title;
    private Button registerRecordatory, viewRecordatories, modifyRecordatory, deleteRecordatory, viewAllRecordatories;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recordatory_section);

        title = findViewById(R.id.titleUsrR);
        registerRecordatory = findViewById(R.id.register_recordatory_vaccine);
        viewRecordatories = findViewById(R.id.view_recordatories_vaccine);
        viewAllRecordatories = findViewById(R.id.view_all_recordatories_vaccine);
        modifyRecordatory = findViewById(R.id.modify_recordatory_vaccine);
        deleteRecordatory = findViewById(R.id.delete_recordatory_vaccine);

        SharedPreferences preferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        String username = preferences.getString("Username","");
        //boolean isUser = preferences.getBoolean("isUser", false);
        int typeUser = preferences.getInt("typeUser", -100);
        title.setText("Welcome " + username + "!");

        if(typeUser==0){
            //Usuario
            viewAllRecordatories.setVisibility(View.GONE);
        }else if(typeUser==1){
            //Veterinario
            //registerRecordatory.setVisibility(View.GONE);
            viewRecordatories.setVisibility(View.GONE);
            //modifyRecordatory.setVisibility(View.GONE);
            deleteRecordatory.setVisibility(View.GONE);
        }else{
            if(typeUser != 10) {
                Toast.makeText(RecordatorySection.this, "Se guardó nullo el typeUser", Toast.LENGTH_SHORT).show();
            }
        }
        
    }

    public void registerVaccineRecordatory(View view){
        Intent intent = new Intent(this,SetRecordatory.class);
        startActivity(intent);
    }

    public void viewVaccinesRecordatory(View view){
        Intent intent = new Intent(this,ViewVaccineRecordatories.class);
        startActivity(intent);
    }

    public void viewAllVaccinesRecordatory(View view){
        Intent intent = new Intent(this,ViewAllRecordatories.class);
        startActivity(intent);
    }

    public void modifyVaccineRecordatory(View view){
        Intent intent = new Intent(this,ShowRecsToModify.class);
        startActivity(intent);
    }

    public void deleteVaccineRecordatory(View view){
        Intent intent = new Intent(this,DeleteRecordatory.class);
        startActivity(intent);
    }
}