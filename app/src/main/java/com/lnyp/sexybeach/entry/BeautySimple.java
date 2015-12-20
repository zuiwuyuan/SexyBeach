package com.lnyp.sexybeach.entry;

import java.io.Serializable;

/**
 * Created by lining on 2015/12/18.
 */
public class BeautySimple implements Serializable {

    /**
     * count : 39
     * fcount : 0
     * galleryclass : 1
     * id : 521
     * img : /ext/151218/3bb44bfaf00e376b9eacae096bb0c4ce.jpg
     * rcount : 0
     * size : 18
     * time : 1450406403000
     * title : 大胸美女模特性感内衣私房写真
     */

    private int count;
    private int fcount;
    private int galleryclass;
    private int id;
    private String img;
    private int rcount;
    private int size;
    private long time;
    private String title;

    public void setCount(int count) {
        this.count = count;
    }

    public void setFcount(int fcount) {
        this.fcount = fcount;
    }

    public void setGalleryclass(int galleryclass) {
        this.galleryclass = galleryclass;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setRcount(int rcount) {
        this.rcount = rcount;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public int getFcount() {
        return fcount;
    }

    public int getGalleryclass() {
        return galleryclass;
    }

    public int getId() {
        return id;
    }

    public String getImg() {
        return img;
    }

    public int getRcount() {
        return rcount;
    }

    public int getSize() {
        return size;
    }

    public long getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }


}
