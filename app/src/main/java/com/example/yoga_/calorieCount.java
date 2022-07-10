package com.example.yoga_;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class calorieCount extends AppCompatActivity {
    TextView finalBMR, caloriesWithActivity;
    RadioGroup exerciseIntensityRadioGroup;
    String selectedExerciseType = "no";
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String weight, height, age, gender;

    int bmr, bmr_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_count);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Calorie counter"); // set the top title
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFF8243")));
        exerciseIntensityRadioGroup = findViewById(R.id.exerciseIntensityRadioGroup);
        finalBMR = findViewById(R.id.finalBMR);
        caloriesWithActivity = findViewById(R.id.caloriesWithActivity);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        DocumentReference docRef = fStore.collection("users").document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid());

        docRef.get().addOnSuccessListener(documentSnapshot -> {

            if (documentSnapshot.exists()) {
                height = documentSnapshot.getString("height");
                weight = documentSnapshot.getString("weight");
                gender = documentSnapshot.getString("gender");
                age = documentSnapshot.getString("age");
            }

            String bmr = "BMR "+ calculateCalorie() + "  calories";
            finalBMR.setText(bmr);
            String caloriesAfterBmr_ = String.valueOf(bmr_) + " calories with the activity level";
            caloriesWithActivity.setText(caloriesAfterBmr_);

        });


        exerciseIntensityRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.noExercise:
                    selectedExerciseType = "no";
                    break;
                case R.id.lightExercise:
                    selectedExerciseType = "light";
                    break;
                case R.id.moderateExercise:
                    selectedExerciseType = "moderate";
                    //Toast.makeText(addDetails.this, "OTHER", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.intenseExercise:
                    selectedExerciseType = "intense";
                    break;

            }

            String bmr ="BMR "+calculateCalorie() + "  calories";
            finalBMR.setText(bmr);
            String caloriesAfterBmr_ = String.valueOf(bmr_) + " calories with the activity level";
            caloriesWithActivity.setText(caloriesAfterBmr_);
        });


    }

    public String calculateCalorie() {
        int w = Integer.parseInt(weight);
        int h = Integer.parseInt(height);
        int ag = Integer.parseInt(age);

//        Men: BMR = 88.362 + (13.397 x weight in kg) + (4.799 x height in cm) – (5.677 x age in years)
//        Women: BMR = 447.593 + (9.247 x weight in kg) + (3.098 x height in cm) – (4.330 x age in years)

        if (gender.contains("male")) {
            bmr = (int) (88.362 + (13.397 * w) + (4.799 * h) - (5.677 * ag));
        } else {
            bmr = (int) (447.593 + (9.247 * w) + (3.098 * h) - (4.330 * ag));
        }

        switch (selectedExerciseType) {
            case "no":
                bmr_ = (int) (bmr * 1.2);
                break;
            case "light":
                bmr_ = (int) (bmr * 1.375);
                break;
            case "moderate":
                bmr_ = (int) (bmr * 1.55);
                break;
            case "intense":
                bmr_ = (int) (bmr * 1.725);
                break;
        }
        return String.valueOf(bmr);
    }

}