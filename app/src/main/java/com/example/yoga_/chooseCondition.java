package com.example.yoga_;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class chooseCondition extends AppCompatActivity {
Button blindCondition, upperBodyCondition, lowerBodyCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_condition);
        blindCondition = findViewById(R.id.blindConditionButton);
        upperBodyCondition = findViewById(R.id.upperBodyCondition);
        lowerBodyCondition = findViewById(R.id.lowerBodyCondition);
    String upper="upperBody", lower="lowerBody", blind="blind";

     blindCondition.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent i = new Intent(chooseCondition.this, video_list.class);
             i.putExtra("condition",blind);
             startActivity(i);
         }
     });

     upperBodyCondition.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            Intent i = new Intent(chooseCondition.this, video_list.class) ;
            i.putExtra("condition", upper);
            startActivity(i);

         }
     });

     lowerBodyCondition.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

             Intent i = new Intent(chooseCondition.this, video_list.class) ;
             i.putExtra("condition", lower);
             startActivity(i);
         }
     });


    }
}