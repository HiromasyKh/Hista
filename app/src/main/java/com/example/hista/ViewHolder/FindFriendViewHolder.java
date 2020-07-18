package com.example.hista.ViewHolder;

import com.example.hista.Model.ShortUserInfo;
import com.example.hista.R;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

public class FindFriendViewHolder extends RecyclerView.ViewHolder {

    private SimpleDraweeView profileImage;
    private TextView userName, userStatus;

    public FindFriendViewHolder(@NonNull View itemView) {
        super(itemView);

        profileImage = itemView.findViewById(R.id.find_friend_user_image);
        userName = itemView.findViewById(R.id.find_friend_user_name);
        userStatus = itemView.findViewById(R.id.find_friend_user_status);
    }

    public void bind(ShortUserInfo shortUserInfo) {
        profileImage.setImageURI(shortUserInfo.getImage());
        userName.setText(shortUserInfo.getName());
        userStatus.setText(shortUserInfo.getStatus());
    }
}
