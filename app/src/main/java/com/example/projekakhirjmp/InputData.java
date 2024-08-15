package com.example.projekakhirjmp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class InputData extends AppCompatActivity {

    EditText edNik, edNama, edTanggalLahir, edAlamat;
    Spinner spinnerJenisKelamin;
    Button btnSimpan;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);

        edNik = findViewById(R.id.ednik);
        edNama = findViewById(R.id.ednama);
        edTanggalLahir = findViewById(R.id.edtanggal_lahir);
        spinnerJenisKelamin = findViewById(R.id.spinner_jenis_kelamin);
        edAlamat = findViewById(R.id.edalamat);
        btnSimpan = findViewById(R.id.btsimpan);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Set up spinner for gender selection
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.jenis_kelamin_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJenisKelamin.setAdapter(adapter);

        // Set up date picker for date of birth
        edTanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        // Handle save button click
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edTanggalLahir.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveData() {
        String id = getIntent().getStringExtra("ID");
        String nik = edNik.getText().toString();
        String nama = edNama.getText().toString();
        String tanggalLahir = edTanggalLahir.getText().toString();
        String jenisKelamin = spinnerJenisKelamin.getSelectedItem().toString();
        String alamat = edAlamat.getText().toString();

        // Validate data
        if (nik.isEmpty() || nama.isEmpty() || tanggalLahir.isEmpty() || jenisKelamin.isEmpty() || alamat.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show();
            return;
        }

        boolean isSuccess;
        if (id != null) {
            // Jika ID tidak null, berarti kita sedang meng-update data
            isSuccess = dbHelper.updateData(id, nik, nama, tanggalLahir, jenisKelamin, alamat);
        } else {
            // Jika ID null, berarti kita sedang menambah data baru
            isSuccess = dbHelper.insertData(nik, nama, tanggalLahir, jenisKelamin, alamat);
        }

        if (isSuccess) {
            Toast.makeText(this, "Data saved successfully", Toast.LENGTH_LONG).show();
            finish(); // Kembali ke aktivitas sebelumnya
        } else {
            Toast.makeText(this, "Data failed to save", Toast.LENGTH_LONG).show();
        }
    }
}
