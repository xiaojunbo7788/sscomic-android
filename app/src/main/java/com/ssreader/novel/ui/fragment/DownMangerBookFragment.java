package com.ssreader.novel.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.DownScrollCancleEdit;
import com.ssreader.novel.model.Downoption;
import com.ssreader.novel.ui.adapter.DownMangerAdapter;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DownMangerBookFragment extends BaseFragment<Downoption> {

    @BindView(R.id.mFragmentBookshelfNoresult)
    LinearLayout mFragmentBookshelfNoresult;
    @BindView(R.id.mActivityDownmangerList)
    ListView mActivityDownmangerList;
    @BindView(R.id.public_recycleview_buttom_1)
    TextView publicRecycleviewButtom1;

    @BindView(R.id.public_recycleview_buttom_2)
    TextView publicRecycleviewButtom2;
    @BindView(R.id.public_recycleview_buttom)
    LinearLayout bottomLayout;

    private DownMangerAdapter downMangerAdapter;
    private List<Downoption> downoptions;

    private TextView public_sns_topbar_right_tv;

    public DownMangerBookFragment() {

    }

    public DownMangerBookFragment(TextView public_sns_topbar_right_tv) {
        this.public_sns_topbar_right_tv = public_sns_topbar_right_tv;
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_downmanger;
    }

    @Override
    public void initView() {
        View temphead = LayoutInflater.from(activity).inflate(R.layout.item_list_head, null);
        mActivityDownmangerList.addHeaderView(temphead, null, false);
        mActivityDownmangerList.setHeaderDividersEnabled(true);
        List<Long> list = new ArrayList<>();
        downoptions = ObjectBoxUtils.getAllData(Downoption.class);
        if (downoptions.size() != 0) {
            Collections.sort(downoptions);//按Bookid 排序
            for (Downoption downoption : downoptions) {
                if (list.contains(downoption.book_id)) {
                    downoption.showHead = false;
                } else {
                    downoption.showHead = true;
                    list.add(downoption.book_id);
                }
            }
        } else {
            mFragmentBookshelfNoresult.setVisibility(View.VISIBLE);
        }
        downMangerAdapter = new DownMangerAdapter(downoptions, activity, scOnItemClickListener);
        mActivityDownmangerList.setAdapter(downMangerAdapter);
    }

    @OnClick({R.id.public_recycleview_buttom_1, R.id.public_recycleview_buttom_2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.public_recycleview_buttom_1:
                int size = downoptions.size();
                boolean allChoose = downMangerAdapter.checkList.size() == size;
                downMangerAdapter.checkList.clear();
                if (!allChoose) {
                    downMangerAdapter.checkList.addAll(downoptions);
                    setDeleteBtn(true, size);
                } else {
                    setDeleteBtn(false, size);
                }
                downMangerAdapter.notifyDataSetChanged();
                break;
            case R.id.public_recycleview_buttom_2:
                if (!downMangerAdapter.checkList.isEmpty()) {
                    for (Downoption downoption : downMangerAdapter.checkList) {
                        EventBus.getDefault().post(new DownScrollCancleEdit(Constant.BOOK_CONSTANT, downoption.book_id));
                        ObjectBoxUtils.deleteData(downoption, Downoption.class);
                    }
                    downoptions.removeAll(downMangerAdapter.checkList);
                    downMangerAdapter.Edit = false;
                    downMangerAdapter.notifyDataSetChanged();
                    if (downoptions.isEmpty()) {
                        mFragmentBookshelfNoresult.setVisibility(View.VISIBLE);
                    }
                    setIsDelete(false);
                }
                break;
        }
    }

    public void setEdit(boolean edit) {
        if (downoptions.isEmpty()) {
            return;
        }
        downMangerAdapter.Edit = edit;
        if (edit) {
            setIsDelete(true);
        } else {
            downMangerAdapter.checkList.clear();
            setIsDelete(false);
        }
        downMangerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void setOnItemClickListener(int size, int position, Downoption o) {
        if (size == 0) {
            publicRecycleviewButtom2.setText(LanguageUtil.getString(activity, R.string.app_delete));
            publicRecycleviewButtom2.setTextColor(ContextCompat.getColor(activity, R.color.gray_9));
        } else {
            publicRecycleviewButtom2.setText(LanguageUtil.getString(activity, R.string.app_delete) + "(" + (size) + ")");
            publicRecycleviewButtom2.setTextColor(ContextCompat.getColor(activity, R.color.red));
        }
        if (size == downoptions.size()) {
            publicRecycleviewButtom1.setText(LanguageUtil.getString(activity, R.string.app_cancel_select_all));
        } else {
            publicRecycleviewButtom1.setText(LanguageUtil.getString(activity, R.string.app_allchoose));
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshProcess(Downoption downoption) {
        if (!downoptions.isEmpty()) {
            for (Downoption downoption1 : downoptions) {
                if (downoption1.file_name.equals(downoption.file_name)) {
                    downoption1.down_cunrrent_num = downoption.down_cunrrent_num;
                    downoption1.downoption_size = downoption.downoption_size;
                    downoption1.isdown = downoption.isdown;
                    downMangerAdapter.notifyDataSetChanged();
                    return;
                }
            }
            downoptions.add(downoption);
            downMangerAdapter.notifyDataSetChanged();
        } else {
            downoptions.add(downoption);
            downMangerAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置是否删除
     * @param isDelete
     */
    private void setIsDelete(boolean isDelete) {
        if (!isDelete) {
            Animation footAnim = AnimationUtils.loadAnimation(activity, R.anim.menu_out);
            bottomLayout.startAnimation(footAnim);
            bottomLayout.setVisibility(View.GONE);
            setDeleteBtn(false, 0);
            public_sns_topbar_right_tv.setText(LanguageUtil.getString(activity, R.string.app_edit));
        } else {
            Animation footAnim = AnimationUtils.loadAnimation(activity, R.anim.menu_in);
            bottomLayout.startAnimation(footAnim);
            bottomLayout.setVisibility(View.VISIBLE);
            public_sns_topbar_right_tv.setText(LanguageUtil.getString(activity, R.string.app_cancle));
        }
    }

    /**
     * 设置删除按钮文案
     * @param isChoose
     * @param size
     */
    private void setDeleteBtn(boolean isChoose, int size) {
        if (isChoose) {
            publicRecycleviewButtom1.setText(LanguageUtil.getString(activity, R.string.app_cancel_select_all));
            publicRecycleviewButtom2.setText(LanguageUtil.getString(activity, R.string.app_delete) + "(" + (size) + ")");
            publicRecycleviewButtom2.setTextColor(ContextCompat.getColor(activity, R.color.maincolor));
        } else {
            publicRecycleviewButtom1.setText(LanguageUtil.getString(activity, R.string.app_allchoose));
            publicRecycleviewButtom2.setText(LanguageUtil.getString(activity, R.string.app_delete));
            publicRecycleviewButtom2.setTextColor(ContextCompat.getColor(activity, R.color.red));
        }
    }
}
