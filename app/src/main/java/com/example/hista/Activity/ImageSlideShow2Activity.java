package com.example.hista.Activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hista.Adapter.ImageSlideShow2Adapter;
import com.example.hista.R;

public class ImageSlideShow2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_slide_show2);

        ViewPager2 viewPager2 = findViewById(R.id.image_slide_view_pager_2);

        int[] imageIds = new int[]{R.drawable.img_1, R.drawable.img_3}; // , R.drawable.img_2, R.drawable.img_4, R.drawable.img_5
        ImageSlideShow2Adapter adapter = new ImageSlideShow2Adapter(imageIds);

        viewPager2.setAdapter(adapter);
    }
}
