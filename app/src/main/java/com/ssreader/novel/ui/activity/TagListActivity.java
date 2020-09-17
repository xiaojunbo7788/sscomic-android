package com.ssreader.novel.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.model.BaseTag;
import com.ssreader.novel.model.TagListBean;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.utils.LanguageUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import butterknife.BindView;

import static com.ssreader.novel.constant.Api.mBookInfoUrl;
import static com.ssreader.novel.constant.Constant.READHISTORY;
import static com.ssreader.novel.constant.Constant.TAG_LIST;

public class TagListActivity extends BaseActivity {

    @BindView(R.id.tag_textLayout)
    TagFlowLayout tagFlowLayout;

    @BindView(R.id.public_sns_topbar_title)
    TextView public_sns_topbar_title;

    @BindView(R.id.public_sns_topbar_back)
    RelativeLayout public_sns_topbar_back;

    @Override
    public int initContentView() {
        return R.layout.activity_tag_list;
    }

    @Override
    public void initView() {
        public_sns_topbar_title.setText("标签列表");
        public_sns_topbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("page_size", 1000 + "");
        HttpUtils.getInstance().sendRequestRequestParams(activity, Api.MyTagList, readerParams.generateParamsJson(), responseListener);
    }

    @Override
    public void initInfo(String json) {
        TagListBean tagListBean = gson.fromJson(json, TagListBean.class);
        tagFlowLayout.setAdapter(new TagAdapter<BaseTag>(tagListBean.getList()) {
            @Override
            public View getView(FlowLayout parent, int position, BaseTag s) {
                LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(activity)
                        .inflate(R.layout.item_info_tag, parent, false);
                TextView textView = linearLayout.findViewById(R.id.item_info_tag_text);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectTag(s);
                    }
                });
                textView.setText(s.getTitle());
                textView.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 15),
                        ContextCompat.getColor(activity, R.color.graybg)));
                return linearLayout;
            }
        });
    }

    private void selectTag(BaseTag tag) {
        activity.startActivity(new Intent(activity, BaseOptionActivity.class)
                .putExtra("title", tag.getTitle())
                .putExtra("tab", tag.getTitle())
                .putExtra("classType",1)
                .putExtra("OPTION", TAG_LIST));
    }
}
