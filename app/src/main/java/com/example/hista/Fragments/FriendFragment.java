package com.example.hista.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hista.Activity.FindFriendActivity;
import com.example.hista.Activity.FriendProfileActivity;
import com.example.hista.Model.ShortUserInfo;
import com.example.hista.R;
import com.example.hista.ViewHolder.FriendViewHolder;
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

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendFragment extends Fragment {

    private View friendsListView;
    private RecyclerView friendsList;

    private DatabaseReference friendReference, userReference;
    private FirebaseAuth firebaseAuth;
    private String currentUserID;

    public FriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        friendsListView = inflater.inflate(R.layout.fragment_friend, container, false);

        friendsList = (RecyclerView) friendsListView.findViewById(R.id.friends_list);
        friendsList.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();

        friendReference = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserID);
        userReference = FirebaseDatabase.getInstance().getReference().child("Users");

        return friendsListView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<ShortUserInfo>()
                .setQuery(friendReference, ShortUserInfo.class)
                .build();

        FirebaseRecyclerAdapter<ShortUserInfo, ContactViewHolder> adapter
                = new FirebaseRecyclerAdapter<ShortUserInfo, ContactViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ContactViewHolder contactViewHolder, int i, @NonNull ShortUserInfo shortUserInfo) {
                String userIDs = getRef(i).getKey();

                userReference.child(userIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("image")) {
                            String userProfileImage = dataSnapshot.child("image").getValue().toString();
                            String profileName = dataSnapshot.child("name").getValue().toString();
                            String profileStatus = dataSnapshot.child("status").getValue().toString();

                            contactViewHolder.userName.setText(profileName);
                            contactViewHolder.userStatus.setText(profileStatus);
                            Picasso.get().load(userProfileImage).placeholder(R.drawable.profile_image).into(contactViewHolder.profileImage);
                        } else {
                            String profileName = dataSnapshot.child("name").getValue().toString();
                            String profileStatus = dataSnapshot.child("status").getValue().toString();

                            contactViewHolder.userName.setText(profileName);
                            contactViewHolder.userStatus.setText(profileStatus);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_find_friend, parent, false);
                ContactViewHolder contactViewHolder = new ContactViewHolder(view);
                return contactViewHolder;
            }
        };

        friendsList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userStatus;
        SimpleDraweeView profileImage;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.find_friend_user_image);
            userName = itemView.findViewById(R.id.find_friend_user_name);
            userStatus = itemView.findViewById(R.id.find_friend_user_status);
        }
    }
}
