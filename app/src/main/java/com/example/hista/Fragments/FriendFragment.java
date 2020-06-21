package com.example.hista.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hista.Model.Users;
import com.example.hista.R;
import com.example.hista.ViewHolder.FriendViewHolder;
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
        friendReference = FirebaseDatabase.getInstance().getReference().child("Chats").child(currentUserID);
        userReference = FirebaseDatabase.getInstance().getReference().child("Users");

        return friendsListView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(friendReference, Users.class)
                        .build();

        FirebaseRecyclerAdapter<Users, FriendViewHolder> adapter =
                new FirebaseRecyclerAdapter<Users, FriendViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final FriendViewHolder friendViewHolder, int i, @NonNull Users users) {
                        String userId = getRef(i).getKey();

                        userReference.child(userId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("image")) {
                                    String profileUserImage = dataSnapshot.child("image").getValue().toString();
                                    String profileName = dataSnapshot.child("name").getValue().toString();
                                    String profileStatus = dataSnapshot.child("status").getValue().toString();

                                    friendViewHolder.userName.setText(profileName);
                                    friendViewHolder.userStatus.setText(profileStatus);
                                    Picasso.get().load(profileUserImage).placeholder(R.drawable.profile_image).into(friendViewHolder.chatProfileImage);
                                } else {
                                    String profileName = dataSnapshot.child("name").getValue().toString();
                                    String profileStatus = dataSnapshot.child("status").getValue().toString();

                                    friendViewHolder.userName.setText(profileName);
                                    friendViewHolder.userStatus.setText(profileStatus);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_friend, parent, false);
                        FriendViewHolder userViewHolder = new FriendViewHolder(view);
                        return userViewHolder;
                    }
                };
        friendsList.setAdapter(adapter);
        adapter.startListening();
    }
}
