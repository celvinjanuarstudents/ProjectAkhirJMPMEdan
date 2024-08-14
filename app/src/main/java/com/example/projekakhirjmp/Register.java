package com.example.projekakhirjmp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Register extends AppCompatActivity {
    Button btnregister;
    EditText eduser, edpass;
    DatabaseHelper dblogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Menghubungkan UI dengan variabel
        btnregister = findViewById(R.id.buttonReg);
        eduser = findViewById(R.id.editTextUser);
        edpass = findViewById(R.id.editTextPass);  // Mengubah ID ke R.id.editTextPass

        // Inisialisasi database helper
        dblogin = new DatabaseHelper(this);

        // Menambahkan onClickListener untuk tombol register
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = eduser.getText().toString();
                String password = edpass.getText().toString();
                Boolean checkUser = dblogin.checkUser(user);

                if (!checkUser) {
                    Boolean insert = dblogin.insertUser(user, password); // Menambahkan koma
                    if (insert) {
                        Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "User Already Exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
