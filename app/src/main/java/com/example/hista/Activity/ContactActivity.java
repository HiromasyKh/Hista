package com.example.hista.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hista.Adapter.PersonAdapter;
import com.example.hista.R;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {
    private static final String TAG = "ContactActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact);

        Log.d(TAG, "onCreate: Started");
        ListView listView = (ListView) findViewById(R.id.profileListView);

        PersonProfile john = new PersonProfile("Name0", "1-1-1999", "Male", "drawable://" + R.drawable.profile_image);
        PersonProfile john1 = new PersonProfile("Name1", "1-1-1999", "Male", "drawable://" + R.drawable.profile_image);
        PersonProfile john2 = new PersonProfile("Name2", "1-1-1999", "Male", "drawable://" + R.drawable.profile_image);
        PersonProfile john3 = new PersonProfile("Name3", "1-1-1999", "Male", "drawable://" + R.drawable.profile_image);
        PersonProfile john4 = new PersonProfile("Name4", "1-1-1999", "Male", "drawable://" + R.drawable.profile_image);
        PersonProfile john5 = new PersonProfile("Name5", "1-1-1999", "Male", "drawable://" + R.drawable.profile_image);
        PersonProfile john6 = new PersonProfile("Name6", "1-1-1999", "Male", "drawable://" + R.drawable.profile_image);
        PersonProfile john7 = new PersonProfile("Name7", "1-1-1999", "Male", "drawable://" + R.drawable.profile_image);
        PersonProfile john8 = new PersonProfile("Name8", "1-1-1999", "Male", "drawable://" + R.drawable.profile_image);
        PersonProfile john9 = new PersonProfile("Name9", "1-1-1999", "Male", "drawable://" + R.drawable.profile_image);
        PersonProfile john10 = new PersonProfile("Name10", "1-1-1999", "Male", "drawable://" + R.drawable.profile_image);
        PersonProfile john11 = new PersonProfile("Name11", "1-1-1999", "Male", "drawable://" + R.drawable.profile_image);
        PersonProfile john12 = new PersonProfile("Name12", "1-1-1999", "Male", "drawable://" + R.drawable.profile_image);
        PersonProfile john13 = new PersonProfile("Name13", "1-1-1999", "Male", "drawable://" + R.drawable.profile_image);
        PersonProfile john14 = new PersonProfile("Name14", "1-1-1999", "Male", "drawable://" + R.drawable.profile_image);
        PersonProfile john15 = new PersonProfile("Name15", "1-1-1999", "Male", "drawable://" + R.drawable.profile_image);
        PersonProfile john16 = new PersonProfile("Name16", "1-1-1999", "Male", "drawable://" + R.drawable.profile_image);


        ArrayList<PersonProfile> people = new ArrayList<>();
        people.add(john);
        people.add(john1);
        people.add(john2);
        people.add(john3);
        people.add(john4);
        people.add(john5);
        people.add(john6);
        people.add(john7);
        people.add(john8);
        people.add(john9);
        people.add(john10);
        people.add(john11);
        people.add(john12);
        people.add(john13);
        people.add(john14);
        people.add(john15);
        people.add(john16);

        PersonAdapter adapter = new PersonAdapter(this, R.layout.activity_list_view, people);
        listView.setAdapter(adapter);
    }
}