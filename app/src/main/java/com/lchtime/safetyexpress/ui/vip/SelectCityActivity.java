package com.lchtime.safetyexpress.ui.vip;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.CitySortAdapter;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.views.CityView.CharacterParser;
import com.lchtime.safetyexpress.views.CityView.CitySortModel;
import com.lchtime.safetyexpress.views.CityView.PinyinComparator;
import com.lchtime.safetyexpress.views.CityView.SideBar;
import com.lidroid.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by android-cp on 2017/5/1.
 */
@ContentView(R.layout.city_activity)
public class SelectCityActivity extends BaseUI{
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private CitySortAdapter adapter;
    private CharacterParser characterParser;
    private List<CitySortModel> SourceDateList;
    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("选择城市");
        initViews();
    }

    @Override
    protected void prepareData() {

    }

    private void initViews() {
        characterParser = CharacterParser.getInstance();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("city",((CitySortModel) adapter.getItem(position)).getName());
                setResult(0,intent);
                finish();
            }
        });

        SourceDateList = filledData(getResources().getStringArray(R.array.provinces));
        Collections.sort(SourceDateList, new PinyinComparator());
        adapter = new CitySortAdapter(this, SourceDateList);
        //sortListView.addHeaderView(initHeadView());
        sortListView.setAdapter(adapter);
    }

    private List<CitySortModel> filledData(String[] date) {
        List<CitySortModel> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();

        for (int i = 0; i < date.length; i++) {
            CitySortModel sortModel = new CitySortModel();
            sortModel.setName(date[i]);
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {

                //对重庆多音字做特殊处理
                if (pinyin.startsWith("zhongqing")) {
                    sortString = "C";
                    sortModel.setSortLetters("C");
                } else {
                    sortModel.setSortLetters(sortString.toUpperCase());
                }

                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            }

            mSortList.add(sortModel);
        }
        Collections.sort(indexString);
        sideBar.setIndexText(indexString);
        return mSortList;

    }
}
