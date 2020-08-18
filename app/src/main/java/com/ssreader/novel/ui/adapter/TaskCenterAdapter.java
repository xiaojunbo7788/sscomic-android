package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.util.ColorParser;
import com.ssreader.novel.R;
import com.ssreader.novel.model.TaskCenter;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.utils.LanguageUtil;

import java.util.List;

public class TaskCenterAdapter extends BaseAdapter {

    private List<TaskCenter.TaskCenter2.Taskcenter> taskCenter2s;
    private Activity activity;
    private int othertask;
    private String title1, title2;

    public TaskCenterAdapter(List<TaskCenter.TaskCenter2.Taskcenter> taskCenter2s, Activity activity, int othertask, String title1, String title2) {
        this.taskCenter2s = taskCenter2s;
        this.othertask = othertask;
        this.title1 = title1;
        this.title2 = title2;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return taskCenter2s.size();
    }

    @Override
    public TaskCenter.TaskCenter2.Taskcenter getItem(int i) {
        return taskCenter2s.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.item_list_taskcenter, null);
        }
        TaskCenter.TaskCenter2.Taskcenter taskCenter2 = getItem(i);
        LinearLayout linearLayout = view.findViewById(R.id.listview_taskcenter_tasktype_layout);
        TextView title = view.findViewById(R.id.listview_taskcenter_tasktype);
        if (i == 0 || i == othertask) {
            if (i == 0) {
                title.setText(title1);
            } else {
                title.setText(title2);
            }
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
        TextView label = view.findViewById(R.id.listview_taskcenter_task_labe);
        View line = view.findViewById(R.id.shuxian);
        TextView award = view.findViewById(R.id.listview_taskcenter_award);
        TextView desc = view.findViewById(R.id.listview_taskcenter_task_desc);
        TextView status = view.findViewById(R.id.listview_taskcenter_status);
        View emptyView = view.findViewById(R.id.listview_task_center_empty_layout);

        label.setText(taskCenter2.getTask_label());
        award.setText(taskCenter2.getTask_award());
        desc.setText(taskCenter2.getTask_desc());
        line.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 10),
                ContextCompat.getColor(activity, R.color.maincolor)));
        if (taskCenter2.getTask_state() == 0) {
            status.setText(LanguageUtil.getString(activity, R.string.TaskCenterActivity_gocomplete));
            status.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 20),
                    ColorParser.parseCssColor("#FB641A")));
        } else {
            status.setText(LanguageUtil.getString(activity, R.string.TaskCenterActivity_yetcomplete));
            status.setBackground(MyShape.setMyshapeMineStroke(activity, 20, 20));
        }
        if (i == taskCenter2s.size() - 1) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }
        return view;
    }
}
