package com.example.hackathon.CompletedQuestion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.hackathon.ShowAsked.AnsActivity;
import com.example.hackathon.AskActivity;
import com.example.hackathon.R;
import com.example.hackathon.ShowAsked.AskedQuestions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class CompletedQuestionView extends AppCompatActivity {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    DatabaseReference myRef;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CompletedQuestionAdapter adapter;
    List<CompletedQuestion> questionList = new ArrayList<CompletedQuestion>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.drawer_layout);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("hackathon/completed");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                CompletedQuestion value = dataSnapshot.getValue(CompletedQuestion.class);
                questionList.add(value);
                Log.d(TAG, "Value is: " + value);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        CompletedQuestion Q1 = new CompletedQuestion("Tell me about yourself.", "Start by describing your background with a summary of your most impressive responsibilities, next briefly summarize your previous experience with key achievements, lastly express how you found the new job and why it's a good fit for you and your goals");
        questionList.add(Q1);
        CompletedQuestion Q2 = new CompletedQuestion("How would you describe yourself", "With this question, your interviewer wants to learn how your qualities and characteristics align with the skills they believe are required to succeed in the role. To answer this question, pick one to a few personal characteristics and elaborate on them with examples.");
        questionList.add(Q2);
        CompletedQuestion Q3 = new CompletedQuestion("What makes you unique", "Assets the employers finds valuable, ways you've been successful in previous roles, traits or skills you've been praised for.");
        questionList.add(Q3);
        CompletedQuestion Q4 = new CompletedQuestion("Why do you want to work here?", "Interviewers often ask this question to determine whether or not you took the time to research the company and think critically about whether you’re a good fit. The best way to prepare for this question is to do your homework and learn about the products, services, mission, history and culture of this workplace. In your answer, mention the aspects of the company that appeals to you and aligns with your values and career goals.");
        questionList.add(Q4);
        CompletedQuestion Q5 = new CompletedQuestion("What interests you about this role?", "Study the job description carefully and compare its requirements to your skills and experience. Choose a few responsibilities you particularly enjoy or excel at and focus on those in your answer.");
        questionList.add(Q5);
        CompletedQuestion Q6 = new CompletedQuestion("How do you handle stress?", "Spend some time thinking about how you approach stress and provide an example that communicates your ability to persevere in stressful situations.");
        questionList.add(Q6);

        recyclerView = findViewById(R.id.completed_ques_view);

        layoutManager = new LinearLayoutManager(this);  //A RecyclerView.LayoutManager implementation which provides similar functionality to ListView.
        recyclerView.setLayoutManager(layoutManager);   // Also StaggeredGridLayoutManager and GridLayoutManager or a custom Layout manager

        adapter = new CompletedQuestionAdapter(this);
        recyclerView.setAdapter(adapter);

        adapter.setData(questionList);

        // toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Interview Helper");


        // navigation menu
        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new myNavListener());
    }

    class myNavListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();

            if(id == R.id.AQ){
                Intent intent = new Intent(CompletedQuestionView.this, AskActivity.class);
                startActivity(intent);
            }
            else if(id == R.id.VQ){
                Intent intent = new Intent(CompletedQuestionView.this, AnsActivity.class);
                startActivity(intent);
            }

            return true;
        }
    }
}