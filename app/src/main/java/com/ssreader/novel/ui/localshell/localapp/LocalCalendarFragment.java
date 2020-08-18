package com.ssreader.novel.ui.localshell.localapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.ui.localshell.adapter.CalendarNotesAdapter;
import com.ssreader.novel.ui.localshell.bean.LocalNotesBean;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.ObjectBoxUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

public class LocalCalendarFragment extends BaseFragment {

    @BindViews({R.id.fragment_local_calendar_tool_month_day, R.id.fragment_local_calendar_tool_year,
            R.id.fragment_local_calendar_tool_lunar, R.id.fragment_local_calendar_tool_current_day})
    List<TextView> toolbarText;
    @BindView(R.id.fragment_local_calendar_calendarView)
    CalendarView calendarView;
    @BindView(R.id.local_calendar_linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.fragment_calendar_rcy)
    SCRecyclerView fragment_calendar_rcy;
    @BindView(R.id.fragment_calendar_title)
    TextView fragment_calendar_title;
    private int mYear;
    private int mDay;
    private CalendarNotesAdapter calendarNotesAdapter;
    public List<LocalNotesBean> list;

    @OnClick({R.id.local_calendar_linearLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.local_calendar_linearLayout:
                Intent intent = new Intent(activity, LocalEditNotesActivity.class);
                intent.putExtra("notes_id", mDay);
                startActivity(intent);
                break;
        }
    }

    @Override
    public int initContentView() {
        USE_EventBus = true;
        return R.layout.fragment_local_calendar;
    }

    @Override
    public void initView() {
        list = new ArrayList<>();
        initListener();
        mDay = calendarView.getCurDay();
        calendarView.scrollToCurrent();
        linearLayout.setBackground(MyShape.setMyshape(activity, ImageUtil.dp2px(activity, 4), ImageUtil.dp2px(activity, 4), ImageUtil.dp2px(activity, 4), ImageUtil.dp2px(activity, 4), 1, ContextCompat.getColor(activity, R.color.updateblue), 0));
        toolbarText.get(1).setText(String.valueOf(calendarView.getCurYear()));
        mYear = calendarView.getCurYear();
        toolbarText.get(0).setText(calendarView.getCurMonth() + "月" + calendarView.getCurDay() + "日");
        toolbarText.get(2).setText("今日");
        toolbarText.get(3).setText(String.valueOf(calendarView.getCurDay()));
        List<LocalNotesBean> allData = ObjectBoxUtils.getAllData(LocalNotesBean.class);
        for (LocalNotesBean allDatum : allData) {
            if (allDatum.day_id == mDay) {
                list.add(allDatum);
            }
        }
        fragment_calendar_rcy.setLoadingMoreEnabled(false);
        fragment_calendar_rcy.setPullRefreshEnabled(false);
        fragment_calendar_rcy.setLayoutManager(new LinearLayoutManager(activity));
        calendarNotesAdapter = new CalendarNotesAdapter(list, activity);
        fragment_calendar_rcy.setAdapter(calendarNotesAdapter);
        noResult();
    }

    private void initListener() {
        calendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {
                list.clear();
                toolbarText.get(0).setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
                toolbarText.get(1).setText(calendar.getYear() + "");
                toolbarText.get(2).setText(calendar.getLunar());
                mYear = calendar.getYear();
                mDay = calendar.getDay();
                List<LocalNotesBean> localNotesBeans = ObjectBoxUtils.getAllData(LocalNotesBean.class);
                for (LocalNotesBean localNotesBean : localNotesBeans) {
                    if (localNotesBean.day_id == mDay) {
                        list.add(localNotesBean);
                    }
                }
                MyToash.Log("localNotesBean", list.size());
                noResult();
            }
        });
        calendarView.setOnYearChangeListener(new CalendarView.OnYearChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onYearChange(int year) {
                toolbarText.get(1).setText(year + "");
            }
        });
    }

    @OnClick({R.id.fragment_local_calendar_tool_current})
    public void onLocalCalendarClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_local_calendar_tool_current:
                calendarView.scrollToCurrent();
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {

    }

    public void noResult() {
        if (!list.isEmpty() && list.size() > 0) {
            fragment_calendar_title.setVisibility(View.VISIBLE);
            fragment_calendar_rcy.setVisibility(View.VISIBLE);
            if(calendarNotesAdapter!=null){
                calendarNotesAdapter.notifyDataSetChanged();
            }
            linearLayout.setVisibility(View.GONE);
        } else {
            fragment_calendar_title.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            fragment_calendar_rcy.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(LocalNotesBean l) {
        initView();
    }
}
