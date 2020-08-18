package com.ssreader.novel.ui.localshell.localapp;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.ui.localshell.bean.LocalNotesBean;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ssreader.novel.ui.utils.StatusBarUtil.setStatusTextColor;

/**
 * 笔记详情
 */
public class LocalNoteDetailActivity extends BaseActivity {

    @BindView(R.id.local_note_detail_title)
    TextView local_note_detail_title;

    @BindView(R.id.local_note_detail_head)
    RelativeLayout toolLayout;
    @BindView(R.id.local_note_detail_back)
    LinearLayout back;
    @BindView(R.id.local_note_img)
    ImageView localNoteImg;
    @BindView(R.id.local_book_title)
    TextView localBookTitle;
    @BindView(R.id.local_book_author)
    TextView localBookAuthor;
    @BindView(R.id.local_book_des)
    TextView localBookDes;
    @BindView(R.id.local_note_detail_edit)
    TextView local_note_detail_edit;



    @BindView(R.id.local_note_detail_background)
    LinearLayout localNoteDetailBackground;
    LocalNotesBean localNotesBean;
    @Override
    public int initContentView() {
        FULL_CCREEN = true;
        return R.layout.activity_local_note_detail;
    }

    @Override
    public void initView() {
        localNotesBean= (LocalNotesBean) formIntent.getSerializableExtra("LocalNotesBean");

        setStatusTextColor(false, activity);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolLayout.getLayoutParams();
        params.topMargin = ImmersionBar.with(activity).getStatusBarHeight(activity);
        toolLayout.setLayoutParams(params);
        localBookTitle.setText(localNotesBean.frombookTitle);
        localBookAuthor.setText(localNotesBean.notesTitle);
        localBookDes.setText(localNotesBean.notesContent);
        local_note_detail_title.setText("笔记详情");
        initListener();
    }

    @OnClick({R.id.local_note_detail_back,R.id.local_note_detail_edit})
    public void onLocalNoteDetailClick(View view) {
        switch (view.getId()) {
            case R.id.local_note_detail_back:
                finish();
                break;
            case R.id.local_note_detail_edit:
                Intent intent = new Intent(activity, LocalEditNotesActivity.class);
                intent.putExtra("LocalNotesBean", localNotesBean);
                startActivity(intent);
                break;
        }
    }

    private void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {

    }
}
