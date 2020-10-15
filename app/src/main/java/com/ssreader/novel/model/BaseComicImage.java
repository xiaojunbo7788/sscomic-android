package com.ssreader.novel.model;

import com.ssreader.novel.manager.UserManager;

public class BaseComicImage {

    public long comic_id;
    public long chapter_id;
    public String image_id;
    public String total_tucao;
    public String update_time;
    public int width;
    public int height;
    private String image;

    private boolean isUpdateImage = false;

    public boolean isUpdateImage() {
        return isUpdateImage;
    }

    public void setUpdateImage(boolean updateImage) {
        isUpdateImage = updateImage;
    }

    public long getComic_id() {
        return comic_id;
    }

    public void setComic_id(long comic_id) {
        this.comic_id = comic_id;
    }

    public long getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(long chapter_id) {
        this.chapter_id = chapter_id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getTotal_tucao() {
        return total_tucao;
    }

    public void setTotal_tucao(String total_tucao) {
        this.total_tucao = total_tucao;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImage() {
//        http://pic.syslly.com/comic/ahri8/songshu/a00149/003.jpg
        if (isUpdateImage) {
            return image;
        }
        if (UserManager.getInstance().getClearData() == 0) {
            if (image != null && image.length() > 0) {
                String[]imgs = image.split("/");
                StringBuilder newImg = new StringBuilder();
                for (int i = 0; i < imgs.length; i++) {
                    if (i == imgs.length -1) {
                        newImg.append("/900/");
                        newImg.append(imgs[i]);
                    } else {
                        newImg.append(imgs[i]+"/");
                    }
                }
                isUpdateImage = true;
                image = newImg.toString();
                return image;
            } else {
                isUpdateImage = true;
                return "";
            }
        } else {
            isUpdateImage = true;
            return image;
        }

    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    @Override
    public String toString() {
        return "BaseComicImage{" +
                "comic_id='" + comic_id + '\'' +
                ", chapter_id='" + chapter_id + '\'' +
                ", image_id='" + image_id + '\'' +
                ", total_tucao='" + total_tucao + '\'' +
                ", update_time='" + update_time + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", image='" + image +
                '}';
    }
}
