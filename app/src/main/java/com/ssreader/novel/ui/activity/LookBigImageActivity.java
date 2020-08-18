package com.ssreader.novel.ui.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.github.chrisbanes.photoview.PhotoView;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.ui.utils.MyGlide;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LookBigImageActivity extends BaseActivity {

    @BindView(R.id.activity_lookbigimage_ViewPager2)
    ViewPager2 activity_lookbigimage_ViewPager2;
    @BindView(R.id.activity_lookbigimage_title1)
    TextView activityLookbigimageTitle1;
    @BindView(R.id.activity_lookbigimage_title2)
    TextView activityLookbigimageTitle2;
    @BindView(R.id.activity_lookbigimage_content)
    TextView activityLookbigimageContent;
    @BindView(R.id.activity_lookbigimage_RelativeLayout)
    View activity_lookbigimage_RelativeLayout;
    @BindView(R.id.activity_lookbigimage_active_today)
    TextView activity_lookbigimage_active_today;

    private List<String> snsShowPictures;
    private int click_position;
    private String lookbigimgcontent;
    private RecyclerView.Adapter adapter;

    @Override
    public int initContentView() {
        FULL_CCREEN = true;
        USE_EventBus = true;
        return R.layout.activity_lookbigimage;
    }

    @OnClick({R.id.activity_lookbigimage_back})
    public void getEvent(View view) {
        finish();
    }

    @Override
    public void initView() {
        activity_lookbigimage_RelativeLayout.setVisibility(View.VISIBLE);
        snsShowPictures = new ArrayList<>();
        adapter = new RecyclerView.Adapter<ViewHolder>() {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(activity).inflate(R.layout.item_lookbigimage_img, parent, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                MyGlide.GlideImageNoSize(activity, snsShowPictures.get(position), holder.item_lookbigimage_img);
                holder.item_lookbigimage_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (activity_lookbigimage_RelativeLayout.getVisibility() == View.VISIBLE) {
                            activity_lookbigimage_RelativeLayout.setVisibility(View.GONE);
                        } else {
                            activity_lookbigimage_RelativeLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }

            @Override
            public int getItemCount() {
                return snsShowPictures.size();
            }
        };

        snsShowPictures.addAll((List<String>) formIntent.getSerializableExtra("snsShowPictures"));
        click_position = formIntent.getIntExtra("click_position", 0);
        lookbigimgcontent = formIntent.getStringExtra("lookbigimgcontent");

        activityLookbigimageTitle1.setText((click_position + 1) + "");
        activityLookbigimageTitle2.setText("/" + (snsShowPictures.size()));
        activityLookbigimageContent.setText(lookbigimgcontent);
        activity_lookbigimage_ViewPager2.setAdapter(adapter);

        if (click_position != 0) {
            activity_lookbigimage_ViewPager2.setCurrentItem(click_position, false);
        }
        activity_lookbigimage_ViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                click_position = position;
                activityLookbigimageTitle1.setText((position + 1) + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_lookbigimage_img)
        PhotoView item_lookbigimage_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNullRefresh() {

    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        //关闭窗体动画显示
        this.overridePendingTransition(R.anim.no_anim, R.anim.activity_top_bottom_close);
    }
}
