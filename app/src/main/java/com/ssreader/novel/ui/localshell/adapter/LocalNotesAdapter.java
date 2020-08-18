package com.ssreader.novel.ui.localshell.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.ui.localshell.bean.LocalNotesBean;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;

import java.util.List;

import butterknife.BindView;

public class LocalNotesAdapter extends BaseRecAdapter<LocalNotesBean, LocalNotesAdapter.ViewHolder> {

    public LocalNotesAdapter(List list, Activity activity, SCOnItemClickListener scOnItemClickListener) {
        super(list, activity, scOnItemClickListener);
        this.list = list;
        this.activity = activity;
    }

    @Override
    public void onHolder(LocalNotesAdapter.ViewHolder viewHolder, LocalNotesBean bean, int position) {
        if (bean.book_id != -1) {
            viewHolder.item_notes_from_add.setVisibility(View.GONE);
            viewHolder.itemNotesbookContent.setText(bean.notesContent);
            viewHolder.itemNotesFromTitle.setText(bean.notesTitle);
            viewHolder.itemNotesbookTitle.setText(bean.frombookTitle);
            viewHolder.itemNotesTime.setText(bean.notesTime);
        } else {
            viewHolder.item_notes_from_content.setVisibility(View.GONE);
        }
    }

    @Override
    public LocalNotesAdapter.ViewHolder onCreateHolder() {
        return new ViewHolder(getViewByRes(R.layout.item_local_notes));
    }

    class ViewHolder extends BaseRecViewHolder {

        @BindView(R.id.item_notes_from_title)
        TextView itemNotesFromTitle;
        @BindView(R.id.item_notes_time)
        TextView itemNotesTime;
        @BindView(R.id.item_notesbook_title)
        TextView itemNotesbookTitle;
        @BindView(R.id.item_notesbook_content)
        TextView itemNotesbookContent;

        @BindView(R.id.item_notes_from_add)
        View item_notes_from_add;
        @BindView(R.id.item_notes_from_content)
        View item_notes_from_content;

        ViewHolder(View view) {
            super(view);
        }
    }
}
