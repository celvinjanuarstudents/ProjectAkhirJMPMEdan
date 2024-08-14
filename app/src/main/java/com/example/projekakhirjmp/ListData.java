package com.example.projekakhirjmp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

        listView = findViewById(R.id.listview);
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

                Intent intent = new Intent(ListData.this, InputData.class);
                intent.putExtra("ID", itemId);
                startActivity(intent);
            }
        });

        // Tekan lama untuk menghapus data
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                String[] itemParts = selectedItem.split("\n");
                String itemId = itemParts[0].replace("ID: ", "");

                Integer deletedRows = myDb.deleteData(itemId);
                if (deletedRows > 0) {
                    Toast.makeText(ListData.this, "Data Deleted", Toast.LENGTH_LONG).show();
                    loadData();
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ListData.this, "Data Not Deleted", Toast.LENGTH_LONG).show();
                }
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
                listData.add("ID: " + data.getString(0) + "\nNama: " + data.getString(1) + "\nUmur: " + data.getString(2) + "\nMoto: " + data.getString(3));
            }
        }
    }
}