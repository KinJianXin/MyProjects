package com.example.hackathon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hackathon.ShowAsked.AskedQuestions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AskActivity extends AppCompatActivity {

    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("hackathon/ask");
    }

    public String readInput(){
        EditText userInput = findViewById(R.id.replyInput);
        return userInput.getText().toString();
    }

    public void submitQuestion(View view){
        AskedQuestions question = new AskedQuestions(readInput());
        myRef.push().setValue(question);
        Toast toast = Toast.makeText(this,
                "Question submitted successfully!",
                Toast.LENGTH_SHORT);
        toast.show();
        finish();
    }
}