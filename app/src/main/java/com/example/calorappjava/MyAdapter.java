package com.example.calorappjava;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.core.Context;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    FoodPlanActivity context;

    ArrayList<FoodItem> list;

    public MyAdapter(FoodPlanActivity context, ArrayList<FoodItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.foodplanitem, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        FoodItem foodItem = list.get(position);
        holder.foodName.setText(foodItem.getFoodName());
        holder.calories.setText(foodItem.getCalories());
        holder.sugar.setText(foodItem.getSugar());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView foodName, calories, sugar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foodName = itemView.findViewById(R.id.foodNameTextView);
            calories = itemView.findViewById(R.id.caloriesTextView);
            sugar = itemView.findViewById(R.id.sugarTextView);

        }
    }

}
