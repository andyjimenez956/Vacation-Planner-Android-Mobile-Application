package com.wgu.vacationscheduler.ui;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wgu.vacationscheduler.R;
import com.wgu.vacationscheduler.entities.Excursion;
import com.wgu.vacationscheduler.entities.Vacation;
import com.wgu.vacationscheduler.repository.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ExcursionDetailActivity extends AppCompatActivity {
    private final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    private EditText etTitle, etDate;
    private Button btnSave, btnDelete, btnAlert;

    private Repository repo;

    private int vacationId;
    private int excursionId = -1;

    private Excursion currentExcursion;
    private Vacation parentVacation;

    private long excursionMillis = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_detail);

        repo = new Repository(getApplication());

        etTitle = findViewById(R.id.etExcTitle);
        etDate = findViewById(R.id.etExcDate);
        btnSave = findViewById(R.id.btnSaveExc);
        btnDelete = findViewById(R.id.btnDeleteExc);
        btnAlert = findViewById(R.id.btnAlertExc);

        vacationId = getIntent().getIntExtra(ExcursionListActivity.EXTRA_VACATION_ID, -1);
        excursionId = getIntent().getIntExtra(ExcursionListActivity.EXTRA_EXCURSION_ID, -1);

        if (vacationId == -1) {
            Toast.makeText(this, "Missing vacation id.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load parent vacation (for date-range validation)
        repo.getVacationById(vacationId, v -> runOnUiThread(() -> parentVacation = v));

        etDate.setOnClickListener(v -> showDatePicker());

        if (excursionId == -1) {
            btnDelete.setEnabled(false);
            btnDelete.setAlpha(0.5f);
        } else {
            loadExcursion(excursionId);
        }

        btnSave.setOnClickListener(v -> saveOrUpdate());
        btnDelete.setOnClickListener(v -> deleteExcursion());
        btnAlert.setOnClickListener(v -> setAlert());
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        if (excursionMillis > 0) cal.setTimeInMillis(excursionMillis);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar chosen = Calendar.getInstance();
                    chosen.set(year, month, dayOfMonth, 0, 0, 0);
                    chosen.set(Calendar.MILLISECOND, 0);

                    excursionMillis = chosen.getTimeInMillis();
                    etDate.setText(formatter.format(new Date(excursionMillis)));
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    private void loadExcursion(int id) {
        repo.getExcursionById(id, e -> runOnUiThread(() -> {
            if (e == null) {
                Toast.makeText(this, "Excursion not found.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            currentExcursion = e;
            etTitle.setText(e.getTitle());
            excursionMillis = e.getDate();
            etDate.setText(formatter.format(new Date(excursionMillis)));

            btnDelete.setEnabled(true);
            btnDelete.setAlpha(1f);
        }));
    }

    private void saveOrUpdate() {
        String title = etTitle.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter an excursion title.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (excursionMillis < 0) {
            Toast.makeText(this, "Please select an excursion date.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Date format validation
        try {
            formatter.parse(etDate.getText().toString());
        } catch (ParseException ex) {
            Toast.makeText(this, "Date must be in MM/dd/yyyy format.", Toast.LENGTH_SHORT).show();
            return;
        }

        // B5e validation: excursion date must be within vacation dates
        if (parentVacation != null) {
            long vStart = parentVacation.getStartDate();
            long vEnd = parentVacation.getEndDate();
            if (excursionMillis < vStart || excursionMillis > vEnd) {
                Toast.makeText(this, "Excursion date must be during the vacation.", Toast.LENGTH_LONG).show();
                return;
            }
        }

        if (excursionId == -1) {
            Excursion e = new Excursion(title, excursionMillis, vacationId);
            repo.insertExcursion(e);
            Toast.makeText(this, "Excursion saved.", Toast.LENGTH_SHORT).show();
        } else {
            currentExcursion.setTitle(title);
            currentExcursion.setDate(excursionMillis);
            repo.updateExcursion(currentExcursion);
            Toast.makeText(this, "Excursion updated.", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void deleteExcursion() {
        if (currentExcursion == null) return;
        repo.deleteExcursion(currentExcursion);
        Toast.makeText(this, "Excursion deleted.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setAlert() {
        if (excursionMillis < 0) {
            Toast.makeText(this, "Select a date first.", Toast.LENGTH_SHORT).show();
            return;
        }

        String title = etTitle.getText().toString().trim();
        scheduleAlert(excursionMillis, "Excursion Alert", title + " is today");
        Toast.makeText(this, "Alert set.", Toast.LENGTH_SHORT).show();
    }

    private void scheduleAlert(long triggerTime, String title, String message) {
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                (int) (triggerTime % Integer.MAX_VALUE),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        }
    }
}
