package com.ssreader.novel.model;

/**
 * 发音人
 */
public class AudioPronunciationBean {

    //发音人姓名
    public String pronunciationName;
    //发音人参数
    public String pronunciationParameter;

    public AudioPronunciationBean(String pronunciationName, String pronunciationParameter) {
        this.pronunciationName = pronunciationName;
        this.pronunciationParameter = pronunciationParameter;
    }

    public String getPronunciationName() {
        return pronunciationName;
    }

    public void setPronunciationName(String pronunciationName) {
        this.pronunciationName = pronunciationName;
    }

    public String getPronunciationParameter() {
        return pronunciationParameter;
    }

    public void setPronunciationParameter(String pronunciationParameter) {
        this.pronunciationParameter = pronunciationParameter;
    }
}
