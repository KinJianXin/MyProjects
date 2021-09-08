package com.example.hackathon.ShowAsked;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hackathon.CompletedQuestion.CompletedQuestion;
import com.example.hackathon.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UploadAnswer extends AppCompatActivity {

    String question;
    DatabaseReference unansweredRef;
    DatabaseReference completedRef;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_answer);
        Bundle extras = getIntent().getExtras();
        submit = findViewById(R.id.submitAnswer);
        TextView theQues = findViewById(R.id.textView5);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAnswer(v);
                System.out.println("done");
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        unansweredRef = database.getReference("hackathon/ask");
        completedRef = database.getReference("hackathon/completed");

        question = extras.getString("question");
        theQues.setText(question);

    }
    public void submitAnswer (View v){
        EditText input = findViewById(R.id.replyInput);
        CompletedQuestion completed = new CompletedQuestion(question, input.getText().toString());
        unansweredRef.child(question).removeValue();
        completedRef.push().setValue(completed);
        Toast toast = Toast.makeText(this,
                "Answer submitted successfully!",
                Toast.LENGTH_SHORT);
        toast.show();
        finish();
    }
}