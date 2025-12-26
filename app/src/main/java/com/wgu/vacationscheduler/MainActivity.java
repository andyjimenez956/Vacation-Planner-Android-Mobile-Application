package com.wgu.vacationscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.wgu.vacationscheduler.ui.VacationListActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnVacations = findViewById(R.id.btnVacations);
        btnVacations.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VacationListActivity.class);
            startActivity(intent);
        });
    }
}
