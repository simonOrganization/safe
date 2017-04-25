package com.lchtime.safetyexpress.ui.circle.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.AddSubscribeAdapter;
import com.lchtime.safetyexpress.bean.CircleSelectBean;
import com.lchtime.safetyexpress.ui.circle.CircleUI;
import com.lchtime.safetyexpress.views.CirclePopView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yxn on 2017/4/23.
 */

public class SubscirbeAllFragment extends Fragment {
    @BindView(R.id.subscribe_all_rc)
    RecyclerView subscribe_all_rc;
    private CirclePopView cp;
    @BindView(R.id.subscribe_all_ll)
    LinearLayout subscribe_all_ll;
    private ArrayList<CircleSelectBean> arrayList = new ArrayList<CircleSelectBean>();
    private Context context;
    private AddSubscribeAdapter addSubscribeAdapter;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subscribe_all_fragment,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        subscribe_all_rc.setLayoutManager(new LinearLayoutManager(context));
        cp = new CirclePopView(getActivity());
        cp.setCirclePopInterface(new CirclePopView.CirclePopInterface() {
            @Override
            public void getPopIds(String positions) {
                Toast.makeText(context,"position:===="+positions,Toast.LENGTH_SHORT).show();
            }
        });
        arrayList.add(new CircleSelectBean(false));
        arrayList.add(new CircleSelectBean(false));
        arrayList.add(new CircleSelectBean(false));
        arrayList.add(new CircleSelectBean(false));
        arrayList.add(new CircleSelectBean(false));
        arrayList.add(new CircleSelectBean(false));
        arrayList.add(new CircleSelectBean(false));
        addSubscribeAdapter = new AddSubscribeAdapter(context);
        subscribe_all_rc.setAdapter(addSubscribeAdapter);
    }
    @OnClick({R.id.subscribe_all_work,R.id.subscribe_all_gangwei,R.id.subscribe_all_address})
    void setOnclick(View view){
        switch (view.getId()){
            case R.id.subscribe_all_work:
                cp.setDataAdapter(arrayList);
                cp.showPopWindow(subscribe_all_ll);
            break;
            case R.id.subscribe_all_gangwei:
                cp.setDataAdapter(arrayList);
                cp.showPopWindow(subscribe_all_ll);
                break;
            case R.id.subscribe_all_address:
                cp.setDataAdapter(arrayList);
                cp.showPopWindow(subscribe_all_ll);
                break;
        }

    }
}
