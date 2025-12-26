package com.wgu.vacationscheduler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wgu.vacationscheduler.R;
import com.wgu.vacationscheduler.repository.Repository;

public class VacationListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerVacations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        VacationAdapter adapter = new VacationAdapter();
        recyclerView.setAdapter(adapter);

        Repository repository = new Repository(getApplication());
        repository.getAllVacations().observe(this, adapter::setVacations);

        adapter.setOnItemClickListener(vacation -> {
            Intent intent = new Intent(VacationListActivity.this, VacationDetailActivity.class);
            intent.putExtra("vacationId", vacation.getId());
            startActivity(intent);
        });

        Button btnAdd = findViewById(R.id.btnAddVacation);
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(VacationListActivity.this, VacationDetailActivity.class);
            startActivity(intent);
        });
    }
}
