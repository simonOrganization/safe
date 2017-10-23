package com.lchtime.safetyexpress.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.ActivityBean;
import com.lchtime.safetyexpress.ui.home.protocal.PictureAdvantage;
import com.lchtime.safetyexpress.utils.SpTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android-cp on 2017/6/29.
 */

public class YDActivity extends Activity {
    private ViewPager vp;
    private List<View> list = new ArrayList<>();
    private Handler handler = new Handler();
    private PictureAdvantage picProtocal = new PictureAdvantage();

    private int[] arr = {R.drawable.first,R.drawable.second,R.drawable.third};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yd);
        //获取活动信息
        loadActivity();

        vp = (ViewPager) findViewById(R.id.vp);

        for (int i = 0 ; i < arr.length; i++){
            ImageView iv = new ImageView(this);
            iv.setImageDrawable(getResources().getDrawable(arr[i]));
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            list.add(iv);
        }

        VPAdapter adapter = new VPAdapter();
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == list.size() - 1){
                    //如果是最后一个界面
                    SpTools.setBoolean(YDActivity.this,"first",false);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(YDActivity.this, TabUI.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 2000);


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 获取活动信息
     */
    private void loadActivity() {
        picProtocal.getActivity(new PictureAdvantage.ActivityListener() {
            @Override
            public void activityResponse(String respose) {
                ActivityBean bean = new Gson().fromJson(respose , ActivityBean.class);
                //保存活动信息
                SpTools.setString(YDActivity.this , "activite" , bean.flag + "");

            }
        });

    }

    class VPAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


         @Override
        public Object instantiateItem(ViewGroup container, int position) {
             //这个方法用来实例化页卡  

            container.addView(list.get(position));//添加页卡  
             return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));//删除页卡  
        }
    }
}
