package com.ssreader.novel.ui.utils;

import android.app.Activity;

import com.ssreader.novel.R;
import com.ssreader.novel.model.Audio;
import com.ssreader.novel.model.AudioHeadRightBean;
import com.ssreader.novel.model.AudioPronunciationBean;
import com.ssreader.novel.model.AudioSpeedBean;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.utils.LanguageUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublicStaticMethod {



    /**
     * 听书语速
     *
     * @param activity
     * @return
     */
    public static List<AudioSpeedBean> getAudioSpeedDate(Activity activity) {
        List<AudioSpeedBean> privilegeItems = new ArrayList<>();
        privilegeItems.add(new AudioSpeedBean(String.format(LanguageUtil.getString(activity, R.string.audio_double_speed), 0.5), 20));
        privilegeItems.add(new AudioSpeedBean(String.format(LanguageUtil.getString(activity, R.string.audio_double_speed), 0.75), 40));
        privilegeItems.add(new AudioSpeedBean(String.format(LanguageUtil.getString(activity, R.string.audio_double_speed), 1), 60, true));
        privilegeItems.add(new AudioSpeedBean(String.format(LanguageUtil.getString(activity, R.string.audio_double_speed), 1.25), 80));
        privilegeItems.add(new AudioSpeedBean(String.format(LanguageUtil.getString(activity, R.string.audio_double_speed), 1.5), 100));
        return privilegeItems;
    }

    /**
     * 获取当前语速的值
     *
     * @param activity
     * @param key
     * @return
     */
    public static String getSpeed(Activity activity, String key) {
        Map<String, String> map = new HashMap<>();
        map.put("20", String.format(LanguageUtil.getString(activity, R.string.audio_double_speed), 0.5));
        map.put("40", String.format(LanguageUtil.getString(activity, R.string.audio_double_speed), 0.75));
        map.put("60", String.format(LanguageUtil.getString(activity, R.string.audio_double_speed), 1.0));
        map.put("80", String.format(LanguageUtil.getString(activity, R.string.audio_double_speed), 1.25));
        map.put("100", String.format(LanguageUtil.getString(activity, R.string.audio_double_speed), 1.5));
        return map.get(key);
    }

    /**
     * 获取当前语速的值
     *
     * @param key
     * @return
     */
    public static Integer getSpeedImage(String key) {
        Map<String, Integer> map = new HashMap<>();
        map.put("20", R.mipmap.audio_speech_speed_0);
        map.put("40", R.mipmap.audio_speech_speed_1);
        map.put("60", R.mipmap.audio_speech_speed_2);
        map.put("80", R.mipmap.audio_speech_speed_3);
        map.put("100", R.mipmap.audio_speech_speed_4);
        return map.get(key) == null ? R.mipmap.audio_speech_speed_2 : map.get(key);
    }

    /**
     * 获取当前语速的值
     *
     * @param key
     * @return
     */
    public static float getSpeed(String key) {
        Map<String, Float> map = new HashMap<>();
        map.put("20", 0.5f);
        map.put("40", 0.75f);
        map.put("60", 1.0f);
        map.put("80", 1.25f);
        map.put("100", 1.5f);
        return map.get(key) == null ? 1.0f : map.get(key);
    }

    /**
     * 听书定时
     *
     * @param activity
     * @return
     */
    public static List<AudioSpeedBean> getTimeDate(Activity activity) {
        List<AudioSpeedBean> privilegeItems = new ArrayList<>();
        privilegeItems.add(new AudioSpeedBean(LanguageUtil.getString(activity, R.string.audio_not_open), 0, true));
        privilegeItems.add(new AudioSpeedBean(LanguageUtil.getString(activity, R.string.listen_current_chapter), -1));
        privilegeItems.add(new AudioSpeedBean(String.format(LanguageUtil.getString(activity, R.string.audio_double_time), 15), 899));
        privilegeItems.add(new AudioSpeedBean(String.format(LanguageUtil.getString(activity, R.string.audio_double_time), 30), 1799));
        privilegeItems.add(new AudioSpeedBean(String.format(LanguageUtil.getString(activity, R.string.audio_double_time), 60), 3599));
        privilegeItems.add(new AudioSpeedBean(String.format(LanguageUtil.getString(activity, R.string.audio_double_time), 90), 5399));
        return privilegeItems;
    }

    /**
     * 发音人
     *
     * @param activity
     * @return
     */
    public static List<AudioPronunciationBean> getPronunciationDate(Activity activity) {
        List<AudioPronunciationBean> privilegeItems = new ArrayList<>();
        privilegeItems.add(new AudioPronunciationBean(LanguageUtil.getString(activity, R.string.audio_emotional_girl),
                "xiaoyan"));
        privilegeItems.add(new AudioPronunciationBean(LanguageUtil.getString(activity, R.string.audio_standard_female_voice),
                "aisjinger"));
        privilegeItems.add(new AudioPronunciationBean(LanguageUtil.getString(activity, R.string.audio_standard_male_voice),
                "aisjiuxu"));
        return privilegeItems;
    }

    /**
     * 获取播音人
     *
     * @param activity
     * @param key
     * @return
     */
    public static String getVoice(Activity activity, String key) {
        Map<String, String> map = new HashMap<>();
        map.put("xiaoyan", LanguageUtil.getString(activity, R.string.audio_emotional_girl));
        map.put("aisjinger", LanguageUtil.getString(activity, R.string.audio_standard_female_voice));
        map.put("aisjiuxu", LanguageUtil.getString(activity, R.string.audio_standard_male_voice));
        return map.get(key);
    }

    /**
     * 听书界面右上角弹窗
     *
     * @param activity
     * @param isSound
     * @param audio
     * @param book
     * @param isDay
     * @return
     */
    public static List<AudioHeadRightBean> getHeadRightDate(Activity activity, boolean isSound, Audio audio, Book book, boolean isDay) {
        List<AudioHeadRightBean> privilegeItems = new ArrayList<>();
        privilegeItems.add(new AudioHeadRightBean(LanguageUtil.getString(activity, R.string.BookInfoActivity_title),
                R.mipmap.audio_book_info_img, "bookInfo"));
        if (isSound) {
            privilegeItems.add(new AudioHeadRightBean(audio.is_collect == 1 ? LanguageUtil.getString(activity, R.string.BookInfoActivity_jiarushujias) :
                    LanguageUtil.getString(activity, R.string.BookInfoActivity_jiarushujia),
                    R.mipmap.audio_add_shelf, "addShelf"));
        } else {
            privilegeItems.add(new AudioHeadRightBean(book.is_collect == 1 ? LanguageUtil.getString(activity, R.string.BookInfoActivity_jiarushujias) :
                    LanguageUtil.getString(activity, R.string.BookInfoActivity_jiarushujia),
                    R.mipmap.audio_add_shelf, "addShelf"));
        }
        privilegeItems.add(new AudioHeadRightBean(LanguageUtil.getString(activity, R.string.BookInfoActivity_downn),
                R.mipmap.audio_down_load, "downLoad"));
        if (isDay) {
            privilegeItems.add(new AudioHeadRightBean(LanguageUtil.getString(activity, R.string.app_night_model),
                    R.mipmap.audio_night, "nightMode"));
        } else {
            privilegeItems.add(new AudioHeadRightBean(LanguageUtil.getString(activity, R.string.app_daytime_model),
                    R.mipmap.audio_day, "nightMode"));
        }
        return privilegeItems;
    }
}
