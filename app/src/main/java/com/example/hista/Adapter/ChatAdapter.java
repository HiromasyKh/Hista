package com.example.hista.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hista.Model.Chat;
import com.example.hista.R;
import com.example.hista.ViewHolder.ChatViewHolder;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    // Dataset
    private Chat[] chats;

    public ChatAdapter(Chat[] chats) {
        this.chats = chats;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.viewholder_chat, parent, false);

        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chats[position];
        holder.bind(chat);
    }

    @Override
    public int getItemCount() {
        return chats.length;
    }
}
