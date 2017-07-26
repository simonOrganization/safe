package com.lchtime.safetyexpress.ui.circle.protocal;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.PhotoAdapter;
import com.lchtime.safetyexpress.adapter.PictureSlidePagerAdapter;
import com.lchtime.safetyexpress.ui.circle.fragment.PictureSlideFragment;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

public class CirclePhone extends AppCompatActivity {

    @BindView(R.id.tv_indicator)
    TextView tvIndicator;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private ArrayList<String> urlList;
    private ArrayList<PhotoView> viewList = new ArrayList<>();
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_circle_phone);
        ButterKnife.bind(this);
        setTitle("图片预览");
        urlList = getIntent().getStringArrayListExtra("url");
        pos = getIntent().getIntExtra("pos" , 0);
        Log.i("qaz", "onCreate1: "+pos);
        if(urlList != null){
            for(String imageUrl : urlList){
                PhotoView photoView = new PhotoView(CirclePhone.this);
                Glide.with(CirclePhone.this).load(imageUrl).into(photoView);
                viewList.add(photoView);
            }
        }

        viewpager.setAdapter(new PhotoAdapter(CirclePhone.this , viewList));
        viewpager.setCurrentItem(pos);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tvIndicator.setText(String.valueOf(position + 1) + "/" + viewList.size());
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }





}


