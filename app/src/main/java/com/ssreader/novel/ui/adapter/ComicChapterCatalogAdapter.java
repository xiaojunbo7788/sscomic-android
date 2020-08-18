package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lihang.ShadowLayout;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.eventbus.OpenComicChapter;
import com.ssreader.novel.model.Comic;
import com.ssreader.novel.model.ComicChapter;
import com.ssreader.novel.ui.activity.ComicLookActivity;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.ssreader.novel.utils.ScreenSizeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

import static com.ssreader.novel.constant.Constant.AgainTime;

public class ComicChapterCatalogAdapter extends BaseRecAdapter<ComicChapter, ComicChapterCatalogAdapter.ViewHolder> {

    public Comic baseComic;
    public boolean shunxu;
    private long currentChapterId;
    private int width, height;
    private long ClickTime;

    public void setCurrentChapterId(long currentChapterId) {
        this.currentChapterId = currentChapterId;
    }

    public ComicChapterCatalogAdapter(List<ComicChapter> comicChapterCatalogList, Activity activity, Comic baseComic, long currentChapterId) {
        super(comicChapterCatalogList, activity);
        this.activity = activity;
        this.baseComic = baseComic;
        width = ScreenSizeUtils.getInstance(activity).getScreenWidth() / 3;
        height = width * 3 / 5 + ImageUtil.dp2px(activity, 10);
        if (currentChapterId != 0) {
            this.currentChapterId = currentChapterId;
        } else {
            if (!comicChapterCatalogList.isEmpty()) {
                this.currentChapterId = comicChapterCatalogList.get(0).chapter_id;
            }
        }
    }

    @Override
    public void onHolder(ViewHolder viewHolder, ComicChapter comicChapterCatalog, int position) {
        ViewGroup.LayoutParams coverParams = viewHolder.catalogCoverLayout.getLayoutParams();
        ViewGroup.LayoutParams vipParams = viewHolder.catalogVipLayout.getLayoutParams();
        coverParams.width = vipParams.width = width;
        coverParams.height = vipParams.height = width * 3 / 5;
        viewHolder.catalogCoverLayout.setLayoutParams(coverParams);
        viewHolder.catalogVipLayout.setLayoutParams(vipParams);

        ViewGroup.LayoutParams layoutParam = viewHolder.catalogLayout.getLayoutParams();
        ViewGroup.LayoutParams readParams = viewHolder.catalogReadLayout.getLayoutParams();
        layoutParam.height = readParams.height = height;
        viewHolder.catalogLayout.setLayoutParams(layoutParam);
        viewHolder.catalogReadLayout.setLayoutParams(readParams);

        MyGlide.GlideImageNoSize(activity, comicChapterCatalog.small_cover, viewHolder.catalogImage);
        viewHolder.catalogTime.setText(comicChapterCatalog.subtitle);
        viewHolder.catalogName.setText(comicChapterCatalog.chapter_title);
        if (comicChapterCatalog.is_preview == 0) {
            viewHolder.catalogVipLayout.setVisibility(View.GONE);
        } else {
            viewHolder.catalogVipLayout.setVisibility(View.VISIBLE);
        }
        if (comicChapterCatalog.isRead() || comicChapterCatalog.chapter_id == currentChapterId) {
            viewHolder.catalogReadLayout.setVisibility(View.VISIBLE);
        } else {
            viewHolder.catalogReadLayout.setVisibility(View.GONE);
        }
        if (comicChapterCatalog.chapter_id == currentChapterId) {
            viewHolder.markImage.setVisibility(View.VISIBLE);
        } else {
            viewHolder.markImage.setVisibility(View.GONE);
        }

        viewHolder.catalogLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long ClickTimeNew = System.currentTimeMillis();
                if (ClickTimeNew - ClickTime > AgainTime) {
                    ClickTime = ClickTimeNew;
                    if (comicChapterCatalog != null) {
                        if (baseComic != null) {
                            baseComic.current_chapter_id = comicChapterCatalog.chapter_id;
                            baseComic.setCurrent_display_order(comicChapterCatalog.display_order);
                            // 防止下载数被修改
                            Comic comic = ObjectBoxUtils.getComic(baseComic.comic_id);
                            if (comic != null) {
                                baseComic.down_chapters = comic.down_chapters;
                            }
                            ObjectBoxUtils.addData(baseComic, Comic.class);
                            Intent intent = new Intent(activity, ComicLookActivity.class);
                            intent.putExtra("baseComic", baseComic);
                            activity.startActivity(intent);
                        } else {
                            EventBus.getDefault().post(new OpenComicChapter(comicChapterCatalog.chapter_id));
                            activity.finish();
                        }
                    }
                }
            }
        });
    }

    @Override
    public ViewHolder onCreateHolder() {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_comic_chapter_catalog, null);
        return new ViewHolder(view);
    }

    public class ViewHolder extends BaseRecViewHolder {

        @BindView(R.id.item_comic_chapter_catalog_layout)
        LinearLayout catalogLayout;
        @BindView(R.id.item_comic_chapter_catalog_img)
        ImageView catalogImage;
        @BindView(R.id.item_comic_chapter_catalog_current_mark)
        ImageView markImage;
        @BindView(R.id.item_comic_chapter_catalog_name)
        TextView catalogName;
        @BindView(R.id.item_comic_chapter_catalog_time)
        TextView catalogTime;

        @BindView(R.id.item_comic_chapter_catalog_cover_layout)
        ShadowLayout catalogCoverLayout;
        @BindView(R.id.item_comic_chapter_catalog_buy_layout)
        RelativeLayout catalogVipLayout;
        @BindView(R.id.item_comic_chapter_catalog_read_layout)
        View catalogReadLayout;

        public ViewHolder(View view) {
            super(view);
        }
    }
}
