package com.lchtime.safetyexpress.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.lchtime.safetyexpress.ui.circle.fragment.PictureSlideFragment;

import java.util.ArrayList;

/**
 * Created by fanyuhong on 2017/7/21.
 */

public class PictureSlidePagerAdapter extends FragmentStatePagerAdapter {

    private final ArrayList<String> urlList;
   // private final FragmentManager fm;

    public PictureSlidePagerAdapter(FragmentManager fm, ArrayList<String> urlList) {
        super(fm);
        this.urlList = urlList;
       // this.fm =fm;
        //super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return PictureSlideFragment.newInstance(urlList.get(position));
    }

    @Override
    public int getCount() {
        return urlList.size();
    }
}
