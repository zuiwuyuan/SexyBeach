package com.lnyp.sexybeach.entry;

import java.io.Serializable;

/**
 * Created by lining on 2015/12/18.
 */
public class BeautyClassify implements Serializable {
    /**
     * description : 性感美女
     * id : 1
     * keywords : 性感美女
     * name : 性感美女
     * seq : 1
     * title : 性感美女
     */

    private String description;
    private int id;
    private String keywords;
    private String name;
    private int seq;
    private String title;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getName() {
        return name;
    }

    public int getSeq() {
        return seq;
    }

    public String getTitle() {
        return title;
    }

}
