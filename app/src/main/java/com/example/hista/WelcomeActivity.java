package com.example.hista;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TableLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class WelcomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAccessAdapter tabAccessAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        toolbar = (Toolbar) findViewById(R.id.welcome_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hista");

        viewPager = (ViewPager) findViewById(R.id.welcome_tab_pager);
        tabAccessAdapter = new TabAccessAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAccessAdapter);

        tabLayout = (TabLayout) findViewById(R.id.welcome_tab);
        tabLayout.setupWithViewPager(viewPager);
    }
}
