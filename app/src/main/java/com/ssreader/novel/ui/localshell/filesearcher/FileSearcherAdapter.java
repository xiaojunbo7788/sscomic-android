package com.ssreader.novel.ui.localshell.filesearcher;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ssreader.novel.R;
import com.ssreader.novel.ui.localshell.filesearcher.searchengine.FileItem;
import com.ssreader.novel.ui.localshell.filesearcher.searchengine.SearchEngine;
import com.ssreader.novel.ui.utils.MyToash;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/1.
 */

public class FileSearcherAdapter extends RecyclerView.Adapter<FileSearcherAdapter.FileSearcherVH> {

    private List<FileItem> items = new ArrayList<>();
    private List<FileItem> selectedItems = new ArrayList<>();
    private OnItemSelectCallback callback;
    private final int colorUnchecked;
    private final int colorChecked;
    private SearchEngine searchEngine;

    private Activity context;

    public FileSearcherAdapter(Activity context,@NonNull SearchEngine searchEngine){
        this.context = context;
        colorUnchecked = context.getResources().getColor(R.color.fileSearcherWhite);
        colorChecked = context.getResources().getColor(R.color.fileSearcherCheckedBackground);
        this.searchEngine = searchEngine;
    }

    @Override
    public FileSearcherVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FileSearcherVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_local_file_searcher_item,parent,false));
    }

    @Override
    public void onBindViewHolder(FileSearcherVH holder, int position) {
        FileItem item = items.get(position);
        holder.title.setText(item.getName());
        holder.path.setText(item.getPath());
        holder.detail.setText(item.getDetail());
        holder.checkBox.setChecked(item.isChecked());
        holder.itemView.setBackgroundColor(item.isChecked()? colorChecked : colorUnchecked);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(FileItem item){
        items.add(item);
        notifyDataSetChanged();
    }
    public void addItem(List<FileItem> item){
        items.addAll(item);
        notifyDataSetChanged();
    }

    public void selectAll(){
        boolean isAllSelected = isAllSelected();
        for(FileItem item : items){
            item.setChecked(!isAllSelected);
        }
        notifyDataSetChanged();
        if(!isAllSelected){
            selectedItems.clear();
            selectedItems.addAll(items);
        }else{
            selectedItems.clear();
        }
        if(callback != null){
            callback.onSelectStateChanged(selectedItems);
        }
    }
    private boolean isAllSelected(){
        for (FileItem item : items){
                if (!item.isChecked()){
                    return false;
                }
        }
        return true;
    }
    public void setOnItemSelectCallback(OnItemSelectCallback callback){
        this.callback = callback;
    }

    class FileSearcherVH extends RecyclerView.ViewHolder{
        TextView title, detail, path;
        CheckBox checkBox;
        public FileSearcherVH(final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.file_searcher_item_text_title);
            detail = itemView.findViewById(R.id.file_searcher_item_text_detail);
            path = itemView.findViewById(R.id.file_searcher_item_text_path);
            checkBox = itemView.findViewById(R.id.file_searcher_item_check_box);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkBox.performClick();
                    //items.get(getLayoutPosition()).setChecked(checkBox.isChecked());
                }
            });
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(searchEngine.isSearching()){
                        MyToash.ToashError(context, "正在扫描本地书籍，请稍等");
                        return;
                    }
                    boolean b = checkBox.isChecked();
                    items.get(getLayoutPosition()).setChecked(b);
                    itemView.setBackgroundColor(b ? colorChecked : colorUnchecked );
                    if(b){
                        selectedItems.add(items.get(getLayoutPosition()));
                    }else{
                        selectedItems.remove(items.get(getLayoutPosition()));
                    }
                    if(callback != null){
                        callback.onSelectStateChanged(selectedItems);
                    }
                }
            });
        }
    }
    interface OnItemSelectCallback {
        void onSelectStateChanged(List<FileItem> items);
    }

}
