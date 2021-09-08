package com.example.carsapp_week2;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carsapp_week2.provider.CarViewModel;

public class RecyclerActivity extends AppCompatActivity {

//    ArrayList<Car> cardList = new ArrayList<Car>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MyRecyclerViewAdapter adapter;
    static CarViewModel mCarViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        Toolbar toolbar = findViewById(R.id.myLayout);
        setSupportActionBar(toolbar);
//        cardList = (ArrayList<Car>) getIntent().getSerializableExtra("cardList");

        recyclerView = findViewById(R.id.my_recycler_view);

        layoutManager = new LinearLayoutManager(this);  //A RecyclerView.LayoutManager implementation which provides similar functionality to ListView.
        recyclerView.setLayoutManager(layoutManager);   // Also StaggeredGridLayoutManager and GridLayoutManager or a custom Layout manager


        adapter = new MyRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        mCarViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        mCarViewModel.getAllCar().observe(this, newData -> {
            adapter.setData(newData);
            adapter.notifyDataSetChanged();
        });



    }
    public void fab_click(View v){
        finish();
    }
}