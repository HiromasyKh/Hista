package com.example.hista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class AppbarActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appbar);

        toolbar = (Toolbar) findViewById(R.id.profile_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hosta");
    }
}
