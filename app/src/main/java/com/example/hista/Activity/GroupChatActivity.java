package com.example.hista.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.media.Image;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hista.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupChatActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton sendMsgBtn;
    private EditText userMsg;
    private ScrollView scrollView;
    private TextView textMsg;

    private String currentGroupName, currentUID, currentUsername, currentDate, currentTime;

    private FirebaseAuth authUser;
    private DatabaseReference userReference, groupReference, groupMsgKeyReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        currentGroupName = getIntent().getExtras().get("groupName").toString();
        Toast.makeText(this, currentGroupName, Toast.LENGTH_LONG).show();

        authUser = FirebaseAuth.getInstance();
        currentUID = authUser.getCurrentUser().getUid();
        userReference = FirebaseDatabase.getInstance().getReference().child("Users");
        groupReference = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName);

        InitializeFields();

        GetUserInfo();

        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMsgToDatabase();
                userMsg.setText("");

                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        groupReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void InitializeFields() {
        toolbar = (Toolbar) findViewById(R.id.group_chat_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentGroupName);

        sendMsgBtn = (ImageButton) findViewById(R.id.group_chat_send_msg_btn);
        userMsg = (EditText) findViewById(R.id.group_chat_input_message);
        scrollView = (ScrollView) findViewById(R.id.group_chat_scroll_view);
        textMsg = (TextView) findViewById(R.id.group_chat_txt_display);
    }

    private void GetUserInfo() {
        userReference.child(currentUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentUsername = dataSnapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SaveMsgToDatabase() {
        String msg = userMsg.getText().toString();
        String msgKey = groupReference.push().getKey();

        if (TextUtils.isEmpty(msg)) {
            Toast.makeText(this, "Please write anything", Toast.LENGTH_SHORT).show();
        } else {
            Calendar calendarForDate = Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            currentDate = currentDateFormat.format(calendarForDate.getTime());

            Calendar calendarForTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = currentTimeFormat.format(calendarForTime.getTime());

            HashMap<String, Object> groupMessageKey = new HashMap<>();
            groupReference.updateChildren(groupMessageKey);

            groupMsgKeyReference = groupReference.child(msgKey);

            HashMap<String, Object> msgInfoMap = new HashMap<> ();
                msgInfoMap.put("name", currentUsername);
                msgInfoMap.put("message", msg);
                msgInfoMap.put("date", currentDate);
                msgInfoMap.put("time", currentTime);
            groupMsgKeyReference.updateChildren(msgInfoMap);
        }
    }

    private void DisplayMessages(DataSnapshot dataSnapshot) {
        Iterator iterator = dataSnapshot.getChildren().iterator();

        while(iterator.hasNext()) {
            String msgDate = (String) ((DataSnapshot) iterator.next()).getValue().toString();
            String msg = (String) ((DataSnapshot) iterator.next()).getValue().toString();
            String msgOwner = (String) ((DataSnapshot) iterator.next()).getValue().toString();
            String msgTime = (String) ((DataSnapshot) iterator.next()).getValue().toString();

            textMsg.append(msgOwner + ": \n" + msg + "\n" + msgTime + "  " + msgDate + "\n\n\n");

            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }
}
