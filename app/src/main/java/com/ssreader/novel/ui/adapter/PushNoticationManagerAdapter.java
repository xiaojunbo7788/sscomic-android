package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suke.widget.SwitchButton;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseListAdapter;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.push.PushManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PushNoticationManagerAdapter extends BaseListAdapter<PushManager> {

    public PushNoticationManagerAdapter(Activity activity, List<PushManager> list) {
        super(activity, list);
    }

    @Override
    public int getViewByRes() {
        return R.layout.item_pushnotication_manager;
    }

    @Override
    public View getOwnView(int position, PushManager been, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder(convertView);
        if (been.getStatus() == 1) {
            viewHolder.SettingsActivityNotifyShelf.setChecked(true);
        } else {
            viewHolder.SettingsActivityNotifyShelf.setChecked(false);
        }
        viewHolder.item_pushnotication_title.setText(been.getLabel());
        viewHolder.SettingsActivityNotifyShelf.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                pushUpHttp(been.getPush_key());
            }
        });
        return convertView;
    }

    /**
     * 是否关闭
     * @param push_key
     */
    private void pushUpHttp(String push_key) {
        ReaderParams readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("push_key", push_key);
        HttpUtils.getInstance().sendRequestRequestParams(activity,Api.push_state_up, readerParams.generateParamsJson(), new HttpUtils.ResponseListener() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onErrorResponse(String ex) {

            }
        });
    }

    class ViewHolder {

        @BindView(R.id.SettingsActivity_notify_shelf)
        SwitchButton SettingsActivityNotifyShelf;
        @BindView(R.id.activity_settings_clear_cache)
        RelativeLayout activitySettingsClearCache;
        @BindView(R.id.item_pushnotication_title)
        TextView item_pushnotication_title;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
