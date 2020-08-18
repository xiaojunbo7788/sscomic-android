package com.ssreader.novel.ui.localshell.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.ui.localshell.bean.LocalNotesBean;

import java.util.List;

import butterknife.BindView;

public class CalendarNotesAdapter extends BaseRecAdapter<LocalNotesBean,CalendarNotesAdapter.ViewHolder> {
    public CalendarNotesAdapter(List<LocalNotesBean> list, Activity activity) {
        super(list, activity);
    }

    @Override
    public void onHolder(ViewHolder viewHolder, LocalNotesBean bean, int position) {
        viewHolder.itemCalenderTitle.setText(bean.notesTitle);
        viewHolder.itemCalenderTime.setText(bean.notesTime);
        viewHolder.itemCalenderContent.setText(bean.notesContent);
    }

    @Override
    public ViewHolder onCreateHolder() {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_calendar_notes, null));
    }

    class ViewHolder extends BaseRecViewHolder {

        @BindView(R.id.item_calender_title)
        TextView itemCalenderTitle;
        @BindView(R.id.item_calender_content)
        TextView itemCalenderContent;
        @BindView(R.id.item_calender_time)
        TextView itemCalenderTime;

        ViewHolder(View view) {
            super(view);
        }
    }
}
