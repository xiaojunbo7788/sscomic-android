package com.ssreader.novel.ui.localshell.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseRecAdapter;
import com.ssreader.novel.base.BaseRecViewHolder;
import com.ssreader.novel.ui.localshell.filesearcher.FileSearcher;
import com.ssreader.novel.ui.localshell.localapp.LocalBook;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.utils.ObjectBoxUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class BookListAdapter extends BaseRecAdapter<LocalBook, BookListAdapter.BookViewHolder> {

    public boolean isDeleteButtonVisible;
    private SCOnItemClickListener scOnItemClickListener;
    private List<LocalBook> checkedBookList;
    public boolean isAdd;

    public BookListAdapter(Activity activity, List<LocalBook> list,boolean isAdd, List<LocalBook> checkedBookList, SCOnItemClickListener scOnItemClickListener) {
        super(list, activity, 1);
        this.isAdd = isAdd;
        MyToash.Log("BookListAdapter",isAdd+"----------------------");
        this.scOnItemClickListener = scOnItemClickListener;
        this.checkedBookList = checkedBookList;
    }

    @Override
    public void onHolder(BookViewHolder holder, LocalBook book, int position) {
        holder.main_book_item_title_lay.setVisibility(View.VISIBLE);
        if (checkedBookList.contains(book)) {
            holder.like.setChecked(true);
        } else {
            holder.like.setChecked(false);
        }
        holder.main_book_item_title_lay.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!isDeleteButtonVisible && book != null) {
                    scOnItemClickListener.OnItemLongClickListener(1, position, book);
                }
                return true;
            }
        });
        holder.main_book_item_title_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (book != null) {
                    scOnItemClickListener.OnItemClickListener(1, position, book);
                    if (isDeleteButtonVisible) {
                        if (holder.like.isChecked()) {
                            holder.like.setChecked(false);
                        } else {
                            holder.like.setChecked(true);
                        }
                    }
                } else {
                    new FileSearcher(activity)
                            .withExtension("txt")
                            .withSizeLimit(10 * 1024, -1)
                            .search(new FileSearcher.FileSearcherCallback() {
                                @Override
                                public void onSelect(final List<File> files) {
                                    addBookFromFile(activity, files);
                                }
                            });
                }
            }
        });
        if (book != null) {
            holder.listview_item_nover_add_layout.setVisibility(View.GONE);
            holder.title.setVisibility(View.VISIBLE);
            if (isDeleteButtonVisible) {
                holder.like.setVisibility(View.VISIBLE);
            } else {
                holder.like.setVisibility(View.GONE);
            }
            holder.main_book_item_img.setVisibility(View.VISIBLE);
            String bookName = book.name.substring(0, book.name.lastIndexOf("."));
            holder.title.setText(bookName);
        } else {
            holder.like.setVisibility(View.GONE);
            if (!isDeleteButtonVisible) {
                holder.title.setVisibility(View.GONE);
                holder.main_book_item_img.setVisibility(View.GONE);
                holder.listview_item_nover_add_layout.setVisibility(View.VISIBLE);
            } else {
                holder.main_book_item_img.setVisibility(View.VISIBLE);
                holder.main_book_item_title_lay.setVisibility(View.GONE);
                holder.listview_item_nover_add_layout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public BookViewHolder onCreateHolder() {
        return new BookViewHolder(getViewByRes(R.layout.item_local_main));
    }

    public boolean isDeleteButtonVisible() {
        return isDeleteButtonVisible;
    }

    class BookViewHolder extends BaseRecViewHolder {

        @BindView(R.id.main_book_item_title)
        TextView title;
        @BindView(R.id.main_book_item_title_like)
        CheckBox like;
        @BindView(R.id.main_book_item_img)
        ImageView main_book_item_img;
        @BindView(R.id.listview_item_nover_add_layout)
        RelativeLayout listview_item_nover_add_layout;
        @BindView(R.id.main_book_item_title_lay)
        View main_book_item_title_lay;

        public BookViewHolder(View itemView) {
            super(itemView);
        }
    }

    private List<LocalBook> getTestData() {
        List<LocalBook> list = new ArrayList<>();
        LocalBook book;
        for (int i = 0; i < 50; i++) {
            book = new LocalBook();
            book.name = ("" + i);
            list.add(book);
        }
        return list;
    }
    @Override
    public int getItemCount() {
        MyToash.Log("BookListAdapter",isAdd+"+++++++++++++++");
        if(isAdd){
            return list.size();
        }else{
            return super.getItemCount();
        }
    }

    public void addBookFromFile(final Activity context, final List<File> files) {
        final List<LocalBook> newDataList = new ArrayList<>();
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("处理中");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (File file : files) {
                    LocalBook book = new LocalBook(file.getName(), file.getPath());
                    if (!list.contains(book)) {
                        newDataList.add(book);
                    }
                }
                if (newDataList.isEmpty()) {
                    dialog.cancel();
                    return;
                }
                ObjectBoxUtils.addData(newDataList, LocalBook.class);
                list.clear();
                list.addAll(ObjectBoxUtils.getLocalBookShelfData());

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
                dialog.cancel();
            }
        }).start();
    }

    public interface ClickCallback {

        void onClick(LocalBook book);

        void onLongClick(LocalBook book);
    }
}
