package com.example.yoga_;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class physicallyHandicappedHome extends AppCompatActivity {
String physicallyHandicappedType;
TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physically_handicapped_home);
        textView = findViewById(R.id.textView);
        Bundle extras = new Bundle();
        extras = getIntent().getExtras();

        physicallyHandicappedType = extras.getString("condition");
        textView.setText(physicallyHandicappedType);
    }
}