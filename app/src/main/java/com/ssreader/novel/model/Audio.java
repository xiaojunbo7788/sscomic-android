package com.ssreader.novel.model;

import android.app.Activity;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.audio.fragment.AudioInfoCatalogFragment;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.ssreader.novel.utils.UserUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Audio implements Comparable<Audio>, Serializable {

    //id字段是必要的字段，不可忽略，不可用修饰符修饰
    @Id(assignable = true)
    public long audio_id;
    public String name;
    public int is_vip, is_baoyue;
    public int is_collect;
    public String cover;
    public String author;
    public String hot_num;
    public int new_chapter;
    public String allPercent;
    public int total_chapter;
    public long current_chapter_id;
    public long current_listen_chapter_id;
    public String current_chapter_text;
    public int is_read;
    public int current_chapter_displayOrder;
    public int current_chapter_listen_displayOrder;
    public String Chapter_text;
    public String description;
    public int BookselfPosition;
    public String language;
    public String display_label, total_favors, play_num, finished;
    public int down_chapters;//下载过章节数 等于0  表示没有下载过
    // 语速
    public String speed = "60";
    public boolean isRecommend;

    public long getCurrent_listen_chapter_id() {
        return current_listen_chapter_id;
    }

    public void setCurrent_listen_chapter_id(long current_listen_chapter_id) {
        this.current_listen_chapter_id = current_listen_chapter_id;
    }

    public String getCurrent_chapter_text() {
        return current_chapter_text;
    }

    public void setCurrent_chapter_text(String current_chapter_text) {
        this.current_chapter_text = current_chapter_text;
    }

    public String getLanguage() {
        return language;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getNew_chapter() {
        return new_chapter;
    }

    public void setNew_chapter(int new_chapter) {
        this.new_chapter = new_chapter;
    }

    public String getAllPercent() {
        return allPercent;
    }

    public void setAllPercent(String allPercent) {
        this.allPercent = allPercent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotal_chapter() {
        return total_chapter;
    }

    public void setTotal_chapter(int total_chapter) {
        this.total_chapter = total_chapter;
    }

    public long getCurrent_chapter_id() {
        return current_chapter_id;
    }

    public void setCurrent_chapter_id(long current_chapter_id) {
        this.current_chapter_id = current_chapter_id;
    }

    public int getCurrent_chapter_displayOrder() {
        return current_chapter_displayOrder;
    }

    public void setCurrent_chapter_displayOrder(int current_chapter_displayOrder) {
        this.current_chapter_displayOrder = current_chapter_displayOrder;
    }

    public int getCurrent_chapter_listen_displayOrder() {
        return current_chapter_listen_displayOrder;
    }

    public void setCurrent_chapter_listen_displayOrder(int current_chapter_listen_displayOrder) {
        this.current_chapter_listen_displayOrder = current_chapter_listen_displayOrder;
    }

    public String getChapter_text() {
        return Chapter_text;
    }

    public void setChapter_text(String chapter_text) {
        Chapter_text = chapter_text;
    }

    public int getBookselfPosition() {
        return BookselfPosition;
    }

    public void setBookselfPosition(int BookselfPosition) {
        this.BookselfPosition = BookselfPosition;
    }

    public long getAudio_id() {
        return audio_id;
    }

    public void setAudio_id(long audio_id) {
        this.audio_id = audio_id;
    }

    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }

    public int getIs_baoyue() {
        return is_baoyue;
    }

    public void setIs_baoyue(int is_baoyue) {
        this.is_baoyue = is_baoyue;
    }

    public String getDisplay_label() {
        return display_label;
    }

    public void setDisplay_label(String display_label) {
        this.display_label = display_label;
    }

    public String getTotal_favors() {
        return total_favors;
    }

    public void setTotal_favors(String total_favors) {
        this.total_favors = total_favors;
    }

    public String getPlay_num() {
        return play_num;
    }

    public void setPlay_num(String play_num) {
        this.play_num = play_num;
    }

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public int getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(int is_collect) {
        this.is_collect = is_collect;
    }

    public void setBook_id(long book_id) {
        this.audio_id = book_id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Audio) {
            return (audio_id == (((Audio) obj).audio_id));
        }
        return super.equals(obj);
    }

    public Audio() {

    }

    public Audio(long book_id, String name, int is_collect, String cover, String author, int new_chapter, String allPercent, int total_chapter, long current_chapter_id, int current_chapter_displayOrder, String chapter_text, String description, int bookselfPosition,
                 int is_vip, int is_baoyue, String display_label, String total_favors, String play_num, String finished, String speed) {
        this.audio_id = book_id;
        this.name = name;
        this.is_collect = is_collect;
        this.cover = cover;
        this.author = author;
        this.new_chapter = new_chapter;
        this.allPercent = allPercent;
        this.total_chapter = total_chapter;
        this.current_chapter_id = current_chapter_id;
        this.current_chapter_displayOrder = current_chapter_displayOrder;
        this.Chapter_text = chapter_text;
        this.description = description;
        BookselfPosition = bookselfPosition;
        this.is_vip = is_vip;
        this.is_baoyue = is_baoyue;
        this.total_favors = total_favors;
        this.play_num = play_num;
        this.display_label = display_label;
        this.finished = finished;
        this.speed = speed;
    }

    @Override
    public int hashCode() {
        return (int) audio_id;
    }

    @Override
    public String toString() {
        return "Audio{" +
                "audio_id=" + audio_id +
                ", name='" + name + '\'' +
                ", is_vip=" + is_vip +
                ", is_baoyue=" + is_baoyue +
                ", is_collect=" + is_collect +
                ", cover='" + cover + '\'' +
                ", new_chapter=" + new_chapter +
                ", allPercent='" + allPercent + '\'' +
                ", total_chapter=" + total_chapter +
                ", current_chapter_id=" + current_chapter_id +
                ", current_listen_chapter_id=" + current_listen_chapter_id +
                ", current_chapter_text='" + current_chapter_text + '\'' +
                ", is_read=" + is_read +
                ", current_chapter_displayOrder=" + current_chapter_displayOrder +
                ", current_chapter_listen_displayOrder=" + current_chapter_listen_displayOrder +
                ", Chapter_text='" + Chapter_text + '\'' +
                ", description='" + description + '\'' +
                ", BookselfPosition=" + BookselfPosition +
                ", language='" + language + '\'' +
                ", display_label='" + display_label + '\'' +
                ", total_favors='" + total_favors + '\'' +
                ", play_num='" + play_num + '\'' +
                ", finished='" + finished + '\'' +
                ", down_chapters=" + down_chapters +
                ", speed='" + speed + '\'' +
                '}';
    }

    @Override
    public int compareTo(Audio Book) {
        return Book.BookselfPosition - this.BookselfPosition;
    }

    /**
     * 获取有声目录
     * @param activity
     * @param getAudioChapterList
     */
    public void getAudioChapterList(Activity activity, AudioInfoCatalogFragment.GetAudioChapterList getAudioChapterList) {
        Audio audio = this;
        List<AudioChapter> audioChapterList = ObjectBoxUtils.getAudioChapterData(audio.audio_id);
        ReaderParams readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("audio_id", audio.audio_id);
        HttpUtils.getInstance().sendRequestRequestParamsSubThread(activity,Api.AUDIO_CATALOG, readerParams.generateParamsJson(),  new HttpUtils.ResponseListener() {
            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        getDataList(jsonObject.getString("chapter_list"), audioChapterList, audio, new AudioInfoCatalogFragment.GetAudioChapterList() {
                            @Override
                            public void getAudioChapterList(List<AudioChapter> chapters) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getAudioChapterList.getAudioChapterList(chapters);
                                    }
                                });
                            }
                        });
                    } catch (JSONException e) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getAudioChapterList.getAudioChapterList(audioChapterList);
                            }
                        });
                    }
                } else {
                    getAudioChapterList.getAudioChapterList(audioChapterList);
                }
            }

            @Override
            public void onErrorResponse(String ex) {
                getAudioChapterList.getAudioChapterList(audioChapterList);
            }
        });
    }

    /**
     * 解析数据
     * @param response
     * @param audioChapterList
     * @param audio
     * @param getAudioChapterList
     */
    private void getDataList(String response, List<AudioChapter> audioChapterList, Audio audio, AudioInfoCatalogFragment.GetAudioChapterList getAudioChapterList) {
        String Chapter_text_Sign = UserUtils.MD5(response);
        List<AudioChapter> audioChapters = HttpUtils.getGson().fromJson(response, new TypeToken<List<AudioChapter>>() {}.getType());
        if (audioChapters == null || audioChapters.isEmpty()) {
            getAudioChapterList.getAudioChapterList(new ArrayList<>());
            return;
        }
        audio.total_chapter = audioChapters.size();
        if (audioChapterList.isEmpty()) {
            audio.Chapter_text = Chapter_text_Sign;
            ObjectBoxUtils.addData(audio, Audio.class);
            ObjectBoxUtils.addData(audioChapters, AudioChapter.class);
            getAudioChapterList.getAudioChapterList(audioChapters);
        } else {
            if (Chapter_text_Sign.equals(audio.getChapter_text())) {
                getAudioChapterList.getAudioChapterList(audioChapterList);
            } else {
                audio.Chapter_text = Chapter_text_Sign;
                ObjectBoxUtils.addData(audio, Audio.class);
                if (!audioChapters.isEmpty()) {
                    for (AudioChapter chapterItem : audioChapters) {
                        int position = audioChapterList.indexOf(chapterItem);
                        if (position != -1) {
                            AudioChapter localChapter = audioChapterList.get(position);
                            chapterItem.setIs_read(localChapter.getIs_read());
                            chapterItem.path = localChapter.path;
                            chapterItem.status = localChapter.status;
                            if (localChapter.getDuration_second() != 0) {
                                chapterItem.setDuration_second(localChapter.getDuration_second());
                            }
                            if (localChapter.getRead_progress() != 0) {
                                chapterItem.setRead_progress(localChapter.getRead_progress());
                            }
                        }
                    }
                    ObjectBoxUtils.deletALLeData(audioChapterList, AudioChapter.class);
                    ObjectBoxUtils.addData(audioChapters, AudioChapter.class);
                    getAudioChapterList.getAudioChapterList(audioChapters);
                } else {
                    getAudioChapterList.getAudioChapterList(audioChapterList);
                }
            }
        }
    }
}
