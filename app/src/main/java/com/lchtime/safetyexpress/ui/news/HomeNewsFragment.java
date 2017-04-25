package com.lchtime.safetyexpress.ui.news;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.HomeNewAdapter;
import com.lchtime.safetyexpress.bean.NewsBean;
import com.lchtime.safetyexpress.views.EmptyRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yxn on 2017/4/25.
 */

public class HomeNewsFragment extends Fragment {
    @BindView(R.id.home_new_fragment_rc)
    EmptyRecyclerView home_new_fragment_rc;
    private Context context;
    private HomeNewAdapter homeNewAdapter;
    private String type_id;
    private ArrayList<NewsBean> commentList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_new_fragment,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        home_new_fragment_rc.setLayoutManager(new LinearLayoutManager(context));
        Bundle bundle = getArguments();
        int position = bundle.getInt("position");
        if(position == 0){
            commentList = bundle.getParcelableArrayList("comments");
            homeNewAdapter = new HomeNewAdapter(context,commentList);
            home_new_fragment_rc.setAdapter(homeNewAdapter);
        }else{
            type_id = bundle.getString("typeId");
        }


    }
}
