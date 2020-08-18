package com.ssreader.novel.model;

public class AudioSpeedBean {

    public String audioName;
    public int audioDate;
    public boolean isChose;

    @Override
    public String toString() {
        return "AudioSpeedBean{" +
                "audioName='" + audioName + '\'' +
                ", audioDate=" + audioDate +
                ", isChose=" + isChose +
                '}';
    }

    public AudioSpeedBean(int audioDate) {
        this.audioDate = audioDate;
    }

    public AudioSpeedBean(String audioName, int audioDate, boolean isChose) {
        this.audioName = audioName;
        this.audioDate = audioDate;
        this.isChose = isChose;
    }

    public AudioSpeedBean(String audioName, int audioDate) {
        this.audioName = audioName;
        this.audioDate = audioDate;
    }

    public boolean isChose() {
        return isChose;
    }

    public void setChose(boolean chose) {
        isChose = chose;
    }
}
