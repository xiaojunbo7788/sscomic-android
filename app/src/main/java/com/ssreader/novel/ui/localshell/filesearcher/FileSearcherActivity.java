package com.ssreader.novel.ui.localshell.filesearcher;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.ui.localshell.filesearcher.filter.FileFilter;
import com.ssreader.novel.ui.localshell.filesearcher.searchengine.FileItem;
import com.ssreader.novel.ui.localshell.filesearcher.searchengine.SearchEngine;
import com.ssreader.novel.ui.utils.MyToash;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class FileSearcherActivity extends AppCompatActivity {

    private SearchEngine searchEngine;
    private FileSearcherAdapter adapter;
    private Toolbar toolbar;
    private List<FileItem> selectedItems;
    private View emptyView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_file_searcher_main);
        initializeSearchEngine();
        initializeView();
        searchEngine.start();
    }

    private void initializeView() {
        emptyView = findViewById(R.id.file_searcher_main_no_result_found);
        toolbar = (Toolbar) findViewById(R.id.file_searcher_main_toolbar);
        toolbar.setNavigationIcon(R.mipmap.back_holo_dark_no_trim_no_padding);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Button fab = findViewById(R.id.file_searcher_main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchEngine.isSearching()) {
                    MyToash.ToashError(FileSearcherActivity.this, "正在扫描本地书籍，请稍等");
                    return;
                }
                ArrayList<File> list = new ArrayList<>();
                if (selectedItems != null) {
                    for (FileItem item : selectedItems) {
                        list.add(item.getFile());
                    }
                }
                if (list.size() == 0) {
                    MyToash.ToashError(FileSearcherActivity.this, "至少选择一本书");
                    return;
                }
                FileSearcher.callback.onSelect(list);
                finish();
            }
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.file_searcher_main_recycler_view);
        adapter = new FileSearcherAdapter(this, searchEngine);
        adapter.setOnItemSelectCallback(new FileSearcherAdapter.OnItemSelectCallback() {
            @Override
            public void onSelectStateChanged(List<FileItem> items) {
                if (selectedItems == null) {
                    selectedItems = items;
                }
                toolbar.setTitle(selectedItems.size() + "/" + adapter.getItemCount());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void initializeSearchEngine() {
        FileFilter filter = (FileFilter) getIntent().getSerializableExtra(FileSearcher.FILE_FILTER);
        File path = (File) getIntent().getSerializableExtra(FileSearcher.SEARCH_PATH);
        if (filter == null || path == null) {
            throw new NullPointerException("the filter and path cannot be null!");
        }
        searchEngine = new SearchEngine(path, filter);
        searchEngine.setCallback(new SearchEngine.SearchEngineCallback() {
            @Override
            public void onFind(List<FileItem> items) {
                adapter.addItem(items);
            }

            @Override
            public void onSearchDirectory(File file) {
                toolbar.setSubtitle(file.getPath().replace(Environment.getExternalStorageDirectory().getPath() + File.separator, ""));
            }

            @Override
            public void onFinish() {
                if (adapter.getItemCount() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                }
                toolbar.setTitle("0/" + adapter.getItemCount());
                toolbar.setSubtitle(getString(R.string.local_file_searcher_searching_completed));
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (searchEngine.isSearching()) {
            showCancelDialog();
        } else {
            super.onBackPressed();
        }
    }

    private void showCancelDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_cancle)).
                setPositiveButton(getString(R.string.app_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        searchEngine.stop();
                        finish();
                    }
                }).setNegativeButton(getString(R.string.app_cancle), null)
                .create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.file_searcher_activity_menu, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileSearcher.callback = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.file_searcher_menu_select_all && !searchEngine.isSearching()) {
            adapter.selectAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
