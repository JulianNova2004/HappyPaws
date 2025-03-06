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
        Intent intent = new Intent(this,PetRegister.class);
        startActivity(intent);
    }


}