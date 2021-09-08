package com.example.carsapp_week2.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CarRepository {
    private CarDao mCarDao;
    private LiveData<List<Car>> mAllCar;
    CarRepository(Application application) {
        CarDatabase db = CarDatabase.getDatabase(application);
        mCarDao = db.carDao();
        mAllCar = mCarDao.getAllCar();
    }
    LiveData<List<Car>> getAllCar() {
        return mAllCar;
    }
    void insert(Car car) {
        CarDatabase.databaseWriteExecutor.execute(() -> mCarDao.addCar(car));
    }
    void deleteAll(){
        CarDatabase.databaseWriteExecutor.execute(()->{
            mCarDao.deleteAllCar();
        });
    }
    void deleteCar(String carModel) {
        CarDatabase.databaseWriteExecutor.execute(() -> mCarDao.deleteCar(carModel));
    }
}
