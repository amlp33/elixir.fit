package com.example.yoga_;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class youtube_player extends YouTubeBaseActivity {
    String value;
    Bundle extras;
    YouTubePlayerView youTubePlayerView;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String docId;
    YouTubePlayer youTubePlayer_;
    long videoDuration;
    DocumentReference docRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);
//        getSupportActionBar().setTitle("workout");
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFF8243")));
        youTubePlayerView = findViewById(R.id.youtubePlayerView);
        extras = getIntent().getExtras();
        value = extras.getString("link");
        String videoID = getYoutubeVideoId(value);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();


        fStore.collection("videoLinks")
                .whereEqualTo("videoLink", value)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        docId = document.getId();
                        docRef = fStore.collection("videoLinks").document(docId);
//                        Toast.makeText(youtube_player.this, "id "+docId, Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getVideoDuration();
                            }
                        }, 1000);
                    }
                } else {
                }

            }
        });



        YouTubePlayer.OnInitializedListener listener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer_ = youTubePlayer;
                youTubePlayer.loadVideo(videoID);
                youTubePlayer.play();
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                Toast.makeText(youtube_player.this, "something is messed up", Toast.LENGTH_SHORT).show();
            }

        };


        youTubePlayerView.initialize("AIzaSyCjOydJ1P4ZXONZ0N8bxBxG26QWaicXVm8", listener);

    }

    public static String getYoutubeVideoId(String youtubeUrl) {
        String video_id = "";
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0 && youtubeUrl.startsWith("http")) {

            String expression = "^.*((youtu.be" + "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
            CharSequence input = youtubeUrl;
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                String groupIndex1 = matcher.group(7);
                if (groupIndex1 != null && groupIndex1.length() == 11)
                    video_id = groupIndex1;
            }
        }
        return video_id;
    }

    public String getVideoDuration() {
        long millis = youTubePlayer_.getDurationMillis();
        videoDuration = TimeUnit.MILLISECONDS.toMinutes(millis);
        Toast.makeText(youtube_player.this, "duration " + videoDuration, Toast.LENGTH_SHORT).show();

        docRef.update("videoDuration", videoDuration).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(youtube_player.this, "added successfully", Toast.LENGTH_SHORT).show();
            }
        });
        return String.valueOf(videoDuration) ;
    }
    protected void onDestroy() {
        super.onDestroy();
        if (youTubePlayer_ != null) {
            youTubePlayer_.release();
        }
    }
}