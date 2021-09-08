package com.example.hackathon.CompletedQuestion;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "CompletedQuestion")
public class CompletedQuestion {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "questionId")
    int id;
    @ColumnInfo(name = "question")
    String question;
    @ColumnInfo(name = "answer")
    String answer;

    public CompletedQuestion(String question, String answer){
        this.question = question;
        this.answer = answer;
    }

    public CompletedQuestion(){}

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
