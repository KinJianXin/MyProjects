package com.example.hackathon.CompletedQuestion;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hackathon.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CompletedQuestionAdapter extends RecyclerView.Adapter<CompletedQuestionAdapter.ViewHolder>{

    List<CompletedQuestion> cardList = new ArrayList<CompletedQuestion>();
    private Context context;

    public CompletedQuestionAdapter(Context context){this.context = context;}

    public void setData(List<CompletedQuestion> cardList) {
        this.cardList = cardList;
    }


    @NonNull
    @Override
    public CompletedQuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_question_card, parent, false); //CardView inflated as RecyclerView list item
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedQuestionAdapter.ViewHolder holder, int position) {
        holder.question.setText(cardList.get(position).getQuestion());
        holder.answer.setText(cardList.get(position).getAnswer());
    }

    @Override
    public int getItemCount() {
        {
            return cardList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView question;
        public TextView answer;
        public View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            question =itemView.findViewById(R.id.ques);
            answer =itemView.findViewById(R.id.ans);
        }
    }
}
