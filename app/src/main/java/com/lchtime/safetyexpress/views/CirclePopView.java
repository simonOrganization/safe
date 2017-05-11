package com.lchtime.safetyexpress.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.CirclePopAdapter;
import com.lchtime.safetyexpress.bean.CircleSelectBean;
import com.lchtime.safetyexpress.bean.ProfessionBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yxn on 2017/4/21.
 */

public class CirclePopView extends PopupWindow {
    private Context context;
    @BindView(R.id.circle_pop_rc)
    RecyclerView circle_pop_rc;
    @BindView(R.id.circle_pop_restart)
    Button circle_pop_restart;
    @BindView(R.id.circle_pop_submit)
    Button circle_pop_submit;
    private List<ProfessionBean.ProfessionItemBean> array;
    private CirclePopAdapter adapter;


    public CirclePopView(Activity context) {
        super(context);
        this.context = context;
        initView(context);
    }

    private void initView(Activity context) {
        View view = LayoutInflater.from(context).inflate(R.layout.circle_pop,null);
        ButterKnife.bind(this,view);
        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();

//        setBackgroundAlpha(0.5f);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        circle_pop_rc.setLayoutManager(new GridLayoutManager(context,2));
//
//        this.setOnDismissListener(new OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                // popupWindow隐藏时恢复屏幕正常透明度
//                setBackgroundAlpha(1.0f);
//            }
//        });
    }
    public void setDataAdapter(final List<ProfessionBean.ProfessionItemBean> array){
        this.array = array;
        adapter = new CirclePopAdapter(context,array);
        adapter.setItemInterface(new CirclePopAdapter.PopItemInterfce() {
            @Override
            public void setItem(int position) {
                if(array.get(position).isSelect){
                    array.get(position).isSelect = false;
                }else{
                    array.get(position).isSelect = true;
                }
                adapter.notifyDataSetChanged();
            }
        });
        circle_pop_rc.setAdapter(adapter);
    }
    public void showPopWindow(View view){
        //Toast.makeText(context,"work",Toast.LENGTH_SHORT).show();
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(view);
        } else {
            this.dismiss();
        }
    }
    @OnClick({R.id.circle_pop_restart,R.id.circle_pop_submit})
    void setOnclick(View view){
        switch (view.getId()){
            case R.id.circle_pop_restart:
                for(int i=0;i<array.size();i++){
                    if(array.get(i).isSelect){
                        array.get(i).isSelect = false;
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.circle_pop_submit:
                CirclePopView.this.dismiss();
                String selectPosition = "";
                for(int i=0;i<array.size();i++){
                    if(array.get(i).isSelect){
                        if (TextUtils.isEmpty(selectPosition)){
                            selectPosition = selectPosition  + i;
                        }else {
                            selectPosition = selectPosition + "," + i;
                        }
                    }
                }
                circlePopInterface.getPopIds(selectPosition);
                break;
        }

    }

public void setBackgroundAlpha(float bgAlpha) {
    WindowManager.LayoutParams lp = ((Activity) context).getWindow()
            .getAttributes();
    lp.alpha = bgAlpha;
    ((Activity) context).getWindow().setAttributes(lp);
}
    private CirclePopInterface circlePopInterface;
    public interface CirclePopInterface{
        void getPopIds(String positions);
    }
    public void setCirclePopInterface(CirclePopInterface circlePopInterface){
        this.circlePopInterface = circlePopInterface;
    }


}
