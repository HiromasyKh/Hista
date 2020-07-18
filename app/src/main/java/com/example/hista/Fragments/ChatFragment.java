package com.example.hista.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hista.Activity.ChatActivity;
import com.example.hista.Activity.FindFriendActivity;
import com.example.hista.Model.ShortUserInfo;
import com.example.hista.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private View privateChatView;
    private RecyclerView chatList;
    private ProgressBar progressBar;

    private DatabaseReference chatReference, userReference;
    private FirebaseAuth auth;
    private String currentUserID;


    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        privateChatView = inflater.inflate(R.layout.fragment_chat, container, false);

        auth = FirebaseAuth.getInstance();
        currentUserID = auth.getCurrentUser().getUid();
        chatReference = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserID);
        userReference = FirebaseDatabase.getInstance().getReference().child("Users");

        chatList = (RecyclerView) privateChatView.findViewById(R.id.private_chat_list);
        chatList.setLayoutManager(new LinearLayoutManager(getContext()));

        return privateChatView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ShortUserInfo> options =
                new FirebaseRecyclerOptions.Builder<ShortUserInfo>()
                .setQuery(chatReference, ShortUserInfo.class)
                .build();

        FirebaseRecyclerAdapter<ShortUserInfo, ChatViewHolder> adapter =
                new FirebaseRecyclerAdapter<ShortUserInfo, ChatViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ChatViewHolder chatViewHolder, int i, @NonNull ShortUserInfo shortUserInfo) {
                        final String userIDs = getRef(i).getKey();
                        final String[] retImage = {"default_image"};

                        userReference.child(userIDs).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    if (dataSnapshot.hasChild("image")) {
                                        retImage[0] = dataSnapshot.child("image").getValue().toString();

                                        Picasso.get().load(retImage[0]).placeholder(R.drawable.profile_image).into(chatViewHolder.profileImage);
                                    }

                                    final String retName = dataSnapshot.child("name").getValue().toString();
                                    final String retStatus = dataSnapshot.child("status").getValue().toString();

                                    chatViewHolder.userName.setText(retName);
                                    chatViewHolder.userStatus.setText("Last Seen: " + " Date: Times");

                                    chatViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                            chatIntent.putExtra("visitUserID", userIDs);
                                            chatIntent.putExtra("visitUserName", retName);
                                            chatIntent.putExtra("visitUserImage", retImage[0]);
                                            startActivity(chatIntent);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_find_friend, parent, false);
                        ChatViewHolder chatViewHolder = new ChatViewHolder(view);
                        return chatViewHolder;
                    }
                };
        chatList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView profileImage;
        TextView userName, userStatus;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.find_friend_user_image);
            userName = itemView.findViewById(R.id.find_friend_user_name);
            userStatus = itemView.findViewById(R.id.find_friend_user_status);
        }
    }

    private void showLoading(boolean state){
        if(state){
            chatList.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            chatList.setVisibility(View.VISIBLE);
        }
    }
}
