package co.edu.unipiloto.happypaws;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FirstPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_page);

    }

    public void signUp(View view){
        Intent intent = new Intent(this,SignUp.class);
        startActivity(intent);

    }

    public void logIn(View view){
        Intent intent = new Intent(this,LogIn.class);
        startActivity(intent);
    }
}