package com.example.projekakhirjmp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListData extends AppCompatActivity {

    DatabaseHelper myDb;
    ListView listView;
    ArrayList<String> listData;
    ArrayAdapter<String> adapter;
    EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);

        listView = findViewById(R.id.listview1);
        editTextSearch = findViewById(R.id.editcari);
        myDb = new DatabaseHelper(this);
        listData = new ArrayList<>();

        loadData();

        // Setup adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);

        // Pencarian data
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Klik item untuk update data
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                String[] itemParts = selectedItem.split("\n");
                String itemId = itemParts[0].replace("ID: ", "");

                Log.d("DEBUG", "Selected ID for Update: " + itemId);

                Intent intent = new Intent(ListData.this, InputData.class);
                intent.putExtra("ID", itemId);
                startActivity(intent);
            }
        });

        // Tekan lama untuk menampilkan dialog dengan tiga pilihan
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                String[] itemParts = selectedItem.split("\n");
                String itemId = itemParts[0].replace("ID: ", "");

                Log.d("DEBUG", "Selected ID for Options: " + itemId);

                showOptionsDialog(itemId);
                return true;
            }
        });
    }

    private void loadData() {
        listData.clear();
        Cursor data = myDb.getAllData();
        if (data.getCount() == 0) {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                // Pastikan format data yang ditampilkan benar
                listData.add(data.getString(2));
            }
        }
    }

    // Method untuk menampilkan dialog dengan tiga pilihan
    private void showOptionsDialog(String itemId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListData.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_options, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Mengambil referensi button dalam dialog
        Button btnLihatData = dialogView.findViewById(R.id.btnLihatData);
        Button btnUpdateData = dialogView.findViewById(R.id.btnUpdateData);
        Button btnHapusData = dialogView.findViewById(R.id.btnHapusData);

        // Set onClickListener untuk setiap button
        btnLihatData.setOnClickListener(v -> {
            lihatData(itemId);
            dialog.dismiss();
        });

        btnUpdateData.setOnClickListener(v -> {
            updateData(itemId);
            dialog.dismiss();
        });

        btnHapusData.setOnClickListener(v -> {
            hapusData(itemId);
            dialog.dismiss();
        });

        dialog.show();
    }

    // Method untuk melihat data
    private void lihatData(String itemId) {
        Cursor data = myDb.getDataById(itemId);
        if (data.moveToFirst()) {
            String nik = data.getString(data.getColumnIndex("NIK"));
            String nama = data.getString(data.getColumnIndex("NAMA"));
            String tanggalLahir = data.getString(data.getColumnIndex("TANGGAL_LAHIR"));
            String jenisKelamin = data.getString(data.getColumnIndex("JENIS_KELAMIN"));
            String alamat = data.getString(data.getColumnIndex("ALAMAT"));

            // Tampilkan data
            Toast.makeText(this, "ID: " + itemId + "\nNIK: " + nik + "\nNama: " + nama +
                    "\nTanggal Lahir: " + tanggalLahir + "\nJenis Kelamin: " + jenisKelamin +
                    "\nAlamat: " + alamat, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    // Method untuk update data
    private void updateData(String itemId) {
        Log.d("DEBUG", "Updating ID: " + itemId);
        Intent intent = new Intent(ListData.this, InputData.class);
        intent.putExtra("ID", itemId);
        startActivity(intent);
    }

    // Method untuk menghapus data
    private void hapusData(String itemId) {
        Integer deletedRows = myDb.deleteData(itemId);
        Log.d("DEBUG", "Deleted Rows: " + deletedRows);
        if (deletedRows > 0) {
            Toast.makeText(ListData.this, "Data Deleted", Toast.LENGTH_LONG).show();
            loadData();  // Refresh list setelah penghapusan
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(ListData.this, "Data Not Deleted", Toast.LENGTH_LONG).show();
        }
    }
}
