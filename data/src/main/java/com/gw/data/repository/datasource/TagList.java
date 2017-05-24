package com.gw.data.repository.datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by vadym on 10.05.17.
 */

@Singleton
public class TagList {

    private Map<String, String> tags;

    @Inject
    public TagList() {
        tags = new HashMap<>();
        initTagList();
    }

    private void initTagList(){
        tags.put("purchase", "покупка");
        tags.put("realty", "нерухомість");
        tags.put("dwelling", "житло");
        tags.put("house", "будинок");
        tags.put("apartment", "квартира");
        tags.put("car", "машина");
        tags.put("vehicles", "транспорт");
        tags.put("bicycle", "велосипед");
        tags.put("investment", "інвестиції");
        tags.put("securities", "цінні_папери");
    }

    public Map<String, String> getTags() {
        return tags;
    }
}
