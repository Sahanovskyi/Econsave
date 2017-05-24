package com.gw.data.entity.mapper;

import com.gw.data.entity.DecisionEntity;
import com.gw.domain.model.DecisionItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by vadym on 10.05.17.
 */
@Singleton
public class DecisionEntityMapper {

    @Inject
    public DecisionEntityMapper() {
    }

    /**
     * Transform a {@link DecisionEntity} into an {@link DecisionItem}.
     *
     * @param itemEntity Object to be transformed.
     * @return {@link DecisionItem} if valid {@link DecisionEntity} otherwise null.
     */
    public DecisionItem transform(DecisionEntity itemEntity) {
        DecisionItem item = null;
        if (itemEntity != null) {
            item = new DecisionItem(itemEntity.getId(), itemEntity.getTitle());
            item.setTags(itemEntity.getTags());
        }
        return item;
    }

    /**
     * Transform a List of {@link DecisionEntity} into a Collection of {@link DecisionItem}.
     *
     * @param itemEntityCollection Object Collection to be transformed.
     * @return {@link DecisionItem} if valid {@link DecisionEntity} otherwise null.
     */
    public List<DecisionItem> transform(Collection<DecisionEntity> itemEntityCollection) {
        final List<DecisionItem> items = new ArrayList<>();
        for (DecisionEntity itemEntity : itemEntityCollection) {
            final DecisionItem item = transform(itemEntity);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }
}
