package com.example.carsapp_week2.provider;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "car")
public class Car implements java.io.Serializable{

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "carId")
    int Id;
    @ColumnInfo(name = "carMaker")
    String maker;
    @ColumnInfo(name = "carModel")
    String model;
    @ColumnInfo(name = "carColour")
    String colour;
    @ColumnInfo(name = "carAddress")
    String address;
    @ColumnInfo(name = "carYear")
    int year;
    @ColumnInfo(name = "carSeats")
    String seats;
    @ColumnInfo(name = "carPrice")
    String price;

    public Car(String maker, String model, String colour, String address, int year, String seats, String price) {
        this.maker = maker;
        this.model = model;
        this.colour = colour;
        this.address = address;
        this.year = year;
        this.seats = seats;
        this.price = price;
    }

    public Car() {
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public int getId() {
        return Id;
    }

    public void setId(@NonNull int id) {
        Id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
