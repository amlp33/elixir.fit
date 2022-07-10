package com.example.yoga_;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class addDetails extends AppCompatActivity {
    String selectedGender = "male";
    String visibilityIssue = "yes";
    String physicallyHandicapped = "no";
    RadioGroup rg, visibilityIssueRadioGroup, physicallyHandicappedRadioGroup;
    TextInputEditText firstNameTextField, lastNameTextField, height, weight, age;
    Button profileSaveButton;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;
    String userID;
    RadioButton noPhysicallyHandicapped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);

        getSupportActionBar().setTitle("  Profile Information"); // set the top title
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFF8243")));
        firstNameTextField = findViewById(R.id.firstNameTextField);
        lastNameTextField = findViewById(R.id.lastNameTextField);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        age = findViewById(R.id.age);
        profileSaveButton = findViewById(R.id.profileSaveButton);
        noPhysicallyHandicapped = findViewById(R.id.noPhysicallyHandicapped);
        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        DocumentReference docRef = fStore.collection("users").document(userID);


        rg = (RadioGroup) findViewById(R.id.genderRadioButtonGroup);
        visibilityIssueRadioGroup = (RadioGroup) findViewById(R.id.visibilityIssueRadioGroup);
        physicallyHandicappedRadioGroup = (RadioGroup) findViewById(R.id.physicallyHandicappedRadioGroup);


        // Radio button
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.maleRadioButton:
                        selectedGender = "male";
                        //Toast.makeText(addDetails.this, "MALE", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.femaleRadioButton:
                        selectedGender = "female";
                        //Toast.makeText(addDetails.this, "FEMALE", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.otherRadioButton:
                        selectedGender = "other";
                        //Toast.makeText(addDetails.this, "OTHER", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

        });

        visibilityIssueRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.yesVisibilityIssue:
                        visibilityIssue = "yes";
                        noPhysicallyHandicapped.setVisibility(View.VISIBLE);

                        break;
                    case R.id.noVisibilityIssue:
                        visibilityIssue = "no";
                        noPhysicallyHandicapped.setVisibility(View.GONE);
                        break;
                    default:
                        visibilityIssue="yes";
                        noPhysicallyHandicapped.setVisibility(View.VISIBLE);
                }
            }
        });


        physicallyHandicappedRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.noPhysicallyHandicapped:
                        physicallyHandicapped = "no";
                        break;
                    case R.id.upperBodyHandicapped:
                        physicallyHandicapped = "upper";
                        break;
                    case R.id.lowerBodyHandicapped:
                        physicallyHandicapped = "lower";
                        break;
                    default:
                        physicallyHandicapped = "no";
                }
            }
        });


        profileSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fName, lName, height_, weight_, age_;
                fName = firstNameTextField.getText().toString();
                lName = lastNameTextField.getText().toString();
                height_ = height.getText().toString();
                weight_ = weight.getText().toString();
                age_ = age.getText().toString();


                if (!fName.isEmpty() && !lName.isEmpty() && !height_.isEmpty() && !weight_.isEmpty() && !age_.isEmpty()) {

                    Map<String, Object> user = new HashMap<>();
                    user.put("firstName", fName);
                    user.put("lastName", lName);
                    user.put("age", age_);
                    user.put("weight", weight_);
                    user.put("height", height_);
                    user.put("gender", selectedGender);
                    user.put("visibilityIssue",visibilityIssue);
                    user.put("physicallyHandicapped", physicallyHandicapped);
                    docRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
//                                if(visibilityIssue.contains("yes")) {
                                  startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                  finish();
//                                }else if(!physicallyHandicapped.contains("no")){
//                                    Intent i = new Intent(getApplicationContext(), physicallyHandicappedHome.class);
//                                    i.putExtra("condition",physicallyHandicapped);
//                                    startActivity(i);
//                                }
                            } else {
                                Toast.makeText(addDetails.this, "Internal Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

    }

}