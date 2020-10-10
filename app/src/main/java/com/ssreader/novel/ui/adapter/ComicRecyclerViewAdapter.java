package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ssreader.novel.R;
import com.ssreader.novel.utils.FileManager;
import com.ssreader.novel.utils.cache.BitmapCache;
import com.ssreader.novel.model.BaseComicImage;
import com.ssreader.novel.model.ComicBitmapResources;
import com.ssreader.novel.ui.activity.ComicLookActivity;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import static com.ssreader.novel.constant.Constant.getMAXheigth;
import static com.ssreader.novel.utils.FileManager.getLocalComicImageFile;

/**
 * 漫画阅读器的适配器
 */
public class ComicRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int WIDTH, topHeight;
    private int MAXheigth;
    // bitmap缓存
    private BitmapCache bitmapCache;

    private List<BaseComicImage> list;
    private Activity activity;
    private View footView;
    private ComicLookActivity.ItemOnclick itemOnclick;

    private boolean isV = false;

    public void setV(boolean v) {
        isV = v;
    }

    public ComicRecyclerViewAdapter(Activity activity, int WIDTH, int topHeight, List<BaseComicImage> list,
                                    View footView, ComicLookActivity.ItemOnclick itemOnclick) {
        this.list = list;
        this.activity = activity;
        this.footView = footView;
        this.itemOnclick = itemOnclick;
        this.WIDTH = WIDTH;
        this.topHeight = topHeight;
        MAXheigth = getMAXheigth();
        bitmapCache = BitmapCache.getInstance();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size()) {
            return 888;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 888) {
            return new MyViewHolderFoot(footView);
        }
        View rootView = LayoutInflater.from(activity).inflate(R.layout.item_comic_recyclerview_, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof MyViewHolder) {
            MyViewHolder holder = (MyViewHolder) viewHolder;
            final BaseComicImage baseComicImage = list.get(position);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.frameLayout.getLayoutParams();
            if (baseComicImage.width != 0) {
                layoutParams.height = WIDTH * baseComicImage.height / baseComicImage.width;//默认
            } else {
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;//默认
            }
            if (position == 0) {
                layoutParams.topMargin = topHeight;
            } else {
                layoutParams.topMargin = 0;
            }
            layoutParams.width = WIDTH;//默认

            if (isV) {
                NestedScrollView.LayoutParams lp = (NestedScrollView.LayoutParams)holder.contentView.getLayoutParams();
                lp.gravity = Gravity.CENTER;
                holder.contentView.setLayoutParams(lp);
            }

            holder.frameLayout.setLayoutParams(layoutParams);



            holder.frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemOnclick.onClick(position, baseComicImage);
                }
            });

            // 加载图片
            if (layoutParams.height <= MAXheigth) {
                holder.comicImage.get(1).setVisibility(View.GONE);
                MyGlide.GlideNewImageNew(activity, layoutParams.width, layoutParams.height, baseComicImage,
                        holder.comicImage.get(0), baseComicImage.getImage());

            } else {
                holder.comicImage.get(1).setVisibility(View.VISIBLE);
                if (bitmapCache != null && bitmapCache.getBitmapFromCache(baseComicImage.image_id) != null) {
                    List<ComicBitmapResources> pieces = ImageUtil.split(bitmapCache.getBitmapFromCache(baseComicImage.image_id));
                    setBitmapIntoImage(pieces, holder.comicImage);
                } else {
                    File localPathFile = getLocalComicImageFile(baseComicImage);
                    if (localPathFile != null && localPathFile.exists()) {
                        byte[] bytes = FileManager.readFile(localPathFile.getAbsolutePath());
                        Glide.with(activity).asBitmap().error(R.mipmap.pic_default).load(bytes).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                if (bitmapCache != null) {
                                    bitmapCache.addBitmapToCache(baseComicImage.image_id, resource);
                                }
                                List<ComicBitmapResources> pieces = ImageUtil.split(resource);
                                setBitmapIntoImage(pieces, holder.comicImage);
                            }
                        });
                    } else {
                        Glide.with(activity).asBitmap().error(R.mipmap.pic_default).load(baseComicImage.getImage()).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                if (bitmapCache != null) {
                                    bitmapCache.addBitmapToCache(baseComicImage.image_id, resource);
                                }
                                List<ComicBitmapResources> pieces = ImageUtil.split(resource);
                                setBitmapIntoImage(pieces, holder.comicImage);
                            }
                        });
                    }
                }
            }
        } else if (viewHolder instanceof MyViewHolderFoot) {
            MyViewHolderFoot holder = (MyViewHolderFoot) viewHolder;
            if (list.size() == 0) {
                holder.itemView.setVisibility(View.GONE);
            } else {
                holder.itemView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    /**
     * 内容
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindViews({R.id.item_comic_recyclerview_photoview, R.id.item_comic_recyclerview_photoview2})
        List<ImageView> comicImage;
        @BindView(R.id.item_comic_recyclerview_layout)
        FrameLayout frameLayout;
        @BindView(R.id.item_comic_layout)
        LinearLayout contentView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 足部布局
     */
    public class MyViewHolderFoot extends RecyclerView.ViewHolder {

        @BindView(R.id.comic_look_foot_layout)
        LinearLayout linearLayout;

        public MyViewHolderFoot(@NonNull View itemView) {
            super(itemView);
        }
    }

    /**
     * 拼接大图
     * @param pieces
     * @param comicImage
     */
    private void setBitmapIntoImage(List<ComicBitmapResources> pieces, List<ImageView> comicImage) {
        if (pieces.size() > 0) {
            comicImage.get(0).setImageBitmap(pieces.get(0).getBitmap());
        }
        if (pieces.size() > 1) {
            comicImage.get(1).setImageBitmap(pieces.get(1).getBitmap());
        }
    }
}



