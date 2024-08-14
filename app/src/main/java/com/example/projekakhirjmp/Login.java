package com.example.projekakhirjmp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    Button btnlogin, btnregister;
    EditText eduserlogin, edpasslogin;
    DatabaseHelper dblogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnlogin = findViewById(R.id.buttonLogin);
        btnregister = findViewById(R.id.buttonRegister);
        eduserlogin = findViewById(R.id.editTextUsername);
        edpasslogin = findViewById(R.id.editTextPassword);

        dblogin = new DatabaseHelper(this); // Inisialisasi DatabaseHelper

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String suser = eduserlogin.getText().toString();
                String spass = edpasslogin.getText().toString();
                Boolean checkUserPassword = dblogin.checkUserPassword(suser, spass);
                if (checkUserPassword) {
                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }
}
