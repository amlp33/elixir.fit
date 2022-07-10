package com.example.yoga_;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class video_list extends AppCompatActivity implements VideoAdapter.OnVideoItemListenener {
    RecyclerView recyclerView;
    ArrayList<Video> videoLinksArrayList, upperBodyList, lowerBodyList, blindList, calorieBurnList, yogaList;
    VideoAdapter videoAdapter;
    FirebaseFirestore Fstore;
    FirebaseAuth fAuth;
    Video getExerciseType;
    Bundle extras;
    String value_;
    String currentList;
    String currentUserDisability="", currentUserVisibility;
    DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFF8243")));
        recyclerView = findViewById(R.id.videoListRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Fstore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        videoLinksArrayList = new ArrayList<Video>();
        upperBodyList = new ArrayList<Video>();
        lowerBodyList = new ArrayList<Video>();
        calorieBurnList = new ArrayList<Video>();
        yogaList = new ArrayList<Video>();
        blindList = new ArrayList<Video>();

        extras = getIntent().getExtras();
        value_ = extras.getString("exerciseType");
        getSupportActionBar().setTitle(value_);


         docRef = Fstore.collection("users").document(fAuth.getCurrentUser().getUid());





        getVideoLinkDataFromFireStore();

    }


    private void getVideoLinkDataFromFireStore() {
        Fstore.collection("videoLinks")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {

                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore Error", error.getMessage());
                            return;
                        }


                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                Toast.makeText(video_list.this, "data "+documentSnapshot, Toast.LENGTH_SHORT).show();
                                currentUserDisability = documentSnapshot.getString("physicallyHandicapped");

//                                Toast.makeText(video_list.this, "data "+currentUserDisability, Toast.LENGTH_SHORT).show();
                                currentUserVisibility = documentSnapshot.getString("visibilityIssue");
                            }
                        });

                        videoLinksArrayList.clear();
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                videoLinksArrayList.add(dc.getDocument().toObject(Video.class));
                            } else if (dc.getType() == DocumentChange.Type.REMOVED) {
                                videoLinksArrayList.remove(dc.getOldIndex());
                                videoAdapter.notifyItemRemoved(dc.getOldIndex());
                                videoAdapter.notifyDataSetChanged();
                            } else if (dc.getType() == DocumentChange.Type.MODIFIED) {
                                videoAdapter.notifyDataSetChanged();
                            }
                        }

//                        Toast.makeText(video_list.this, "Target Body "+currentUserDisability, Toast.LENGTH_SHORT).show();

                        for (int i = 0, l = videoLinksArrayList.size(); i < l; i++) {

                            Video vd = videoLinksArrayList.get(i);

                            if (vd.type.contains("yoga")) {
                                yogaList.add(vd);
                            } else if (vd.type.contains("calorieBurn")) {
                                calorieBurnList.add(vd);
                            } else if(vd.type.contains("yoga")){
                                yogaList.add(vd);
                            }else if(vd.type.contains("calorieBurn")){
                               calorieBurnList.add(vd) ;
                            }
                        }

                        if (extras != null) {
                            if (value_.equals("yoga")) {
                                videoAdapter = new VideoAdapter(video_list.this, yogaList, video_list.this);
                                recyclerView.setAdapter(videoAdapter);
                            } else if (value_.equals("cardio")) {
                                videoAdapter = new VideoAdapter(video_list.this, calorieBurnList, video_list.this);
                                recyclerView.setAdapter(videoAdapter);
                            }
                            else {
                                videoAdapter = new VideoAdapter(video_list.this, blindList,video_list.this);
                            }
                        }

                        videoAdapter.notifyDataSetChanged();

                    }
                });
    }

    @Override
    public void onVideoClick(int position) {
        String link = "";

        Intent i = new Intent(getApplicationContext(), youtube_player.class);
        if (extras != null) {
            if (value_.equals("yoga")) {
                Video vd = yogaList.get(position);
                link = vd.videoLink.toString();
                i.putExtra("link", link);
                startActivity(i);
            } else if (value_.equals("cardio")) {
                Video vd = calorieBurnList.get(position);
                link = vd.videoLink.toString();
                i.putExtra("link", link);
                startActivity(i);
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}