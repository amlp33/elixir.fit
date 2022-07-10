package com.example.yoga_;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class notificationTab extends AppCompatActivity {
    TextView noNotificationText, notificationInfo, notificationLink;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DocumentReference notificationDocRef, currentUser;
    String targetedBodyPart = "",visibilityIssue="";
    ListView notificationLinks;
    Bundle extras;

    ArrayAdapter<String> arr;
    ArrayList<String> totalNotifications = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_tab);
        getSupportActionBar().setTitle("Notifications"); // set the top title
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFF8243")));
        noNotificationText = findViewById(R.id.noNotificationText);
        notificationInfo = findViewById(R.id.notificationInfo);
        notificationLink = findViewById(R.id.notificationLink);
        notificationLinks = findViewById(R.id.notificationLinks);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        notificationDocRef = fStore.collection("notification").document();


        extras = getIntent().getExtras();
        targetedBodyPart = extras.getString("targetBodyType");
        visibilityIssue = extras.getString("visibilityIssue");
        Toast.makeText(this, "body part  " + targetedBodyPart, Toast.LENGTH_SHORT).show();
        currentUser = fStore.collection("users").document(fAuth.getCurrentUser().getUid());


        currentUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null) {
                    String visibilityIssue_ = documentSnapshot.getString("visibilityIssue");
                    String physicallyHandicapped_ = documentSnapshot.getString("physicallyHandicapped");
//                    if (visibilityIssue_.equals("yes")) {
//                        targetedBodyPart = "full";
//                    } else if (visibilityIssue_.equals("no") && physicallyHandicapped_.equals("upper")) {
//                        targetedBodyPart = "upper";
//                    } else if (visibilityIssue_.equals("no") && physicallyHandicapped_.equals("lower")) {
//                        targetedBodyPart = "lower";
//                    }
                }
            }
        });

        fStore.collection("notifications").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                noNotificationText.setVisibility(View.GONE);


                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        String type = d.getString("targetedBodyPart");
                        String vIssue = d.getString("visibilityIssue");
                        String link = d.getString("zoomLink");
                        //Toast.makeText(notificationTab.this, ""+d, Toast.LENGTH_SHORT).show();
                        if (type.equals(targetedBodyPart)) {
                            totalNotifications.add(link);
//                            Linkify.addLinks(notificationLink, Linkify.ALL);
                            arr = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_notification_list, R.id.link, totalNotifications);
                            notificationLinks.setAdapter(arr);
                        }
//                        else if (type.equals("no")) {
//                            totalNotifications.add(link);
//                            arr = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_notification_list, R.id.link, totalNotifications);
//                            notificationLinks.setAdapter(arr);
//                        }
//                        else{
//                            notificationLink.setVisibility(View.GONE);
//                        }
                    }
                }

            }


        });


    }
}