package com.example.carsapp_week2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Bid extends AppCompatActivity {
    TextView temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.activity_bid);
        temp = findViewById(R.id.buymaker);
        temp.setText(extras.getString("maker"));
        temp = findViewById(R.id.buymodel);
        temp.setText(extras.getString("model"));
        temp = findViewById(R.id.buyprice);
        temp.setText(extras.getString("price"));
        Button tnc = findViewById(R.id.tnc);
        Button buy = findViewById(R.id.buy);
        tnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buy.setVisibility(View.VISIBLE);
            }
        });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String car = extras.getString("car");
                RecyclerActivity.mCarViewModel.deleteCar(car);
                Toast myMessage = Toast.makeText(v.getContext(),"Car successfully bought!",Toast.LENGTH_SHORT);
                myMessage.show();
                finish();
            }
        });
    }
}