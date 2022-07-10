package com.example.yoga_;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.api.Distribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    Bundle extras;
    String visibilityIssue, physicallyHandicapped;
    TextView textView;
    TextView bmi , calorieIntake;
    MaterialButton registerYogaSessionbutton;
    MaterialCardView cardio, yoga;
    String userID;
    RelativeLayout mainActivityContainer;
    DocumentReference yogaRegistrationDocRef;
    String targetBodyType="",visibilityIssue_1;
    Button feedbackButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFF8243")));
        mainActivityContainer = findViewById(R.id.mainActivityContainer);
        bmi = (TextView) findViewById(R.id.bmi);
        calorieIntake = (TextView)  findViewById(R.id.calorieIntake);
        cardio = (MaterialCardView)  findViewById(R.id.cardio);
        yoga = (MaterialCardView)  findViewById(R.id.yoga);
        registerYogaSessionbutton = findViewById(R.id.registerYogaSessionbutton);
        feedbackButton = findViewById(R.id.feedbackButton);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        DocumentReference docRef = fStore.collection("users").document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid());

        userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
         yogaRegistrationDocRef = fStore.collection("yogaRegistration").document(userID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String fName = documentSnapshot.getString("firstName");
                getSupportActionBar().setTitle("Hi, " + fName); // set the top title
                targetBodyType = documentSnapshot.getString("physicallyHandicapped");
                visibilityIssue_1  = documentSnapshot.getString("visibilityIssue");
            }
        });


       feedbackButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
              startActivity(new Intent(getApplicationContext(), feedback.class));
           }
       });


        bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),bmiCalculator.class));
            }
        });

        calorieIntake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), calorieCount.class));
            }
        });


       cardio.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
              Intent i = new Intent(getApplicationContext(), video_list.class);
              i.putExtra("exerciseType","cardio");
              i.putExtra("currentUserDisability",targetBodyType);
              startActivity(i);
           }
       });

       yoga.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent(getApplicationContext(), video_list.class);
               i.putExtra("exerciseType","yoga");
               i.putExtra("currentUserDisability",targetBodyType);
               startActivity(i);
           }
       });



        registerYogaSessionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                   @Override
                   public void onSuccess(DocumentSnapshot documentSnapshot) {

                       String physicallyHandicapped_, visibilityIssue_,firstName_, lastName_;
                       physicallyHandicapped_ = documentSnapshot.getString("physicallyHandicapped");
                       visibilityIssue_ = documentSnapshot.getString("visibilityIssue");
                       firstName_ = documentSnapshot.getString("firstName");
                       lastName_ = documentSnapshot.getString("lastName");
                       Map<String, Object> registration = new HashMap<>();

                       registration.put("firstName",firstName_);
                       registration.put("lastName",lastName_);
                       registration.put("physicallyHandicapped",physicallyHandicapped_);
                       registration.put("visibilityIssue",visibilityIssue_);

                       yogaRegistrationDocRef.set(registration).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())      {

                                    Snackbar.make(mainActivityContainer, "Registered !. Keep a tap on notification for the link", Snackbar.LENGTH_INDEFINITE)
                                            .setBackgroundTint(getResources().getColor(R.color.black))
                                            .setActionTextColor(getResources().getColor(R.color.white_1))
                                            .setDuration(5000)
                                            .show();

                                    checkUserRegistration();

                                }else{


                                    Snackbar.make(mainActivityContainer, "Something went wrong, try again later", Snackbar.LENGTH_SHORT)
                                            .setBackgroundTint(getResources().getColor(R.color.red))
                                            .setActionTextColor(getResources().getColor(R.color.white_1))
                                            .show();

                                }
                           }
                       });
                   }
               })  ;




            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), login.class));
        } else if (item.getItemId() == R.id.profile) {
            startActivity(new Intent(getApplicationContext(), profilePage.class));
        }
        else if(item.getItemId() == R.id.notification){
            Intent i = new Intent(getApplicationContext(), notificationTab.class);
            i.putExtra("targetBodyType",targetBodyType);
            i.putExtra("visiblilityIssue",visibilityIssue_1);
//            startActivity((new Intent(getApplicationContext(), notificationTab.class)));
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(fAuth.getCurrentUser() != null){
            checkUserRegistration();
        }
    }


    private void checkUserRegistration(){
     yogaRegistrationDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
         @Override
         public void onSuccess(DocumentSnapshot documentSnapshot) {
             if(documentSnapshot.exists()){
                 registerYogaSessionbutton.setEnabled(false);
                 registerYogaSessionbutton.setBackgroundColor(getResources().getColor(R.color.red_200));
                 registerYogaSessionbutton.setTextSize(12);
                 registerYogaSessionbutton.setText("You have registered for the session");
             }
         }
     });
    }
}