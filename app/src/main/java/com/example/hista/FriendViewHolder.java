package com.example.hista;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView chatProfileImage;
    public TextView userName;
    public TextView userStatus;

    public FriendViewHolder(@NonNull View itemView) {
        super(itemView);

        chatProfileImage = itemView.findViewById(R.id.friend_list_user_profile);
        userName = itemView.findViewById(R.id.friend_list_user_name);
        userStatus = itemView.findViewById(R.id.friend_list_user_status);
    }
}
