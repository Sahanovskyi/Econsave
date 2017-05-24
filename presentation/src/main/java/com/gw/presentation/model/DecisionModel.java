package com.gw.presentation.model;

import android.media.Image;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;



public class DecisionModel implements Comparable<DecisionModel>{
    private final int id;
    private List<String> tags;
    private String title;
    private Image image;

    public DecisionModel(int id, String title) {
        this.id = id;
        this.title = title;
        this.tags = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTagsString(){
        StringBuilder sb = new StringBuilder();
        for (String tag : tags) {
            sb.append("#" + tag + ", ");
        }
        sb.deleteCharAt(sb.length() - 2);

        return sb.toString();
    }

    @Override
    public int compareTo(@NonNull DecisionModel o) {
        return getTitle().compareTo(o.getTitle());
    }

}
