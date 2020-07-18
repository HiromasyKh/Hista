package com.example.hista.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hista.Model.Messages;

import java.util.List;
import com.example.hista.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> userMessagesList;
    private FirebaseAuth auth;
    private DatabaseReference userRef;

    public MessageAdapter (List<Messages> userMessagesList) {
        this.userMessagesList = userMessagesList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_msg_layout, parent, false);

        auth = FirebaseAuth.getInstance();

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position) {
        String msgSenderId = auth.getCurrentUser().getUid();
        Messages msg = userMessagesList.get(position);

        String fromUserID = msg.getFrom();
        String fromMsgType = msg.getType();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserID);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("image")) {
                    String receiverImage = dataSnapshot.child("image").getValue().toString();

                    Picasso.get().load(receiverImage).placeholder(R.drawable.profile_image).into(holder.receiverProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (fromMsgType.equals("text")) {
            holder.receiverMsgText.setVisibility(View.INVISIBLE);
            holder.receiverProfileImage.setVisibility(View.INVISIBLE);
            holder.senderMsgText.setVisibility(View.INVISIBLE);

            if (fromUserID.equals(msgSenderId)) {
                holder.senderMsgText.setVisibility(View.VISIBLE);
                holder.senderMsgText.setBackgroundResource(R.drawable.sender_msg_layout);
                holder.senderMsgText.setText(msg.getMessage());
            } else {
                holder.receiverProfileImage.setVisibility(View.VISIBLE);
                holder.receiverMsgText.setVisibility(View.VISIBLE);

                holder.receiverMsgText.setBackgroundResource(R.drawable.receiver_msg_layout);
                holder.receiverMsgText.setText(msg.getMessage());
            }
        }
    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView senderMsgText, receiverMsgText;
        public CircleImageView receiverProfileImage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMsgText = (TextView) itemView.findViewById(R.id.sender_msg_text);
            receiverMsgText = (TextView) itemView.findViewById(R.id.receiver_msg_text);
            receiverProfileImage = (CircleImageView) itemView.findViewById(R.id.msg_profile_img);
        }
    }
}
