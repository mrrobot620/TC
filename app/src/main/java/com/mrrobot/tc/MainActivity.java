package com.mrrobot.tc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private int gridCodeCounter = 0;
    private RecyclerView recyclerView;
    private GridCodeAdapter adapter;
    private List<GridCodeEntry> gridCodeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        gridCodeList = new ArrayList<>();
        adapter = new GridCodeAdapter(gridCodeList);
        recyclerView.setAdapter(adapter);




        Button btn1 = findViewById(R.id.clearbtn);
        Button btn2 = findViewById(R.id.refreshbtn);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearGrid();
                gridCodeList.clear();
                adapter.notifyDataSetChanged();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGridCodes();
            }
        });




    }
    // Inside loadGridCodes method
    private void loadGridCodes() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Load the counter value
        gridCodeCounter = prefs.getInt("grid_code_counter", 0);

        // Clear the existing data
        gridCodeList.clear();

        // Load individual grid code entries
        for (int key = 0; key < gridCodeCounter; key++) {
            String gridCode = prefs.getString("grid_" + key, "");
            if (!gridCode.isEmpty()) {
                gridCodeList.add(new GridCodeEntry(key, gridCode));
            }
        }

        adapter.notifyDataSetChanged();
    }

    public void clearGrid() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();

        // Clear all grid code entries
        for (int key = 0; key < gridCodeCounter; key++) {
            editor.remove("grid_" + key);
        }

        // Reset the counter
        gridCodeCounter = 0;
        editor.putInt("grid_code_counter", gridCodeCounter);
        editor.apply();

        gridCodeList.clear();
        adapter.notifyDataSetChanged();
    }

    protected void onResume(){
        super.onResume();
        loadGridCodes();
    }

}

