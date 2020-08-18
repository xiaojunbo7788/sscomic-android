package com.ssreader.novel.model;

import android.app.Activity;
import android.text.TextUtils;

import com.ssreader.novel.constant.Api;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.fragment.ComicinfoMuluFragment;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.ssreader.novel.utils.UserUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;

@Entity
public class Comic implements Comparable<Comic>, Serializable {

    @Id(assignable = true)
    public long comic_id;//": 26470, //漫画id
    public String name;//": "一拳超人", //漫画名
    public String vertical_cover;//"": "http://i0.hdslb.com/bfs/manga-static/4761ca39b699bb2738c326ce812c6c56ee3b1458.jpg", //竖封面
    public int total_chapters;//": 48, //总章节数
    public int new_chapter;//": 48, //总章节数
    public String description;
    public int is_collect;
    public String author;//"": "黎明C", //作者
    public long current_chapter_id;//最近阅读章节ID
    public int is_read;
    public int current_display_order;//最近阅读章节序号
    public String current_chapter_name;//最近阅读章节name
    public String Chapter_text;//本书的目录数据的MD5
    public String last_chapter_time;
    public int down_chapters;//下载过章节数 等于0  表示没有下载过
    public int BookselfPosition;
    public boolean isRecommend;
    @Transient
    private String finished;
    @Transient
    public String horizontal_cover;
    @Transient
    private String flag;

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public String getHorizontal_cover() {
        return horizontal_cover;
    }

    public void setHorizontal_cover(String horizontal_cover) {
        this.horizontal_cover = horizontal_cover;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }



    @Override
    public boolean equals(Object obj) {
        return this.comic_id == (((Comic) obj).comic_id);
    }

    @Override
    public int hashCode() {
        return (int) this.comic_id;
    }

    public long getComic_id() {
        return comic_id;
    }

    public void setComic_id(long comic_id) {
        this.comic_id = comic_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVertical_cover() {
        return vertical_cover;
    }

    public void setVertical_cover(String vertical_cover) {
        this.vertical_cover = vertical_cover;
    }

    public int getTotal_chapters() {
        return total_chapters;
    }

    public void setTotal_chapters(int total_chapters) {
        this.total_chapters = total_chapters;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCurrent_chapter_id() {
        return current_chapter_id;
    }

    public void setCurrent_chapter_id(long current_chapter_id) {
        this.current_chapter_id = current_chapter_id;
    }

    public int getCurrent_display_order() {
        return current_display_order;
    }

    public void setCurrent_display_order(int current_display_order) {
        this.current_display_order = current_display_order;
    }

    public String getCurrent_chapter_name() {
        return current_chapter_name;
    }

    public void setCurrent_chapter_name(String current_chapter_name) {
        this.current_chapter_name = current_chapter_name;
    }

    public String getChapter_text() {
        return Chapter_text;
    }

    public void setChapter_text(String chapter_text) {
        Chapter_text = chapter_text;
    }

    public String getLast_chapter_time() {
        return last_chapter_time;
    }

    public void setLast_chapter_time(String last_chapter_time) {
        this.last_chapter_time = last_chapter_time;
    }

    public int getDown_chapters() {
        return down_chapters;
    }

    public void setDown_chapters(int down_chapters) {
        this.down_chapters = down_chapters;
    }

    public int getBookselfPosition() {
        return BookselfPosition;
    }

    public void setBookselfPosition(int BookselfPosition) {
        this.BookselfPosition = BookselfPosition;
    }

    public Comic() {
    }

    public Comic(long comic_id) {
        this.comic_id = comic_id;
    }

    @Override
    public int compareTo(Comic baseCartoon) {
        return baseCartoon.BookselfPosition - this.BookselfPosition;
    }

    @Override
    public String toString() {
        return "Comic{" +
                "comic_id=" + comic_id +
                ", name='" + name + '\'' +
                ", vertical_cover='" + vertical_cover + '\'' +
                ", total_chapters=" + total_chapters +
                ", new_chapter=" + new_chapter +
                ", description='" + description + '\'' +
                ", is_collect=" + is_collect +
                ", current_chapter_id=" + current_chapter_id +
                ", current_display_order=" + current_display_order +
                ", current_chapter_name='" + current_chapter_name + '\'' +
                ", Chapter_text='" + Chapter_text + '\'' +
                ", last_chapter_time='" + last_chapter_time + '\'' +
                ", down_chapters=" + down_chapters +
                ", BookselfPosition=" + BookselfPosition +
                ", finished=" + finished +
                ", horizontal_cover='" + horizontal_cover + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }

    public void GetCOMIC_catalog(Activity activity, ComicinfoMuluFragment.GetCOMIC_catalogList getCOMIC_catalogList) {
        Comic comic = this;
        Comic localComic = ObjectBoxUtils.getComic(comic.comic_id);
        if (localComic != null) {
            comic.down_chapters = localComic.down_chapters;
        }
        List<ComicChapter> comicChapterList = ObjectBoxUtils.getComicChapterItemfData(comic.comic_id);
        ReaderParams params = new ReaderParams(activity);
        params.putExtraParams("comic_id", comic.comic_id);
        String json = params.generateParamsJson();
        HttpUtils.getInstance().sendRequestRequestParamsSubThread(activity,Api.COMIC_catalog, json, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        getDataList(result, comic, comicChapterList, new ComicinfoMuluFragment.GetCOMIC_catalogList() {
                            @Override
                            public void GetCOMIC_catalogList(List<ComicChapter> comicChapterList) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getCOMIC_catalogList.GetCOMIC_catalogList(comicChapterList);
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        if (!TextUtils.isEmpty(ex) && ex.equals("750")) {
                            getCOMIC_catalogList.GetCOMIC_catalogList(null);
                        } else {
                            getCOMIC_catalogList.GetCOMIC_catalogList(comicChapterList);
                        }
                    }
                }
        );
    }

    /**
     * 将本地数据与网络数据补充
     * @param result
     * @param comic
     * @param comicChapterList
     * @param getCOMIC_catalogList
     */
    private void getDataList(String result, Comic comic, List<ComicChapter> comicChapterList,
                             ComicinfoMuluFragment.GetCOMIC_catalogList getCOMIC_catalogList) {
        ComicChapterList comicChapterList1 = HttpUtils.getGson().fromJson(result, ComicChapterList.class);
        List<ComicChapter> httpcomicChapterList = comicChapterList1.chapter_list;
        if (httpcomicChapterList == null || httpcomicChapterList.isEmpty()) {
            getCOMIC_catalogList.GetCOMIC_catalogList(new ArrayList<>());
            return;
        }
        String Chapter_text_Sign = UserUtils.MD5(result);
        if (comicChapterList.isEmpty() ) {
            comic.total_chapters = httpcomicChapterList.size();
            comic.setChapter_text(Chapter_text_Sign);
            ObjectBoxUtils.addData(comic,Comic.class);
            ObjectBoxUtils.addData(httpcomicChapterList, ComicChapter.class);
            getCOMIC_catalogList.GetCOMIC_catalogList(httpcomicChapterList);
            return;
        }
        if (!TextUtils.isEmpty(comic.getChapter_text()) && comic.getChapter_text().equals(Chapter_text_Sign)) {
            getCOMIC_catalogList.GetCOMIC_catalogList(comicChapterList);
        } else {
            comic.setChapter_text(Chapter_text_Sign);
            comic.total_chapters = httpcomicChapterList.size();
            ObjectBoxUtils.addData(comic, Comic.class);
            for (ComicChapter chapterItem : httpcomicChapterList) {
                int position = comicChapterList.indexOf(chapterItem);
                if (position != -1) {
                    ComicChapter chapterItem1 = comicChapterList.get(position);
                    chapterItem.setCurrent_read_img_image_id(chapterItem1.getCurrent_read_img_image_id());
                    chapterItem.setCurrent_read_img_order(chapterItem1.getCurrent_read_img_order());
                    chapterItem.IsRead = chapterItem1.IsRead;
                    chapterItem.ImagesText = chapterItem1.ImagesText;
                    chapterItem.downStatus = chapterItem1.downStatus;
                }
            }
            ObjectBoxUtils.deletALLeData(comicChapterList, ComicChapter.class);
            ObjectBoxUtils.addData(httpcomicChapterList, ComicChapter.class);
            getCOMIC_catalogList.GetCOMIC_catalogList(httpcomicChapterList);
        }
    }
}
