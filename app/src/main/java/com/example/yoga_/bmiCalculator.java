package com.example.yoga_;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class bmiCalculator extends AppCompatActivity {


    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
   TextView userBmi, userbmiSuggestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_calculator);

        getSupportActionBar().setTitle("BMI Calculator"); // set the top title
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFF8243")));
        userBmi = (TextView) findViewById(R.id.userBmi);
        userbmiSuggestion = (TextView)  findViewById(R.id.userBmiSuggestion);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        DocumentReference docRef = fStore.collection("users").document(fAuth.getCurrentUser().getUid());
       docRef .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
           @Override
           public void onSuccess(DocumentSnapshot documentSnapshot) {
               if (documentSnapshot.exists()) {
                   String weight, height;
                   weight = documentSnapshot.getString("weight");
                   height = documentSnapshot.getString("height");
                   int w = Integer.parseInt(weight);
                   double h = Double.parseDouble(height);
                   double meter = h/100;
                   double bmi =  w/(meter*meter) ;
                   String  finalBmi = String.format("%.2f", bmi);
                   userBmi.setText(finalBmi);

                   if(bmi <= 18.5) {
                       userbmiSuggestion.setText("You are under weight");
                   }else if(bmi > 18.5 && bmi < 24.9){
                       userbmiSuggestion.setText("Your weight is normal");
                   }else if(bmi >=25 && bmi < 29.9){
                       userbmiSuggestion.setText("You are over-weight");
                   }else if(bmi >=30 && bmi < 34.9){
                       userbmiSuggestion.setText("You are Obese");
                   }else if(bmi >=34){
                       userbmiSuggestion.setText("You are extremely Obese");
                   }

               }
           }
       });
    }
}