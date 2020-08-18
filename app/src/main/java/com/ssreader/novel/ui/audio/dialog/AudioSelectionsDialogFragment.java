package com.ssreader.novel.ui.audio.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.model.AudioSelectionsBean;
import com.ssreader.novel.ui.audio.adapter.AudioSelectionsAdapter;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 用于选集
 */
public class AudioSelectionsDialogFragment extends DialogFragment {

    @BindView(R.id.dialog_audio_selections_layout)
    LinearLayout linearLayout;
    @BindView(R.id.dialog_audio_selections_recyclerView_layout)
    RelativeLayout relativeLayout;
    @BindView(R.id.dialog_audio_selections_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.dialog_audio_selections_cancel)
    RelativeLayout cancel;

    private Activity activity;
    private int position = -1;

    private OnSelectionsDialogListener onSelectionsDialogListener;

    public void setOnSelectionsDialogListener(OnSelectionsDialogListener onSelectionsDialogListener) {
        this.onSelectionsDialogListener = onSelectionsDialogListener;
    }

    private LinearLayoutManager linearLayoutManager;

    private List<AudioSelectionsBean> audioSelectionsBeanList;
    private AudioSelectionsAdapter audioSelectionsAdapter;

    public AudioSelectionsDialogFragment(Activity activity, List<AudioSelectionsBean> audioSelectionsBeanList) {
        this.activity = activity;
        this.audioSelectionsBeanList = audioSelectionsBeanList;
    }

    /**
     * 设置高亮显示
     * @param position
     */
    public void setPosition(int position) {
        if (audioSelectionsAdapter != null) {
            this.position = position;
            audioSelectionsAdapter.setPosition(position);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // 窗口底部弹出
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.BottomDialogFragment);
        linearLayoutManager = new LinearLayoutManager(activity);
        audioSelectionsAdapter = new AudioSelectionsAdapter(activity, audioSelectionsBeanList, position);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_audio_selections, container);
        ButterKnife.bind(this, view);
        linearLayout.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 10), ImageUtil.dp2px(activity, 10),
                0, 0, ContextCompat.getColor(activity, R.color.white)));
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(audioSelectionsAdapter);
        if (audioSelectionsBeanList.size() > 4) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) relativeLayout.getLayoutParams();
            params.height = ImageUtil.dp2px(activity, 200);
            relativeLayout.setLayoutParams(params);
        }
        initListener();
        // 请求数据
        return view;
    }

    private void initListener() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        audioSelectionsAdapter.setOnSelectionsListener(new AudioSelectionsAdapter.OnSelectionsListener() {
            @Override
            public void onSelection(int position) {
                audioSelectionsAdapter.notifyDataSetChanged();
                if (onSelectionsDialogListener != null) {
                    onSelectionsDialogListener.onSelection(position);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public interface OnSelectionsDialogListener {

        void onSelection(int position);
    }
}
