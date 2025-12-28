package com.wgu.vacationscheduler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wgu.vacationscheduler.R;
import com.wgu.vacationscheduler.repository.Repository;

public class ExcursionListActivity extends AppCompatActivity{

    public static final String EXTRA_VACATION_ID = "vacationId";
    public static final String EXTRA_EXCURSION_ID = "excursionId";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_list);

        int vacationId = getIntent().getIntExtra(EXTRA_VACATION_ID, -1);
        if(vacationId == -1) {
            Toast.makeText(this, "Missing vacation id.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerExcursions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ExcursionAdapter adapter = new ExcursionAdapter();
        recyclerView.setAdapter(adapter);

        Repository repo = new Repository(getApplication());
        repo.getExcursionsForVacation(vacationId).observe(this, adapter::setExcursions);

        Button btnAdd = findViewById(R.id.btnAddExcursion);
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExcursionDetailActivity.class);
            intent.putExtra(EXTRA_VACATION_ID, vacationId);
            startActivity(intent);
        });

        adapter.setOnItemClickListener(excursion -> {
            Intent intent = new Intent(this, ExcursionDetailActivity.class);
            intent.putExtra("excursionId", excursion.getId());
            intent.putExtra(EXTRA_VACATION_ID, vacationId);
            startActivity(intent);
        });
    }

}
