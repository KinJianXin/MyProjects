package com.example.carsapp_week2;
//X7;BMW;2000;White;4;1000;Here

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carsapp_week2.provider.Car;
import com.example.carsapp_week2.provider.CarViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    EditText UserMakerInput;
    EditText temptext;
    DrawerLayout drawer;
    ArrayList<String> myList = new ArrayList<String>();
    ArrayList<Car> cardList = new ArrayList<Car>();
    ArrayAdapter myAdapter;
    private CarViewModel mCarViewModel;
    MyRecyclerViewAdapter adapter;
    DatabaseReference myRef;
    View myLayout;
    int xdown;
    int ydown;
    GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);

        Toolbar toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.myDrawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar, R.string.nagivation_drawer_open, R.string.nagivation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        ListView listView = findViewById(R.id.lv);
        myAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,myList);
//        listView.setAdapter(myAdapter);

        NavigationView nagivationView = findViewById(R.id.myNagivationView);
        nagivationView.setNavigationItemSelectedListener(new myNavigationListener());

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);

        MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();
        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER));

        adapter = new MyRecyclerViewAdapter(this);

        mCarViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        mCarViewModel.getAllCar().observe(this, newData -> {
            adapter.setData(newData);
            adapter.notifyDataSetChanged();
            TextView tv = findViewById(R.id.mcounter);
            tv.setText(newData.size() + "");
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("car/carObjects");

        myLayout = findViewById(R.id.myLayout);
        gestureDetector = new GestureDetector(this, new MyGestureDetector());

        myLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            gestureDetector.onTouchEvent(event);
            return true;
            }
        });
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener{
        @Override
        public void onLongPress(MotionEvent e) {
            clear(null);
            super.onLongPress(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            EditText seats = findViewById(R.id.SeatsInput);
            seats.setText(Integer.parseInt(seats.getText().toString())+1+"");
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            EditText maker = findViewById(R.id.MakerInput);
            EditText model = findViewById(R.id.ModelInput);
            EditText year = findViewById(R.id.YearInput);
            EditText colour = findViewById(R.id.ColourInput);
            EditText seats = findViewById(R.id.SeatsInput);
            EditText price = findViewById(R.id.PriceInput);
            EditText address = findViewById(R.id.AddressInput);

            maker.setText("BMW");
            model.setText("X5");
            year.setText("2015");
            colour.setText("Black");
            seats.setText("4");
            price.setText("1000");
            address.setText("Here");

            return super.onDoubleTap(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (velocityX > 1000 || velocityY > 1000)
            {
                moveTaskToBack(true);
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            EditText price = findViewById(R.id.PriceInput);
            int x = (int)distanceX;
            price.setText(Integer.parseInt(price.getText().toString())+x+"");
            if (Integer.parseInt(price.getText().toString()) < 0){
                price.setText("0");
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    class myNavigationListener implements NavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent i = new Intent(MainActivity.this,RecyclerActivity.class);
            int id = item.getItemId();
            if(id == R.id.menu_add_car){
                add_car_button_clicked(null);}
            else if(id == R.id.menu_remove_last){
                remove_last_car(null);}
            else if(id == R.id.menu_remove_all){
                mCarViewModel.deleteAll();
                myRef.removeValue();}
            else if(id == R.id.menu_close){
                close_clicked(null);}
            else if(id == R.id.menu_list_all){
                startActivity(i);
            }
            else{drawer.closeDrawers();}
            drawer.closeDrawers();
            return true;
        }
    }

    class MyBroadCastReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);
            show_sms_toast(msg);
            StringTokenizer sT = new StringTokenizer(msg, ";");

            String input = sT.nextToken();
            EditText temp = findViewById(R.id.MakerInput);
            temp.setText(input);

            input = sT.nextToken();
            temp = findViewById(R.id.ModelInput);
            temp.setText(input);

            input = sT.nextToken();
            temp = findViewById(R.id.YearInput);
            temp.setText(input);

            input = sT.nextToken();
            temp = findViewById(R.id.ColourInput);
            temp.setText(input);

            input = sT.nextToken();
            temp = findViewById(R.id.SeatsInput);
            if (Integer.parseInt(input)>=4 && Integer.parseInt(input)<=8){
                temp.setText(input);
            }
            else{
                input = "Error: Must be 4-8";
                temp.setText(input);
            }

            input = sT.nextToken();
            temp = findViewById(R.id.PriceInput);
            temp.setText(input);

            input = sT.nextToken();
            temp = findViewById(R.id.AddressInput);
            temp.setText(input);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EditText temp;
        String string_value;
        SharedPreferences myData = getSharedPreferences("file1",0);
        SharedPreferences myMaker = getSharedPreferences("carMakerFile",0);

        string_value = myMaker.getString("maker","");
        temp = findViewById(R.id.MakerInput);
        temp.setText(string_value);

        string_value = myData.getString("model","");
        temp = findViewById(R.id.ModelInput);
        temp.setText(string_value);

        string_value = myData.getString("colour","");
        temp = findViewById(R.id.ColourInput);
        temp.setText(string_value);

        string_value = myData.getString("address","");
        temp = findViewById(R.id.AddressInput);
        temp.setText(string_value);

        string_value = myData.getString("year", "");
        temp = findViewById(R.id.YearInput);
        temp.setText(string_value);

        string_value = myData.getString("seat", "");
        temp = findViewById(R.id.SeatsInput);
        temp.setText(string_value);

        string_value = myData.getString("price", "");
        temp = findViewById(R.id.PriceInput);
        temp.setText(string_value);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
        EditText temp;
        String string_value;
        SharedPreferences myMaker = getSharedPreferences("carMakerFile",0);

        string_value = myMaker.getString("maker","");
        temp = findViewById(R.id.MakerInput);
        temp.setText(string_value);
        temp = findViewById(R.id.ModelInput);
        temp.setText("");
        temp = findViewById(R.id.ColourInput);
        temp.setText("");
        temp = findViewById(R.id.AddressInput);
        temp.setText("");
        temp = findViewById(R.id.YearInput);
        temp.setText("");
        temp = findViewById(R.id.SeatsInput);
        temp.setText("");
        temp = findViewById(R.id.PriceInput);
        temp.setText("");
    }

    public void add_car_button_clicked(View v){

        Car car = new Car();
        UserMakerInput = findViewById(R.id.MakerInput);
        Toast myMessage = Toast.makeText(this,
                "We added a new car ("+UserMakerInput.getText().toString()+")",
                Toast.LENGTH_SHORT);
        myMessage.show();

        EditText temp;
        String string_value;
        SharedPreferences myData = getSharedPreferences("file1",0);
        SharedPreferences myMaker = getSharedPreferences("carMakerFile",0);
        SharedPreferences.Editor myEditor = myData.edit();
        SharedPreferences.Editor MakerEditor = myMaker.edit();

        temp = findViewById(R.id.MakerInput);
        String maker_string_value = temp.getText().toString();
        MakerEditor.putString("maker",maker_string_value);
        car.setMaker(maker_string_value);

        temp = findViewById(R.id.ModelInput);
        String model_string_value = temp.getText().toString();
        myEditor.putString("model",model_string_value);
        myList.add(maker_string_value + " | " + model_string_value);
        myAdapter.notifyDataSetChanged();
        car.setModel(model_string_value);

        temp = findViewById(R.id.ColourInput);
        string_value = temp.getText().toString();
        myEditor.putString("colour",string_value);
        car.setColour(string_value);

        temp = findViewById(R.id.AddressInput);
        string_value = temp.getText().toString();
        myEditor.putString("address",string_value);
        car.setAddress(string_value);

        temp = findViewById(R.id.YearInput);
        string_value = temp.getText().toString();
        int int_value = Integer.parseInt(string_value);
        myEditor.putString("year",string_value);
        car.setYear(int_value);

        temp = findViewById(R.id.SeatsInput);
        string_value = temp.getText().toString();
        myEditor.putString("seat",string_value);
        car.setSeats(string_value);

        temp = findViewById(R.id.PriceInput);
        string_value = temp.getText().toString();
        myEditor.putString("price",string_value);
        car.setPrice(string_value);

        myRef.push().setValue(car);
//        myRef.setValue(car);
        mCarViewModel.insert(car);

        myEditor.commit();
        MakerEditor.commit();
    }

    public void load(View v){
        EditText temp;
        String string_value;
        SharedPreferences myMaker = getSharedPreferences("carMakerFile",0);
        string_value = myMaker.getString("maker","");
        temp = findViewById(R.id.MakerInput);
        temp.setText(string_value);
    }

    public void clear(View v){
        temptext = findViewById(R.id.SeatsInput);
        temptext.setText("");
        temptext = findViewById(R.id.ModelInput);
        temptext.setText("");
        temptext = findViewById(R.id.MakerInput);
        temptext.setText("");
        temptext = findViewById(R.id.YearInput);
        temptext.setText("");
        temptext = findViewById(R.id.ColourInput);
        temptext.setText("");
        temptext = findViewById(R.id.PriceInput);
        temptext.setText("");
        temptext = findViewById(R.id.AddressInput);
        temptext.setText("");
    }

    public void clear_all(View v){
        temptext = findViewById(R.id.SeatsInput);
        temptext.setText("");
        temptext = findViewById(R.id.ModelInput);
        temptext.setText("");
        temptext = findViewById(R.id.MakerInput);
        temptext.setText("");
        temptext = findViewById(R.id.YearInput);
        temptext.setText("");
        temptext = findViewById(R.id.ColourInput);
        temptext.setText("");
        temptext = findViewById(R.id.PriceInput);
        temptext.setText("");
        temptext = findViewById(R.id.AddressInput);
        temptext.setText("");
        SharedPreferences myData = getSharedPreferences("file1",0);
        SharedPreferences myMaker = getSharedPreferences("carMakerFile",0);
        SharedPreferences.Editor myEditor = myData.edit();
        SharedPreferences.Editor MakerEditor = myMaker.edit();
        MakerEditor.remove("maker");
        myEditor.remove("model");
        myEditor.remove("colour");
        myEditor.remove("address");
        myEditor.remove("year");
        myEditor.remove("seat");
        myEditor.remove("price");
        myEditor.commit();
        MakerEditor.commit();
    }

    public void remove_last_car(View v){
        myList.remove(myList.size()-1);
        myAdapter.notifyDataSetChanged();
    }

    public void remove_all_cars(View v){
        myList.clear();
        myAdapter.notifyDataSetChanged();
    }

    public void close_clicked(View v){
        finish();
    }

    public void count_clicked(View v){
        Toast myMessage = Toast.makeText(this,
                "Car count: "+ myList.size(),
                Toast.LENGTH_SHORT);
        myMessage.show();

    }

    public void show_sms_toast(String msg){
        Toast myMessage = Toast.makeText(this,
                msg,
                Toast.LENGTH_SHORT);
        myMessage.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_clear)
            clear_all(null);
        else if(id == R.id.menu_count)
            count_clicked(null);
        return super.onOptionsItemSelected(item);

    }
}