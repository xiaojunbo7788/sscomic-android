package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseListAdapter;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.model.FeedBackPhotoBean;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.net.UploadUtils.HttpUtil;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.ScreenSizeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedBackPhotoAdapter extends BaseAdapter {

    public int WIDTH;
    private List<FeedBackPhotoBean> list;
    private Activity activity;
    public ImageView imageView;
    public ImageView imageViewadd;

    public FeedBackPhotoAdapter(Activity activity, List list) {
        this.list = list;
        this.activity = activity;
        WIDTH = (ScreenSizeUtils.getInstance(activity).getScreenWidth() - ImageUtil.dp2px(activity, 30)) / 3;
    }

    @Override
    public int getCount() {
        if (list.size() >= 3) {
            return 3;
        } else {
            return list.size() + 1;
        }
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_feed_back_photo, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ViewGroup.LayoutParams layoutParamsIMG = viewHolder.itemSqureShowListLayout.getLayoutParams();
        layoutParamsIMG.width = WIDTH;
        layoutParamsIMG.height = WIDTH;
        viewHolder.itemSqureShowListLayout.setLayoutParams(layoutParamsIMG);
        viewHolder.itemSqureShowListDelete.setVisibility(View.VISIBLE);
        if (list.size() < 3) {
            if (position < getCount() - 1) {
                viewHolder.itemSqureShowListDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ReaderParams readerParams = new ReaderParams(activity);
                        readerParams.putExtraParams("image", list.get(position).img);
                        HttpUtils.getInstance().sendRequestRequestParams(activity, Api.DeleteImage, readerParams.generateParamsJson(), new HttpUtils.ResponseListener() {
                            @Override
                            public void onResponse(String response) {
                                if (list.size() > 0 && list.size() - 1 >= position) {
                                    list.remove(position);
                                    notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onErrorResponse(String ex) {

                            }
                        });

                    }
                });

                viewHolder.itemSqureShowListAdd.setVisibility(View.GONE);
                MyGlide.GlideImageRoundedCornersNoSize(6, activity, list.get(position).show_img, viewHolder.itemSqureShowListImgs);
                if (position == 4 && list.size() > 5) {
                    viewHolder.itemSqureShowListDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            list.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                    viewHolder.itemSqureShowListTextView.setVisibility(View.VISIBLE);
                    viewHolder.itemSqureShowListTextView.setText("+ " + (list.size() - 5));
                    ViewGroup.LayoutParams item_squre_show_list_TextViewlayoutParamsIMG = viewHolder.itemSqureShowListTextView.getLayoutParams();
                    item_squre_show_list_TextViewlayoutParamsIMG.width = WIDTH;
                    item_squre_show_list_TextViewlayoutParamsIMG.height = WIDTH;
                    viewHolder.itemSqureShowListTextView.setLayoutParams(item_squre_show_list_TextViewlayoutParamsIMG);
                    viewHolder.itemSqureShowListTextView.setBackground(MyShape.setMyshape(10, "#CC000000"));
                } else {
                    viewHolder.itemSqureShowListTextView.setVisibility(View.VISIBLE);
                }
            } else {
                imageView = viewHolder.itemSqureShowListImgs;
                imageViewadd = viewHolder.itemSqureShowListAdd;
                MyGlide.GlideImageNoSize(activity, null, viewHolder.itemSqureShowListImgs);
                viewHolder.itemSqureShowListDelete.setVisibility(View.GONE);
                viewHolder.itemSqureShowListImgs.setImageResource(R.color.gray_f9);
                viewHolder.itemSqureShowListAdd.setVisibility(View.VISIBLE);
            }
        } else if (list.size() == 3) {
            MyGlide.GlideImageNoSize(activity, list.get(position).show_img, viewHolder.itemSqureShowListImgs);
            viewHolder.itemSqureShowListAdd.setVisibility(View.GONE);
        }
        notifyDataSetChanged();
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.item_squre_show_list_imgs)
        ImageView itemSqureShowListImgs;
        @BindView(R.id.item_squre_show_list_TextView)
        TextView itemSqureShowListTextView;
        @BindView(R.id.item_squre_show_list_add)
        ImageView itemSqureShowListAdd;
        @BindView(R.id.item_squre_show_list_delete)
        RelativeLayout itemSqureShowListDelete;
        @BindView(R.id.item_squre_show_list_layout)
        FrameLayout itemSqureShowListLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void setOnAddPhoto(FeedBackPhotoAdapter.onAddPhoto onAddPhoto) {
        this.onAddPhoto = onAddPhoto;
    }

    onAddPhoto onAddPhoto;

    public interface onAddPhoto {
        void addPhoto();
    }
}
