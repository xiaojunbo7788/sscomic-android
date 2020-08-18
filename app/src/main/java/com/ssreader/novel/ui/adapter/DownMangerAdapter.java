package com.ssreader.novel.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseListAdapter;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.model.BookChapter;
import com.ssreader.novel.model.Downoption;
import com.ssreader.novel.ui.activity.BookInfoActivity;
import com.ssreader.novel.ui.read.manager.ChapterManager;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.utils.InternetUtils;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownMangerAdapter extends BaseListAdapter<Downoption> {

    public List<Downoption> checkList;
    public boolean Edit;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public DownMangerAdapter(List<Downoption> list, Activity activity, SCOnItemClickListener scOnItemClickListener) {
        super(activity, list, scOnItemClickListener);
        checkList = new ArrayList<>();
    }

    @Override
    public int getViewByRes() {
        return R.layout.item_downmanger;
    }

    @Override
    public View getOwnView(int position, Downoption downoption, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        viewHolder = new ViewHolder(convertView);
        viewHolder.mItemDowmmangerCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Edit) {
                    activity.startActivity(new Intent(activity, BookInfoActivity.class)
                            .putExtra("book_id", downoption.book_id));
                } else {
                    onClickEdit(position, downoption, viewHolder.itemBianjiCheckBox);
                }
            }
        });
        viewHolder.mItemDowmmangerLinearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Edit) {
                    onClickEdit(position, downoption, viewHolder.itemBianjiCheckBox);
                }
            }
        });

        MyGlide.GlideImageNoSize(activity, downoption.cover, viewHolder.mItemDowmmangerCover);
        viewHolder.mItemDowmmangerName.setText(downoption.bookname);
        viewHolder.mItemDowmmangerDownoptionTitle.setText(downoption.start_order + "-" + downoption.end_order + LanguageUtil.getString(activity, R.string.BookInfoActivity_down_title_chapter));
        viewHolder.mItemDowmmangerDownoptionDate.setText(formatter.format(downoption.downoption_date));
        viewHolder.mItemDowmmangerDownoptionSize.setText(downoption.downoption_size);
        if (downoption.isdown) {
            viewHolder.mItemDowmmangerDownoptionYixizai.setText(LanguageUtil.getString(activity, R.string.BookInfoActivity_down_yixiazai));
        } else {
            BigDecimal b = new BigDecimal(((float) downoption.down_cunrrent_num / (float) downoption.down_num));
            viewHolder.mItemDowmmangerDownoptionYixizai.setText(b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() + "%");
        }
        viewHolder.openBookLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Edit) {
                    Book book = ObjectBoxUtils.getBook(downoption.book_id);
                    if (book == null) {
                        book = new Book();
                        book.setbook_id(downoption.book_id);
                        book.setName(downoption.bookname);
                        book.setCover(downoption.cover);
                        book.setDescription(downoption.description);
                        book.is_collect = 0;
                        ObjectBoxUtils.addData(book, Book.class);
                    }
                    if (!InternetUtils.internet(activity) && book.current_chapter_id_hasData != 0) {
                        book.current_chapter_id = book.current_chapter_id_hasData;
                    }
                   /* if(!InternetUtils.internet(activity)){
                        book.current_chapter_id=downoption.s_chapter;
                    }*/
                    ChapterManager.getInstance().openBook(activity, book);
                } else {
                    onClickEdit(position, downoption, viewHolder.itemBianjiCheckBox);
                }
            }
        });
        if (Edit) {
            viewHolder.mItemDowmmangerOpen.setVisibility(View.GONE);
            viewHolder.itemBianjiCheckBox.setVisibility(View.VISIBLE);
            if (checkList.contains(downoption)) {
                viewHolder.itemBianjiCheckBox.setChecked(true);
            } else {
                viewHolder.itemBianjiCheckBox.setChecked(false);
            }
        } else {
            viewHolder.mItemDowmmangerOpen.setVisibility(View.VISIBLE);
            viewHolder.itemBianjiCheckBox.setVisibility(View.GONE);
        }
        return convertView;
    }

    private void onClickEdit(int position, Downoption downoption, CheckBox itemBianjiCheckBox) {
        if (checkList.contains(downoption)) {
            checkList.remove(downoption);
            itemBianjiCheckBox.setChecked(false);
        } else {
            checkList.add(downoption);
            itemBianjiCheckBox.setChecked(true);
        }
        if (scOnItemClickListener != null) {
            scOnItemClickListener.OnItemClickListener(checkList.size(), position, downoption);
        }
    }


    class ViewHolder {
        @BindView(R.id.item_dowmmanger_cover)
        ImageView mItemDowmmangerCover;
        @BindView(R.id.item_dowmmanger_open_layout)
        LinearLayout openBookLayout;
        @BindView(R.id.item_dowmmanger_name)
        TextView mItemDowmmangerName;
        @BindView(R.id.item_dowmmanger_open)
        TextView mItemDowmmangerOpen;

        @BindView(R.id.item_dowmmanger_LinearLayout1)
        LinearLayout mItemDowmmangerLinearLayout1;

        @BindView(R.id.item_dowmmanger_Downoption_title)
        TextView mItemDowmmangerDownoptionTitle;
        @BindView(R.id.item_dowmmanger_Downoption_date)
        TextView mItemDowmmangerDownoptionDate;
        @BindView(R.id.item_dowmmanger_Downoption_size)
        TextView mItemDowmmangerDownoptionSize;
        @BindView(R.id.item_dowmmanger_Downoption_yixizai)
        TextView mItemDowmmangerDownoptionYixizai;

        @BindView(R.id.item_bianji_checkBox)
        public CheckBox itemBianjiCheckBox;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
