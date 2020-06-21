package com.example.hista.ViewHolder;

import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hista.Model.Chat;
import com.example.hista.R;
import com.facebook.drawee.view.SimpleDraweeView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatViewHolder extends RecyclerView.ViewHolder {

    private TextView chatFriendName;
    private TextView chatLastMessage;
    private TextView chatLastMessageTime;
    private SimpleDraweeView chatProfileImage;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);

        chatProfileImage = itemView.findViewById(R.id.chat_user_profile_image);
        chatFriendName = itemView.findViewById(R.id.chat_user_profile_name);
        chatLastMessage = itemView.findViewById(R.id.chat_user_last_msg);
        chatLastMessageTime = itemView.findViewById(R.id.chat_user_last_sent);
    }

    public void bind(Chat chat) {
        chatProfileImage.setImageURI(chat.getProfileImage());
        chatFriendName.setText(chat.getName());
        chatLastMessage.setText(chat.getChatContent());
        chatLastMessageTime.setText(chat.getSentTime());
    }
}
