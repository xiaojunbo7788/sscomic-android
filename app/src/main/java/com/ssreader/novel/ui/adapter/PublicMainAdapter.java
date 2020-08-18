package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.eventbus.DiscoverExpierTimeEnd;
import com.ssreader.novel.model.BaseBookComic;
import com.ssreader.novel.model.BaseLabelBean;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.activity.BaseOptionActivity;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.view.CountDownView;
import com.ssreader.novel.utils.ScreenSizeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

import static com.ssreader.novel.constant.Constant.AUDIO_CONSTANT;
import static com.ssreader.novel.constant.Constant.BOOK_CONSTANT;
import static com.ssreader.novel.constant.Constant.COMIC_CONSTANT;
import static com.ssreader.novel.constant.Constant.LOOKMORE;

/**
 * 推荐位模块适配器
 */
public class PublicMainAdapter extends BaseRecAdapter<BaseLabelBean, PublicMainAdapter.ViewHolder> {

    private int productType;
    private boolean isChange, isMore;
    private int minSize = 0;
    private long bookId;

    public PublicMainAdapter(List<BaseLabelBean> list, int productType, Activity context, boolean isChange, boolean isMore) {
        super(list, context);
        this.productType = productType;
        this.isChange = isChange;
        this.isMore = isMore;
    }

    public PublicMainAdapter(List<BaseLabelBean> list, int productType, Activity context) {
        super(list, context);
        this.productType = productType;
    }

    @Override
    public void onHolder(ViewHolder viewHolder, BaseLabelBean baseLabelBean, int position) {
        ViewGroup.LayoutParams mallParams = viewHolder.itemBook_stoare_lable_image.getLayoutParams();
        if (productType == COMIC_CONSTANT) {
            mallParams.width = mallParams.height = ImageUtil.dp2px(activity, 20);
            viewHolder.itemBook_stoare_lable_image.setImageResource(R.mipmap.comic_mall_title_hold);
        } else {
            mallParams.width = mallParams.height = ImageUtil.dp2px(activity, 16);
        }
        viewHolder.itemBook_stoare_lable_image.setLayoutParams(mallParams);
        ViewGroup.LayoutParams mItemBookStoareRcyBottom = viewHolder.mItemBookStoareRcyBottom.getLayoutParams();
        mItemBookStoareRcyBottom.width = ScreenSizeUtils.getInstance(activity).getScreenWidth() - ImageUtil.dp2px(activity, 20);
        viewHolder.mItemBookStoareRcyBottom.setLayoutParams(mItemBookStoareRcyBottom);

        if (baseLabelBean.ad_type == 0) {
            viewHolder.itemBook_stoare_lable_layout.setVisibility(View.VISIBLE);
            viewHolder.list_ad_view_layout.setVisibility(View.GONE);
            viewHolder.mItemBookStoareLableTitle.setText(baseLabelBean.getLabel());
            // 倒计时
            if (baseLabelBean.getExpire_time() > 0) {
                viewHolder.mItemBookStroareTitleTime.setVisibility(View.VISIBLE);
                viewHolder.mItemBookStroareTitleTime.start(baseLabelBean.getExpire_time());
                viewHolder.mItemBookStroareTitleTime.setCountDownListener(new CountDownView.OnCountDownListener() {
                    @Override
                    public void onEnd() {
                        EventBus.getDefault().post(new DiscoverExpierTimeEnd());
                        viewHolder.mItemBookStroareTitleTime.cancel();
                        viewHolder.mItemBookStroareTitleTime.setVisibility(View.GONE);
                    }
                });
            } else {
                viewHolder.mItemBookStroareTitleTime.setVisibility(View.GONE);
            }

            if (baseLabelBean.getList() != null && !baseLabelBean.getList().isEmpty()) {
                if (productType == BOOK_CONSTANT || productType == AUDIO_CONSTANT) {
                    HuanyihuanBook(activity, baseLabelBean.getStyle(), viewHolder.mItemBookStoareRcy, viewHolder.mItemBookStoareRcyBottom, baseLabelBean);
                } else if (productType == COMIC_CONSTANT) {
                    HuanyihuanComic(activity, baseLabelBean.getStyle(), viewHolder.mItemBookStoareRcy, viewHolder.mItemBookStoareRcyBottom, baseLabelBean);
                }
            }

            if (!isChange) {
                viewHolder.mItemBookStoreTopMoreLayout.setVisibility(View.GONE);
                viewHolder.mItemBookStoreTopChangeLayout.setVisibility(View.GONE);
                viewHolder.item_book_stoare_more_huanyihuan_layout.setVisibility(View.VISIBLE);
                if (baseLabelBean.isCan_more() && baseLabelBean.isCan_refresh()) {
                    viewHolder.mItemBookStoareMoreLayout.setVisibility(View.VISIBLE);
                    viewHolder.mItemBookStoareHuanYiHuanLayout.setVisibility(View.VISIBLE);
                } else {
                    if (baseLabelBean.isCan_more()) {
                        viewHolder.mItemBookStoareMoreLayout.setVisibility(View.VISIBLE);
                        viewHolder.mItemBookStoareHuanYiHuanLayout.setVisibility(View.GONE);
                    } else if (baseLabelBean.isCan_refresh()) {
                        viewHolder.mItemBookStoareHuanYiHuanLayout.setVisibility(View.VISIBLE);
                        viewHolder.mItemBookStoareMoreLayout.setVisibility(View.GONE);
                    }else {
                        viewHolder.item_book_stoare_more_huanyihuan_layout.setVisibility(View.GONE);
                    }
                    // 宽度为屏幕的一半
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.item_book_stoare_more_huanyihuan_layout.getLayoutParams();
                    params.width = ScreenSizeUtils.getInstance(activity).getScreenWidth() / 2 +
                            ImageUtil.dp2px(activity, 40);
                    viewHolder.item_book_stoare_more_huanyihuan_layout.setLayoutParams(params);
                }
                viewHolder.mItemBookStoareMoreLayout.setBackground(MyShape.setMyshapeMineStroke(activity, 20, 11));
                viewHolder.mItemBookStoareHuanYiHuanLayout.setBackground(MyShape.setMyshapeMineStroke(activity, 20, 11));
                // 更多
                viewHolder.mItemBookStoareMoreLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(activity, BaseOptionActivity.class)
                                .putExtra("OPTION", LOOKMORE)
                                .putExtra("productType", productType)
                                .putExtra("recommend_id", baseLabelBean.getRecommend_id() + "");
                        activity.startActivity(intent);
                    }
                });
                // 换一换
                viewHolder.mItemBookStoareHuanYiHuanLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Animation anim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        anim.setDuration(800);
                        viewHolder.mItemBookStoareHuanYiHuanImageView.startAnimation(anim);
                        ReaderParams readerParams = new ReaderParams(activity);
                        readerParams.putExtraParams("recommend_id", list.get(position).getRecommend_id());
                        String httpUrl = "";
                        if (productType == BOOK_CONSTANT) {
                            httpUrl = Api.book_refresh;
                        } else if (productType == COMIC_CONSTANT) {
                            httpUrl = Api.COMIC_home_refresh;
                        } else if (productType == AUDIO_CONSTANT) {
                            httpUrl = Api.AUDIO_REFRESH;
                        }
                        HttpUtils.getInstance().sendRequestRequestParams(activity,httpUrl, readerParams.generateParamsJson(), new HttpUtils.ResponseListener() {
                            @Override
                            public void onResponse(String response) {
                                Huanyihuan(activity, response, baseLabelBean.getStyle(), productType, viewHolder.mItemBookStoareRcy, viewHolder.mItemBookStoareRcyBottom);
                            }

                            @Override
                            public void onErrorResponse(String ex) {
                            }
                        });
                    }
                });
            } else {
                viewHolder.item_book_stoare_more_huanyihuan_layout.setVisibility(View.GONE);
                if (isMore) {
                    viewHolder.mItemBookStoreTopChangeLayout.setVisibility(View.GONE);
                    if (baseLabelBean.isCan_more()) {
                        viewHolder.mItemBookStoreTopMoreLayout.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.mItemBookStoreTopMoreLayout.setVisibility(View.GONE);
                    }
                    // 更多
                    viewHolder.mItemBookStoreTopMoreLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(activity, BaseOptionActivity.class)
                                    .putExtra("OPTION", LOOKMORE)
                                    .putExtra("productType", productType)
                                    .putExtra("recommend_id", baseLabelBean.getRecommend_id() + "");
                            activity.startActivity(intent);
                        }
                    });
                } else {
                    viewHolder.mItemBookStoreTopMoreLayout.setVisibility(View.GONE);
                    if (baseLabelBean.isCan_refresh()) {
                        viewHolder.mItemBookStoreTopChangeLayout.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.mItemBookStoreTopChangeLayout.setVisibility(View.GONE);
                    }
                    // 刷新
                    viewHolder.mItemBookStoreTopChangeLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Animation anim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                            anim.setDuration(800);
                            viewHolder.mItemBookStoreTopChangeImg.startAnimation(anim);
                            if (bookId == 0) {
                                return;
                            }
                            ReaderParams readerParams = new ReaderParams(activity);
                            readerParams.putExtraParams("book_id", bookId);
                            HttpUtils.getInstance().sendRequestRequestParams(activity,Api.BOOK_GUESS_LIKE, readerParams.generateParamsJson(), new HttpUtils.ResponseListener() {
                                @Override
                                public void onResponse(String response) {
                                    Huanyihuan(activity, response, baseLabelBean.getStyle(), productType, viewHolder.mItemBookStoareRcy, viewHolder.mItemBookStoareRcyBottom);
                                }

                                @Override
                                public void onErrorResponse(String ex) {
                                }
                            });
                        }
                    });
                }
            }
        } else {
            viewHolder.itemBook_stoare_lable_layout.setVisibility(View.GONE);
            viewHolder.list_ad_view_layout.setVisibility(View.VISIBLE);
            baseLabelBean.setAd(activity, viewHolder.list_ad_view_layout, 1);
        }
    }

    public void Huanyihuan(Activity activity, String response, int style, int productType, RecyclerView gridViewForScrollView, RecyclerView bottomGrid) {
        if (productType == BOOK_CONSTANT || productType == AUDIO_CONSTANT) {
            BaseLabelBean baseLabelBean = HttpUtils.getGson().fromJson(response, BaseLabelBean.class);
            HuanyihuanBook(activity, style, gridViewForScrollView, bottomGrid, baseLabelBean);
        } else {
            BaseLabelBean baseLabelBean = HttpUtils.getGson().fromJson(response, BaseLabelBean.class);
            HuanyihuanComic(activity, style, gridViewForScrollView, bottomGrid, baseLabelBean);
        }
    }

    @Override
    public ViewHolder onCreateHolder() {
        return new ViewHolder(getViewByRes(R.layout.item_main_stoare));
    }

    /**
     * 设置bookId
     *
     * @param bookId
     */
    public void setBookEndBookId(long bookId) {
        this.bookId = bookId;
    }

    public class ViewHolder extends BaseRecViewHolder {

        @BindView(R.id.itemBook_stoare_lable_title)
        TextView mItemBookStoareLableTitle;
        @BindView(R.id.item_Book_stoare_rcy)
        RecyclerView mItemBookStoareRcy;

        @BindView(R.id.item_Book_store_top_change_layout)
        LinearLayout mItemBookStoreTopChangeLayout;
        @BindView(R.id.item_Book_store_top_change_img)
        ImageView mItemBookStoreTopChangeImg;
        @BindView(R.id.item_Book_stoare_top_more_layout)
        LinearLayout mItemBookStoreTopMoreLayout;
        @BindView(R.id.item_Book_stoare_more_layout)
        LinearLayout mItemBookStoareMoreLayout;
        @BindView(R.id.item_Book_stoare_huan_yi_huan_layout)
        LinearLayout mItemBookStoareHuanYiHuanLayout;
        @BindView(R.id.item_Book_stoare_huan_yi_huan_imageView)
        ImageView mItemBookStoareHuanYiHuanImageView;

        @BindView(R.id.itemBook_stoare_lable_image)
        ImageView itemBook_stoare_lable_image;
        @BindView(R.id.itemBook_stoare_lable_title_time)
        CountDownView mItemBookStroareTitleTime;
        @BindView(R.id.item_Book_stoare_rcy_bottom)
        RecyclerView mItemBookStoareRcyBottom;
        @BindView(R.id.itemBook_stoare_lable_layout)
        View itemBook_stoare_lable_layout;
        @BindView(R.id.list_ad_view_layout)
        FrameLayout list_ad_view_layout;
        @BindView(R.id.item_book_stoare_more_huanyihuan_layout)
        LinearLayout item_book_stoare_more_huanyihuan_layout;

        ViewHolder(View view) {
            super(view);
        }
    }

    private void HuanyihuanComic(Activity activity, int style, RecyclerView gridViewForScrollView, RecyclerView bottomGrid, BaseLabelBean baseLabelBean) {
        int minSize;
        List<BaseBookComic> baseBookComics = baseLabelBean.getList();
        GridLayoutManager gridLayoutManager = null;
        LinearLayoutManager linearLayoutManager = null;
        PublicStoreListAdapter publicStoreListAdapter = null;
        int size = baseBookComics.size();
        if (style == 1 || style == 2) {
            bottomGrid.setVisibility(View.GONE);
            if (style == 1) {
                gridLayoutManager = new GridLayoutManager(activity, 2);
                minSize = Math.min(size, 4);
                List<BaseBookComic> BookComics = baseBookComics.subList(0, minSize);
                publicStoreListAdapter = new PublicStoreListAdapter(activity, 3, BookComics);
            } else {
                gridLayoutManager = new GridLayoutManager(activity, 3);
                minSize = Math.min(size, 6);
                List<BaseBookComic> BookComics = baseBookComics.subList(0, minSize);
                publicStoreListAdapter = new PublicStoreListAdapter(activity, 1, BookComics);
            }
            gridViewForScrollView.setLayoutManager(gridLayoutManager);
            gridViewForScrollView.setAdapter(publicStoreListAdapter);

        } else if (style == 3) {
            bottomGrid.setVisibility(View.VISIBLE);
            minSize = Math.min(size, 4);
            bottomGrid.setVisibility(View.VISIBLE);
            if (minSize > 0) {
                linearLayoutManager = new LinearLayoutManager(activity);
                List<BaseBookComic> BookComics = baseBookComics.subList(0, 1);
                publicStoreListAdapter = new PublicStoreListAdapter(activity, 2, BookComics);
                gridViewForScrollView.setLayoutManager(linearLayoutManager);
                gridViewForScrollView.setAdapter(publicStoreListAdapter);
            }
            if (minSize > 1) {
                gridLayoutManager = new GridLayoutManager(activity, 3);
                List<BaseBookComic> BookComics = baseBookComics.subList(1, minSize);
                publicStoreListAdapter = new PublicStoreListAdapter(activity, 1, BookComics);
                bottomGrid.setLayoutManager(gridLayoutManager);
                bottomGrid.setAdapter(publicStoreListAdapter);
            }
        }
    }

    public void HuanyihuanBook(Activity activity, int style, RecyclerView rcyForScrollView, RecyclerView bottomGrid, BaseLabelBean baseLabelBean) {
        List<BaseBookComic> baseBookComics = baseLabelBean.getList();
        GridLayoutManager gridLayoutManager = null;
        LinearLayoutManager linearLayoutManager = null;
        PublicStoreListAdapter publicStoreListAdapter = null;
        int size = baseBookComics.size();
        if (style == 1 || style == 2) {
            if (style == 1) {
                minSize = Math.min(size, 3);
            } else {
                minSize = Math.min(size, 6);
            }
            bottomGrid.setVisibility(View.GONE);
            gridLayoutManager = new GridLayoutManager(activity, 3);
            List<BaseBookComic> BookComics = baseBookComics.subList(0, minSize);
            publicStoreListAdapter = new PublicStoreListAdapter(activity, 1, BookComics);
            rcyForScrollView.setLayoutManager(gridLayoutManager);
            rcyForScrollView.setAdapter(publicStoreListAdapter);
        } else if (style == 3) {
            minSize = Math.min(size, 3);
            // 三横
            List<BaseBookComic> BookComics = baseLabelBean.getList().subList(0, minSize);
            gridLayoutManager = new GridLayoutManager(activity, 3);
            publicStoreListAdapter = new PublicStoreListAdapter(activity, 1, BookComics);
            rcyForScrollView.setLayoutManager(gridLayoutManager);
            rcyForScrollView.setAdapter(publicStoreListAdapter);
            if (size > 3) {
                bottomGrid.setVisibility(View.VISIBLE);
                //三竖
                List<BaseBookComic> BookComics1 = baseLabelBean.getList().subList(3, Math.min(size, 6));
                linearLayoutManager = new LinearLayoutManager(activity);
                publicStoreListAdapter = new PublicStoreListAdapter(activity, 4, BookComics1);
                publicStoreListAdapter.NoLinePosition = BookComics1.size()-1;
                bottomGrid.setLayoutManager(linearLayoutManager);
                bottomGrid.setAdapter(publicStoreListAdapter);
            } else {
                bottomGrid.setVisibility(View.GONE);
            }
        } else if (style == 4) {
            minSize = Math.min(size, 4);
            bottomGrid.setVisibility(View.VISIBLE);
            if (minSize > 0) {
                linearLayoutManager = new LinearLayoutManager(activity);
                List<BaseBookComic> BookComics = baseBookComics.subList(0, 1);
                rcyForScrollView.setLayoutManager(linearLayoutManager);
                publicStoreListAdapter = new PublicStoreListAdapter(activity, 4, BookComics);
                publicStoreListAdapter.NoLinePosition = 0;
                rcyForScrollView.setAdapter(publicStoreListAdapter);
            }
            if (minSize > 1) {
                gridLayoutManager = new GridLayoutManager(activity, 3);
                List<BaseBookComic> BookComics = baseBookComics.subList(1, minSize);
                publicStoreListAdapter = new PublicStoreListAdapter(activity, 1, BookComics);
                bottomGrid.setLayoutManager(gridLayoutManager);
                bottomGrid.setAdapter(publicStoreListAdapter);
            }
        } else if (style == 5) {
            //横向展示2个小图
            gridLayoutManager = new GridLayoutManager(activity, 2);
            minSize = Math.min(size, 10);//
            List<BaseBookComic> firstList = baseBookComics.subList(0, minSize);
            publicStoreListAdapter = new PublicStoreListAdapter(activity, 1, firstList);
            rcyForScrollView.setLayoutManager(gridLayoutManager);
            rcyForScrollView.setAdapter(publicStoreListAdapter);
        }
    }
}
