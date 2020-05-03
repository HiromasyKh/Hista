package com.example.hista.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.hista.Fragments.ChatFragment;
import com.example.hista.Fragments.GroupFragment;
import com.example.hista.Fragments.ProfileFragment;
import com.example.hista.Fragments.FriendFragment;

public class TabAccessAdapter extends FragmentPagerAdapter {
    public TabAccessAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;
            case 1:
                GroupFragment groupFragment = new GroupFragment();
                return groupFragment;
            case 2:
                FriendFragment friendFragment = new FriendFragment();
                return friendFragment;
            case 3:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Chat";
            case 1:
                return "Group";
            case 2:
                return "Friend";
            case 3:
                return "Profile";
            default:
                return null;
        }
    }
}
