package com.example.carsapp_week2.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface CarDao {

    @Query("select * from car")
    LiveData<List<Car>> getAllCar();

    @Query("select * from car where carId=:carId")
    List<Car> getCar(int carId);

    @Insert
    void addCar(Car car);

    @Query("delete from car where carModel=:carModel")
    void deleteCar(String carModel);

    @Query("delete from car where carYear<:year")
    void deleteYear(String year);

    @Query("delete FROM car")
    void deleteAllCar();

}
