package com.example.hista.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hista.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    private Button updateProfileBtn;
    private EditText userName, userStatus;
    private CircleImageView userProfileImage;
    private String currentUID;

    private FirebaseAuth authUser;
    private DatabaseReference roofReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        authUser = FirebaseAuth.getInstance();
        currentUID = authUser.getCurrentUser().getUid();
        roofReference = FirebaseDatabase.getInstance().getReference();

        InitializeFields();

        userName.setVisibility(View.INVISIBLE);

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSettings();
            }
        });

        GetUserInfo();
    }

    private void InitializeFields() {
        updateProfileBtn = (Button) findViewById(R.id.update_profile_button);
        userName = (EditText) findViewById(R.id.set_username);
        userStatus = (EditText) findViewById(R.id.set_status);
        userProfileImage = (CircleImageView) findViewById(R.id.profile_image_view);
    }

    private void UpdateSettings() {
        String name = userName.getText().toString();
        String status = userStatus.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(SettingActivity.this, "Please write your user name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(status)) {
            Toast.makeText(SettingActivity.this, "Please write your status", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(name) && TextUtils.isEmpty(status)) {
            Toast.makeText(SettingActivity.this, "Please write your user name and status", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, String> profileMap = new HashMap<>();
                profileMap.put("uid", currentUID);
                profileMap.put("name", name);
                profileMap.put("status", status);
            roofReference.child("Users").child(currentUID).setValue(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                SendUserToMainActivity();
                                Toast.makeText(SettingActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                            } else {
                                String errorMsg = task.getException().toString();
                                Toast.makeText(SettingActivity.this, "Error: " + errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
     }

    private void GetUserInfo() {
        roofReference.child("Users").child(currentUID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && dataSnapshot.hasChild("name") && dataSnapshot.hasChild("image")) {
                            String getUsername = dataSnapshot.child("name").getValue().toString();
                            String getStatus = dataSnapshot.child("status").getValue().toString();
//                            String getProfileImage = dataSnapshot.child("image").getValue().toString();

                            userName.setText(getUsername);
                            userStatus.setText(getStatus);
                        } else if (dataSnapshot.exists() && dataSnapshot.hasChild("name")) {
                            String getUsername = dataSnapshot.child("name").getValue().toString();

                            String getStatus = dataSnapshot.child("status").getValue().toString();
//                            String getProfileImage = dataSnapshot.child("image").getValue().toString();

                            userName.setText(getUsername);
                            userStatus.setText(getStatus);
                        } else {
                            userName.setVisibility(View.VISIBLE);
                            Toast.makeText(SettingActivity.this, "Please set profile details", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(SettingActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
