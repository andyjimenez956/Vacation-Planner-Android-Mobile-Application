package com.wgu.vacationscheduler.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wgu.vacationscheduler.R;
import com.wgu.vacationscheduler.entities.Vacation;
import com.wgu.vacationscheduler.repository.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class VacationDetailActivity extends AppCompatActivity {

    private static final String EXTRA_VACATION_ID = "vacationId";

    private final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    private EditText etTitle, etHotel, etStartDate, etEndDate;
    private Button btnSave, btnDelete;

    private Repository repo;

    private int vacationId = -1;          // -1 means "new vacation"
    private Vacation currentVacation;     // used for update/delete

    private long startMillis = -1;
    private long endMillis = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_detail);

        repo = new Repository(getApplication());

        etTitle = findViewById(R.id.etTitle);
        etHotel = findViewById(R.id.etHotel);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        // Date pickers
        etStartDate.setOnClickListener(v -> showDatePicker(true));
        etEndDate.setOnClickListener(v -> showDatePicker(false));

        // Read id if editing
        if (getIntent() != null && getIntent().hasExtra(EXTRA_VACATION_ID)) {
            vacationId = getIntent().getIntExtra(EXTRA_VACATION_ID, -1);
        }

        if (vacationId == -1) {
            // New vacation
            btnDelete.setEnabled(false);
            btnDelete.setAlpha(0.5f);
        } else {
            // Existing vacation: load + enable delete
            loadVacation(vacationId);
        }

        btnSave.setOnClickListener(v -> saveOrUpdateVacation());
        btnDelete.setOnClickListener(v -> attemptDeleteVacation());
    }

    private void loadVacation(int id) {
        repo.getVacationById(id, vacation -> runOnUiThread(() -> {
            if (vacation == null) {
                Toast.makeText(this, "Vacation not found.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            currentVacation = vacation;

            etTitle.setText(vacation.getTitle());
            etHotel.setText(vacation.getHotel());

            startMillis = vacation.getStartDate();
            endMillis = vacation.getEndDate();

            etStartDate.setText(formatter.format(new Date(startMillis)));
            etEndDate.setText(formatter.format(new Date(endMillis)));

            btnDelete.setEnabled(true);
            btnDelete.setAlpha(1f);
        }));
    }

    private void showDatePicker(boolean isStart) {
        Calendar cal = Calendar.getInstance();

        // If already selected, open picker at that date
        long existing = isStart ? startMillis : endMillis;
        if (existing > 0) cal.setTimeInMillis(existing);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar chosen = Calendar.getInstance();
                    chosen.set(year, month, dayOfMonth, 0, 0, 0);
                    chosen.set(Calendar.MILLISECOND, 0);

                    long millis = chosen.getTimeInMillis();
                    String text = formatter.format(new Date(millis));

                    if (isStart) {
                        startMillis = millis;
                        etStartDate.setText(text);
                    } else {
                        endMillis = millis;
                        etEndDate.setText(text);
                    }
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();
    }

    private void saveOrUpdateVacation() {
        String title = etTitle.getText().toString().trim();
        String hotel = etHotel.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Dates selected?
        if (startMillis < 0 || endMillis < 0) {
            Toast.makeText(this, "Please select start and end dates.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Date format validation (if user somehow edits)
        try {
            formatter.parse(etStartDate.getText().toString());
            formatter.parse(etEndDate.getText().toString());
        } catch (ParseException e) {
            Toast.makeText(this, "Dates must be in MM/dd/yyyy format.", Toast.LENGTH_SHORT).show();
            return;
        }

        // End after start validation
        if (endMillis < startMillis) {
            Toast.makeText(this, "End date must be after start date.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (vacationId == -1) {
            // INSERT new
            Vacation v = new Vacation(title, hotel, startMillis, endMillis);
            repo.insertVacation(v);
            Toast.makeText(this, "Vacation saved.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // UPDATE existing
            if (currentVacation == null) {
                Toast.makeText(this, "Unable to update. Vacation not loaded.", Toast.LENGTH_SHORT).show();
                return;
            }

            currentVacation.setTitle(title);
            currentVacation.setHotel(hotel);
            currentVacation.setStartDate(startMillis);
            currentVacation.setEndDate(endMillis);

            repo.updateVacation(currentVacation);
            Toast.makeText(this, "Vacation updated.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void attemptDeleteVacation() {
        if (vacationId == -1 || currentVacation == null) {
            Toast.makeText(this, "Nothing to delete.", Toast.LENGTH_SHORT).show();
            return;
        }

        // B1.b validation: block delete if excursions exist
        repo.countExcursionsForVacation(vacationId, count -> runOnUiThread(() -> {
            if (count > 0) {
                Toast.makeText(this, "Cannot delete vacation with associated excursions.", Toast.LENGTH_LONG).show();
            } else {
                repo.deleteVacation(currentVacation);
                Toast.makeText(this, "Vacation deleted.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }));
    }
}
