package co.edu.unipiloto.happypaws;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class DeletePet extends AppCompatActivity {

    private Button btnNext, btnConfirm;
    private LinearLayout contenedorDelete;
    private EditText petId, confirmation;
    private TextView txtView1, txtView2;
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
        show();
    }


    public void show(){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        layoutParams.setMargins(0, 30, 0, 30);

        txtView1 = new TextView(DeletePet.this);
        txtView1.setText("Llene el siguiente campo para verificar su identidad");
        txtView1.setLayoutParams(layoutParams);
        //txtView1.setGravity(1);

        confirmation = new EditText(DeletePet.this);
        confirmation.setHint("Ingrese su contraseña");
        confirmation.setLayoutParams(layoutParams);

        //btnConfirm = new Button(DeletePet.this);
        //btnConfirm = new Button(new ContextThemeWrapper(DeletePet.this, com.google.android.material.R.style.Widget_Material3_Button), null, 0);
        //btnConfirm = new Button(new ContextThemeWrapper(DeletePet.this, androidx.appcompat.R.style.Widget_AppCompat_Button), null, 0);

        btnConfirm = new MaterialButton(new ContextThemeWrapper(DeletePet.this, com.google.android.material.R.style.Widget_Material3_Button), null, 0);
        btnConfirm.setText("Eliminar mascota");
        btnConfirm.setLayoutParams(layoutParams);

        txtView2 = new TextView(DeletePet.this);
        txtView2.setText("Si su contraseña es correcta, se borrará su mascota");
        txtView2.setLayoutParams(layoutParams);

        contenedorDelete.addView(txtView1);
        //contenedorDelete.setPadding(0, 30, 0, 60);
        contenedorDelete.addView(confirmation);
        //contenedorDelete.setPadding(0, 30, 0, 60);
        contenedorDelete.addView(btnConfirm);
        //contenedorDelete.setPadding(0, 30, 0, 60);
        contenedorDelete.addView(txtView2);
        contenedorDelete.setVisibility(View.VISIBLE);
    }
}