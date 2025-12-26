package com.wgu.vacationscheduler.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.wgu.vacationscheduler.dao.ExcursionDao;
import com.wgu.vacationscheduler.dao.VacationDao;
import com.wgu.vacationscheduler.entities.Excursion;
import com.wgu.vacationscheduler.entities.Vacation;

@Database(entities = {Vacation.class, Excursion.class}, version = 1, exportSchema = false)
public abstract class VacationDatabase extends RoomDatabase {

    public abstract VacationDao vacationDao();
    public abstract ExcursionDao excursionDao();

    private static volatile VacationDatabase INSTANCE;

    public static VacationDatabase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (VacationDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            VacationDatabase.class,
                            "vacation_db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
