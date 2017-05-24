package com.gw.data.entity.mapper;


import com.gw.data.entity.TransactionItemEntity;
import com.gw.domain.model.TransactionItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * Mapper class used to transform {@link com.gw.data.entity.TransactionItemEntity} (in the data layer) to {@link com.gw.domain.model.TransactionItem} in the
 * domain layer.
 */
@Singleton
public class TransactionItemEntityDataMapper {

    @Inject
    public TransactionItemEntityDataMapper() {
    }


    /**
     * Transform a {@link TransactionItemEntity} into an {@link TransactionItem}.
     *
     * @param itemEntity Object to be transformed.
     * @return {@link TransactionItem} if valid {@link TransactionItemEntity} otherwise null.
     */
    public TransactionItem transform(TransactionItemEntity itemEntity) {
        TransactionItem item = null;
        if (itemEntity != null) {
            item = new TransactionItem(itemEntity.getDate(), itemEntity.getAmount());
            item.setBalance(itemEntity.getBalance());
            item.setDescription(itemEntity.getDescription());
            item.setCurrency(itemEntity.getCurrency());
        }
        return item;
    }

    /**
     * Transform a List of {@link TransactionItemEntity} into a Collection of {@link TransactionItem}.
     *
     * @param itemEntityCollection Object Collection to be transformed.
     * @return {@link TransactionItem} if valid {@link TransactionItemEntity} otherwise null.
     */
    public List<TransactionItem> transform(Collection<TransactionItemEntity> itemEntityCollection) {
        final List<TransactionItem> items = new ArrayList<>();
        for (TransactionItemEntity itemEntity : itemEntityCollection) {
            final TransactionItem item = transform(itemEntity);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }


}
