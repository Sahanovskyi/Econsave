package com.gw.data.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vadym on 10.05.17.
 */

public class DecisionEntity {
    private final int id;
    private List<String> tags;
    private String title;

    public DecisionEntity(int id, String title) {
        this.id = id;
        this.title = title;
        this.tags = new ArrayList<>();
    }

    public DecisionEntity(int id, String title, String[] tags) {
        this.id = id;
        this.title = title;
        this.tags = new ArrayList<>();
        this.tags.addAll(Arrays.asList(tags));
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

}
