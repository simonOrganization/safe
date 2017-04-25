package com.lchtime.safetyexpress.ui.circle.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.AddSubscribeAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yxn on 2017/4/23.
 */

public class SubscirbeCommendFragment extends Fragment {
    @BindView(R.id.subscribe_comm_rc)
    RecyclerView subscribe_comm_rc;
    private Context context;
    private AddSubscribeAdapter addSubscribeAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subscribe_comm_fragment,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        subscribe_comm_rc.setLayoutManager(new LinearLayoutManager(context));
        addSubscribeAdapter = new AddSubscribeAdapter(context);
        subscribe_comm_rc.setAdapter(addSubscribeAdapter);
    }
}
