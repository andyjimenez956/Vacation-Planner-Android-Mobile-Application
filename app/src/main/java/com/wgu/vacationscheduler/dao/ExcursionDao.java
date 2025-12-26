package com.wgu.vacationscheduler.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.wgu.vacationscheduler.entities.Excursion;

import java.util.List;
@Dao
public interface ExcursionDao {
    @Insert
    long insert(Excursion excursion);

    @Update
    void update(Excursion excursion);
    @Delete
    void delete(Excursion excursion);

    @Query("SELECT * FROM excursions WHERE vacationId = :vacationId ORDER BY date ASC")
    LiveData<List<Excursion>> getExcursionForVacation(int vacationId);

    @Query("SELECT COUNT(*) FROM excursions WHERE vacationId = :vacationId")
    int countExcursionsForVacationSync(int vacationId);

    @Query("SELECT * FROM excursions WHERE id = :excursionId LIMIT 1")
    Excursion getExcursionByIdSync(int excursionId);

}
