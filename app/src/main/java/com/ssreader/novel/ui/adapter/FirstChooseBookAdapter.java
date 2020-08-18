package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseListAdapter;
import com.ssreader.novel.model.Recommend;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ScreenSizeUtils;

import java.util.List;

public class FirstChooseBookAdapter extends BaseListAdapter<Recommend.RecommendProduc> {

    private int WIDTH, HEIGHT;
    private SCOnItemClickListener scOnItemClickListener;

    public FirstChooseBookAdapter(Activity activity, List<Recommend.RecommendProduc> list, SCOnItemClickListener scOnItemClickListener) {
        super(activity, list);
        WIDTH = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        WIDTH = (WIDTH - ImageUtil.dp2px(activity, 30)) / 3;
        HEIGHT = WIDTH * 4 / 3;
        this.scOnItemClickListener = scOnItemClickListener;
    }

    @Override
    public int getViewByRes() {
        return R.layout.activity_home_viewpager_classfy_gridview_item;
    }

    @Override
    public View getOwnView(int position, Recommend.RecommendProduc recommendProduc, View view, ViewGroup parent) {
        ImageView activity_home_viewpager_classfy_GridView_img = view.findViewById(R.id.activity_home_viewpager_classfy_GridView_img);
        View layoutBg = view.findViewById(R.id.activity_home_viewpager_classfy_GridView_img_bg);
        TextView flag = view.findViewById(R.id.activity_home_flag);
        TextView name = view.findViewById(R.id.activity_home_viewpager_classfy_GridView_tv);
        RelativeLayout relativeLayout = view.findViewById(R.id.activity_home_viewpager_classfy_GridView_laiout);
        CheckBox checkBox = view.findViewById(R.id.activity_home_viewpager_classfy_GridView_box);

        ViewGroup.LayoutParams layoutParams = relativeLayout.getLayoutParams();
        layoutParams.width = WIDTH;
        layoutParams.height = HEIGHT;
        relativeLayout.setLayoutParams(layoutParams);
        ViewGroup.LayoutParams params = layoutBg.getLayoutParams();
        params.width = WIDTH;
        params.height = HEIGHT;
        layoutBg.setLayoutParams(params);

        if (recommendProduc.book_id != 0) {
            flag.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 5),
                    ContextCompat.getColor(activity, R.color.maincolor)));
            flag.setText(LanguageUtil.getString(activity, R.string.noverfragment_xiaoshuo));
        } else if (recommendProduc.comic_id != 0) {
            flag.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 5),
                    ContextCompat.getColor(activity, R.color.red)));
            flag.setText(LanguageUtil.getString(activity, R.string.noverfragment_manhua));
        } else if (recommendProduc.audio_id != 0){
            flag.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 5),
                    ContextCompat.getColor(activity, R.color.audio_mark_bg)));
            flag.setText(LanguageUtil.getString(activity, R.string.noverfragment_audio));
        }
        name.setText(recommendProduc.name + "");
        MyGlide.GlideImage(activity, recommendProduc.cover, activity_home_viewpager_classfy_GridView_img, WIDTH, HEIGHT);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scOnItemClickListener.OnItemClickListener(0, position, recommendProduc);
                if (recommendProduc.isChoose) {
                    checkBox.setChecked(true);
                    layoutBg.setVisibility(View.GONE);
                } else {
                    checkBox.setChecked(false);
                    layoutBg.setVisibility(View.VISIBLE);
                }
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 加这一句，否则当我setChecked()时会触发此listener
                if (buttonView.isPressed()) {
                    scOnItemClickListener.OnItemClickListener(0, position, recommendProduc);
                    if (isChecked) {
                        layoutBg.setVisibility(View.GONE);
                    } else {
                        layoutBg.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        return view;
    }
}
