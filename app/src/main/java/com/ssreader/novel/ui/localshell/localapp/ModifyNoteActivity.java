package com.ssreader.novel.ui.localshell.localapp;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.ui.localshell.bean.LocalNotesBean;
import com.ssreader.novel.ui.dialog.BottomMenuDialog;
import com.ssreader.novel.ui.dialog.SetCodeDialog;
import com.ssreader.novel.ui.utils.MyOpenCameraAlbum;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class ModifyNoteActivity extends BaseActivity {

    @BindView(R.id.edit_note_title_layout)
    RelativeLayout editNoteTitleLayout;
    @BindView(R.id.edit_note_author_layout)
    RelativeLayout editNoteAuthorLayout;
    @BindView(R.id.edit_note_context_layout)
    RelativeLayout editNoteContextLayout;
    @BindView(R.id.edit_note_touxiang_layout)
    RelativeLayout edit_note_touxiang_layout;
    @BindView(R.id.edit_success)
    TextView editSuccess;
    @BindView(R.id.local_note_title)
    TextView localNoteTitle;
    @BindView(R.id.local_note_author)
    TextView localNoteAuthor;
    @BindView(R.id.local_note_context)
    TextView localNoteContext;

    @OnClick({R.id.edit_note_title_layout,
            R.id.edit_note_author_layout,
            R.id.edit_note_context_layout,
            R.id.edit_note_touxiang_layout,
            R.id.edit_success})
    public void onClick(View view) {
        LocalNotesBean localNotesBean = new LocalNotesBean();
        switch (view.getId()) {
            case R.id.edit_note_title_layout:
                //修改名字
                SetCodeDialog setCodeDialog = new SetCodeDialog();
                setCodeDialog.showSetCodeDialog(activity, "修改笔记", localNoteTitle.getText().toString());
                setCodeDialog.setOnVerificationSuccess(new SetCodeDialog.onVerificationSuccess() {
                    @Override
                    public void success(String editText) {
                        localNoteTitle.setText(editText);
                    }
                });
                break;
            case R.id.edit_note_author_layout:
                //修改作者
                SetCodeDialog setCodeDialog1 = new SetCodeDialog();
                setCodeDialog1.showSetCodeDialog(activity, "修改笔记", localNoteAuthor.getText().toString());
                setCodeDialog1.setOnVerificationSuccess(new SetCodeDialog.onVerificationSuccess() {
                    @Override
                    public void success(String editText) {
                        localNoteAuthor.setText(editText);
                    }
                });
                break;
            case R.id.edit_note_context_layout:
                //修改内容
                SetCodeDialog setCodeDialog2 = new SetCodeDialog();
                setCodeDialog2.showSetCodeDialog(activity,"修改笔记", localNoteContext.getText().toString());
                setCodeDialog2.setOnVerificationSuccess(new SetCodeDialog.onVerificationSuccess() {
                    @Override
                    public void success(String editText) {
                        localNoteContext.setText(editText);
                    }
                });
                break;
            case R.id.edit_note_touxiang_layout:
                //修改封面
                new BottomMenuDialog().showBottomMenuDialog(activity, new String[]{
                        LanguageUtil.getString(activity, R.string.MineUserInfo_PaiZhao),
                        LanguageUtil.getString(activity, R.string.MineUserInfoXiangCe)
                }, new SCOnItemClickListener() {
                    @Override
                    public void OnItemClickListener(int flag, int position, Object O) {
                        if (position == 0) {
                            MyOpenCameraAlbum.openCamera(activity,MyOpenCameraAlbum.CAMERA);
                        } else if (position == 1) {
                            MyOpenCameraAlbum.openPhotoAlbum(activity,MyOpenCameraAlbum.GALLERY);
                        }
                    }

                    @Override
                    public void OnItemLongClickListener(int flag, int position, Object O) {

                    }
                });
                break;
            case R.id.edit_success:
                //修改完成
                localNotesBean.setUpdate(true);
                if(localNoteTitle.getText().toString()!=null&&localNoteContext.getText().toString()!=null&&localNoteAuthor.getText().toString()!=null){
                    localNotesBean.setFrombookTitle(localNoteTitle.getText().toString());
                    localNotesBean.setNotesContent(localNoteContext.getText().toString());
                    localNotesBean.setNotesTitle(localNoteAuthor.getText().toString());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");// HH:mm:ss
                    Date date = new Date(System.currentTimeMillis());
                    localNotesBean.setNotesTime(simpleDateFormat.format(date));
                    ObjectBoxUtils.addData(localNotesBean,LocalNotesBean.class);
                    EventBus.getDefault().post(localNotesBean);
                    finish();
                }else{
                    MyToash.ToashSuccess(activity,"请将内容进行修改之后才能保存");
                }

                break;

        }
    }

    @Override
    public int initContentView() {
        USE_PUBLIC_BAR = true;
        public_sns_topbar_title_id = R.string.local_modify_note;
        return R.layout.activity_local_modify_note;
    }

    @Override
    public void initView() {
        String localtitle = formIntent.getStringExtra("Localtitle");
        String localAuther = formIntent.getStringExtra("LocalAuther");
        String localDes = formIntent.getStringExtra("LocalDes");
        localNoteTitle.setText(localtitle);
        localNoteAuthor.setText(localAuther);
        localNoteContext.setText(localDes);
        editSuccess.setBackground(MyShape.setMyshapeStroke2(activity,0,0,0, ContextCompat.getColor(activity,R.color.maincolor)));
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {

    }
}
