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

public class PetSection extends AppCompatActivity {

    private TextView title;
    private Button registerPet, viewPets, modifyPet, deletePet, viewAllPets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pet_section);

        title = findViewById(R.id.titleUsrP);
        registerPet = findViewById(R.id.register_pet);
        viewPets = findViewById(R.id.viewPets);
        viewAllPets = findViewById(R.id.viewAllPets);
        modifyPet = findViewById(R.id.modify_pet);
        deletePet = findViewById(R.id.delete_pet);

        SharedPreferences preferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        String username = preferences.getString("Username","");
        //boolean isUser = preferences.getBoolean("isUser", false);
        int typeUser = preferences.getInt("typeUser", 10);
        title.setText("Welcome " + username + "!");

        if(typeUser==0){
            viewAllPets.setVisibility(View.GONE);
        }else if(typeUser==1){
            registerPet.setVisibility(View.GONE);
            viewPets.setVisibility(View.GONE);
            modifyPet.setVisibility(View.GONE);
            deletePet.setVisibility(View.GONE);
        }else{
            Toast.makeText(PetSection.this, "Se guardó nullo el typeUser", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendActivityRegister(View view){
        Intent intent = new Intent(this,PetRegister.class);
        startActivity(intent);
    }

    public void viewPets(View view){
        Intent intent = new Intent(this,ViewPets.class);
        startActivity(intent);
    }

    public void viewAllPets(View view){

        //Activity --> ver todas las mascotas de la db
        Intent intent = new Intent(this,ViewPets.class);
        startActivity(intent);
    }

    public void sendActivityModify(View view){
        Intent intent = new Intent(this,ShowPetsToModify.class);
        startActivity(intent);
    }

    public void deletePet(View view){

        //view_medical_history_home --> class = DeletePet
        Intent intent = new Intent(this,DeletePet.class);
        startActivity(intent);
    }


}