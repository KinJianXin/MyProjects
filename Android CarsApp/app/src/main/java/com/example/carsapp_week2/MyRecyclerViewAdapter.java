package com.example.carsapp_week2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carsapp_week2.provider.Car;
import com.example.carsapp_week2.provider.CarViewModel;

import java.util.ArrayList;
import java.util.List;


public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    List<Car> cardList = new ArrayList<Car>();
    private Context context;

    public MyRecyclerViewAdapter (Context context){this.context = context;}

    public void setData(List<Car> cardList) {
        this.cardList = cardList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false); //CardView inflated as RecyclerView list item
        ViewHolder viewHolder = new ViewHolder(v);Log.d("week6App","onCreateViewHolder");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.maker.setText(cardList.get(position).getMaker());
        holder.model.setText(cardList.get(position).getModel());
        String temp = String.valueOf(cardList.get(position).getYear());
        holder.year.setText(temp);
        holder.colour.setText(cardList.get(position).getColour());
        holder.seats.setText(cardList.get(position).getSeats());
        holder.price.setText(cardList.get(position).getPrice());
        holder.address.setText(cardList.get(position).getAddress());


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),Bid.class);
                Toast myMessage = Toast.makeText(v.getContext(),"Car No: "
                        + Integer.toString(position)
                        + " Name: " + holder.maker.getText() + " Model: " + holder.model.getText(),Toast.LENGTH_SHORT);
                myMessage.show();
                String temp = cardList.get(position).getModel();
                i.putExtra("maker",holder.maker.getText());
                i.putExtra("model",holder.model.getText());
                i.putExtra("price",holder.price.getText());
                i.putExtra("car",temp);
                context.startActivity(i);
            }
        });



    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView maker;
        public TextView model;
        public TextView year;
        public TextView colour;
        public TextView seats;
        public TextView price;
        public TextView address;
        public View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            maker = itemView.findViewById(R.id.maker);
            model = itemView.findViewById(R.id.model);
            year = itemView.findViewById(R.id.year);
            colour = itemView.findViewById(R.id.colour);
            seats = itemView.findViewById(R.id.seats);
            price = itemView.findViewById(R.id.price);
            address = itemView.findViewById(R.id.address);



        }
    }
}

