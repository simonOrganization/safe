package com.lchtime.safetyexpress.ui.home;

import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.HomeQuestionAdapter;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 疑难问答
 * Created by user on 2017/4/18.
 */
@ContentView(R.layout.home_question_ui)
public class HomeQuestionUI extends BaseUI {

    //列表展示
    @ViewInject(R.id.lv_home_question)
    private ListView lv_home_question;

    private HomeQuestionAdapter homeQuestionAdapter;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("问答");

        View home_question_header = View.inflate(HomeQuestionUI.this, R.layout.home_question_header, null);
        LinearLayout ll_home_question = (LinearLayout) home_question_header.findViewById(R.id.ll_home_question);
        ll_home_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        lv_home_question.addHeaderView(home_question_header);

        homeQuestionAdapter = new HomeQuestionAdapter(HomeQuestionUI.this);
        lv_home_question.setAdapter(homeQuestionAdapter);
        lv_home_question.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    @Override
    protected void prepareData() {

    }

}
