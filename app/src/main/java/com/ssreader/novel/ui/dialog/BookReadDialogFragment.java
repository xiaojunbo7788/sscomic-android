package com.ssreader.novel.ui.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ssreader.novel.R;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.ui.adapter.MyFragmentPagerAdapter;
import com.ssreader.novel.ui.fragment.MonthTicketFragment;
import com.ssreader.novel.ui.fragment.RewardFragment;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ScreenSizeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookReadDialogFragment extends DialogFragment {

    private Map<Integer, TextView> textViewMap;
    private Activity activity;
    private List<String> tabList;
    private List<Fragment> fragmentList;
    private ViewHolder viewHolder;

    private RewardFragment rewardFragment;
    private MonthTicketFragment monthTicketFragment;

    private boolean isPost = false;
    private long book_id;
    private int CURRENT_OPTION;
    private boolean isFirst;

    public BookReadDialogFragment(Activity activity, long book_id, boolean isFirst) {
        this.activity = activity;
        this.book_id = book_id;
        this.isFirst = isFirst;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 窗口底部弹出
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = ImageUtil.dp2px(activity, 310);
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.BottomDialogFragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_base_fragment, null);
        view.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 8), ImageUtil.dp2px(activity, 8), 0, 0, ContextCompat.getColor(activity, R.color.white)));
        viewHolder = new ViewHolder(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        textViewMap = new HashMap<>();
        tabList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        if (Constant.getMonthlyTicket(activity) == 1 && Constant.getRewardSwitch(activity) == 1) {
            tabList.add(LanguageUtil.getString(activity, R.string.dialog_Reward));
            tabList.add(LanguageUtil.getString(activity, R.string.dialog_Monthly_pass));
            fragmentList.add(rewardFragment = new RewardFragment(book_id, this));
            fragmentList.add(monthTicketFragment = new MonthTicketFragment(book_id, this));
        } else if (Constant.getMonthlyTicket(activity) == 1) {
            tabList.add(LanguageUtil.getString(activity, R.string.dialog_Monthly_pass));
            fragmentList.add(monthTicketFragment = new MonthTicketFragment(book_id, this));
        } else if (Constant.getRewardSwitch(activity) == 1) {
            tabList.add(LanguageUtil.getString(activity, R.string.dialog_Reward));
            fragmentList.add(rewardFragment = new RewardFragment(book_id, this));
        }
        viewHolder.viewPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList, tabList));
        viewHolder.smartTabLayout.setViewPager(viewHolder.viewPager);
        int SIZE = tabList.size();
        if (SIZE > 1) {
            if (!isFirst) {
                viewHolder.viewPager.setCurrentItem(1);
                CURRENT_OPTION = 1;
            }
            for (int k = 0; k < SIZE; k++) {
                TextView textView = viewHolder.smartTabLayout.getTabAt(k).findViewById(R.id.item_tablayout_text);
                if (k != 0) {
                    if (isFirst) {
                        textView.setTextColor(ContextCompat.getColor(activity, R.color.black_6));
                        textView.setTextSize(14);
                    } else {
                        textView.setTextColor(ContextCompat.getColor(activity, R.color.black));
                        textView.setTextSize(16);
                    }
                } else {
                    if (isFirst) {
                        textView.setTextColor(ContextCompat.getColor(activity, R.color.black));
                        textView.setTextSize(16);
                    } else {
                        textView.setTextColor(ContextCompat.getColor(activity, R.color.black_6));
                        textView.setTextSize(14);
                    }
                }
                textViewMap.put(k, textView);
            }
        }
        viewHolder.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (textViewMap != null) {
                    textViewMap.get(position).setTextColor(ContextCompat.getColor(activity, R.color.black));
                    textViewMap.get(position).setTextSize(16);
                    textViewMap.get(CURRENT_OPTION).setTextColor(ContextCompat.getColor(activity, R.color.black_6));
                    textViewMap.get(CURRENT_OPTION).setTextSize(14);
                }
                CURRENT_OPTION = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isPost) {
            changePositionBean();
        }
        isPost = true;
    }

    /**
     * 刷新数据
     */
    public void changePositionBean() {
        if (rewardFragment != null) {
            rewardFragment.initData();
        }
        if (monthTicketFragment != null) {
            monthTicketFragment.initData();
        }
    }

    public class ViewHolder {

        @BindView(R.id.book_read_dialog_XTabLayout)
        SmartTabLayout smartTabLayout;
        @BindView(R.id.activity_baseoption_viewpage)
        ViewPager viewPager;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
