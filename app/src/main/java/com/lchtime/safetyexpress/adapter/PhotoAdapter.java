package com.lchtime.safetyexpress.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Administrator on 2017/7/24.
 */

public class PhotoAdapter extends PagerAdapter {

    //private ArrayList<String> urlList;
    private ArrayList<PhotoView> viewList;


    public PhotoAdapter(Context context ,ArrayList<PhotoView> viewList) {
        //this.urlList = urlList;
        /*viewList = new ArrayList<>();
        if(urlList != null){
            for(String imageUrl : urlList){
                PhotoView photoView = new PhotoView(context);
                Glide.with(context).load(imageUrl).into(photoView);
                viewList.add(photoView);
            }
        }*/
        this.viewList = viewList;

    }

    @Override
    public int getCount() {
        return viewList == null ? 0 :viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
        //super.destroyItem(container, position, object);
    }

}
