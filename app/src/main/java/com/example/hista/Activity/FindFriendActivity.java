package com.example.hista.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hista.Model.ShortUserInfo;
import com.example.hista.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriendActivity extends AppCompatActivity {

    private Toolbar findFriendToolBar;
    private RecyclerView recyclerView;
    private DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        userReference = FirebaseDatabase.getInstance().getReference().child("Users");

        recyclerView = (RecyclerView) findViewById(R.id.find_friend_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findFriendToolBar = (Toolbar) findViewById(R.id.find_friend_bar);
        setSupportActionBar(findFriendToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.find_friend_activity));
    }


    // Dealing with Find Friend RecyclerView
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ShortUserInfo> options =
                new FirebaseRecyclerOptions.Builder<ShortUserInfo>()
                    .setQuery(userReference, ShortUserInfo.class) // set query that retrieved data from the database
                    .build();

        FirebaseRecyclerAdapter<ShortUserInfo, FindFriendViewHolder> adapter =
                new FirebaseRecyclerAdapter<ShortUserInfo, FindFriendViewHolder>(options) { // add options defined above
                    @Override
                    protected void onBindViewHolder(@NonNull FindFriendViewHolder findFriendViewHolder, final int i, @NonNull ShortUserInfo shortUserInfo) {
                        findFriendViewHolder.userName.setText(shortUserInfo.getName());
                        findFriendViewHolder.userStatus.setText(shortUserInfo.getStatus());
                        Picasso.get().load(shortUserInfo.getImage()).placeholder(R.drawable.profile_image).into(findFriendViewHolder.profileImage);


                        findFriendViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String visitUserID = getRef(i).getKey();

                                Intent profileIntent = new Intent(FindFriendActivity.this, FriendProfileActivity.class);
                                profileIntent.putExtra("visit_user_id", visitUserID);
                                startActivity(profileIntent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_find_friend, parent, false);
                        FindFriendViewHolder findFriendViewHolder = new FindFriendViewHolder(view);
                        return findFriendViewHolder;
                    }
                };

        recyclerView.setAdapter(adapter);

        adapter.startListening();
    }

    public static class FindFriendViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView profileImage;
        TextView userName, userStatus;

        public FindFriendViewHolder(@Nullable View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.find_friend_user_image);
            userName = itemView.findViewById(R.id.find_friend_user_name);
            userStatus = itemView.findViewById(R.id.find_friend_user_status);
        }
    }
}
