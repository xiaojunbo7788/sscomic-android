package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.eventbus.ToStore;
import com.ssreader.novel.model.Audio;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.model.Comic;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ScreenSizeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.ssreader.novel.constant.Constant.BOOK_CONSTANT;
import static com.ssreader.novel.constant.Constant.COMIC_CONSTANT;
import static com.ssreader.novel.constant.Constant.AUDIO_CONSTANT;

/**
 * 书架适配器
 */
public class ShelfAdapter extends BaseRecAdapter<Object, ShelfAdapter.ViewHolder> {

    public boolean mIsDeletable;
    public List checkedBookList;

    private int productType;
    private int WIDTH, WIDTH0, HEIGHT, HEIGHT0;
    private SCOnItemClickListener scOnItemClickListener;
    private TextView mDeleteBtn;
    private TextView fragment_novel_allchoose;

    public ShelfAdapter(Activity context, List list, int productType, TextView mDeleteBtn, SCOnItemClickListener scOnItemClickListener, TextView fragment_novel_allchoose) {
        super(list, context, 1);
        this.fragment_novel_allchoose = fragment_novel_allchoose;
        this.productType = productType;
        this.mDeleteBtn = mDeleteBtn;
        this.scOnItemClickListener = scOnItemClickListener;
        checkedBookList = new ArrayList<>();
        WIDTH0 = (ScreenSizeUtils.getInstance(context).getScreenWidth() - ImageUtil.dp2px(context, 40)) / 3;
        HEIGHT0 = WIDTH0 * 4 / 3;
        WIDTH = (ScreenSizeUtils.getInstance(context).getScreenWidth() - ImageUtil.dp2px(context, 43)) / 3;
        HEIGHT = WIDTH * 4 / 3;
    }

    public void setDelStatus(boolean status) {
        if (mIsDeletable != status) {
            mIsDeletable = status;
            notifyDataSetChanged();
            EventBus.getDefault().post(new ToStore(-1, status));
            refreshBtn();
        }
    }

    @Override
    public void onHolder(ViewHolder viewHolder, Object bean, int position) {
        ViewGroup.LayoutParams shelfitem_img_container = viewHolder.shelfitem_img_container.getLayoutParams();
        shelfitem_img_container.width = WIDTH0;
        shelfitem_img_container.height = HEIGHT0;
        viewHolder.shelfitem_img_container.setLayoutParams(shelfitem_img_container);
        viewHolder.shelfitem_img_container.setVisibility(View.VISIBLE);
        if (bean != null) {
            viewHolder.listview_item_nover_add_layout.setVisibility(View.GONE);
            viewHolder.shelfitem_img.setVisibility(View.VISIBLE);
            viewHolder.shelfitem_top_newchapter.setVisibility(View.VISIBLE);

            ViewGroup.LayoutParams layoutParams = viewHolder.shelfitem_img.getLayoutParams();
            layoutParams.width = WIDTH;
            layoutParams.height = HEIGHT;
            viewHolder.shelfitem_img.setLayoutParams(layoutParams);

            ViewGroup.LayoutParams shelfitem_check_container = viewHolder.shelfitem_check_container.getLayoutParams();
            shelfitem_check_container.width = WIDTH;
            shelfitem_check_container.height = HEIGHT;
            viewHolder.shelfitem_check_container.setLayoutParams(shelfitem_check_container);

            ViewGroup.LayoutParams recommendMarkParams = viewHolder.recommendMark.getLayoutParams();
            recommendMarkParams.width = WIDTH / 3;
            viewHolder.recommendMark.setLayoutParams(recommendMarkParams);
            viewHolder.recommendMark.setBackground(MyShape.setGradient(Color.parseColor("#DC4833"),
                    Color.parseColor("#FA7234"), 0, 0,
                    ImageUtil.dp2px(activity, 8), 0, 0));

            if (mIsDeletable) {
                viewHolder.shelfitem_check_container.setVisibility(View.VISIBLE);
                viewHolder.shelfitem_check_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!checkedBookList.contains(bean)) {
                            checkedBookList.add(bean);
                            viewHolder.shelfitem_check.setChecked(true);
                            viewHolder.shelfitem_check_container.setBackground(null);
                        } else {
                            checkedBookList.remove(bean);
                            viewHolder.shelfitem_check.setChecked(false);
                            viewHolder.shelfitem_check_container.setBackgroundColor(ContextCompat.getColor(activity, R.color.shelf_bg));
                        }
                        refreshBtn();
                    }
                });
                if (checkedBookList.contains(bean)) {
                    viewHolder.shelfitem_check.setChecked(true);
                    viewHolder.shelfitem_check_container.setBackground(null);
                } else {
                    viewHolder.shelfitem_check.setChecked(false);
                    viewHolder.shelfitem_check_container.setBackgroundColor(ContextCompat.getColor(activity, R.color.shelf_bg));
                }
            } else {
                viewHolder.shelfitem_check_container.setVisibility(View.INVISIBLE);
                viewHolder.shelfitem_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        scOnItemClickListener.OnItemClickListener(1, position, bean);
                    }
                });
                viewHolder.shelfitem_img.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        setDelete(bean, position, productType);
                        return true;
                    }
                });
            }
            viewHolder.shelfitem_title.setVisibility(View.VISIBLE);
            if (productType == BOOK_CONSTANT) {
                if (bean instanceof Book) {
                    Book book = (Book) bean;
                    MyGlide.GlideImageNoSize(activity, book.cover, viewHolder.shelfitem_img);
                    if (!TextUtils.isEmpty(book.getName())) {
                        viewHolder.shelfitem_title.setText(book.getName());
                    }
                    if (book.new_chapter > 0) {
                        viewHolder.shelfitem_top_newchapter.setVisibility(View.VISIBLE);
                        viewHolder.shelfitem_top_newchapter.setText(book.new_chapter + "");
                    } else {
                        viewHolder.shelfitem_top_newchapter.setVisibility(View.GONE);
                    }
                    if (book.isRecommend) {
                        viewHolder.recommendMark.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.recommendMark.setVisibility(View.GONE);
                    }
                }
            } else if (productType == COMIC_CONSTANT) {
                viewHolder.recommendMark.setVisibility(View.GONE);
                if (bean instanceof Comic) {
                    Comic comic = (Comic) bean;
                    MyGlide.GlideImageNoSize(activity, comic.vertical_cover, viewHolder.shelfitem_img);
                    if (comic.new_chapter > 0) {
                        viewHolder.shelfitem_top_newchapter.setVisibility(View.VISIBLE);
                        viewHolder.shelfitem_top_newchapter.setText(comic.new_chapter + "");
                    } else {
                        viewHolder.shelfitem_top_newchapter.setVisibility(View.GONE);
                    }
                    if (!TextUtils.isEmpty(comic.getName())) {
                        viewHolder.shelfitem_title.setText(comic.getName());
                    }
                    if (comic.isRecommend) {
                        viewHolder.recommendMark.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.recommendMark.setVisibility(View.GONE);
                    }
                }
            } else if (productType == AUDIO_CONSTANT) {
                if (bean instanceof Audio) {
                    Audio audio = (Audio) bean;
                    MyGlide.GlideImageNoSize(activity, audio.cover, viewHolder.shelfitem_img);
                    if (!TextUtils.isEmpty(audio.getName())) {
                        viewHolder.shelfitem_title.setText(audio.getName());
                    }
                    if (audio.new_chapter > 0) {
                        viewHolder.shelfitem_top_newchapter.setVisibility(View.VISIBLE);
                        viewHolder.shelfitem_top_newchapter.setText(audio.new_chapter + "");
                    } else {
                        viewHolder.shelfitem_top_newchapter.setVisibility(View.GONE);
                    }
                    if (audio.isRecommend) {
                        viewHolder.recommendMark.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.recommendMark.setVisibility(View.GONE);
                    }
                }
                viewHolder.item_store_lable_play_img.setVisibility(View.VISIBLE);
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewHolder.item_store_lable_play_img.getLayoutParams();
                params.height = params.width = (int) (HEIGHT * 0.1);
                params.leftMargin = params.bottomMargin = (int) (HEIGHT * 0.05);
                viewHolder.item_store_lable_play_img.setLayoutParams(params);
            }
        } else {
            if (!mIsDeletable) {
                viewHolder.recommendMark.setVisibility(View.GONE);
                viewHolder.listview_item_nover_add_layout.setVisibility(View.VISIBLE);
                viewHolder.shelfitem_title.setVisibility(View.INVISIBLE);
                viewHolder.shelfitem_img.setVisibility(View.INVISIBLE);
                viewHolder.shelfitem_top_newchapter.setVisibility(View.INVISIBLE);
                viewHolder.shelfitem_check_container.setVisibility(View.INVISIBLE);
                viewHolder.shelfitem_check_container.setBackgroundColor(ContextCompat.getColor(activity, R.color.shelf_bg));
                ViewGroup.LayoutParams layoutParams = viewHolder.listview_item_nover_add_layout.getLayoutParams();
                layoutParams.width = WIDTH;
                layoutParams.height = HEIGHT;
                viewHolder.listview_item_nover_add_layout.setLayoutParams(layoutParams);

                viewHolder.listview_item_nover_add_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        scOnItemClickListener.OnItemClickListener(2, position, bean);
                    }
                });
            } else {
                viewHolder.shelfitem_img_container.setVisibility(View.GONE);
            }
        }
    }

    public void setDelete(Object bean, int position, int productType) {
        if (this.productType == productType) {
            checkedBookList.clear();
            if (bean != null) {
                checkedBookList.add(bean);
            }
            scOnItemClickListener.OnItemClickListener(-1, position, bean);
            setDelStatus(true);
        }
    }

    @Override
    public ViewHolder onCreateHolder() {
        return new ViewHolder(getViewByRes(R.layout.item_shelf));
    }

    class ViewHolder extends BaseRecViewHolder {

        @BindView(R.id.shelfitem_check)
        public CheckBox shelfitem_check;
        @BindView(R.id.shelfitem_title)
        public TextView shelfitem_title;
        @BindView(R.id.shelfitem_img)
        public ImageView shelfitem_img;

        @BindView(R.id.item_shelf_recommend_mark)
        TextView recommendMark;
        @BindView(R.id.shelfitem_check_container)
        public View shelfitem_check_container;
        @BindView(R.id.shelfitem_top_newchapter)
        public TextView shelfitem_top_newchapter;
        @BindView(R.id.listview_item_nover_add_image)
        public ImageView listview_item_nover_add_image;
        @BindView(R.id.item_store_lable_play_img)
        ImageView item_store_lable_play_img;
        @BindView(R.id.listview_item_nover_add_layout)
        public RelativeLayout listview_item_nover_add_layout;

        @BindView(R.id.shelfitem_img_container)
        public View shelfitem_img_container;

        public ViewHolder(View itemView) {
            super(itemView);
            shelfitem_top_newchapter.setBackground(MyShape.setMyshapeStrokeMyBg(activity, ImageUtil.dp2px(activity, 10),
                    ImageUtil.dp2px(activity, 1), ContextCompat.getColor(activity, R.color.white), ContextCompat.getColor(activity, R.color.red)));
        }
    }


    public void selectAll() {
        boolean selectAll = (checkedBookList.size() == list.size());
        checkedBookList.clear();
        if (!selectAll) {
            checkedBookList.addAll(list);
        }
        notifyDataSetChanged();
        refreshBtn();
    }


    public void refreshBtn() {
        int size = checkedBookList.size();
        if (size == list.size()) {
            fragment_novel_allchoose.setText(LanguageUtil.getString(activity, R.string.app_cancel_select_all));
        } else {
            fragment_novel_allchoose.setText(LanguageUtil.getString(activity, R.string.app_allchoose));
        }
        if (size == 0) {
            mDeleteBtn.setClickable(false);
            mDeleteBtn.setBackgroundColor(ContextCompat.getColor(activity, R.color.graybg));
            mDeleteBtn.setTextColor(ContextCompat.getColor(activity, R.color.gray_b0));
            mDeleteBtn.setText(String.format(LanguageUtil.getString(activity, R.string.app_delete_plural), 0));
        } else {
            mDeleteBtn.setClickable(true);
            mDeleteBtn.setText(String.format(LanguageUtil.getString(activity, R.string.app_delete_plural), size));
            mDeleteBtn.setTextColor(ContextCompat.getColor(activity, R.color.white));
            mDeleteBtn.setBackgroundColor(ContextCompat.getColor(activity, R.color.red));
        }
    }
}
