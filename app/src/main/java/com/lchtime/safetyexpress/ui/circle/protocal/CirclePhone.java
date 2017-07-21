package com.lchtime.safetyexpress.ui.circle.protocal;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.PictureSlidePagerAdapter;
import com.lchtime.safetyexpress.ui.circle.fragment.PictureSlideFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CirclePhone extends AppCompatActivity {

    @BindView(R.id.tv_indicator)
    TextView tvIndicator;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private ArrayList<String> url;
    private ArrayList<String> urlList;
    private String[] urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_circle_phone);
        ButterKnife.bind(this);
        setTitle("图片预览");
         url = getIntent().getStringArrayListExtra("url");
        Log.i("qaz", "onCreate1: "+url);

      /*  if (!TextUtils.isEmpty(url)) {
            Picasso.with(this).load(url).into(iv_circle_photo);
        } else {
            Picasso.with(this).load(R.drawable.banner_default).into(iv_circle_photo);
        }*/
      /*  if (url !=null) {
            final int size =url.size();
            urls = url.toArray(new String[size]);
        }else{
            Log.i("qaz", "onCreate2: "+"11122222");
        }*/
      //  Log.i("qaz", "onCreate2: "+urls);
       // urlList = new ArrayList<>();
       // Collections.addAll(urlList, urls);


        viewpager.setAdapter(new PictureSlidePagerAdapter(getSupportFragmentManager(),url));
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                tvIndicator.setText(String.valueOf(position + 1) + "/" + urlList.size());
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


