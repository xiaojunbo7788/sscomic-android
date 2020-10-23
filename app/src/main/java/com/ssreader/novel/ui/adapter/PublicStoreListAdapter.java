package com.ssreader.novel.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lihang.ShadowLayout;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.model.BaseBookComic;
import com.ssreader.novel.model.BaseTag;
import com.ssreader.novel.ui.activity.AudioInfoActivity;
import com.ssreader.novel.ui.activity.BookInfoActivity;
import com.ssreader.novel.ui.activity.ComicInfoActivity;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.utils.ScreenSizeUtils;

import java.util.List;

import butterknife.BindView;

import static com.ssreader.novel.constant.Constant.AgainTime;

public class PublicStoreListAdapter extends BaseRecAdapter<BaseBookComic, PublicStoreListAdapter.ViewHolder> {

    private int imgWidth, W10;
    private int imgHeight;
    private int style, typeRank;
    private boolean isMargin;
    private long ClickTime;

    public PublicStoreListAdapter(Activity activity, int style, List<BaseBookComic> list, boolean isMargin, int typeRank) {
        this(activity, style, list);
        this.isMargin = isMargin;
        this.typeRank = typeRank;
    }

    public PublicStoreListAdapter(Activity activity, List<BaseBookComic> list, SCOnItemClickListener scOnItemClickListener) {
        this(activity, 1, list);
        BasescOnItemClickListener = scOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public PublicStoreListAdapter(Activity activity, int style, List<BaseBookComic> list) {
        super(list, activity);
        this.style = style;
        int screenWidth = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        float width, height;
        switch (style) {
            case 1: // 三个一行
                W10 = ImageUtil.dp2px(activity, 10);
                width = (screenWidth - ImageUtil.dp2px(activity, 40)) / 3;
                height = width * 4 / 3;
                break;
            case 2: // 一个单独一行
                width = screenWidth - ImageUtil.dp2px(activity, 20);
                height = width * 5 / 9;
                break;
            case 3: // 两个一行
                width = (screenWidth - ImageUtil.dp2px(activity, 30)) / 2;
                height = width * 2 / 3;
                break;
            case 4: // 横向列表
                width = (ScreenSizeUtils.getInstance(activity).getScreenWidth() - ImageUtil.dp2px(activity, 70)) / 3;
                height = width * 4 / 3;
                break;
            default://竖向列表
                width = (screenWidth - ImageUtil.dp2px(activity, 30)) / 3;
                height = width * 4 / 3;
                break;
        }
        imgWidth = (int) width;
        imgHeight = (int) height;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onHolder(ViewHolder viewHolder, BaseBookComic baseBookComic, int position) {
        if (style == 4) {
            viewHolder.public_list_line_id.setVisibility((baseBookComic.ad_type != 0 || position == NoLinePosition) ? View.GONE : View.VISIBLE);
            if (baseBookComic.ad_type == 0) {
                viewHolder.mListAdViewLayout.setVisibility(View.GONE);
                viewHolder.itemStoreLayout.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHolder.itemStoreLayout.getLayoutParams();
                if (isMargin){
                    layoutParams.setMargins(ImageUtil.dp2px(activity, 10), 0, ImageUtil.dp2px(activity, 10), 0);
                }
                viewHolder.itemStoreLayout.setLayoutParams(layoutParams);
                ViewGroup.LayoutParams layoutParamsimageView = viewHolder.itemStoreImg.getLayoutParams();
                layoutParamsimageView.width = imgWidth;
                layoutParamsimageView.height = imgHeight;
                viewHolder.itemStoreImg.setLayoutParams(layoutParamsimageView);

                if (baseBookComic.audio_id != 0) {
                    viewHolder.itemStorePlayer.setVisibility(View.VISIBLE);
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewHolder.itemStorePlayer.getLayoutParams();
                    params.height = params.width = (int) (imgHeight * 0.1);
                    params.leftMargin = params.bottomMargin = (int) (imgHeight * 0.05);
                    viewHolder.itemStorePlayer.setLayoutParams(params);
                } else {
                    viewHolder.itemStorePlayer.setVisibility(View.GONE);
                }
                viewHolder.name.setText(baseBookComic.name);
                viewHolder.description.setText(baseBookComic.description);
                if (baseBookComic.author != null) {
                    viewHolder.author.setText(baseBookComic.author.replaceAll(",", " "));
                }

                String str = "";
                if (baseBookComic.tag != null) {
                    for (BaseTag tag : baseBookComic.tag) {
                        str += "&nbsp&nbsp<font color='" + tag.getColor() + "'>" + tag.getTab() + "</font>";
                    }
                    viewHolder.itemStoreLabelMaleHorizontalTag.setText(Html.fromHtml(str));
                }

                MyGlide.GlideImageNoSize(activity, baseBookComic.cover, viewHolder.itemStoreImg);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long ClickTimeNew = System.currentTimeMillis();
                        if (ClickTimeNew - ClickTime > AgainTime) {
                            ClickTime = ClickTimeNew;
                            Intent intent = new Intent();
                            if (baseBookComic.book_id != 0) {
                                intent.setClass(activity, BookInfoActivity.class);
                                intent.putExtra("book_id", baseBookComic.book_id);
                            } else if (baseBookComic.comic_id != 0) {
                                intent.setClass(activity, ComicInfoActivity.class);
                                intent.putExtra("comic_id", baseBookComic.comic_id);
                            } else if (baseBookComic.audio_id != 0) {
                                intent.setClass(activity, AudioInfoActivity.class);
                                intent.putExtra("audio_id", baseBookComic.audio_id);
                            }
                            activity.startActivity(intent);
                            if (BasescOnItemClickListener != null) {
                                BasescOnItemClickListener.OnItemClickListener(0, 0, null);
                            }
                        }
                    }
                });
                if (typeRank == Constant.PAIHANG) {
                    viewHolder.item_store_rank_layout.setVisibility(View.VISIBLE);
                    viewHolder.item_store_rank_layout.setBackgroundResource(baseBookComic.display_no == 1 ?
                            R.mipmap.img_rank_one : (baseBookComic.display_no == 2 ?
                            R.mipmap.img_rank_two : (baseBookComic.display_no == 3 ?
                            R.mipmap.img_rank_three : R.mipmap.img_rank_default)));
                    viewHolder.item_store_rank_title.setText(baseBookComic.display_no + "");
                    if (baseBookComic.display_no > 20) {
                        viewHolder.item_store_rank_layout.setVisibility(View.GONE);
                    } else {
                        viewHolder.item_store_rank_layout.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                viewHolder.itemStoreLayout.setVisibility(View.GONE);
                viewHolder.mListAdViewLayout.setVisibility(View.VISIBLE);
                baseBookComic.setAd(activity, viewHolder.mListAdViewLayout, 1);
            }
        } else {
            ViewGroup.LayoutParams layoutParams = viewHolder.itemStoreImg.getLayoutParams();
            layoutParams.width = imgWidth;
            layoutParams.height = imgHeight;
            viewHolder.itemStoreImg.setLayoutParams(layoutParams);
            if (baseBookComic.audio_id != 0) {
                viewHolder.itemStorePlayer.setVisibility(View.VISIBLE);
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewHolder.itemStorePlayer.getLayoutParams();
                params.height = params.width = (int) (imgHeight * 0.1);
                params.leftMargin = params.bottomMargin = (int) (imgHeight * 0.05);
                viewHolder.itemStorePlayer.setLayoutParams(params);
            } else {
                viewHolder.itemStorePlayer.setVisibility(View.GONE);
            }
            viewHolder.name.setText(baseBookComic.name);
            if (baseBookComic.tag != null) {
                if (!baseBookComic.tag.isEmpty()) {
                    StringBuffer str = new StringBuffer();
                    for (BaseTag baseTag : baseBookComic.tag) {
                        str.append(baseTag.getTab() + " ");
                    }
                    viewHolder.itemStoreText2.setText(str);
                }
            } else {
                if (baseBookComic.author != null) {
                    viewHolder.itemStoreText2.setText(baseBookComic.author.replaceAll(",", " "));
                }
            }
            if (baseBookComic.book_id != 0 || baseBookComic.audio_id != 0) {
                MyGlide.GlideImageNoSize(activity, baseBookComic.cover, viewHolder.itemStoreImg);
                viewHolder.itemStoreImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        long ClickTimeNew = System.currentTimeMillis();
                        if (ClickTimeNew - ClickTime > AgainTime) {
                            ClickTime = ClickTimeNew;
                            if (baseBookComic.book_id != 0) {
                                Intent intent = new Intent(activity, BookInfoActivity.class);
                                intent.putExtra("book_id", baseBookComic.book_id);
                                activity.startActivity(intent);
                            } else {
                                Intent intent = new Intent(activity, AudioInfoActivity.class);
                                intent.putExtra("audio_id", baseBookComic.audio_id);
                                activity.startActivity(intent);
                            }
                        }
                    }
                });
            } else {
                if (style == 2 || style == 3) {
                    if (!TextUtils.isEmpty(baseBookComic.description)) {
                        viewHolder.itemStoreText2.setText(baseBookComic.description);
                    }
                }
                viewHolder.itemStoreText2.setPadding(0, 0, W10, 0);
                if (!TextUtils.isEmpty(baseBookComic.flag) && style != 2) {
                    ViewGroup.LayoutParams comicFlagLayout = viewHolder.item_store_label_comic_flag_layout.getLayoutParams();
                    comicFlagLayout.width = imgWidth;
                    viewHolder.item_store_label_comic_flag_layout.setLayoutParams(comicFlagLayout);
                    viewHolder.item_store_label_comic_flag_layout.setVisibility(View.VISIBLE);
                    viewHolder.item_store_label_comic_flag_text.setText(baseBookComic.flag);
                }
                if ((style == 2 || style == 3) && baseBookComic.horizontal_cover != null) {
                    MyGlide.GlideImageNoSize(activity, baseBookComic.horizontal_cover, viewHolder.itemStoreImg, R.mipmap.icon_comic_def_h);
                } else if (style == 1) {
                    MyGlide.GlideImageNoSize(activity, baseBookComic.vertical_cover, viewHolder.itemStoreImg);
                }
                viewHolder.itemStoreLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        long ClickTimeNew = System.currentTimeMillis();
                        if (ClickTimeNew - ClickTime > AgainTime) {
                            ClickTime = ClickTimeNew;
                            Intent intent = new Intent(activity, ComicInfoActivity.class);
                            intent.putExtra("comic_id", baseBookComic.comic_id);
                            activity.startActivity(intent);
                        }
                    }
                });
            }
        }
    }

    @Override
    public ViewHolder onCreateHolder() {
        if (style == 4) {
            return new ViewHolder(getViewByRes(R.layout.item_store_horizontal, true));
        } else {
            return new ViewHolder(getViewByRes(R.layout.item_store_vertical, true));
        }
    }

    public class ViewHolder extends BaseRecViewHolder {

        @BindView(R.id.item_store_img)
        ImageView itemStoreImg;
        @BindView(R.id.item_store_player)
        ImageView itemStorePlayer;
        @BindView(R.id.item_store_name)
        TextView name;
        @BindView(R.id.item_store_text2)
        TextView itemStoreText2;
        @BindView(R.id.LinearLayout)
        LinearLayout LinearLayout;
        @BindView(R.id.item_store_label_male_horizontal_description)
        TextView description;
        @BindView(R.id.item_store_label_male_horizontal_author)
        TextView author;
        @BindView(R.id.item_store_label_male_horizontal_tag)
        TextView itemStoreLabelMaleHorizontalTag;
        @BindView(R.id.item_store_layout)
        LinearLayout itemStoreLayout;
        @BindView(R.id.item_layout)
        LinearLayout itemLayout;

        @BindView(R.id.item_store_label_comic_flag_layout)
        FrameLayout item_store_label_comic_flag_layout;
        @BindView(R.id.item_store_label_comic_flag_text)
        TextView item_store_label_comic_flag_text;

        @BindView(R.id.public_list_line_id)
        View public_list_line_id;

        @BindView(R.id.list_ad_view_img)
        ImageView mListAdViewImg;
        @BindView(R.id.list_ad_view_layout)
        FrameLayout mListAdViewLayout;
        @BindView(R.id.item_store_rank_layout)
        RelativeLayout item_store_rank_layout;
        @BindView(R.id.item_store_rank_title)
        TextView item_store_rank_title;

        ViewHolder(View view) {
            super(view);
        }
    }
}
