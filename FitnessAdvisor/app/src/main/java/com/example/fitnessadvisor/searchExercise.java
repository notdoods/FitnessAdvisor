package com.example.fitnessadvisor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.text.Editable;
import android.text.TextWatcher;

import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class searchExercise extends AppCompatActivity {
    DatabaseReference exerciseDatabase;
    EditText SearchField;
    RecyclerView ResultList;
    ArrayList<String> namesList;
    ArrayList<String> focusList;
    ArrayList<String> instructionsList;
    ArrayList<String> gymList;
    ArrayList<String> weatherList;
    ArrayList<Long> difficultyList;
    SearchAdapter searchAdapter;
    private static final String TAG = "searchExercise";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_exercise);

        exerciseDatabase = FirebaseDatabase.getInstance().getReference("workout_info");
        SearchField = findViewById(R.id.search_field);

        ResultList = findViewById(R.id.results);
        ResultList.setHasFixedSize(true);
        ResultList.setLayoutManager(new LinearLayoutManager(this));
        ResultList.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        namesList = new ArrayList<>();
        difficultyList = new ArrayList<>();
        focusList = new ArrayList<>();
        instructionsList = new ArrayList<>();
        gymList = new ArrayList<>();
        weatherList = new ArrayList<>();

        searchAdapter = new SearchAdapter(searchExercise.this, namesList, focusList, difficultyList, instructionsList, weatherList, gymList);
        ResultList.setAdapter(searchAdapter);
        SearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    setAdapter(s.toString());
                } else {
                    namesList.clear();
                    focusList.clear();
                    difficultyList.clear();
                    ResultList.removeAllViews();
                }
            }

        });
    }

    private void setAdapter(final String searchedString){
        exerciseDatabase.addListenerForSingleValueEvent(
                new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*
                Clears previous search results when adding new characters to search
                 */
                namesList.clear();
                focusList.clear();
                difficultyList.clear();
                ResultList.removeAllViews();
                /*
                Gather all workout information, then filters into respective ArrayList based on user
                query either based on name or focus area.
                 */
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String Name = snapshot.child("Name").getValue(String.class);
                    String FocusArea = snapshot.child("FocusArea").getValue(String.class);
                    Long Difficulty = snapshot.child("Difficulty").getValue(Long.class);
                    String Instructions = snapshot.child("Instructions").getValue(String.class);
                    String Weather = snapshot.child("Weather").getValue(String.class);
                    String Gym = snapshot.child("Gym").getValue(String.class);

                    if (Name.toLowerCase().contains(searchedString.toLowerCase())) {
                        namesList.add(Name);
                        focusList.add(FocusArea);
                        difficultyList.add(Difficulty);
                        weatherList.add(Weather);
                        instructionsList.add(Instructions);
                        gymList.add(Gym);
                    } else if (FocusArea.toLowerCase().contains(searchedString.toLowerCase())) {
                        namesList.add(Name);
                        focusList.add(FocusArea);
                        difficultyList.add(Difficulty);
                        weatherList.add(Weather);
                        instructionsList.add(Instructions);
                        gymList.add(Gym);
                    }
                }
                /*
                Creates a search adapter, which takes all the lists of workout information in order
                to provide the search results + on click button.
                 */
                searchAdapter = new SearchAdapter(searchExercise.this, namesList, focusList, difficultyList, instructionsList, weatherList, gymList);
                ResultList.setAdapter(searchAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        }

}
