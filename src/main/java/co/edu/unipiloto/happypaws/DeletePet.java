package co.edu.unipiloto.happypaws;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DeletePet extends AppCompatActivity {

    private Button btnNext, btnConfirm;
    private LinearLayout contenedorDelete;
    private EditText petId, confirmation;
    private TextView txtView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delete_pet);

        petId = findViewById(R.id.petIdD);
        btnNext = findViewById(R.id.send_comprobation);
        contenedorDelete = findViewById(R.id.contenedorDelete);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
    }

    public void search(){
        //buscar mascota y llamar show()
    }


    public void show(){
        txtView = new TextView(DeletePet.this);
        txtView.setText("A continuación, ingresará su contraseña para eliminar su mascota");

        confirmation = new EditText(DeletePet.this);
        confirmation.setHint("Ingrese su contraseña");

        btnConfirm = new Button(DeletePet.this);
        btnConfirm.setText("Eliminar mascota");

        contenedorDelete.addView(txtView);
        contenedorDelete.addView(confirmation);
        contenedorDelete.setVisibility(View.VISIBLE);
    }
}