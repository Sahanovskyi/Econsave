package com.gw.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vadym on 02.05.17.
 */

public class DecisionItem {
    private final int id;
    private List<String> tags;
    private String title;

    public DecisionItem(int id, String title) {
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
}
