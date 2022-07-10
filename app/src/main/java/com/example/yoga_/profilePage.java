package com.example.yoga_;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class profilePage extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    EditText userName, userAge, userWeight, userHeight;
    Button profileEditButton, profileSaveButton;
    LinearLayout profileMainContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        getSupportActionBar().setTitle("Profile"); // set the top title
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFF8243")));
        userName = (EditText) findViewById(R.id.userName);
        userAge = (EditText) findViewById(R.id.userAge);
        userWeight = (EditText) findViewById(R.id.userWeight);
        userHeight = (EditText) findViewById(R.id.userHeight);
        profileEditButton = (Button) findViewById(R.id.profileEditButton);
        profileSaveButton = (Button) findViewById(R.id.profileSaveButton);
        profileMainContainer = (LinearLayout) findViewById(R.id.profileMainContainer);

        profileEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userAge.setEnabled(true);
                userWeight.setEnabled(true);
                userHeight.setEnabled(true);
                userHeight.setTextColor(getResources().getColor(R.color.red_200));
                userWeight.setTextColor(getResources().getColor(R.color.red_200));
                userAge.setTextColor(getResources().getColor(R.color.red_200));
                profileSaveButton.setVisibility(View.VISIBLE);
                profileEditButton.setVisibility(View.GONE);

            }
        });

        profileSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DocumentReference docRef = fStore.collection("users").document(fAuth.getCurrentUser().getUid());
                String uAge, uWeight, uHeight;
                uAge = userAge.getText().toString();
                uWeight = userWeight.getText().toString();
                uHeight = userHeight.getText().toString();
                docRef.update("age", uAge, "weight", uWeight, "height", uHeight).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Snackbar.make(profileMainContainer, "Updated Successfully", Snackbar.LENGTH_LONG)
                                .setBackgroundTint(getResources().getColor(R.color.green))
                                .setActionTextColor(getResources().getColor(R.color.white))
                                .show();


                        userAge.setEnabled(false);
                        userWeight.setEnabled(false);
                        userHeight.setEnabled(false);
                        profileSaveButton.setVisibility(View.GONE);
                        profileEditButton.setVisibility(View.VISIBLE);
                    }
                });
            }
        });


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        DocumentReference docRef = fStore.collection("users").document(fAuth.getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(documentSnapshot -> {


            Snackbar.make(profileMainContainer, "Loading...", Snackbar.LENGTH_INDEFINITE)
                    .setBackgroundTint(getResources().getColor(R.color.gray))
                    .setActionTextColor(getResources().getColor(R.color.white))
                    .setDuration(500)
                    .show();
            if (documentSnapshot.exists()) {
                String firstName = documentSnapshot.getString("firstName");
                String lastName = documentSnapshot.getString("lastName");
                String fullName = firstName + "  " + lastName;
                String age = documentSnapshot.getString("age");
                String height = documentSnapshot.getString("height");
                String weight = documentSnapshot.getString("weight");
                userName.setText(fullName);
                userAge.setText(age);
                userWeight.setText(weight);
                userHeight.setText(height);

            }
        });
    }
}