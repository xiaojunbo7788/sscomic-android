package com.ssreader.novel.ui.audio.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.model.AudioSelectionsBean;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.utils.ScreenSizeUtils;

import java.util.List;

import butterknife.BindView;

/**
 * 选章适配器
 */
public class AudioSelectionsAdapter extends BaseRecAdapter<AudioSelectionsBean, AudioSelectionsAdapter.ViewHolder> {

    private OnSelectionsListener onSelectionsListener;

    public void setOnSelectionsListener(OnSelectionsListener onSelectionsListener) {
        this.onSelectionsListener = onSelectionsListener;
    }

    private int index = -1;

    /**
     * 设置高亮
     *
     * @param position
     */
    public void setPosition(int position) {
        this.index = position;
    }

    public AudioSelectionsAdapter(Activity activity, List<AudioSelectionsBean> list, int position) {
        super(list, activity);
        this.index = position;
    }

    @Override
    public void onHolder(ViewHolder holder, AudioSelectionsBean bean, int position) {
        if (bean != null) {
            if (position == 0) {
                holder.line.setVisibility(View.GONE);
            } else {
                holder.line.setVisibility(View.VISIBLE);
            }
            holder.textView.setText(bean.getStartNum() + "-" + bean.getEndNum());
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (index != position && onSelectionsListener != null) {
                        index = position;
                        onSelectionsListener.onSelection(index);
                    }
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateHolder() {
        return new ViewHolder(getViewByRes(R.layout.item_audio_selections));
    }

    public class ViewHolder extends BaseRecViewHolder {

        @BindView(R.id.item_audio_selections_line)
        View line;
        @BindView(R.id.item_audio_selections_text)
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            ViewGroup.LayoutParams params = textView.getLayoutParams();
            params.width = ScreenSizeUtils.getInstance(activity).getScreenWidth() - ImageUtil.dp2px(activity, 40);
            textView.setLayoutParams(params);
        }
    }

    public interface OnSelectionsListener {

        void onSelection(int position);
    }
}
