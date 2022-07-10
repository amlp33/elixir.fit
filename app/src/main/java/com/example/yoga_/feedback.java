package com.example.yoga_;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class feedback extends AppCompatActivity {
    EditText overAllRating, difficultiesEditText, extraFeatureEditText;
    MaterialCardView difficultiesCardView, yesNewFeatureCardView;
    RadioGroup easyToUseRadioGroup, accurateBmiRadioGroup, interestedFeatureRadioGroup, extraFeatureRadioGroup;
    RadioButton yesEasyToUse, noEasyToUse;
    String overAllRatingString, difficultiesFacingString = "no", accurateBMI = "yes", connectPeopleFeature = "no", anyExtraFeature = "no";
    Button submitFeedback;
    RelativeLayout feedbackMainContainer;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getSupportActionBar().setTitle("Feedback");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFF8243")));
        feedbackMainContainer = findViewById(R.id.feedbackMainContainer);
        overAllRating = findViewById(R.id.overAllRating);
        difficultiesEditText = findViewById(R.id.difficultiesEditText);
        easyToUseRadioGroup = findViewById(R.id.easyToUseRadioGroup);
        yesEasyToUse = findViewById(R.id.yesEasyToUse);
        noEasyToUse = findViewById(R.id.noEasyToUse);
        extraFeatureEditText = findViewById(R.id.extraFeatureEditText);
        difficultiesCardView = findViewById(R.id.difficultiesCardView);
        accurateBmiRadioGroup = findViewById(R.id.accurateBmiRadioGroup);
        interestedFeatureRadioGroup = findViewById(R.id.interestedFeatureRadioGroup);
        extraFeatureRadioGroup = findViewById(R.id.extraFeatureRadioGroup);
        overAllRatingString = overAllRating.getText().toString();
        yesNewFeatureCardView = findViewById(R.id.yesNewFeatureCardView);
        submitFeedback = findViewById(R.id.submitFeedback);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        DocumentReference docRef = fStore.collection("feedbacks").document();


        extraFeatureEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                anyExtraFeature = extraFeatureEditText.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        difficultiesEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                difficultiesFacingString = difficultiesEditText.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        easyToUseRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.yesEasyToUse) {
                difficultiesCardView.setVisibility(View.GONE);
                difficultiesFacingString = "no";
            } else if (checkedId == R.id.noEasyToUse) {
                difficultiesCardView.setVisibility(View.VISIBLE);
            } else {
                difficultiesFacingString = "no";
            }
        });


        accurateBmiRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.yesAccurateBMI) {
                    accurateBMI = "yes";
                } else if (i == R.id.noAccurateBMI) {
                    accurateBMI = "no";
                } else {
                    accurateBMI = "yes";
                }
            }
        });


        interestedFeatureRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.yesInterestedFeature) {
                    connectPeopleFeature = "yes";
                } else if (i == R.id.noInterestedFeature) {
                    connectPeopleFeature = "no";
                } else {
                    connectPeopleFeature = "no";
                }
            }
        });


        extraFeatureRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.noNewFeature) {
                    yesNewFeatureCardView.setVisibility(View.GONE);
                    anyExtraFeature = "no";
                } else if (i == R.id.yesNewFeature) {
                    yesNewFeatureCardView.setVisibility(View.VISIBLE);
                } else {
                    anyExtraFeature = "no";
                }
            }
        });


        submitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overAllRatingString = overAllRating.getText().toString();
                String all = overAllRatingString + "  " + difficultiesFacingString + "  " + accurateBMI + " " + connectPeopleFeature + "  " + anyExtraFeature;
                Toast.makeText(feedback.this, "" + all, Toast.LENGTH_SHORT).show();


                if (!overAllRatingString.isEmpty() && !difficultiesFacingString.isEmpty() && !accurateBMI.isEmpty() && !connectPeopleFeature.isEmpty() && !anyExtraFeature.isEmpty()) {

                    Map<String, Object> feedback_ = new HashMap<>();
                    feedback_.put("rating", overAllRatingString);
                    feedback_.put("facingDifficulties", difficultiesFacingString);
                    feedback_.put("isBmiAccurate", accurateBMI);
                    feedback_.put("connectWithOtherPeople", connectPeopleFeature);
                    feedback_.put("anyExtraFeatureSuggestion", anyExtraFeature);

                    docRef.set(feedback_).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                            Snackbar.make(feedbackMainContainer, "Thank you so much for the feedback.", Snackbar.LENGTH_LONG)
                                    .setBackgroundTint(getResources().getColor(R.color.green))
                                    .setActionTextColor(getResources().getColor(R.color.white))
                                    .show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Snackbar.make(feedbackMainContainer, "Failed to submit", Snackbar.LENGTH_LONG)
                                    .setBackgroundTint(getResources().getColor(R.color.red))
                                    .setActionTextColor(getResources().getColor(R.color.white))
                                    .show();
                        }
                    });

                }


            }
        });
    }
}