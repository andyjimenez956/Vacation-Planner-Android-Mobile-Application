package com.wgu.vacationscheduler.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.wgu.vacationscheduler.dao.ExcursionDao;
import com.wgu.vacationscheduler.dao.VacationDao;
import com.wgu.vacationscheduler.database.VacationDatabase;
import com.wgu.vacationscheduler.entities.Excursion;
import com.wgu.vacationscheduler.entities.Vacation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private final VacationDao vacationDao;
    private final ExcursionDao excursionDao;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public Repository(Application application){
        VacationDatabase db = VacationDatabase.getInstance(application);
        vacationDao = db.vacationDao();
        excursionDao = db.excursionDao();
    }

    public LiveData<List<Vacation>> getAllVacations(){
        return vacationDao.getAllVacations();
    }

    public void insertVacation(Vacation vacation){
        executor.execute(() -> vacationDao.insert(vacation));
    }

    public void updateVacation(Vacation vacation){
        executor.execute(() -> vacationDao.update(vacation));
    }

    public void deleteVacation(Vacation vacation){
        executor.execute(() -> vacationDao.delete(vacation));
    }

    public LiveData<List<Excursion>> getExcursionsForVacation(int vacationId){
        return excursionDao.getExcursionForVacation(vacationId);
    }

    public void insertExcursion(Excursion excursion){
        executor.execute(() -> excursionDao.insert(excursion));
    }

    public void updateExcursion(Excursion excursion){
        executor.execute(() -> excursionDao.update(excursion));
    }

    public void deleteExcursion(Excursion excursion){
        executor.execute(() -> excursionDao.delete(excursion));
    }

    public void countExcursionsForVacation(int vacationId, Callback<Integer> callback) {
        executor.execute(() -> {
            int count = excursionDao.countExcursionsForVacationSync(vacationId);
            callback.onResult(count);
        });

    }

    public interface Callback<T>{
        void onResult(T value);
    }


    public void getVacationById(int vacationId, Callback<Vacation> callback) {
        executor.execute(() -> {
            Vacation vacation = vacationDao.getVacationByIdSync(vacationId);
            callback.onResult(vacation);
        });
    }

    public void getExcursionById(int excursionId, Callback<Excursion> callback){
        executor.execute(() -> {
            Excursion excursion = excursionDao.getExcursionByIdSync(excursionId);
            callback.onResult(excursion);
        });
    }

}
