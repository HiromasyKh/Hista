package com.example.hista.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hista.Adapter.MessageAdapter;
import com.example.hista.Model.Chat;
import com.example.hista.Model.Messages;
import com.example.hista.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private String msgReceiverId, msgReceiverName, msgReceiverImage, msgSenderID;
    private TextView userName, userLastSeen;
    private CircleImageView userImage;

    private Toolbar chatToolBar;

    private ImageButton sendMessageBtn;
    private EditText msgInputText;

    private FirebaseAuth auth;
    private DatabaseReference rootRef;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private RecyclerView userMessagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        auth = FirebaseAuth.getInstance();
        msgSenderID = auth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

        msgReceiverId = getIntent().getExtras().get("visitUserID").toString();
        msgReceiverName = getIntent().getExtras().get("visitUserName").toString();
        msgReceiverImage = getIntent().getExtras().get("visitUserImage").toString();

        InitializeControllers();

        userName.setText(msgReceiverName);
        Picasso.get().load(msgReceiverImage).placeholder(R.drawable.profile_image).into(userImage);

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMsg();
            }
        });
    }

    private void InitializeControllers() {
        chatToolBar = (Toolbar) findViewById(R.id.private_chat_toolbar);
        setSupportActionBar(chatToolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = layoutInflater.inflate(R.layout.custom_chat_bar, null);
        actionBar.setCustomView(actionBarView);

        userImage = (CircleImageView)findViewById(R.id.custom_chat_profile_image);
        userName = (TextView)findViewById(R.id.custom_chat_profile_name);
        userLastSeen = (TextView)findViewById(R.id.custom_chat_user_last_seen);

        sendMessageBtn = (ImageButton) findViewById(R.id.private_chat_send_btn);
        msgInputText = (EditText) findViewById(R.id.private_chat_input_msg);

        messageAdapter = new MessageAdapter(messagesList);
        userMessagesList = (RecyclerView) findViewById(R.id.private_chat_list_messages);
        linearLayoutManager = new LinearLayoutManager(this);
        userMessagesList.setLayoutManager(linearLayoutManager);
        userMessagesList.setAdapter(messageAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        rootRef.child("Messages").child(msgSenderID).child(msgReceiverId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Messages messages = dataSnapshot.getValue(Messages.class);

                        messagesList.add(messages);

                        messageAdapter.notifyDataSetChanged();

                        userMessagesList.smoothScrollToPosition(userMessagesList.getAdapter().getItemCount());
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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

    private void SendMsg() {
        String msgText = msgInputText.getText().toString();
        if (TextUtils.isEmpty(msgText)) {
            Toast.makeText(this, "Write your message...", Toast.LENGTH_SHORT).show();
        } else {
            String msgSenderRef = "Messages/" + msgSenderID + "/" + msgReceiverId;
            String msgReceiverRef = "Messages/" + msgReceiverId + "/" + msgSenderID;

            DatabaseReference userMsgKeyRef = rootRef.child("Messages")
                    .child(msgSenderID).child(msgReceiverId).push();

            String msgPushID = userMsgKeyRef.getKey();

            Map msgTextBody = new HashMap();
            msgTextBody.put("message", msgText);
            msgTextBody.put("type", "text");
            msgTextBody.put("from", msgSenderID);

            Map msgBodyDetail = new HashMap();
            msgBodyDetail.put(msgSenderRef + "/" + msgPushID, msgTextBody);
            msgBodyDetail.put(msgReceiverRef + "/" + msgPushID, msgTextBody);

            rootRef.updateChildren(msgBodyDetail).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ChatActivity.this, "Message sent...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChatActivity.this, "Error...", Toast.LENGTH_SHORT).show();
                    }
                    msgInputText.setText("");
                }
            });
        }
    }
}
