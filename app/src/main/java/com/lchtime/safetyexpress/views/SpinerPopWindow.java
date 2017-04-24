package com.lchtime.safetyexpress.views;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;


import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.SpinerAdapter;

import java.util.ArrayList;

/**
 * Created by yxn on 2017/3/23.
 */

public class SpinerPopWindow extends PopupWindow {//implements AdapterView.OnItemClickListener
    private Context mContext;
    private RecyclerView recyclerView;
    private SpinerAdapter spinerAdapter;
    private SpinerInterface spinerInterface;
    private ArrayList<String> mDatas;


    public SpinerPopWindow(Context mContext, ArrayList<String> datas){
        super(mContext);
        this.mContext = mContext;
        this.mDatas = datas;
        init();
    }

    public void setAdapter(SpinerAdapter adapter){
        this.spinerAdapter = adapter;
        recyclerView.setAdapter(spinerAdapter);
    }
    private void init(){
        Log.i("yang","init");
        View view = LayoutInflater.from(mContext).inflate(R.layout.spiner_pop_layout,null);
        this.setContentView(view);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);//
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        setBackgroundDrawable(dw);

        recyclerView = (RecyclerView) view.findViewById(R.id.spiner_pop_rc);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        Log.i("yang","setLayoutManager==="+mDatas.size());
        spinerAdapter = new SpinerAdapter(mDatas,mContext);
        spinerAdapter.setOnItemSelectLs(new SpinerAdapter.IOnItemSelectListener() {
            @Override
            public void onItemClick(int pos) {
                spinerInterface.setSpinerInterface(pos);
            }
        });
        recyclerView.setAdapter(spinerAdapter);
    }
    public void refreshData(ArrayList<String> list, int selIndex)
    {
        if (list != null && selIndex  != -1)
        {
            if (spinerAdapter != null){
                spinerAdapter.refreshData(list, selIndex);
            }
        }
    }
    public interface SpinerInterface{
        void setSpinerInterface(int position);
    }
    public void setSpinerInterface(SpinerInterface spinerInterface){
        this.spinerInterface = spinerInterface;
    }
}
