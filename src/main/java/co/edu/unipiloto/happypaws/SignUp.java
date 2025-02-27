package co.edu.unipiloto.happypaws;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    private EditText username, password, firstname, lastname, ID, address, mail, phoneNumber;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_user);
        //EdgeToEdge.enable(this);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        ID = findViewById(R.id.ID);
        address = findViewById(R.id.address);
        mail = findViewById(R.id.mail);
        phoneNumber = findViewById(R.id.phone_number);
        btnSend = findViewById(R.id.sign_up);



    }
    public void onSendMessage(View view){
        SignUpUser();
        Intent intent = new Intent(this,Home.class);
        startActivity(intent);
    }
    public void SignUpUser(){
          String usernameStr = username.getText().toString().trim();
          String passwordStr = password.getText().toString().trim();
          String firstnameStr = firstname.getText().toString().trim();
          String lastnameStr = lastname.getText().toString().trim();
          String IDStr = ID.getText().toString().trim();
          String addressStr = address.getText().toString().trim();
          String mailStr = mail.getText().toString().trim();
          String phoneNumberStr = phoneNumber.getText().toString().trim();

          if (usernameStr.isEmpty() || passwordStr.isEmpty() || firstnameStr.isEmpty() ||
          lastnameStr.isEmpty() || IDStr.isEmpty() || addressStr.isEmpty() || mailStr.isEmpty()
                  || phoneNumberStr.isEmpty()){


          }

    }
}