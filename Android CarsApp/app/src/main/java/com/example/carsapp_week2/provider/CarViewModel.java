package com.example.carsapp_week2.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CarViewModel extends AndroidViewModel {

    private CarRepository mRepository;
    private LiveData<List<Car>> mAllCar;
    public CarViewModel(@NonNull Application application) {
        super(application);
        mRepository = new CarRepository(application);
        mAllCar = mRepository.getAllCar();
    }
    public LiveData<List<Car>> getAllCar() {
        return mAllCar;
    }
    public void insert(Car car) {
        mRepository.insert(car);
    }
    public void deleteAll(){
        mRepository.deleteAll();
    }
    public void deleteCar(String carModel){
        mRepository.deleteCar(carModel);
    }
}
