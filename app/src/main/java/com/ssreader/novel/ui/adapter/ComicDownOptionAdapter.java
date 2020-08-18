package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseListAdapter;

import com.ssreader.novel.model.ComicChapter;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.LanguageUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComicDownOptionAdapter extends BaseListAdapter<ComicChapter> {

    public List<ComicChapter> comicDownOptionListChooseDwn;
    private List<ComicChapter> comicDownOptionList;
    private TextView activity_comicdown_choose_count, activity_comicdown_down;
    boolean flag;
    private TextView activity_comicdown_quanxuan;
    public int YetDownNum;

    public ComicDownOptionAdapter(Activity activity, List<ComicChapter> comicDownOptionList, TextView activity_comicdown_choose_count, TextView activity_comicdown_down, boolean flag, TextView activity_comicdown_quanxuan) {
        super(activity, comicDownOptionList);
        this.flag = flag;
        this.activity_comicdown_quanxuan = activity_comicdown_quanxuan;
        this.comicDownOptionList = comicDownOptionList;
        this.activity_comicdown_choose_count = activity_comicdown_choose_count;
        this.activity_comicdown_down = activity_comicdown_down;
        comicDownOptionListChooseDwn = new ArrayList<>();
    }

    @Override
    public int getViewByRes() {
        return R.layout.item_comicdownoption;
    }

    @Override
    public View getOwnView(int position, ComicChapter comicDownOption, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        viewHolder = new ViewHolder(convertView);
        if (comicDownOption.display_label != null) {
            viewHolder.item_comicdownoption_text.setText(comicDownOption.display_label);
        }
        int status = 1;
        if (!flag) {
            status = comicDownOption.downStatus;
            if (status == 0 || status == 3) {
                viewHolder.item_comicdownoption_downstatus.setVisibility(View.GONE);
                if (comicDownOptionListChooseDwn.contains(comicDownOption)) {
                    viewHolder.item_comicdownoption_text.setBackground(MyShape.setMyCustomizeShape(activity,
                            5, R.color.maincolor));
                    viewHolder.item_comicdownoption_text.setTextColor(Color.WHITE);
                    viewHolder.item_comicdownoption_vip.setImageResource(R.mipmap.lock_white);
                } else {
                    viewHolder.item_comicdownoption_text.setBackground(MyShape.setMyCustomizeShape(activity, 5, R.color.white));
                    viewHolder.item_comicdownoption_text.setTextColor(Color.BLACK);
                    viewHolder.item_comicdownoption_vip.setImageResource(R.mipmap.lock_orange);
                }
                if (comicDownOption.is_preview == 0) {
                    viewHolder.item_comicdownoption_vip.setVisibility(View.GONE);
                } else {
                    viewHolder.item_comicdownoption_vip.setVisibility(View.VISIBLE);
                }
                if (status == 3) {

                    viewHolder.item_comicdownoption_downstatus.setVisibility(View.VISIBLE);
                    viewHolder.item_comicdownoption_downstatus.setBackground(MyShape.setMyCustomizeShape(activity, 10, R.color.red));
                    viewHolder.item_comicdownoption_downstatus.setText(LanguageUtil.getString(activity, R.string.ComicDownActivity_down_error));
                } else {
                    viewHolder.item_comicdownoption_downstatus.setVisibility(View.GONE);
                }
            } else {
                viewHolder.item_comicdownoption_text.setBackground(MyShape.setMyshapeMineStroke(activity, 5, 14));
                viewHolder.item_comicdownoption_text.setTextColor(Color.BLACK);
                viewHolder.item_comicdownoption_downstatus.setVisibility(View.VISIBLE);
                if (status == 2) {
                    viewHolder.item_comicdownoption_downstatus.setBackground(MyShape.setMyCustomizeShape(activity, 10, R.color.light_green));
                    viewHolder.item_comicdownoption_downstatus.setText(LanguageUtil.getString(activity, R.string.ComicDownActivity_downing));
                } else {
                    viewHolder.item_comicdownoption_downstatus.setBackground(MyShape.setMyCustomizeShape(activity, 10, R.color.light_green_local));
                    viewHolder.item_comicdownoption_downstatus.setText(LanguageUtil.getString(activity, R.string.ComicDownActivity_local));
                }
            }
        } else {
            viewHolder.item_comicdownoption_downstatus.setText(LanguageUtil.getString(activity, R.string.ComicDownActivity_local));
            viewHolder.item_comicdownoption_downstatus.setBackground(MyShape.setMyCustomizeShape(activity, 10, R.color.light_green_local));

            if (comicDownOptionListChooseDwn.contains(comicDownOption)) {
                viewHolder.item_comicdownoption_downstatus.setVisibility(View.GONE);
                viewHolder.item_comicdownoption_text.setBackground(MyShape.setMyCustomizeShape(activity, 5, R.color.maincolor));
                viewHolder.item_comicdownoption_text.setTextColor(Color.WHITE);
            } else {
                viewHolder.item_comicdownoption_downstatus.setVisibility(View.VISIBLE);
                viewHolder.item_comicdownoption_text.setBackground(MyShape.setMyCustomizeShape(activity, 5, R.color.white));
                viewHolder.item_comicdownoption_text.setTextColor(Color.BLACK);
            }
        }
        viewHolder.item_comicdownoption_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    if (!comicDownOptionListChooseDwn.contains(comicDownOption)) {
                        viewHolder.item_comicdownoption_downstatus.setVisibility(View.GONE);
                        comicDownOptionListChooseDwn.add(comicDownOption);
                        viewHolder.item_comicdownoption_text.setBackground(MyShape.setMyCustomizeShape(activity, 5, R.color.maincolor));
                        viewHolder.item_comicdownoption_text.setTextColor(Color.WHITE);
                    } else {
                        viewHolder.item_comicdownoption_downstatus.setVisibility(View.VISIBLE);
                        comicDownOptionListChooseDwn.remove(comicDownOption);
                        viewHolder.item_comicdownoption_text.setBackground(MyShape.setMyCustomizeShape(activity, 5, R.color.white));
                        viewHolder.item_comicdownoption_text.setTextColor(Color.BLACK);
                    }
                    refreshBtn(comicDownOptionListChooseDwn.size());
                } else {
                    int status = comicDownOption.downStatus;
                    if (status == 0 || status == 3) {
                        if (!comicDownOptionListChooseDwn.contains(comicDownOption)) {
                            comicDownOptionListChooseDwn.add(comicDownOption);
                            viewHolder.item_comicdownoption_text.setBackground(MyShape.setMyCustomizeShape(activity, 5, R.color.maincolor));
                            viewHolder.item_comicdownoption_vip.setImageResource(R.mipmap.lock_white);
                            viewHolder.item_comicdownoption_text.setTextColor(Color.WHITE);
                        } else {
                            comicDownOptionListChooseDwn.remove(comicDownOption);
                            viewHolder.item_comicdownoption_text.setBackground(MyShape.setMyshapeMineStroke(activity, 5, 8));
                            viewHolder.item_comicdownoption_text.setTextColor(Color.BLACK);
                            viewHolder.item_comicdownoption_vip.setImageResource(R.mipmap.lock_orange);
                        }
                        refreshBtn(comicDownOptionListChooseDwn.size());
                    }
                }
            }
        });

        return convertView;
    }

    public void selectAll(boolean addDown) {
        if (!flag && (addDown || (comicDownOptionList.size() == (comicDownOptionListChooseDwn.size() + YetDownNum)))) {
            comicDownOptionListChooseDwn.clear();
            if (addDown) {
                activity_comicdown_down.setClickable(false);
                activity_comicdown_down.setTextColor(ContextCompat.getColor(activity, R.color.gray_b0));
                activity_comicdown_down.setTextColor(Color.BLACK);
                activity_comicdown_down.setClickable(true);
                activity_comicdown_quanxuan.setText(LanguageUtil.getString(activity, R.string.app_allchoose));
                activity_comicdown_choose_count.setText(String.format(LanguageUtil.getString(activity, R.string.ComicDownActivity_choosecount), 0));
            } else {
                refreshBtn(0);
            }
        } else {
            comicDownOptionListChooseDwn.clear();
            if (!AllChoose) {
                if (!flag) {
                    for (ComicChapter comicChapter : comicDownOptionList) {
                        int status = comicChapter.downStatus;
                        if (status == 0 || status == 3) {
                            comicDownOptionListChooseDwn.add(comicChapter);
                        }
                    }
                } else {
                    comicDownOptionListChooseDwn.addAll(comicDownOptionList);
                }
            }
            AllChoose = !AllChoose;
            refreshBtn(comicDownOptionListChooseDwn.size());
        }
        //MyToash.Log("comicDownOpti", flag + "  " + comicDownOptionListChooseDwn.size());
        notifyDataSetChanged();
    }

    private boolean AllChoose;

    public void refreshBtn(int size) {
        if (size == 0) {
            activity_comicdown_down.setClickable(false);
            activity_comicdown_down.setTextColor(ContextCompat.getColor(activity, R.color.gray_b0));
        } else {
            activity_comicdown_down.setTextColor(Color.BLACK);
            activity_comicdown_down.setClickable(true);
        }
        if (size == comicDownOptionList.size() - YetDownNum) {
            activity_comicdown_quanxuan.setText(LanguageUtil.getString(activity, R.string.app_cancel_select_all));
        } else {
            activity_comicdown_quanxuan.setText(LanguageUtil.getString(activity, R.string.app_allchoose));
        }
        activity_comicdown_choose_count.setText(String.format(LanguageUtil.getString(activity, R.string.ComicDownActivity_choosecount), size));
    }

    public class ViewHolder {

        @BindView(R.id.item_comicdownoption_downstatus)
        TextView item_comicdownoption_downstatus;
        @BindView(R.id.item_comicdownoption_vip)
        ImageView item_comicdownoption_vip;
        @BindView(R.id.item_comicdownoption_text)
        TextView item_comicdownoption_text;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
