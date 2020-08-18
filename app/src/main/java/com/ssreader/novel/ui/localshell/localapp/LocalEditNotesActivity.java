package com.ssreader.novel.ui.localshell.localapp;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.ui.localshell.bean.LineBreakLayoutBeen;
import com.ssreader.novel.ui.localshell.bean.LocalNotesBean;
import com.ssreader.novel.ui.localshell.filesearcher.FileSearcher;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.localshell.localapp.view.LineBreakLayout;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LocalEditNotesActivity extends BaseActivity {

    @BindView(R.id.public_sns_topbar_right_tv)
    TextView publicSnsTopbarRightTv;
    @BindView(R.id.public_sns_topbar_right_img)
    ImageView publicSnsTopbarRightImg;
    @BindView(R.id.public_sns_topbar_right_other)
    RelativeLayout publicSnsTopbarRightOther;
    @BindView(R.id.public_sns_topbar_back_img)
    ImageView publicSnsTopbarBackImg;
    @BindView(R.id.public_sns_topbar_back_tv)
    TextView publicSnsTopbarBackTv;
    @BindView(R.id.public_sns_topbar_title)
    TextView publicSnsTopbarTitle;
    @BindView(R.id.public_sns_topbar_layout)
    RelativeLayout publicSnsTopbarLayout;
    @BindView(R.id.activity_feedback_content)
    EditText activityFeedbackContent;
    @BindView(R.id.activity_feedback_percentage)
    TextView activityFeedbackPercentage;
    @BindView(R.id.edit_idea)
    EditText editIdea;
    @BindView(R.id.tiezi_tags)
    LineBreakLayout tieziTags;
    @BindView(R.id.add_book_layout)
    LinearLayout add_book_layout;
    @BindView(R.id.edit_note_title)
    EditText edit_note_title;
    LocalNotesBean localNotesBean;
    public int id = 33;
    private int notes_id;

    @OnClick({R.id.public_sns_topbar_right_other, R.id.public_sns_topbar_back_tv, R.id.add_book_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.public_sns_topbar_right_other:
                if(!TextUtils.isEmpty(activityFeedbackContent.getText().toString())) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");// HH:mm:ss
                    Date date = new Date(System.currentTimeMillis());
                    LocalNotesBean localNotesBean = new LocalNotesBean();
                    localNotesBean.frombookTitle = edit_note_title.getText().toString();
                    localNotesBean.notesTime = simpleDateFormat.format(date);
                    localNotesBean.notesContent =  activityFeedbackContent.getText().toString();
                    localNotesBean.notesTitle =  editIdea.getText().toString();
                    if(notes_id!=0){
                        localNotesBean.book_id = notes_id;
                        localNotesBean.day_id=notes_id;
                    }else{
                        localNotesBean.book_id=id;
                    }
                    ObjectBoxUtils.addData(localNotesBean, LocalNotesBean.class);
                    EventBus.getDefault().post(localNotesBean);
                    finish();
                }
                break;
            case R.id.public_sns_topbar_back_tv:
                finish();
                break;
            case R.id.add_book_layout:
                new FileSearcher(activity)
                        .withExtension("txt")
                        .withSizeLimit(10 * 1024, -1)
                        .search(new FileSearcher.FileSearcherCallback() {
                            @Override
                            public void onSelect(final List<File> files) {
//                                addBookFromFile(activity, files);
                            }
                        });
                break;
        }

    }

    List<LineBreakLayoutBeen> list;
    private List<String> tags = new ArrayList<>();

    @Override
    public int initContentView() {
        return R.layout.activity_local_edit_notes;
    }

    @Override
    public void initView() {
        notes_id = formIntent.getIntExtra("notes_id", 0);
        localNotesBean= (LocalNotesBean) formIntent.getSerializableExtra("LocalNotesBean");
        publicSnsTopbarTitle.setVisibility(View.VISIBLE);
        if(localNotesBean!=null) {
            edit_note_title.setText(localNotesBean.notesTitle);
            activityFeedbackContent.setText(localNotesBean.notesContent);
            editIdea.setText(localNotesBean.frombookTitle);
            publicSnsTopbarTitle.setText(LanguageUtil.getString(activity, R.string.local_edit_notes));
        }else {
            publicSnsTopbarTitle.setText("新增笔记");
            localNotesBean=new LocalNotesBean();
        }
        publicSnsTopbarRightOther.setVisibility(View.VISIBLE);
        publicSnsTopbarRightTv.setVisibility(View.VISIBLE);
        publicSnsTopbarRightTv.setText("完成");
        publicSnsTopbarRightTv.setTextSize(16);
        publicSnsTopbarRightTv.setTextColor(Color.BLACK);
        publicSnsTopbarRightImg.setVisibility(View.GONE);
        publicSnsTopbarBackImg.setVisibility(View.GONE);
        publicSnsTopbarBackTv.setVisibility(View.VISIBLE);
        publicSnsTopbarBackTv.setText("取消");
        publicSnsTopbarBackTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list = new ArrayList<>();

        list.add(new LineBreakLayoutBeen("都市现实"));
        list.add(new LineBreakLayoutBeen("武侠仙侠"));
        list.add(new LineBreakLayoutBeen("玄幻奇幻"));
        list.add(new LineBreakLayoutBeen("现代言情"));
        list.add(new LineBreakLayoutBeen("军事"));
        tieziTags.setLablesTieZiTags(activity, list);
        tieziTags.setOnItemOnclickListener(new LineBreakLayout.OnItemOnclickListener() {
            @Override
            public void OnItemOnclick(TextView tv, String text, int position) {
                if (tags.contains(text)) {
                    tags.remove(text);
                    tv.setBackground(ContextCompat.getDrawable(activity, R.color.lightgray1));
                    tv.setTextColor(ContextCompat.getColor(activity, R.color.gray));
                } else {
                    if (tags.size() < 3) {
                        tags.add(text);
                        tv.setBackground(ContextCompat.getDrawable(activity, R.color.maincolor));
                        tv.setTextColor(ContextCompat.getColor(activity, R.color.black1));
                    } else {
                        MyToash.Toash(activity, LanguageUtil.getString(activity, R.string.local_choose_tiezi_lable_max_three));
                    }
                }
            }
        });


    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {

    }
}
